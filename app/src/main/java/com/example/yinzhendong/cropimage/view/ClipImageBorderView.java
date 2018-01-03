package com.example.yinzhendong.cropimage.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @ClassName: ClipImageBorderView
 * @Description: 图片裁剪功能白色边框
 * @author xiechengfa2000@163.com
 */
public class ClipImageBorderView extends View {
	private int horizontalPadding = 1;
	private int verticalPadding;
	private int borderWidth = 1;
	private Paint paint;
	private int width;
	private int borderColor = Color.parseColor("#FFFFFF");

	public ClipImageBorderView(Context context) {
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		horizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalPadding, getResources().getDisplayMetrics());
		borderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, borderWidth, getResources().getDisplayMetrics());
		paint = new Paint();
		paint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		width = getWidth() - horizontalPadding*2;
		verticalPadding = (getHeight() - width) / 2;

		paint.setColor(Color.parseColor("#AA000000"));
		paint.setStyle(Style.FILL);
		canvas.drawRect(0, 0, horizontalPadding, getHeight(), paint);
		canvas.drawRect(getWidth() - horizontalPadding, 0, getWidth(), getHeight(), paint);
		canvas.drawRect(horizontalPadding, 0, getWidth() - horizontalPadding, verticalPadding, paint);
		canvas.drawRect(horizontalPadding, getHeight() - verticalPadding, getWidth() - horizontalPadding, getHeight(), paint);

		paint.setColor(borderColor);
		paint.setStrokeWidth(borderWidth);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(horizontalPadding, verticalPadding, getWidth() - horizontalPadding, getHeight() - verticalPadding, paint);
	}

	public void setHorizontalPadding(int horizontalPadding) {
		this.horizontalPadding = horizontalPadding;
	}

	public void setVerticalPadding(int verticalPadding) {
		this.verticalPadding = verticalPadding;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}

}