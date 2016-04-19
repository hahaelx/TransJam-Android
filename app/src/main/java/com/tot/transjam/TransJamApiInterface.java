package com.tot.transjam;

import com.tot.transjam.data.ArticleDetail;
import com.tot.transjam.data.ArticleListData;
import com.tot.transjam.data.StartData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * Created by apple on 2016/1/28.
 */
public interface TransJamApiInterface {
    @GET("prod/getstart")
    Call<StartData> getstart();

    @GET("prod/getarticle")
    Call<ArticleListData> getarticle();

    @GET("prod/getarticledetail")
    Call<ArticleDetail> getarticledetail();
}
