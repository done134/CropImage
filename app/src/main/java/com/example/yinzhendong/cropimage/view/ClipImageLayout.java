package com.example.yinzhendong.cropimage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.yinzhendong.cropimage.R;

/**
 * Created by YinZhendong on 2017/12/19.
 */

public class ClipImageLayout extends RelativeLayout {

    private PinchImageView mZoomImageView;

    private ClipImageBorderView mBorderView;

    public ClipImageLayout(Context context) {
        super(context);
        initView(context,null);
    }

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setBackgroundResource(android.R.color.black);
        mBorderView = new ClipImageBorderView(context);
        mZoomImageView = new PinchImageView(context);

        mBorderView.setHorizontalPadding(100);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mZoomImageView, lp);
        addView(mBorderView, lp);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mZoomImageView.setImageBitmap(bitmap);
    }
}
