package com.tot.transjam.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tot.transjam.R;
import com.tot.unit.UiTool;

/**
 * Created by apple on 2016/4/2.
 */
public class TransView extends RelativeLayout implements View.OnClickListener {
    String TAG = "TransView";
    Context context;
    public TextView vocabulary;
    public TextView explanation;
    public ImageButton addWord;
    public Button more;
    int padding = 20;
    int height;
    int arrowWidth;
    Animation zoomin;
    Animation zoomout;
    View arrowUp;
    View arrowDown;

    public TransView(Context context) {
        super(context);
        init(context);
    }

    public TransView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setVocabulary(String str) {
        vocabulary.setText(str);
    }

    private void init(Context context) {
        this.context = context;
        zoomin = AnimationUtils.loadAnimation(context, R.anim.zoomin);
        zoomout = AnimationUtils.loadAnimation(context, R.anim.zoomout);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_transview, this, true);
        vocabulary = (TextView) findViewById(R.id.vocabulary);
        explanation = (TextView) findViewById(R.id.explanation);
        addWord = (ImageButton) findViewById(R.id.addWord);
        addWord.setOnClickListener(this);
        more = (Button) findViewById(R.id.more);
        more.setOnClickListener(this);
        arrowUp = findViewById(R.id.arrow_up);
        arrowDown = findViewById(R.id.arrow_down);
        UiTool.setRipple(addWord);
        UiTool.setRipple(more);
        height = (int) UiTool.convertDpToPixel(147, context);
        padding = (int) UiTool.convertDpToPixel(padding, context);
    }

    public void setPosition(int postionX, int postionY) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
        Log.d(TAG, "height=" + height);
        if (postionY < height) {
            //show up
            ViewGroup.MarginLayoutParams arrowParams = (ViewGroup.MarginLayoutParams) arrowUp.getLayoutParams();
            arrowParams.setMargins(postionX-(arrowUp.getWidth())/2,0,0,0);
            Log.d(TAG, "postionY<height :" + (postionY ));
            params.setMargins(0, postionY , 0, 0);
            arrowUp.setVisibility(View.VISIBLE);
            arrowDown.setVisibility(View.GONE);
        } else {
            // show down
            ViewGroup.MarginLayoutParams arrowParams = (ViewGroup.MarginLayoutParams) arrowDown.getLayoutParams();
            arrowParams.setMargins(postionX-(arrowDown.getWidth())/2,0,0,0);
            Log.d(TAG, "postionY>=height :" + (postionY - height));
            params.setMargins(0, postionY - height , 0, 0);
            arrowUp.setVisibility(View.GONE);
            arrowDown.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            if (visibility == VISIBLE) {
                if (zoomin != null) startAnimation(zoomin);
            } else if ((visibility == INVISIBLE) || (visibility == GONE)) {
                if (zoomout != null) startAnimation(zoomout);
            }
        }
        super.setVisibility(visibility);
    }

    @Override
    public void onClick(View view) {
        if (view == addWord) {
            if (addWord.getDrawable().getLevel() == 0) {
                addWord.getDrawable().setLevel(1);
            } else {
                addWord.getDrawable().setLevel(0);
            }

        } else if (view == more) {
            String url = "https://translate.google.com.tw/m/translate?hl=zh-TW#auto/zh-TW/"+vocabulary.getText().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        }
    }

}

