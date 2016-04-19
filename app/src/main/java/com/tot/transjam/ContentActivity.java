package com.tot.transjam;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;
import com.tot.base.BaseActivity;
import com.tot.transjam.data.ArticleData;
import com.tot.transjam.data.ArticleDetail;
import com.tot.transjam.view.Toolbar;
import com.tot.transjam.view.TransView;
import com.tot.unit.UiTool;
import com.tot.view.ClickTextListener;
import com.tot.view.ClickTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by apple on 2016/1/7.
 */
public class ContentActivity extends BaseActivity implements Callback,ObservableScrollViewCallbacks, View.OnClickListener,ClickTextListener {
    public final static String TAG="ContentActivity";
    ImageView image;
    ObservableScrollView scrollable;
    ArticleData articleData;
    ArticleDetail articleDetail;
    Toolbar toolbar;
    ClickTextView title;
    TextView newsDate;
    TextView newsFrom;
    ClickTextView article;
    TransView transView;
    public static final String BASE_URL = "https://bbsvacsho2.execute-api.ap-northeast-1.amazonaws.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_content);
        showLoadingBar();
        articleData = (ArticleData)getIntent().getSerializableExtra("articleData");
        scrollable=(ObservableScrollView)findViewById(R.id.scrollable);
        scrollable.setScrollViewCallbacks(this);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentTitle();
        toolbar.leftButton.setOnClickListener(this);
        title = (ClickTextView)findViewById(R.id.title);
        title.setClickTextListener(this);
        newsDate = (TextView)findViewById(R.id.newsDate);
        newsFrom = (TextView)findViewById(R.id.newsFrom);
        article = (ClickTextView)findViewById(R.id.article);
        article.setClickTextListener(this);
        image = (ImageView)findViewById(R.id.image);
        transView = (TransView)findViewById(R.id.transView);


    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TransJamApiInterface transJamApiInterface = retrofit.create(TransJamApiInterface.class);
        Call<ArticleDetail> call = transJamApiInterface.getarticledetail();
        call.enqueue(this);
    }
    @Override
    public void onResponse(Response response) {
        Log.d(TAG, "onResponse");
        if (response.body() instanceof ArticleDetail) {
            articleDetail = (ArticleDetail) response.body();
            Log.d(TAG, "title=" + articleDetail.title);
            title.setClickText(articleDetail.title, Color.BLACK);
            newsDate.setText(articleDetail.updated);
            newsFrom.setText("from " + articleDetail.from + ", by " + articleDetail.by);
//            article.setClickText(Html.fromHtml("<h2 style=\"color:black;\">" + articleDetail.title + "</h2>" +
//                    "<font color=\"#4c4c4c\">"+articleDetail.article+"</font>"));
            article.setClickText(Html.fromHtml(articleDetail.article));
            UiTool.imageLoad(this, image, articleDetail.imageUrl);
        }
        dismissLoadingBar();
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d(TAG,"onFailure: "+t.getMessage());
        dismissLoadingBar();
    }
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if(transView.getVisibility()==View.VISIBLE){
            transView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            if (toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(toolbar) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(toolbar) == -toolbar.getHeight();
    }
    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-toolbar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(toolbar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(toolbar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(toolbar, translationY);
                ViewHelper.setTranslationY((View) scrollable, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) scrollable).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) scrollable).requestLayout();
            }
        });
        animator.start();
    }
    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

    @Override
    public void onClick(View v) {
        if(v==toolbar.leftButton){
            onBackPressed();
        }
    }


    @Override
    public void onTextClick(String text, int postionX, int postionY) {
        Log.d(TAG, "text: "+text);
        Log.d(TAG, "postionX: "+postionX);
        Log.d(TAG, "postionY: "+postionY);
        transView.setVocabulary(text);
        transView.setPosition(postionX, postionY);
        transView.setVisibility(View.VISIBLE);
    }

    private void popGoogleTranslate(String word){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String url=String.format("https://translate.google.com.tw/?hl=zh-TW#en/zh-TW/%s", word);
        Log.d(TAG, "url=" + url);
        WebView webView = new WebView(this);
        webView.loadUrl("https://translate.google.com.tw/m/translate?hl=zh-TW#auto/zh-TW/"+word);
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);
        WebChromeClient webChromeClient=new WebChromeClient();
        webView.setWebChromeClient(webChromeClient);
        alert.setView(webView);
        alert.show();
    }

}
