package com.xedus.tutorials.utils;
import java.util.Hashtable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.xedus.tutorials.R;
public class TextViewPlus extends TextView {
    private static final String TAG = "TextView";
    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();
    public TextViewPlus(Context context) {
        super(context);
    }

    public TextViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        String customFont = a.getString(R.styleable.TextViewPlus_fontName);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = fontCache.get(asset);
        if(tf == null) {
            try {
            	tf = Typeface.createFromAsset(ctx.getAssets(), asset);
            }
            catch (Exception e) {
            	Log.e(TAG, "Could not get typeface: "+e.getMessage());
                return false;
            }
            fontCache.put(asset, tf);
        }
        setTypeface(tf);  
        return true;
    }

}