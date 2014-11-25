package com.inter.trade.ui.fragment.checking;

import com.inter.trade.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 自定义手势密码提示
 * @author zhichao.huang
 *
 */
public class LockHintIndicator extends View {
	private static final String TAG = "LockHintIndicator";
	private int count;
	private float space, radius;
	private int point_normal_color, point_seleted_color;
	
	public static boolean DEBUG = true;

	// 选中
	private int seleted = 0;

	// background seleted normal

	public LockHintIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.LockHintIndicator);

		count = a.getInteger(R.styleable.LockHintIndicator_count, 9);
		space = a.getDimension(R.styleable.LockHintIndicator_space, 9);
		radius = a.getDimension(R.styleable.LockHintIndicator_point_radius, 9);

		point_normal_color = a.getColor(
				R.styleable.LockHintIndicator_point_normal_color, 0x000000);
		point_seleted_color = a.getColor(
				R.styleable.LockHintIndicator_point_seleted_color, 0xffff07);

		int sum = attrs.getAttributeCount();
		if (DEBUG) {
			String str = "";
			for (int i = 0; i < sum; i++) {
				String name = attrs.getAttributeName(i);
				String value = attrs.getAttributeValue(i);
				str += "attr_name :" + name + ": " + value + "\n";
			}
			Log.i("attribute", str);
		}
		a.recycle();
	}

	public void setSeletion(int index) {
		this.seleted = index;
		invalidate();
	}

	public void setCount(int count) {
		this.count = count;
		invalidate();
	}

	public void next() {
		if (seleted < count - 1)
			seleted++;
		else
			seleted = 0;
		invalidate();
	}

	public void previous() {
		if (seleted > 0)
			seleted--;
		else
			seleted = count - 1;
		invalidate();
	}
	
	String lockstring ;
	
	/**
	 * 绘制所有选择的点
	 * @param lockstr  如01234
	 */
	public void setSeletions(String lockstr) {
		lockstring = lockstr;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		float width = (getWidth() - ((radius * 2 * 3) + (space * (3 - 1)))) / 2.f;
		for(int j = 0; j < 3; j ++) {
			for(int i= 0; i < 3; i ++) {
				
				if(lockstring != null && !lockstring.equals("")) {
					
					if(j == 0 && i == 0){
						if(lockstring.contains("0")){
							paint.setColor(point_seleted_color);
							paint.setStyle(Paint.Style.FILL_AND_STROKE);
							paint.setStrokeWidth(2); 
						}else{
							paint.setColor(point_normal_color);
							paint.setStyle(Paint.Style.STROKE);
					        paint.setStrokeWidth(2); 
						}
					}else if(j == 0 && i == 1) {
						if(lockstring.contains("1")){
							paint.setColor(point_seleted_color);
							paint.setStyle(Paint.Style.FILL_AND_STROKE);
							paint.setStrokeWidth(2); 
						}else{
							paint.setColor(point_normal_color);
							paint.setStyle(Paint.Style.STROKE);
					        paint.setStrokeWidth(2); 
						}
					}else if(j == 0 && i == 2) {
						if(lockstring.contains("2")){
							paint.setColor(point_seleted_color);
							paint.setStyle(Paint.Style.FILL_AND_STROKE);
							paint.setStrokeWidth(2); 
						}else{
							paint.setColor(point_normal_color);
							paint.setStyle(Paint.Style.STROKE);
					        paint.setStrokeWidth(2); 
						}
					}else if(j == 1 && i == 0){
						if(lockstring.contains("3")){
							paint.setColor(point_seleted_color);
							paint.setStyle(Paint.Style.FILL_AND_STROKE);
							paint.setStrokeWidth(2); 
						}else{
							paint.setColor(point_normal_color);
							paint.setStyle(Paint.Style.STROKE);
					        paint.setStrokeWidth(2); 
						}
					}else if(j == 1 && i == 1) {
						if(lockstring.contains("4")){
							paint.setColor(point_seleted_color);
							paint.setStyle(Paint.Style.FILL_AND_STROKE);
							paint.setStrokeWidth(2); 
						}else{
							paint.setColor(point_normal_color);
							paint.setStyle(Paint.Style.STROKE);
					        paint.setStrokeWidth(2); 
						}
					}else if(j == 1 && i == 2) {
						if(lockstring.contains("5")){
							paint.setColor(point_seleted_color);
							paint.setStyle(Paint.Style.FILL_AND_STROKE);
							paint.setStrokeWidth(2); 
						}else{
							paint.setColor(point_normal_color);
							paint.setStyle(Paint.Style.STROKE);
					        paint.setStrokeWidth(2); 
						}
					}else if(j == 2 && i == 0){
						if(lockstring.contains("6")){
							paint.setColor(point_seleted_color);
							paint.setStyle(Paint.Style.FILL_AND_STROKE);
							paint.setStrokeWidth(2); 
						}else{
							paint.setColor(point_normal_color);
							paint.setStyle(Paint.Style.STROKE);
					        paint.setStrokeWidth(2); 
						}
					}else if(j == 2 && i == 1) {
						if(lockstring.contains("7")){
							paint.setColor(point_seleted_color);
							paint.setStyle(Paint.Style.FILL_AND_STROKE);
							paint.setStrokeWidth(2); 
						}else{
							paint.setColor(point_normal_color);
							paint.setStyle(Paint.Style.STROKE);
					        paint.setStrokeWidth(2); 
						}
//						}
					}else if(j == 2 && i == 2) {
						if(lockstring.contains("8")){
							paint.setColor(point_seleted_color);
							paint.setStyle(Paint.Style.FILL_AND_STROKE);
							paint.setStrokeWidth(2); 
						}else{
							paint.setColor(point_normal_color);
							paint.setStyle(Paint.Style.STROKE);
					        paint.setStrokeWidth(2); 
						}
					}
					
				}else {
					paint.setColor(point_normal_color);
					paint.setStyle(Paint.Style.STROKE);
			        paint.setStrokeWidth(2); 
				}
					
				canvas.drawCircle(width + getPaddingLeft() + radius + i
						* (space + radius + radius), 
						width  + radius + j
						* (space + radius + radius), radius, paint);
			}
				
		}
			
			
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) (getPaddingLeft() + getPaddingRight()
					+ (count * 2 * radius) + (count - 1) * radius + 1);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) (2 * radius + getPaddingTop() + getPaddingBottom() + 1);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

}
