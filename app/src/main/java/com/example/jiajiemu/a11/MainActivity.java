package com.example.jiajiemu.a11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Button mBtn;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void synRequest(){
        Request request = new Request.Builder().url("http://www.baidu.com")
                .get().build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
    public void asyRequest(){
        Request request = new Request.Builder().url("http://www.baidu.com")
                .get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println(response.body().string());

            }
        });
    }
    public void httpCache(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(8, TimeUnit.SECONDS);  //设置连接超时时间
        builder.writeTimeout(8, TimeUnit.SECONDS);//设置写入超时时间
        builder.readTimeout(8, TimeUnit.SECONDS);//设置读取数据超时时间
        builder.retryOnConnectionFailure(false);//设置不进行连接失败重试
        client = builder.build();
    }
    String url = "http://图片地址";

    public void loadImageSimple(View view){
        Glide
                .with(getApplicationContext())
                .load(url)
                .into(imageView);
    }

    public void loadImage(View view) {
        Glide.with(getApplicationContext()) // 指定Context
                .load(url)// 指定图片的URL
                .placeholder(R.mipmap.ic_launcher)// 指定图片未成功加载前显示的图片
                .error(R.mipmap.ic_launcher)// 指定图片加载失败显示的图片
                .override(300, 300)//指定图片的尺寸
                .fitCenter()//指定图片缩放类型为
                .centerCrop()// 指定图片缩放类型为
                .skipMemoryCache(true)// 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//仅仅只缓存原来的全分辨率的图像
                .diskCacheStrategy(DiskCacheStrategy.RESULT)//仅仅缓存最终的图像
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
                .priority(Priority.HIGH)//指定优先级.Glide将会用他们作为一个准则，并尽可能的处理这些请求，但是它不能保证所有的图片都会按照所要求的顺序加载。优先级排序:IMMEDIATE > HIGH > NORMAL >　LOW
                .into(imageView);//指定显示图片的Imageview
    }

    public void cacheOkhttpRequest(){
        OkHttpClient client=new OkHttpClient
                .Builder()
                .cache(new Cache(new File("cache"),24*1024*1024)).build();

        Request request=new Request.Builder().url("http://www.baidu.com").build();

        Call call=client.newCall(request);

        try {
            Response response=call.execute();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //异步请求
        call.enqueue(new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    //请求处理,输出结果
                System.out.println(response.body());
            }
        });

        //同步
        try {
            Response response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }













    public void loadSimpleImage(View view){
        String url ="";
        Glide.with(this).load(url).into(imageView);
    }



    public void netRequestWithRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);
        Call repos = (Call) service.listRepos("XXXX");
        repos.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }

        });
    }




    public void MyRetrofitExample(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") // 设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();
        MyInterface myInterface = retrofit.create(MyInterface.class);
        Call call = (Call) myInterface.getCall();


    }



}
