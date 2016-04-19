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
import android.widget.Button;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.tot.transjam.CatActivity;
import com.tot.transjam.R;
import com.tot.transjam.data.CategoryData;
import com.tot.unit.UiTool;

import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by apple on 2016/3/10.
 */
public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {
    Context context;
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    int color[] = {
            Color.parseColor("#cbc6c0"),
            Color.parseColor("#8d807a"),
            Color.parseColor("#6f6c67"),
            Color.parseColor("#9f968f"),
            Color.parseColor("#967a6f"),
            Color.parseColor("#a48b77")
    };
    public List<CategoryData> categoryList;

    public CatAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<CategoryData> categoryList) {
        Log.d(CatActivity.TAG, "setData");
        this.categoryList = categoryList;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(CatActivity.TAG, "onCreateViewHolder");
        View itemLayoutView = LayoutInflater.from(context).inflate(R.layout.cat_item, parent,false);
        CatViewHolder viewHolder = new CatViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CatViewHolder holder, final int position) {
        Log.d(CatActivity.TAG, "position=" + position);
        Log.d(CatActivity.TAG, "categoryName=" + categoryList.get(position).categoryName);
        Log.d(CatActivity.TAG, "color size=" + color.length);
        UiTool.setRipple(holder.catTitle);
        holder.catTitle.setText(categoryList.get(position).categoryName);
        holder.catTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewItemClickListener != null) {
                    onRecyclerViewItemClickListener.onItemClick(v, categoryList.get(position));
                }
            }
        });
        holder.setPosition(position);
        if (position < color.length) {
            holder.catTitle.setBackgroundColor(color[position]);
        } else {
            int p = position % color.length;
            holder.catTitle.setBackgroundColor(color[p]);
        }
        setAnimation(holder.catTitle, position);
    }

    /**
     * Here is the key method to apply the animation
     */
    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CatViewHolder extends AnimateViewHolder {
        public TextView catTitle;
        private int position;

        //        public MaterialRippleLayout ripple;
        public CatViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            catTitle = (TextView) itemLayoutView.findViewById(R.id.catTitle);
//            ripple = (MaterialRippleLayout) itemLayoutView.findViewById(R.id.ripple);
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
