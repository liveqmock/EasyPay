package com.inter.trade.ui.fragment.gamerecharge;

import java.util.HashMap;
import java.util.List;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.DaikuanActivity;
import com.inter.trade.ui.GameRechargeMainActivity;
import com.inter.trade.ui.GameRechargeRecordActivity;
import com.inter.trade.ui.IndexActivity;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
//import com.inter.trade.ui.WaterElectricGasPayActivity;
import com.inter.trade.ui.WaterElectricGasPayActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.gamerecharge.util.GameChargeRecordAdapter;
//import com.inter.trade.ui.fragment.qmoney.util.WaterElectricGasPayData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanNoParser;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasBillData;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasBillParser;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasPayData;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasPayParser;
import com.inter.trade.ui.func.FuncMap;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 游戏充值成功Fragment
 * @author Lihaifeng
 *
 */

@SuppressLint("ValidFragment")
public class GameRechargeSuccessFragment extends BaseFragment implements OnClickListener{
	
	
	private Bundle bundle;
	
	private Button btnCommit;
	
	private Button btnGoto;
	
	private TextView tvMoney;
	private TextView tvGameName;
	private TextView tvBankNo;
	
	
	public static GameRechargeSuccessFragment create(double value,String couponId){
		return new GameRechargeSuccessFragment();
	}
	
	public GameRechargeSuccessFragment()
	{
	}
	
	public GameRechargeSuccessFragment(Bundle b) {
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
		View view = inflater.inflate(R.layout.fragment_game_charge_success, container,false);
		
		setTitle("充值成功");
		setBackVisible();
		initView(view);
		updateView();
		
		return view;
	}

	
	private void updateView() {
		tvMoney.setText(bundle.getString("money"));
		tvGameName.setText(bundle.getString("gamename"));
		tvBankNo.setText(bundle.getString("bankno"));
	}

	private void initView(View view) {
		btnCommit=(Button) view.findViewById(R.id.btn_record);
		btnCommit.setOnClickListener(this);
		
		btnGoto=(Button) view.findViewById(R.id.btn_goto);
		btnGoto.setOnClickListener(this);
		
		tvMoney=(TextView) view.findViewById(R.id.tv_game_money);
		tvGameName=(TextView) view.findViewById(R.id.tv_game_class);
		tvBankNo=(TextView) view.findViewById(R.id.tv_game_account);
	}

	@Override
	public void onClick(View view) {
		Intent intent=null;
		switch (view.getId()) {
		case R.id.btn_record://游戏充值历史按钮
			//for test
			intent =new Intent(getActivity(),GameRechargeRecordActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_goto://返回主游戏充值
			intent=new Intent(getActivity(),IndexActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(FragmentFactory.INDEX_KEY, FuncMap.GAME_RECHARGE_INDEX_FUNC);
			
			startActivity(intent);
		default:
			break;
		}
	}
	
	
}
