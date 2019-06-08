package com.sjk.zcmu.score.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.sjk.zcmu.score.R;

import androidx.annotation.Nullable;

public class Divider extends LinearLayout {
    public Divider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_divider, this);
    }
}
