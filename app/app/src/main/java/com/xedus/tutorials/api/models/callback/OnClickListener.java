package com.xedus.tutorials.api.models.callback;

import android.view.View;

/**
 * Created by PRAV on 22-06-2016.
 */
public interface OnClickListener {
    void onClickListener(View view, int position);
    void onLongClickListener(View view,int position);
}
