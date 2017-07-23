package com.example.administrator.myapplication1.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.administrator.myapplication1.R;
import com.example.administrator.myapplication1.customview.widget.CircleView;

public class CustomViewActivity extends AppCompatActivity {

    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        circleView = (CircleView) findViewById(R.id.circleView);
        circleView.smoothScrollTo();
    }
}
