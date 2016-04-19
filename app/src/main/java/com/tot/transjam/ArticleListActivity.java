package com.tot.transjam;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import com.tot.base.BaseActivity;
import com.tot.transjam.adapter.ArticleListAdapter;
import com.tot.transjam.adapter.OnRecyclerViewItemClickListener;
import com.tot.transjam.data.ArticleData;
import com.tot.transjam.data.ArticleListData;
import com.tot.transjam.data.CategoryData;
import com.tot.transjam.view.Toolbar;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by apple on 2016/3/12.
 */
public class ArticleListActivity extends BaseActivity implements Callback, View.OnClickListener, OnRecyclerViewItemClickListener,SwipeRefreshLayout.OnRefreshListener {
    final public static String TAG = "ArticleListActivity";
    public static final String BASE_URL = "https://bbsvacsho2.execute-api.ap-northeast-1.amazonaws.com/";
    SwipeRefreshLayout swipeRefreshLayout;
    ArticleListData articleListData;
    RecyclerView recyclerView;
    ArticleListAdapter articleListAdapter;
    Toolbar toolbar;
    CategoryData categoryData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryData=(CategoryData)getIntent().getSerializableExtra("categoryData");
        Log.d(TAG,"categoryData:"+categoryData.categoryName);
        setContentView(R.layout.activity_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(categoryData.categoryName);
        toolbar.setBlurryTitle();
        toolbar.setBack();
        toolbar.leftButton.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.catRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        recyclerView.getItemAnimator().setAddDuration(1000);
        recyclerView.getItemAnimator().setRemoveDuration(1000);
        recyclerView.getItemAnimator().setMoveDuration(1000);
        recyclerView.getItemAnimator().setChangeDuration(1000);
    }
    private void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransJamApiInterface transJamApiInterface = retrofit.create(TransJamApiInterface.class);
        Call<ArticleListData> call = transJamApiInterface.getarticle();
        call.enqueue(this);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        if (v == toolbar.leftButton) {
            onBackPressed();
        }
    }

    @Override
    public void onItemClick(View view, Object data) {
        Log.d(TAG, "onItemClick");
        if(data instanceof ArticleData){
            ArticleData articleData=(ArticleData)data;
            Log.d(TAG, "articleData.title = "+articleData.title);
            Intent intent = new Intent(this,ContentActivity.class);
            intent.putExtra("articleData", articleData);
            startActivity(intent);
        }
    }

    @Override
    public void onResponse(Response response) {
        Log.d(TAG, "onResponse");
        if (response.body() instanceof ArticleListData) {
            Log.d(TAG, "onResponse " + response.message());
            Log.d(TAG, "code " + response.code());
            articleListData = (ArticleListData) response.body();
            Log.d(TAG, "title " + articleListData.articleList.get(0).title);
            articleListAdapter = new ArticleListAdapter(this);
            articleListAdapter.setData(articleListData.articleList);
            articleListAdapter.setOnRecyclerViewItemClickListener(this);
            recyclerView.setAdapter(articleListAdapter);
        }
        swipeRefreshLayout.setRefreshing(false);
        dismissLoadingBar();
    }

    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void onStart() {
        super.onStart();
        showLoadingBar();
        getData();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh");
        getData();
    }
}
