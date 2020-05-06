package com.example.jiajiemu.blockdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_1)
    TextView textView;
    @BindView(R.id.tv_2)
    TextView textView2;

    @OnClick(R.id.tv_1)
    void onClick(View view) {
        textView.setText("我被click了");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        textView.setText("123");
        textView2.setText("456");
    }
}
