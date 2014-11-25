package com.inter.trade.ui.fragment.express;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.coupon.BuySuccessFragment;
import com.inter.trade.ui.fragment.express.util.ExpressData;
import com.inter.trade.ui.fragment.express.util.ExpressDetial;
import com.inter.trade.ui.fragment.express.util.ExpressDetial.InnerStatus;
import com.inter.trade.ui.fragment.express.util.ExpressDetialParser;
import com.inter.trade.ui.fragment.express.util.ExpressEntity;
import com.inter.trade.ui.fragment.express.util.ExpressInfoUtils;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class ExpressDetialFragment extends BaseFragment implements
		OnClickListener {
	private Button find_more;
	private TextView express_title_tv, tvPhone,tv_kefu;
	private ImageView ivIcon;
	private EditText order_no_edit;
	private String mOrderNo;
	private ExpressData mCurrentData;
	public static ExpressEntity mEntity;
	public static String url = "";

	private ArrayList<ExpressData> mList = new ArrayList<ExpressData>();
	private RecordTask mRecordTask;

	public ExpressDetialFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("快递查询");
		mCurrentData = ExpressGridFragment.mExpressData;
		LoginUtil.detection(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.express_find_layout, container,
				false);
		find_more = (Button) view.findViewById(R.id.order_query_btn);
		express_title_tv = (TextView) view.findViewById(R.id.express_title_tv);
		order_no_edit = (EditText) view.findViewById(R.id.order_no_edit);
		tvPhone = (TextView) view.findViewById(R.id.tv_phone);
		ivIcon=(ImageView) view.findViewById(R.id.item_icon);
		
		tv_kefu=(TextView) view.findViewById(R.id.tv_kefu);
		
		find_more.setOnClickListener(this);
		tvPhone.setOnClickListener(this);

		setBackVisible();
		initData();
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("快递查询");
	}

	private void initData() {
		if (mCurrentData == null) {
			return;
		}
		express_title_tv.setText(mCurrentData.comname);
		String expressPhone = ExpressInfoUtils.getExpressPhone(mCurrentData.comname);
		if(!TextUtils.isEmpty(expressPhone)){
			//tv_kefu.setText(mCurrentData.comname+"客服服务热线:");
			tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			tvPhone.setText(expressPhone);
			
		}
		ivIcon.setBackgroundDrawable(getActivity().getResources().getDrawable(ExpressInfoUtils.getExpressDrawable(mCurrentData.comname)));
	}

	private boolean checkInput() {
		String no = order_no_edit.getText().toString();
		if (null == no || "".equals(no)) {
			PromptUtil.showToast(getActivity(), "请输入订单号");
			return false;
		}
		mOrderNo = no;
		return true;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mRecordTask != null) {
			mRecordTask.cancel(true);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.order_query_btn:
			if (checkInput()) {
				mRecordTask = new RecordTask();
				mRecordTask.execute("");
			}
			// showChuxuka();
			break;
		case R.id.tv_phone:
			String expressPhone = ExpressInfoUtils.getExpressPhone(mCurrentData.comname);
			try {
				Intent intent = new Intent();
				//intent.setAction(Intent.ACTION_DIAL);
				intent.setAction(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + expressPhone.replaceAll("-", "")));
				getActivity().startActivity(intent);
			}catch(SecurityException ex){
				Toast.makeText(getActivity(),
						"拨号失败，请检查相关权限。", Toast.LENGTH_SHORT).show();
			}catch (Exception e) {
				Toast.makeText(getActivity(),
						"拨号失败。", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	private void showChuxuka() {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.func_container, new BuySuccessFragment());
		transaction.commit();
	}

	public class RecordTask extends AsyncTask<String, Integer, Boolean> {
		ProtocolRsp mRsp = null;
		FragmentActivity mActivity;
		private String mResultString;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources()
					.getString(R.string.loading));
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				data.putValue("nu", mOrderNo);
				data.putValue("com", mCurrentData.com);
				List<ProtocolData> mDatas = createRequest(data);
				ExpressDetialParser versionParser = new ExpressDetialParser();
				mRsp = HttpUtil.doRequest(versionParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Logger.e(e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			if (mRsp == null) {
				PromptUtil.showToast(getActivity(), getActivity()
						.getResources().getString(R.string.net_error));
			} else {
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);

					if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
							getActivity())) {
						return;
					}

					if (mEntity.apitype != null
							&& mEntity.apitype.equals("gethtmlorder")) {
						showWebView();
					} else {
						if (mEntity.mDetial.mInnerStatus != null
								&& mEntity.mDetial.mInnerStatus.size() > 0) {
							showDetial();
						} else {
							PromptUtil.showToast(getActivity(), "暂无数据");
						}

					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					PromptUtil.showToast(getActivity(), "暂无数据");
				}

			}
		}
	}

	private void showWebView() {
		Intent intent = new Intent();
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.EXPRESS_WEBVIEW_INDEX);
		intent.setClass(getActivity(), FunctionActivity.class);
		getActivity().startActivity(intent);
	}

	private void showDetial() {
		Intent intent = new Intent();
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.EXPRESS_RESULT_INDEX);
		intent.setClass(getActivity(), FunctionActivity.class);
		getActivity().startActivity(intent);
	}

	private List<ProtocolData> createRequest(CommonData data) {
		return ProtocolUtil.getRequestDatas("ApiKuaiDiinfo", "chaxunKuaiDiNo",
				data);
	}

	private void parserResponse(List<ProtocolData> mDatas) {
		mEntity = new ExpressEntity();
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : mDatas) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);
				// List<ProtocolData> req_seq = data.find("/req_seq");
				// if (req_seq != null) {
				// response.setReq_seq(req_seq.get(0).mValue);
				// }
				//
				// List<ProtocolData> ope_seq = data.find("/ope_seq");
				// if (ope_seq != null) {
				// response.setOpe_seq(ope_seq.get(0).mValue);
				// }
				//
				// List<ProtocolData> rettype = data.find("/retinfo/rettype");
				// if (rettype != null) {
				// response.setRettype(rettype.get(0).mValue);
				// }
				//
				// List<ProtocolData> retcode = data.find("/retinfo/retcode");
				// if (retcode != null) {
				// response.setRetcode(retcode.get(0).mValue);
				// }
				//
				// List<ProtocolData> retmsg = data.find("/retinfo/retmsg");
				// if (retmsg != null) {
				// response.setRetmsg(retmsg.get(0).mValue);
				// }

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result = data.find("/result");
				if (result != null) {
					mEntity.result = result.get(0).mValue;
					LoginUtil.mLoginStatus.result = result.get(0).mValue;
				}

				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}

				List<ProtocolData> apitype = data.find("/apitype");
				if (apitype != null) {
					mEntity.apitype = apitype.get(0).mValue;

				}
				List<ProtocolData> apiurl = data.find("/apiurl");
				if (apiurl != null) {
					url = apiurl.get(0).mValue;
				}
				ExpressDetial detail = new ExpressDetial();
				detail.mInnerStatus = new ArrayList<ExpressDetial.InnerStatus>();
				mEntity.mDetial = detail;
				List<ProtocolData> nu = data.find("/nu");
				if (nu != null) {
					detail.nu = nu.get(0).mValue;
				}
				List<ProtocolData> com = data.find("/com");
				if (com != null) {
					detail.com = com.get(0).mValue;
				}
				List<ProtocolData> updatetime = data.find("/updatetime");
				if (updatetime != null) {
					detail.updatetime = updatetime.get(0).mValue;
				}
				List<ProtocolData> status = data.find("/status");
				if (status != null) {
					detail.status = status.get(0).mValue;
				}

				List<ProtocolData> data1 = data.find("/data");
				if (data1 != null) {
					List<ProtocolData> aupic = data1.get(0).find("/msgchild");
					for (ProtocolData child : aupic) {
						InnerStatus picData = new InnerStatus();
						if (child.mChildren != null
								&& child.mChildren.size() > 0) {
							Set<String> keys = child.mChildren.keySet();
							for (String key : keys) {
								List<ProtocolData> rs = child.mChildren
										.get(key);
								for (ProtocolData item : rs) {
									if (item.mKey.equals("ftime")) {
										picData.ftime = item.mValue;

									} else if (item.mKey.equals("context")) {
										picData.context = item.mValue;

									} else if (item.mKey.equals("time")) {

										picData.time = item.mValue;

									}
								}
							}
						}

						detail.mInnerStatus.add(picData);
					}
				}
			}
		}
	}

}
