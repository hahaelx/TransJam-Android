package com.tot.transjam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.tot.base.BaseActivity;
import com.tot.transjam.adapter.CatAdapter;
import com.tot.transjam.adapter.OnRecyclerViewItemClickListener;
import com.tot.transjam.data.CategoryData;
import com.tot.transjam.view.Toolbar;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by apple on 2016/3/12.
 */
public class CatActivity extends BaseActivity implements View.OnClickListener,OnRecyclerViewItemClickListener {
    final public static String TAG="CatActivity";
    RecyclerView catRecyclerView;
    CatAdapter catAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBlurryTitle();
        toolbar.leftButton.setOnClickListener(this);
        catRecyclerView = (RecyclerView)findViewById(R.id.catRecyclerView);
        catRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        catAdapter=new CatAdapter(this);
        catAdapter.setData(HomeActivity.startData.categoryList);
        catAdapter.setOnRecyclerViewItemClickListener(this);
        Log.d(TAG, "categoryList size" + HomeActivity.startData.categoryList.size());
        catRecyclerView.setAdapter(catAdapter);
        catRecyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        catRecyclerView.getItemAnimator().setAddDuration(1000);
        catRecyclerView.getItemAnimator().setRemoveDuration(1000);
        catRecyclerView.getItemAnimator().setMoveDuration(1000);
        catRecyclerView.getItemAnimator().setChangeDuration(1000);
    }

    @Override
    public void onClick(View v) {
        if(v==toolbar.leftButton){
            onBackPressed();
        }
    }

    @Override
    public void onItemClick(View view, Object data) {
        Log.d(TAG, "onItemClick");
        if(data instanceof CategoryData){
            CategoryData categoryData=(CategoryData)data;
            Log.d(TAG, "click categoryName = "+categoryData.categoryName);
            Intent intent = new Intent(this,ArticleListActivity.class);
            intent.putExtra("categoryData", categoryData);
            startActivity(intent);
        }
    }
}
