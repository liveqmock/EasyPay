package com.inter.trade.ui.fragment.agent;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.imageframe.ImageCache;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.AgentApplyActivity;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.DaikuanActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.UserInfoFragment;
import com.inter.trade.ui.fragment.agent.util.AgentData;
import com.inter.trade.ui.fragment.agent.util.AgentParser;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanNoParser;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

@SuppressLint("ValidFragment")
public class AgentApplyFragment extends BaseFragment implements OnClickListener{
	/***
	 * 使用照相机拍照获取图片
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/***
	 * 使用相册中的图片
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	private Uri photoUri;
	private String picPath;
	private Cursor mCursor;
	private int pos = 0;
	private String[] path = new String[2];
	private boolean isPicChange = false;
	private ImageView bigpicture;
	private Bitmap identityBitmap1;
	private Bitmap identityBitmap2;
	
	private Button agent_appling_agree_btn;
	private Button agent_appling_refuse_btn;
	private Button agent_applying_photo_btn;
	private Button agent_applying_submit_btn;
	private ImageView agent_applying_photo_done_img;
	private ImageView agent_applying_photo_img;
	private EditText agent_applying_name_edit;
	private EditText agent_applying_phone_edit;
	private EditText agent_applying_adress_edit;
	
	private String mBkntno;
	private String mMessage ="";
	private String mResult =""; 
//	private LinearLayout agent_applying_layout;
	private ScrollView agent_applying_layout;
	private RelativeLayout agent_applying_agreement_layout;
	
	private BuyTask mBuyTask;
//	private DaikuanActivity mActivity;
	
	private Bundle bundle;
	private AgentData agentData = new AgentData();
	
	
	public static AgentApplyFragment create(double value,String couponId){
		return new AgentApplyFragment();
	}
	
	public AgentApplyFragment()
	{
	}
	
	public AgentApplyFragment(Bundle b) {
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		if(getActivity() instanceof DaikuanActivity){
//			 mActivity = (DaikuanActivity)getActivity();
//		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.agent_appling_layout, container,false);
		initView(view);
		
		setTitle("通付宝代理商申请");
		setBackVisible();
		
		return view;
	}
	
	private void initView(View view){
		agent_appling_agree_btn = (Button)view.findViewById(R.id.agent_appling_agree_btn);
		agent_appling_refuse_btn = (Button)view.findViewById(R.id.agent_appling_refuse_btn);
		agent_applying_layout =  (ScrollView)view.findViewById(R.id.agent_applying_layout);
		agent_applying_layout.setVisibility(View.GONE);
		agent_applying_agreement_layout =  (RelativeLayout)view.findViewById(R.id.agent_applying_agreement_layout);
		agent_applying_agreement_layout.setVisibility(View.VISIBLE);
		agent_applying_name_edit = (EditText)view.findViewById(R.id.agent_applying_name_edit);
		agent_applying_phone_edit = (EditText)view.findViewById(R.id.agent_applying_phone_edit);
		agent_applying_adress_edit = (EditText)view.findViewById(R.id.agent_applying_adress_edit);
		agent_applying_photo_btn = (Button)view.findViewById(R.id.agent_applying_photo_btn);
		agent_applying_submit_btn = (Button)view.findViewById(R.id.agent_applying_submit_btn);
		agent_applying_photo_img = (ImageView)view.findViewById(R.id.agent_applying_photo_img);
		agent_applying_photo_done_img = (ImageView)view.findViewById(R.id.agent_applying_photo_done_img);
		agent_applying_photo_done_img.setVisibility(View.GONE);
		//agent_applying_pay_mode_tv2 = (TextView)view.findViewById(R.id.agent_applying_pay_mode_tv2);
		
		agent_appling_refuse_btn.setOnClickListener(this);
		agent_appling_agree_btn.setOnClickListener(this);
		agent_applying_photo_btn.setOnClickListener(this);
		agent_applying_submit_btn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.agent_appling_refuse_btn://拒绝代理商申请协议
			exitAgentApplication();
			break;
		case R.id.agent_appling_agree_btn://同意代理商申请协议
			writeAgentApplication();
			break;
		case R.id.agent_applying_photo_btn://拍照存身份证
			getAgentIdentity();
			break;
		case R.id.agent_applying_submit_btn://提交代理商申请
			sumitAgentApplication();
			break;

		default:
			break;
		}
	}
	
	private void writeAgentApplication()
	{
		agent_applying_agreement_layout.setVisibility(View.GONE);
		agent_applying_layout.setVisibility(View.VISIBLE);
	}
	
	private void exitAgentApplication()
	{
		getActivity().finish();
	}
	private void getAgentIdentity()
	{
		showPicture(0);
		
//		agent_applying_photo_done_img.setVisibility(View.VISIBLE);
	}
	private void sumitAgentApplication()
	{
		//do something
		if(checkInput()){
			mBuyTask = new BuyTask();
			mBuyTask.execute("");
			//getActivity().finish();
		}
	}
	
	private boolean checkInput(){
		String name = agent_applying_name_edit.getText().toString();
		String phone = agent_applying_phone_edit.getText().toString();
		String address = agent_applying_adress_edit.getText().toString();
		if(null== name || "".equals(name)){
			PromptUtil.showToast(getActivity(), "请输入正确的姓名");
			return false;
		}
		if(null== phone || "".equals(phone) 
			|| phone.length() != 11 || !isMobileNum(phone)){
			PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
			return false;
		}
		if(null== address || "".equals(address)){
			PromptUtil.showToast(getActivity(), "请输入正确的地址");
			return false;
		}
//		agentData.name=name;
//		agentData.phone=phone;
//		agentData.address=address;
		return true;
	}
	
	private boolean isMobileNum(String mobile) {
		return UserInfoCheck.checkMobilePhone(mobile);
//		 Pattern p = Pattern
//	                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//	        Matcher m = p.matcher(mobile);
//	        return m.matches();
	}
	
	
	public void showPicture(int index) {
		pos = index;
		CharSequence[] items = { "相册", "相机", "查看大图" };
		new AlertDialog.Builder(getActivity()).setTitle("选择图片来源")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							pickPhoto();
						} else if (which == 1) {
							takePhoto();
						} else {
//							showBigPicure();
						}
					}
				}).create().show();
	}
	
	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}
	
	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			photoUri = getActivity().getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/** ----------------- */
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(getActivity(), "内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == Activity.RESULT_OK) {

				doPhoto(requestCode, data);
//				agent_applying_photo_done_img.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(getActivity(), "请重新选择图片", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	private void doPhoto(int requestCode, Intent data) {
		if (requestCode == SELECT_PIC_BY_PICK_PHOTO) // 从相册取图片，有些手机有异常情况，请注意
		{
			if (data == null) {
				Toast.makeText(getActivity(), "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if (photoUri == null) {
				Toast.makeText(getActivity(), "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		String[] pojo = { MediaStore.Images.Media.DATA };
		mCursor = getActivity().managedQuery(photoUri, pojo, null, null, null);
		if (mCursor != null) {
			int columnIndex = mCursor.getColumnIndexOrThrow(pojo[0]);
			mCursor.moveToFirst();
			picPath = mCursor.getString(columnIndex);
			// cursor.close();
		}
		if (picPath != null
				&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")
						|| picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
			try {
				File file = new File(picPath);
				FileInputStream inputStream = new FileInputStream(file);
				Logger.d("file", "length " + inputStream.available());
//				inputStream.close();
			

//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inSampleSize = 4;
//			Bitmap bm = BitmapFactory.decodeFile(picPath, options);
			Bitmap bm = decodeSampledBitmapFromDescriptor(inputStream.getFD()	, 300, 300, null);
			if (pos == UserInfoFragment.IDENTITY_FIRST) {
//				agent_applying_photo_img.setBackgroundDrawable(null);
//				agent_applying_photo_img.setImageBitmap(bm);
				identityBitmap1 = Bitmap.createScaledBitmap(bm, 200, 300, true);
				path[0] = picPath;
			} else {
//				mFragment.setBGSec(bm);
//				agent_applying_photo_img.setBackgroundDrawable(null);
//				agent_applying_photo_img.setImageBitmap(bm);
				identityBitmap2 = Bitmap.createScaledBitmap(bm, 200, 300, true);
				path[1] = picPath;
			}
			isPicChange = true;
			agent_applying_photo_done_img.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			Toast.makeText(getActivity(), "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
	}
	
	public static Bitmap decodeSampledBitmapFromDescriptor(
            FileDescriptor fileDescriptor, int reqWidth, int reqHeight, ImageCache cache) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqWidth);
//
//        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inScaled=false;
        //测试
//        options.inInputShareable = true;
//        options.inPurgeable = true;
//        
        // If we're running on Honeycomb or newer, try to use inBitmap

        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mBuyTask != null){
			mBuyTask.cancel(true);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("通付宝代理商申请");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		log("onStop endCallStateService");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		log("onDetach endCallStateService");
	}

	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
//				data.putValue("address",agentData.address);
//				data.putValue("phone",agentData.phone);
//				data.putValue("name",agentData.name);
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiTempAgentInfo", 
						"addAgentInfo", data);
				AgentParser authorRegParser = new AgentParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mRsp =null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			PromptUtil.dissmiss();
			if(mRsp==null){
				PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
						getActivity().finish();
					}
//					else {
//						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
//					}
				} catch (Exception e) {
					PromptUtil.showToast(getActivity(),getString(R.string.req_error));
				}
			
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}
	}
	private void parserResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					mResult = result1.get(0).mValue;
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					mMessage = message.get(0).mValue;
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
			}
		}
	}
}
