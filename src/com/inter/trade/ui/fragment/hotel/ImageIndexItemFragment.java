package com.inter.trade.ui.fragment.hotel;


import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.trade.R;
import com.inter.trade.ui.AgentQueryWheelDateActivity;
import com.inter.trade.ui.HotelSelectPriceActivity;
import com.inter.trade.ui.HotelSelectStarLevelActivity;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityData;
import com.inter.trade.ui.fragment.hotel.HotelDetailDialogFragment.ImageAdapter;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.childfragment.BankFragment;
import com.inter.trade.ui.func.childfragment.ChildIndexItem;
import com.inter.trade.ui.func.childfragment.FavourFragment;
import com.inter.trade.ui.func.childfragment.MoreFragment;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.PromptUtil;

/**
 * 酒店预订 图片滑动展示
 * @author haifengli
 *
 */
public class ImageIndexItemFragment extends IBaseFragment implements OnTouchListener{
	
	private static final String TAG = ImageIndexItemFragment.class.getName();
	
	private View rootView;
//	private ImageView img;
	private String url;
	
	private Bundle data = null;
//	private HotelListData mHotelListData;
	
	private ImageView imgview;
    private ImageView img;
  
 /////////////////////////////    
    private Matrix matrix=new Matrix();
    private Matrix savedMatrix=new Matrix();
     
    static final int NONE = 0; 
    static final int DRAG = 1; 
    static final int ZOOM = 2; 
    int mode = NONE; 
 
    // Remember some things for zooming 
    PointF start = new PointF(); 
    PointF mid = new PointF(); 
    float oldDist = 1f; 
/////////////////////////////	
	
	
	public static ImageIndexItemFragment create(String url) {
		final ImageIndexItemFragment f = new ImageIndexItemFragment();

		final Bundle args = new Bundle();
		args.putString("URL", url);
		f.setArguments(args);
		return f;
	}

	public ImageIndexItemFragment() {

	}
	
	public static ImageIndexItemFragment newInstance (Bundle data) {
		ImageIndexItemFragment fragment = new ImageIndexItemFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		Bundle bundle = getArguments();
		if(bundle != null) {
			data = bundle;
			url=data.getString("URL");
//			mHotelListData = (HotelListData) data.getSerializable("hotelDetail");
		}
		
		
	}
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hotel_view_page_item_test_layout, container,false);
//		rootView = inflater.inflate(R.layout.hotel_view_page_item_layout, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
//		if(img != null && url != null){
//			FinalBitmap.create(getActivity()).display(img, url);
//		}
	}

	@Override
	public void onRefreshDatas() {
		Log.i(TAG, "onRefreshDatas");
	}

	private void initViews (View view) {
//		img = (ImageView) view.findViewById(R.id.image);
		
		/////////////////////////////////////////
		imgview=(ImageView)view.findViewById(R.id.imag1);
//		imgview.setAnimation(AnimationUtils.loadAnimation(this, R.anim.newanim));
		
//		img=(ImageView)view.findViewById(R.id.imag);
//		Matrix mt=img.getImageMatrix();
//		mt.postScale(0.5f,0.5f);mt.postScale(1.5f,1.5f);
//		mt.postRotate(30, 130, 100);
//		mt.postTranslate(100, 10);
//		
//		img.setImageMatrix(mt);
		
		imgview.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.tfb_picture));
		imgview.setOnTouchListener(this);
		imgview.setLongClickable(true);
		/////////////////////////////
	}
	
	
	private float spacing(MotionEvent event) { 
        float x = event.getX(0) - event.getX(1); 
        float y = event.getY(0) - event.getY(1); 
        return FloatMath.sqrt(x * x + y * y); 
} 
 
  
      private void midPoint(PointF point, MotionEvent event) { 
        float x = event.getX(0) + event.getX(1); 
        float y = event.getY(0) + event.getY(1); 
        point.set(x / 2, y / 2); 
} 
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        /*
        Log.d("Infor", "类别:"+event.getAction());
        Log.d("Infor", "mask:"+event.getActionMasked());
        Log.d("Infor", "index:"+event.getActionIndex());
        Log.d("Infor", "points:"+event.getPointerCount());*/
        Log.d("Infor", "size:"+event.getSize());
        if(event.getActionMasked()==MotionEvent.ACTION_POINTER_UP)
            Log.d("Infor", "多点操作");
        switch(event.getActionMasked()){
        case MotionEvent.ACTION_DOWN:
              matrix.set(imgview.getImageMatrix());
              savedMatrix.set(matrix);
              start.set(event.getX(),event.getY());
              Log.d("Infor", "触摸了...");
              mode=DRAG;
              break;
        case MotionEvent.ACTION_POINTER_DOWN:  //多点触控
             oldDist=this.spacing(event);
            if(oldDist>10f){
             Log.d("Infor", "oldDist"+oldDist);
             savedMatrix.set(matrix);
             midPoint(mid,event);
             mode=ZOOM;
            }
            break;
        case MotionEvent.ACTION_POINTER_UP:
            mode=NONE;
            break;
        case MotionEvent.ACTION_MOVE:
            if(mode==DRAG){         //此实现图片的拖动功能...
                matrix.set(savedMatrix);
                matrix.postTranslate(event.getX()-start.x, event.getY()-start.y);
            }
                else if(mode==ZOOM){// 此实现图片的缩放功能...
             float newDist=spacing(event);
             if(newDist>10){
                 matrix.set(savedMatrix);
                 float scale=newDist/oldDist;
                 matrix.postScale(scale, scale, mid.x, mid.y);              
             }
                }
            break;
        }
        imgview.setImageMatrix(matrix);
        return false;
    }
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}


}
