package com.inter.trade.ui.fragment.gamerecharge;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.GameRechargeCompanyAdapter;
import com.inter.trade.adapter.GameRechargeGameAdapter;
import com.inter.trade.adapter.GameRechargeSelectAdapter;
import com.inter.trade.data.ArriveData;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.TaskData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.net.SunHttpApi;
import com.inter.trade.ui.ArriveView;
import com.inter.trade.ui.CommonActivity;
import com.inter.trade.ui.GameRechargeInfoActivity;
import com.inter.trade.ui.GameRechargeSelectActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.AgentApplyActivity;
import com.inter.trade.ui.AgentMainContentActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.gamerecharge.data.GameListData;
import com.inter.trade.ui.fragment.gamerecharge.data.GameSelectListData;
import com.inter.trade.ui.fragment.gamerecharge.parser.GameListParser;
import com.inter.trade.ui.fragment.gamerecharge.parser.GameSelectListParser;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyDenominationData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyDenominationParser;
import com.inter.trade.ui.fragment.waterelectricgas.WaterElectricGasMainFragment;
import com.inter.trade.ui.views.IndexableListView;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 游戏充值主Fragment
 * @author Lihaifeng
 *
 */
@SuppressLint("ValidFragment")
public class GameRechargeSelectFragment extends BaseFragment implements OnItemClickListener{
	private ListView mListView;
	private GameRechargeSelectAdapter mAdapter;
	private ArrayList<String> mArrayList = new ArrayList<String>();
	/**
	 * 传过来的数据
	 */
	private Bundle bundle;
	private GameListData data;
	
	private ArrayList<GameSelectListData> netData;
	
	private GameSelectListTask task;

	
	public GameRechargeSelectFragment()
	{
	}
	
	public GameRechargeSelectFragment(Bundle b) {
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle=getActivity().getIntent().getBundleExtra("gameSelect");
		data=(GameListData) bundle.get("data");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.game_recharge_company_list, container,false);
		initView(view);
		setTitle("面值选择");
		setBackVisible();
		
		if(savedInstanceState!=null){
			netData=(ArrayList<GameSelectListData>) savedInstanceState.getSerializable("data");
		}
		if(netData==null){
			task=new GameSelectListTask();
			task.execute();
		}
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("data", netData);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task!=null && !task.isCancelled()){
			task.cancel(true);
		}
	}
	private void initView(View view){
		mListView = (ListView) view.findViewById(R.id.listview);
		mListView.setFastScrollEnabled(true);
	    mListView.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		GameSelectListData gameSelectListData = netData.get(position);
		Bundle bundle=new Bundle();
		bundle.putSerializable("selectData", gameSelectListData);
		Intent intent=new Intent(getActivity(),GameRechargeInfoActivity.class);
		intent.putExtra("data", bundle);
		startActivity(intent);
		
		
	}
	
	private class GameSelectListTask extends AsyncTask<Void, Void, Void>{
		ProtocolRsp mRsp;
		@Override
		protected Void doInBackground(Void... params) {
			try {
				CommonData mData = new CommonData();
				if(data.getGameName()!=null){
					mData.putValue("gameId", data.getGameId());
				}
				
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiGameRecharge", 
						"getChildGame", mData);
				GameSelectListParser myParser = new GameSelectListParser();
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
					netData=new ArrayList<GameSelectListData>();
					if(aupic!=null)
					for(ProtocolData child:aupic){
						GameSelectListData game=new GameSelectListData();
						if (child.mChildren != null && child.mChildren.size() > 0) {
							Set<String> keys = child.mChildren.keySet();
							for(String key:keys){
								List<ProtocolData> rs = child.mChildren.get(key);
								for(ProtocolData item: rs){
									if(item.mKey.equals("gameId")){
										game.setGameId(item.mValue);
									}else if(item.mKey.equals("gameName")){
										game.setGameName(item.mValue);
									}else if(item.mKey.equals("price")){
										game.setPrice(item.mValue);
									}else if(item.mKey.equals("cost")){
										game.setCost(item.mValue);
									}
								}
								
							}
						}
						netData.add(game);
//						mList.add(picData);
					}
				}
			}
			 mAdapter = new GameRechargeSelectAdapter(getActivity(), netData);
			 mListView.setAdapter(mAdapter);
		}
		
	}
}
