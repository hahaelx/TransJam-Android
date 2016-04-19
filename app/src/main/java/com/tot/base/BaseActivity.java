package com.tot.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tot.transjam.R;

import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by apple on 2016/1/2.
 */
public class BaseActivity extends AppCompatActivity {
    ProgressBar progressBar;
    RelativeLayout loadingLayout;
    boolean isLoading=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            // Translucent status bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void showLoadingBar(){
        isLoading=true;
        if(loadingLayout==null){
            loadingLayout=(RelativeLayout)findViewById(R.id.loadingLayout);
        }

        loadingLayout.setVisibility(View.VISIBLE);
        if(progressBar==null){
            progressBar = (ProgressBar)findViewById(R.id.progressbar);
            progressBar.setIndeterminateDrawable(new CircularProgressDrawable
                    .Builder(this)
                    .colors(getResources().getIntArray(R.array.gplus_colors))
                    .sweepSpeed(1f).build());
        }

    }
    public void dismissLoadingBar(){
        loadingLayout.setVisibility(View.GONE);
        isLoading=false;
    }

    @Override
    public void onBackPressed() {
        if(!isLoading){
            super.onBackPressed();
        }

    }
}
