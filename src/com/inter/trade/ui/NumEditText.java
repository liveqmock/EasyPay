package com.inter.trade.ui;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class NumEditText extends EditText{
	private static final String TAG= "NumEditText";
	public NumEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setListener();
	}

	public NumEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setListener();
	}

	public NumEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setListener();
	}
	
	private void setListener()
	{
//		this.setOnFocusChangeListener(focusChangeListener);
		this.addTextChangedListener(watcher);
		InputFilter[] filters = {new LengthFilter(8)};
		this.setFilters(filters);
	}
	
	OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			
		}
	};
	
	TextWatcher watcher = new TextWatcher() {
		boolean isZeroFirst=false;
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable edt) {
			// TODO Auto-generated method stub
			 String temp = edt.toString();
			//只能输入两位小数
	          int posDot = temp.indexOf(".");
	          
			 //不能连续输入
//			 int firstIndex  =  temp.lastIndexOf('0');
//	          if(firstIndex==0 ){
//	        	  	isZeroFirst=true;
//	          }
//	          
//	          if(temp.length()>1 && temp.charAt(0)!='0'){
//	        	  	isZeroFirst=false;
//	          }
//	          
//	          if(isZeroFirst && posDot!=1&& temp.length()>=2){
//	        	  	edt.delete(1, 2);
//	          }
			 
			 
	          
	          if (posDot <= 0) return;
	          if (temp.length() - posDot - 1 > 2)
	          {
	              edt.delete(posDot + 3, posDot + 4);
	          }
	         
	          
		}
	};
}
