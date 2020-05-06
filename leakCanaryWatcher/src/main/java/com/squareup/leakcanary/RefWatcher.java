/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.leakcanary;

import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.squareup.leakcanary.HeapDumper.RETRY_LATER;
import static com.squareup.leakcanary.Preconditions.checkNotNull;
import static com.squareup.leakcanary.Retryable.Result.DONE;
import static com.squareup.leakcanary.Retryable.Result.RETRY;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Watches references that should become weakly reachable. When the {@link RefWatcher} detects that
 * a reference might not be weakly reachable when it should, it triggers the {@link HeapDumper}.
 *
 * <p>This class is thread-safe: you can call {@link #watch(Object)} from any thread.
 */
public final class RefWatcher {

  public static final RefWatcher DISABLED = new RefWatcherBuilder<>().build();

  //内存泄露检测用的 Executor
  private final WatchExecutor watchExecutor;
  //用于查询是否在 debug 调试模式下，调试中不会执行内存泄漏检测
  private final DebuggerControl debuggerControl;
  //用来处理gc，判断内存泄露前，再次gc
  private final GcTrigger gcTrigger;
  //dump出文件
  private final HeapDumper heapDumper;
  //已经产生了内存队列的key
  private final Set<String> retainedKeys;
  //引用队列，
  private final ReferenceQueue<Object> queue;
  //分析产生heap文件的对调
  private final HeapDump.Listener heapdumpListener;
  //排除系统造成的内存泄露
  private final ExcludedRefs excludedRefs;

  RefWatcher(WatchExecutor watchExecutor, DebuggerControl debuggerControl, GcTrigger gcTrigger,
      HeapDumper heapDumper, HeapDump.Listener heapdumpListener, ExcludedRefs excludedRefs) {
    this.watchExecutor = checkNotNull(watchExecutor, "watchExecutor");
    this.debuggerControl = checkNotNull(debuggerControl, "debuggerControl");
    this.gcTrigger = checkNotNull(gcTrigger, "gcTrigger");
    this.heapDumper = checkNotNull(heapDumper, "heapDumper");
    this.heapdumpListener = checkNotNull(heapdumpListener, "heapdumpListener");
    this.excludedRefs = checkNotNull(excludedRefs, "excludedRefs");
    retainedKeys = new CopyOnWriteArraySet<>();
    queue = new ReferenceQueue<>();
  }

  /**
   * Identical to {@link #watch(Object, String)} with an empty string reference name.
   *
   * @see #watch(Object, String)
   */
  public void watch(Object watchedReference) {
    watch(watchedReference, "");
  }

  /**
   * Watches the provided references and checks if it can be GCed. This method is non blocking,
   * the check is done on the {@link WatchExecutor} this {@link RefWatcher} has been constructed
   * with.
   *
   * @param referenceName An logical identifier for the watched object.
   */
  public void watch(Object watchedReference, String referenceName) {
    if (this == DISABLED) {
      return;
    }
    checkNotNull(watchedReference, "watchedReference");
    checkNotNull(referenceName, "referenceName");
    final long watchStartNanoTime = System.nanoTime();
    //添加唯一的key值
    String key = UUID.randomUUID().toString();
    retainedKeys.add(key);
    //这是一个弱引用的子类拓展类 用于 我们之前所说的 watchedReference 和  queue 的联合使用
    final KeyedWeakReference reference =
        new KeyedWeakReference(watchedReference, key, referenceName, queue);
  //开启异步线程进行分析工作
    ensureGoneAsync(watchStartNanoTime, reference);
  }

  private void ensureGoneAsync(final long watchStartNanoTime, final KeyedWeakReference reference) {
    watchExecutor.execute(new Retryable() {
      @Override public Retryable.Result run() {
        //确认系统已经进行了内存回收了，不要进行误判了，这是容错性考虑
        return ensureGone(reference, watchStartNanoTime);
      }
    });
  }

  @SuppressWarnings("ReferenceEquality") // Explicitly checking for named null.
  Retryable.Result ensureGone(final KeyedWeakReference reference, final long watchStartNanoTime) {
    long gcStartNanoTime = System.nanoTime();
    long watchDurationMs = NANOSECONDS.toMillis(gcStartNanoTime - watchStartNanoTime);

    //清除已经到达引用队列的弱引用
    removeWeaklyReachableReferences();

    if (debuggerControl.isDebuggerAttached()) {
      // The debugger can create false leaks.
      return RETRY;
    }

    if (gone(reference)) {
      //已经不属于内存泄露
      return DONE;
    }
    //如果该对象没有改变可达性状态，调用gc来进行手动回收
    gcTrigger.runGc();
    removeWeaklyReachableReferences();
    if (!gone(reference)) {
      //已经出现了内存泄露
      long startDumpHeap = System.nanoTime();
      long gcDurationMs = NANOSECONDS.toMillis(startDumpHeap - gcStartNanoTime);

      ////dump 出 Head 报告
      File heapDumpFile = heapDumper.dumpHeap();
      if (heapDumpFile == RETRY_LATER) {
        // Could not dump the heap.
        return RETRY;
      }
      long heapDumpDurationMs = NANOSECONDS.toMillis(System.nanoTime() - startDumpHeap);
      //最后进行分析 这份 HeapDump
      //LeakCanary 分析内存泄露用的是一个第三方工具 HAHA 别笑 真的是这个名字
      heapdumpListener.analyze(
          new HeapDump(heapDumpFile, reference.key, reference.name, excludedRefs, watchDurationMs,
              gcDurationMs, heapDumpDurationMs));
    }
    return DONE;
  }

  private boolean gone(KeyedWeakReference reference) {
    return !retainedKeys.contains(reference.key);
  }

  private void removeWeaklyReachableReferences() {
    // WeakReferences are enqueued as soon as the object to which they point to becomes weakly
    // reachable. This is before finalization or garbage collection has actually happened.
    KeyedWeakReference ref;
    while ((ref = (KeyedWeakReference) queue.poll()) != null) {
      retainedKeys.remove(ref.key);
    }
  }
}
