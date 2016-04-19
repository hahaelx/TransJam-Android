package com.tot.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by apple on 2016/3/29.
 */
public class ClickTextView extends TextView {
    private static final int MAX_CLICK_DURATION = 300;
    private long startClickTime;
    public ClickTextListener clickTextListener;
    String TAG = "ClickTextView";
    int positionX;
    int positionY;
    int fontColor = Color.parseColor("#4c4c4c");
    private Context context;

    public ClickTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ClickTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ClickTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public void setClickTextListener(ClickTextListener clickTextListener) {
        this.clickTextListener = clickTextListener;
    }

    private void init() {
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setClickText(CharSequence text, int color) {
        this.fontColor = color;
        setClickText(text);
    }

    public void setClickText(CharSequence text) {
        setText(text, BufferType.SPANNABLE);
        Spannable spans = (Spannable) getText();
        Integer[] indices = getIndices(getText().toString(), ' ');
        int start = 0;
        int end = 0;
        for (int i = 0; i <= indices.length; i++) {
            ClickableSpan clickSpan = getClickableSpan();
            // to cater last/only word
            end = (i < indices.length ? indices[i] : spans.length());
            spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end + 1;
        }
    }

    private ClickableSpan getClickableSpan() {

        return new ClickableSpan() {

            private Rect getPosition(TextView parentTextView) {
                Rect parentTextViewRect = new Rect();
                SpannableString completeText = (SpannableString) (parentTextView).getText();
                Layout textViewLayout = parentTextView.getLayout();

                double startOffsetOfClickedText = completeText.getSpanStart(this);
                double endOffsetOfClickedText = completeText.getSpanEnd(this);
                double startXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int) startOffsetOfClickedText);
                double endXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int) endOffsetOfClickedText);


                // Get the rectangle of the clicked text
                int currentLineStartOffset = textViewLayout.getLineForOffset((int) startOffsetOfClickedText);
                int currentLineEndOffset = textViewLayout.getLineForOffset((int) endOffsetOfClickedText);
                boolean keywordIsInMultiLine = currentLineStartOffset != currentLineEndOffset;
                textViewLayout.getLineBounds(currentLineStartOffset, parentTextViewRect);


                // Update the rectangle position to his real position on screen
                int[] parentTextViewLocation = {0, 0};
                parentTextView.getLocationOnScreen(parentTextViewLocation);

                double parentTextViewTopAndBottomOffset = (
                        parentTextViewLocation[1] -
                                parentTextView.getScrollY() +
                                parentTextView.getCompoundPaddingTop()
                );
                parentTextViewRect.top += parentTextViewTopAndBottomOffset;
                parentTextViewRect.bottom += parentTextViewTopAndBottomOffset;

                // In the case of multi line text, we have to choose what rectangle take
                if (keywordIsInMultiLine) {
                    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                    int screenHeight = metrics.heightPixels;
                    int dyTop = parentTextViewRect.top;
                    int dyBottom = screenHeight - parentTextViewRect.bottom;
                    boolean onTop = dyTop > dyBottom;

                    if (onTop) {
                        endXCoordinatesOfClickedText = textViewLayout.getLineRight(currentLineStartOffset);
                    } else {
                        parentTextViewRect = new Rect();
                        textViewLayout.getLineBounds(currentLineEndOffset, parentTextViewRect);
                        parentTextViewRect.top += parentTextViewTopAndBottomOffset;
                        parentTextViewRect.bottom += parentTextViewTopAndBottomOffset;
                        startXCoordinatesOfClickedText = textViewLayout.getLineLeft(currentLineEndOffset);
                    }

                }

                parentTextViewRect.left += (
                        parentTextViewLocation[0] +
                                startXCoordinatesOfClickedText +
                                parentTextView.getCompoundPaddingLeft() -
                                parentTextView.getScrollX()
                );
                parentTextViewRect.right = (int) (
                        parentTextViewRect.left +
                                endXCoordinatesOfClickedText -
                                startXCoordinatesOfClickedText
                );
                Log.v("characters position", "this.parentTextViewRect.top="+parentTextViewRect.top);
                Log.v("characters position", "this.parentTextViewRect.bottom="+parentTextViewRect.bottom);
                Log.v("characters position", "this.parentTextViewRect.right="+parentTextViewRect.right);
                Log.v("characters position", "this.parentTextViewRect.left="+parentTextViewRect.left);
                return parentTextViewRect;
            }

            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                String string = tv.getText().subSequence(tv.getSelectionStart(),
                        tv.getSelectionEnd()).toString();
                SpannableString completeText = (SpannableString) ((TextView) widget).getText();
                Log.v("characters position", completeText.getSpanStart(this) + " / " + completeText.getSpanEnd(this));
                Rect rect = getPosition(tv);
                if (clickTextListener != null) {
                    clickTextListener.onTextClick(string, rect.left+((rect.right-rect.left)/2), rect.top);
                }
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(fontColor);
                ds.setUnderlineText(false);
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startClickTime = Calendar.getInstance().getTimeInMillis();
                break;
            }
            case MotionEvent.ACTION_UP: {
                long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                if (clickDuration < MAX_CLICK_DURATION) {
                    //click event has occurred
                    positionX = x;
                    positionY = y;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public static Integer[] getIndices(String s, char c) {
        int pos = s.indexOf(c, 0);
        List<Integer> indices = new ArrayList<Integer>();
        while (pos != -1) {
            indices.add(pos);
            pos = s.indexOf(c, pos + 1);
        }
        return (Integer[]) indices.toArray(new Integer[0]);
    }
}
