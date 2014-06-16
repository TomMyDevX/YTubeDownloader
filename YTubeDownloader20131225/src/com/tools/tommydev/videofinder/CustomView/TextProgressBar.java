package com.tools.tommydev.videofinder.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.tools.tommydev.videofinder.R;

/**
 * Created by tommydev on 8/22/13.
 */
public class TextProgressBar extends ProgressBar {

    private String text = "";
    private int textColor = Color.BLACK;
    private float textSize = 15;
    private boolean ShowText=true;
    public TextProgressBar(Context context) {
        super(context);
    }
    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttrs(attrs);
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttrs(attrs);
    }

    private void setAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextProgressBar, 0, 0);
            setText(a.getString(R.styleable.TextProgressBar_text));
            setTextColor(a.getColor(R.styleable.TextProgressBar_textColor, Color.BLACK));
            setTextSize(a.getDimension(R.styleable.TextProgressBar_textSize, 15));
           // setBackgroundColor(Color.argb(255,0,0,0));
            a.recycle();
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(ShowText){
            canvas.drawARGB(50,0,0,0);
            Paint textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setColor(textColor);
            textPaint.setTextSize(textSize);
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);
            int x = getWidth() / 2 - bounds.centerX();
            int y = getHeight() / 2 - bounds.centerY();
            canvas.drawText(text, x, y, textPaint);
        }


    }

    public void setShowText(Boolean ShowText){
            this.ShowText=ShowText;
    }


    public String getText() {
        return text;
    }

    public synchronized void setText(String text) {
        if (text != null) {
            this.text = text;
        } else {
            this.text = "";
        }
        postInvalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public synchronized void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public synchronized void setTextSize(float textSize) {
        this.textSize = textSize;
        postInvalidate();
    }
}