package com.tot.transjam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tot.transjam.CatActivity;
import com.tot.transjam.R;
import com.tot.transjam.data.ArticleData;
import com.tot.transjam.data.CategoryData;
import com.tot.unit.UiTool;

import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by apple on 2016/3/10.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {
    Context context;
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public List<ArticleData> articleList;

    public ArticleListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ArticleData> articleList) {
        this.articleList = articleList;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(CatActivity.TAG, "onCreateViewHolder");
        View itemLayoutView = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false);
        ArticleViewHolder viewHolder = new ArticleViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, final int position) {
        final ArticleData articleData = articleList.get(position);
        Log.d(CatActivity.TAG, "position=" + position);
        holder.title.setText(articleData.title);
        holder.newsDate.setText("2015/01/01 19:00");
        holder.newsFront.setText("by Evan Lee from TOT Studio.");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(CatActivity.TAG, "position=" + position);
                if (onRecyclerViewItemClickListener != null) {
                    onRecyclerViewItemClickListener.onItemClick(v, articleData);
                }
            }
        });
        holder.setPosition(position);
        UiTool.imageLoad(context, holder.image,articleData.imageUrl);
        UiTool.setRipple(holder.itemView);
//        setAnimation(holder.catTitle, position);
    }

    /**
     * Here is the key method to apply the animation
     */
    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ArticleViewHolder extends AnimateViewHolder {
        public View itemLayoutView;
        public TextView newsDate;
        public TextView newsFront;
        public TextView title;
        public ImageView image;
        public LinearLayout itemView;
        private int position;

        public ArticleViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.itemLayoutView = itemLayoutView;
            newsDate = (TextView) itemLayoutView.findViewById(R.id.newsDate);
            newsFront = (TextView) itemLayoutView.findViewById(R.id.newsFront);
            title = (TextView) itemLayoutView.findViewById(R.id.title);
            image = (ImageView) itemLayoutView.findViewById(R.id.image);
            itemView = (LinearLayout) itemLayoutView.findViewById(R.id.itemView);

        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void animateAddImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView)
                    .translationY(0)
                    .alpha(1)
                    .setDuration(300)
                    .setListener(listener)
                    .start();
        }

        @Override
        public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {
            ViewCompat.animate(itemView)
                    .translationY(0)
                    .alpha(1)
                    .setDuration(300)
                    .setListener(listener)
                    .start();
        }

    }
}
