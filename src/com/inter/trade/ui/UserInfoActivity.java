package com.inter.trade.ui;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.ModifyAuthorInfoParser;
import com.inter.protocol.body.UploadAuthorPicParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.DLCell;
import com.inter.trade.data.PicData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.imageframe.ImageCache.ImageCacheParams;
import com.inter.trade.imageframe.ImageCache;
import com.inter.trade.imageframe.ImageFetcher;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.UploadPic.ProgressListener;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.UserInfoFragment;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class UserInfoActivity extends FragmentActivity {
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

	private UserInfoFragment mFragment;

	public static String UPLOAD_URL = "http://14.18.207.56/mobilepay/upfile/mobileauthor/authorIDcard2";
	private Cursor mCursor;
	public static ImageFetcher mImageFetcher;
	private ImageFetcher mBigFetcher;
	private int mImageThumbSize;
	public static final String IMAGE_CACHE_DIR = "image";
	private int pos = 0;

	private String[] path = new String[2];

	private static final String SUCCESS = "1";
	private static final String FAIL = "0";
	private UploadPic mUploadPic;
	private boolean isPicChange = false;

	private static final int HANDLE_START = 0;
	private static final int HANDLE_DLING = 1;
	private static final int HANDLE_DLFAIL = 2;
	private static final int HANDLE_DLFINISH = 3;

	private TextView prompt;
	private ProgressBar picProgress;
	private int count = 0;

	private LinearLayout common_layout;
	private RelativeLayout picLayout;
	private ImageView bigpicture;
	
	private Bitmap identityBitmap1;
	private Bitmap identityBitmap2;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case HANDLE_START:
				if (count == 0) {
					prompt.setText("正在上传第一张身份证照片");
				} else {
					prompt.setText("正在上传第二张身份证照片");
				}
				dialog.show();
				picProgress.setMax(100);
				picProgress.setProgress(0);
				break;
			case HANDLE_DLING:
				DLCell cell = (DLCell) msg.obj;
				picProgress
						.setProgress((int) (cell.current * 100 / cell.total));
				break;
			case HANDLE_DLFAIL:
				dialog.dismiss();
				PromptUtil.showToast(UserInfoActivity.this, "上传失败");
				break;
			case HANDLE_DLFINISH:
				count++;
				if (count == 2) {
					count = 0;
					dialog.dismiss();
					PromptUtil.showToast(UserInfoActivity.this, "证件上传成功");
				}

				break;

			default:
				break;
			}
		}

	};
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.userinfo_layout);
		// LoginUtil.detection(this);
		setupView();
		initFetcher();
