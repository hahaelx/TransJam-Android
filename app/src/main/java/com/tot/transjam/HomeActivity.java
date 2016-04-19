package com.tot.transjam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tot.base.BaseActivity;
import com.tot.transjam.view.Toolbar;
import com.tot.transjam.data.StartData;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends BaseActivity implements Callback,View.OnClickListener {
    public static String TAG = "HomeActivity";
    public static final String BASE_URL = "https://bbsvacsho2.execute-api.ap-northeast-1.amazonaws.com/";
    public static StartData startData;
    Button myWords;
    Button newJam;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_home);
        myWords=(Button)findViewById(R.id.myWords);
        myWords.setOnClickListener(this);
        newJam=(Button)findViewById(R.id.newJam);
        newJam.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingBar();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransJamApiInterface transJamApiInterface = retrofit.create(TransJamApiInterface.class);

        Call<StartData> call = transJamApiInterface.getstart();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Response response) {
        if (response.body() instanceof StartData) {
            Log.d(TAG, "onResponse " + response.message());
            Log.d(TAG, "code " + response.code());
            HomeActivity.startData = (StartData) response.body();
            Log.d(TAG, "apiUrl " +  HomeActivity.startData.apiUrl);
            Log.d(TAG, "data.categoryList: " +  HomeActivity.startData.categoryList.size());
            Log.d(TAG, "data.updateState: " +  HomeActivity.startData.updateState);
        }
        dismissLoadingBar();
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d(TAG, "onFailure " + t);
        dismissLoadingBar();
    }

    @Override
    public void onClick(View v) {
        if(v==myWords){

        }else if(v==newJam){
            Intent intent=new Intent(this, CatActivity.class);
            startActivity(intent);
        }
    }
}
