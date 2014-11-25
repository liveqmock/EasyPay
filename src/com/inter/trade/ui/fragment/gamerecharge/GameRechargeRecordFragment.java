package com.inter.trade.ui.fragment.gamerecharge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.adapter.GameRechargeCompanyAdapter;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.DaikuanActivity;
import com.inter.trade.ui.GameRechargeSuccessActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
//import com.inter.trade.ui.WaterElectricGasPayActivity;
import com.inter.trade.ui.WaterElectricGasPayActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.gamerecharge.data.GameListData;
import com.inter.trade.ui.fragment.gamerecharge.data.GameRecordListData;
import com.inter.trade.ui.fragment.gamerecharge.parser.GameListParser;
import com.inter.trade.ui.fragment.gamerecharge.parser.GameRecordListParser;
import com.inter.trade.ui.fragment.gamerecharge.util.GameChargeRecordAdapter;
//import com.inter.trade.ui.fragment.qmoney.util.WaterElectricGasPayData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanNoParser;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasBillData;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasBillParser;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasPayData;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasPayParser;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 游戏充值历史Fragment
 * @author Lihaifeng
 *
 */

@SuppressLint("ValidFragment")
public class GameRechargeRecordFragment extends BaseFragment{
	
	
	private Bundle bundle;
	
	private ExpandableListView mListView;
	
	private TextView tvEmpty;
	
	private ArrayList<GameRecordListData> netData;
	
	private GameChargeRecordAdapter adapter;
	
	private RecordListTask task;
	
	public static GameRechargeRecordFragment create(double value,String couponId){
		return new GameRechargeRecordFragment();
	}
	
	public GameRechargeRecordFragment()
	{
	}
	
	public GameRechargeRecordFragment(Bundle b) {
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.fragment_game_charge_record, container,false);
		
		setTitle("游戏充值历史");
		setBackVisible();
		initView(view);
		
		task=new RecordListTask();
		task.execute();
		
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task!=null){
			task.cancel(true);
		}
	}

	private void initView(View view) {
		mListView=(ExpandableListView) view.findViewById(R.id.lv_record);
		tvEmpty=(TextView) view.findViewById(R.id.tv_empty);
		mListView.setEmptyView(tvEmpty);
		mListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				for (int i = 0; i < adapter.getGroupCount(); i++) {
					if (groupPosition != i) {
						mListView.collapseGroup(i);
					}
				}

			}

		});
	}
	private class RecordListTask extends AsyncTask<Void, Void, Void>{
		ProtocolRsp mRsp;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				CommonData mData = new CommonData();
				mData.putValue("msgstart", "0");
				mData.putValue("msgdisplay", "100");
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiGameRecharge", 
						"getOrderHistory", mData);
				GameRecordListParser myParser = new GameRecordListParser();
				mRsp = HttpUtil.doRequest(myParser, mDatas);
				} catch (Exception e) {
					e.printStackTrace();
					mRsp =null;
				}
				return null;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			if(mRsp==null){
				PromptUtil.showToast(getActivity(), getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
				} catch (Exception e) {
					PromptUtil.showToast(getActivity(),getString(R.string.req_error));
				}
			}
		}
		
		private void parserResponse(List<ProtocolData> mDatas) {
			ResponseData response = new ResponseData();
			LoginUtil.mLoginStatus.mResponseData =response;
			for (ProtocolData data : mDatas) {
				if (data.mKey.equals(ProtocolUtil.msgheader)) {
					ProtocolUtil.parserResponse(response, data);

				} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

					List<ProtocolData> result = data.find("/result");
					if (result != null) {
						LoginUtil.mLoginStatus.result = result.get(0).getmValue();
					}
					
					List<ProtocolData> message = data.find("/message");
					if (result != null) {
						LoginUtil.mLoginStatus.message = message.get(0).getmValue();
					}
					
					List<ProtocolData> aupic = data.find("/msgchild");
					netData=new ArrayList<GameRecordListData>();
					if(aupic!=null)
					for(ProtocolData child:aupic){
						GameRecordListData game=new GameRecordListData();
						if (child.mChildren != null && child.mChildren.size() > 0) {
							Set<String> keys = child.mChildren.keySet();
							for(String key:keys){
								List<ProtocolData> rs = child.mChildren.get(key);
								for(ProtocolData item: rs){
									if(item.mKey.equals("gamename")){
										game.setGameName(item.mValue);
									}else if(item.mKey.equals("status")){
										game.setStatus(item.mValue);
									}else if(item.mKey.equals("totalPrice")){
										game.setTotalPrice(item.mValue);
									}else if(item.mKey.equals("completeTime")){
										game.setCompleteTime(item.mValue);
									}else if(item.mKey.equals("bkntno")){
										game.setBkntno(item.mValue);
									}else if(item.mKey.equals("quantity")){
										game.setQuantity(item.mValue);
									}else if(item.mKey.equals("area")){
										game.setArea(item.mValue);
									}else if(item.mKey.equals("price")){
										game.setPrice(item.mValue);
									}else if(item.mKey.equals("account")){
										game.setAccount(item.mValue);
									}
								}
								
							}
						}
						netData.add(game);
					}
				}
			}
			if(netData!=null&&netData.size()>0){
				//更新记录列表
				adapter =new GameChargeRecordAdapter(getActivity(),netData);
				mListView.setAdapter(adapter);
			}
			
		}
	}
		
	}
	
	