//		initBigFetcher();
		mUploadPic = new UploadPic();
		mUploadPic.setListener(mProgressListener);
		initProgress();

		int index = 0;
		index = getIntent() == null ? 0 : getIntent().getIntExtra(
				FragmentFactory.INDEX_KEY, 0);
		mFragment = new UserInfoFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.func_container, mFragment);
		fragmentTransaction.commit();
	}

	private void setupView() {
		common_layout = (LinearLayout) findViewById(R.id.common_layout);
		picLayout = (RelativeLayout) findViewById(R.id.picLayout);
		bigpicture = (ImageView) findViewById(R.id.bigpicture);
		
		picLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				picLayout.setVisibility(View.GONE);
				common_layout.setVisibility(View.VISIBLE);
			}
		});
	}

	private void initFetcher() {
		ImageCacheParams cacheParams = new ImageCacheParams(this,
				IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory
		mImageThumbSize = getResources().getDimensionPixelSize(
				R.dimen.image_thumbnail_size);
		mImageFetcher = new ImageFetcher(this, mImageThumbSize);
		mImageFetcher.setLoadingImage(R.drawable.add_pic);
		mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
	}
	
	private void initBigFetcher() {
		ImageCacheParams cacheParams = new ImageCacheParams(this,
				IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory
		int width  = getResources().getDimensionPixelSize(
				R.dimen.image_big_width);
		int height  = getResources().getDimensionPixelSize(
				R.dimen.image_big_height);
		mBigFetcher = new ImageFetcher(this, width,height);
		mBigFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
	}

	public ImageFetcher getFetcher() {
		return mImageFetcher;
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == RESULT_OK) {

				doPhoto(requestCode, data);
			} else {
				Toast.makeText(this, "请重新选择图片", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

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
			photoUri = this.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/** ----------------- */
			startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	public void upload() {
		new UploadTask().execute("");
	}

	private void doPhoto(int requestCode, Intent data) {
		if (requestCode == SELECT_PIC_BY_PICK_PHOTO) // 从相册取图片，有些手机有异常情况，请注意
		{
			if (data == null) {
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if (photoUri == null) {
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}
		String[] pojo = { MediaStore.Images.Media.DATA };
		mCursor = managedQuery(photoUri, pojo, null, null, null);
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
				mFragment.setBGFirst(bm);
//				identityBitmap1 = decodeSampledBitmapFromDescriptor(new FileInputStream(file).getFD(), 300, 300, null);
				identityBitmap1 = Bitmap.createScaledBitmap(bm, 200, 300, true);
				path[0] = picPath;
			} else {
				mFragment.setBGSec(bm);
				identityBitmap2 = Bitmap.createScaledBitmap(bm, 200, 300, true);
				path[1] = picPath;
			}
			isPicChange = true;
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else {
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
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
//	        // Decode bitmap with inSampleSize set
	        options.inJustDecodeBounds = false;
	        options.inScaled=false;
	        //测试
//	        options.inInputShareable = true;
//	        options.inPurgeable = true;
//	        

	        // If we're running on Honeycomb or newer, try to use inBitmap

	        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
	    }
	   public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	            int reqWidth, int reqHeight) {

	        // First decode with inJustDecodeBounds=true to check dimensions
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeResource(res, resId, options);

	        // Calculate inSampleSize
	        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	        // If we're running on Honeycomb or newer, try to use inBitmap

	        // Decode bitmap with inSampleSize set
	        options.inJustDecodeBounds = false;
	        return BitmapFactory.decodeResource(res, resId, options);
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
	private void initProgress() {
		int theme = R.style.Dialog;
		dialog = new Dialog(this, theme);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.upload_progress, null);
		prompt = (TextView) layout.findViewById(R.id.prompt);
		picProgress = (ProgressBar) layout.findViewById(R.id.picProgress);
		dialog.setContentView(layout);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mCursor != null) {
			mCursor.close();
		}
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

	public void showPicture(int index) {
		pos = index;
		CharSequence[] items = { "相册", "相机", "查看大图" };
		new AlertDialog.Builder(this).setTitle("选择图片来源")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							pickPhoto();
						} else if (which == 1) {
							takePhoto();
						} else {
							showBigPicure();
						}
					}
				}).create().show();
	}

	private void showBigPicure() {
		
		ArrayList<PicData> datas = LoginUtil.mUserInfo.mPicData;
		if (datas != null && datas.size() > 0) {
			if(pos == UserInfoFragment.IDENTITY_FIRST ){
//				bigpicture.setBackgroundDrawable(mFragment.identity_1.getDrawable());
				
				if(path[0] != null ){
					bigpicture.setImageBitmap(identityBitmap1);
				}else{
					if(datas.get(0) != null){
						mImageFetcher.setImageSize(200, 300);
						mImageFetcher.loadImage(datas.get(0).picpath, bigpicture);
					}
					
				}
			}else if(pos == UserInfoFragment.IDENTITY_SECOND){
				if(path[1] != null ){
					bigpicture.setImageBitmap(identityBitmap2);
				}else{
					if(datas.get(1) != null){
						mImageFetcher.setImageSize(200, 300);
						mImageFetcher.loadImage(datas.get(1).picpath, bigpicture);
					}
					
				}
			}
			
//			if (datas.get(0) != null && pos == UserInfoFragment.IDENTITY_FIRST) {
//				mImageFetcher.loadImage(datas.get(0).picpath, bigpicture);
//			} else if (datas.get(1) != null
//					&& pos == UserInfoFragment.IDENTITY_SECOND) {
//				mImageFetcher.loadImage(datas.get(1).picpath, bigpicture);
//			}
			common_layout.setVisibility(View.GONE);
			picLayout.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 判断用户是否修改了图片
	 * 
	 * @return
	 */
	public boolean isPicInput() {
		boolean flag = true;
		if (isPicChange) {
			flag = checkIdentityPic();
		}
		return flag;
	}

	public boolean checkIdentityPic() {
		boolean flag = false;
		if (path[0] != null || path[1] != null) {
			flag = true;
		}
		return flag;
	}

	class UploadTask extends AsyncTask<String, Integer, Boolean> {
		private ProtocolRsp mRsp;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean flag = true;
			// UPLOAD_URL =
			// LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {
				// if(checkIdentityPic()){
				// flag = uploadPic(0);
				// if(flag){
				// Thread.sleep(1000);
				// flag = uploadPic(1);
				// }
				// }
				if (path[0] != null) {
					flag = uploadPic(0);
				}
				if (path[1] != null) {
					if (flag) {
						Thread.sleep(1000);
						flag = uploadPic(1);
					}
				}

				if (flag) {
					List<ProtocolData> mDatas = getRequestDatas();
					ModifyAuthorInfoParser authorRegParser = new ModifyAuthorInfoParser();
					ProtocolParser.instance().setParser(authorRegParser);
					String content = ProtocolParser.instance().aToXml(mDatas);

					Logger.d("HttpApi", "request\n" + content);
					mRsp = HttpUtil.getRequest(content, authorRegParser);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return flag;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				if (result) {
					List<ProtocolData> datas = mRsp.mActions;
					parserResoponse(datas);

					if (LoginUtil.mLoginStatus.result
							.equals(ProtocolUtil.SUCCESS)) {
						PromptUtil.showToast(UserInfoActivity.this,
								LoginUtil.mLoginStatus.message);
						UserInfoActivity.this.finish();
					} else {
						PromptUtil.showToast(UserInfoActivity.this,
								LoginUtil.mLoginStatus.message);
					}
				} else {
					PromptUtil.showToast(UserInfoActivity.this, "提交失败");
					mFragment.isClickUpload = false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				Logger.e(e);
				mFragment.isClickUpload = false;
				PromptUtil.showToast(UserInfoActivity.this, "提交失败");
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// PromptUtil.showDialog(UserInfoActivity.this, "");
		}
	}

	ProgressListener mProgressListener = new ProgressListener() {

		@Override
		public void progress(int state, DLCell cell) {
			// TODO Auto-generated method stub
			switch (state) {
			case UploadPic.DL_START:
				Message message = mHandler.obtainMessage();
				message.obj = cell;
				message.what = HANDLE_START;
				mHandler.sendMessage(message);
				break;
			case UploadPic.DL_ING:
				Message msgDling = mHandler.obtainMessage();
				msgDling.obj = cell;
				msgDling.what = HANDLE_DLING;
				mHandler.sendMessage(msgDling);
				break;
			case UploadPic.DL_FINISH:
				Message msgFinish = mHandler.obtainMessage();
				msgFinish.obj = cell;
				msgFinish.what = HANDLE_DLFINISH;
				mHandler.sendMessage(msgFinish);
				break;
			case UploadPic.DL_FAIL:
				Message msgFail = mHandler.obtainMessage();
				msgFail.obj = cell;
				msgFail.what = HANDLE_DLFAIL;
				mHandler.sendMessage(msgFail);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 根据索引上传对应图片
	 * 
	 * @param index
	 * @return
	 */
	private boolean uploadPic(int index) {
		boolean flag = false;
		if (index >= path.length) {
			return false;
		}
		if (path[index] != null) {
			String url = LoginUtil.mUserInfo.mPicData.get(index).uploadurl;
			String picName = getPicName();
			flag = mUploadPic.uploadFile(url, path[index], picName);

			if (!url.endsWith("/")) {
				url += "/";
			}
			String result = FAIL;
			if (flag) {
				result = SUCCESS;
			}
			String tempPath = url + picName;
			List<ProtocolData> mDatas = getUploadDatas(index, tempPath, result);
			UploadAuthorPicParser authorRegParser = new UploadAuthorPicParser();

			ProtocolRsp mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			if (mRsp != null) {
				parserResoponse(mRsp.mActions);
			}
			if (LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)) {
				flag = true;
			}

		}
		return flag;
	}

	/**
	 * 根据索引上传图片信息
	 * 
	 * @param index
	 * @return
	 */
	private List<ProtocolData> getUploadDatas(int index, String path,
			String isSuccess) {
		CommonData data = new CommonData();
		PicData picData = LoginUtil.mUserInfo.mPicData.get(index);
		data.putValue("picid", picData.picid);
		data.putValue("picpath", path);
		data.putValue("uploadmethod", picData.uploadmethod);
		data.putValue("uploadpictype", picData.uploadpictype);
		data.putValue("uploadmark", isSuccess);

		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
				"ApiAuthorInfo", "uploadAuthorPic", data);

		return mDatas;
	}

	/**
	 * 请求修改身份信息
	 * 
	 * @return
	 */
	private List<ProtocolData> getRequestDatas() {
		CommonData data = new CommonData();
		data.putValue("autruename", mFragment.mRigesterData.getName());
		data.putValue("auidcard", mFragment.mRigesterData.getIdentity());
		data.putValue("auemail", mFragment.mRigesterData.getEmail());
		
		if(MainActivity.currentState == 1) {
			data.putValue("agentcompany", mFragment.mRigesterData.getAgentcompany());
			data.putValue("agentarea", mFragment.mRigesterData.getAgentarea());
			data.putValue("agentaddress", mFragment.mRigesterData.getAgentaddress());
			data.putValue("agentmanphone", mFragment.mRigesterData.getAgentmanphone());
			data.putValue("agentfax", mFragment.mRigesterData.getAgentfax());
		}
		
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
				"ApiAuthorInfo", "modifyAuthorInfo", data);

		return mDatas;
	}

	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : params) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {
				List<ProtocolData> result1 = data.find("/result");
				if (result1 != null) {
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}

				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
			}
		}
	}

	private String getPicName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String temp = "";
		try {
			temp = format.format(new Date(System.currentTimeMillis()));
		} catch (Exception e) {
			// TODO: handle exception
		}
		String firstName = LoginUtil.mUserInfo.aumobile + "_"
				+ LoginUtil.mUserInfo.mPicData.get(0).uploadpictype + "_"
				+ temp + ".jpg";
		return firstName;
	}

}
