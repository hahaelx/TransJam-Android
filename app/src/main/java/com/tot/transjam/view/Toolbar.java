package com.tot.transjam.view;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.tot.transjam.R;

/**
 * Created by apple on 2016/1/9.
 */
public class Toolbar extends RelativeLayout {
    Context context;
    public FloatingActionButton leftButton;
    public ImageView logo;
    public TextView title;

    public Toolbar(Context context) {
        super(context);
        init(context);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_toolbar, this, true);
        leftButton = (FloatingActionButton) findViewById(R.id.leftButton);
        logo = (ImageView) findViewById(R.id.logo);
        title = (TextView) findViewById(R.id.title);
    }

    public void setTitle(String text) {
        logo.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        title.setText(text);
    }

    public void setBlurryTitle() {
        this.setBackgroundResource(R.drawable.bar_1);
    }

    public void setBack() {
        leftButton.setImageResource(R.drawable.back);
    }
    public void setContentTitle() {
        leftButton.setImageResource(R.drawable.back);
        logo.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
    }

}
