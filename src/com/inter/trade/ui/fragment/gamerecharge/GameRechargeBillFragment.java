package com.inter.trade.ui.fragment.gamerecharge;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.trade.R;
import com.inter.trade.data.CardData;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.gamerecharge.data.BillData;
import com.inter.trade.ui.fragment.gamerecharge.data.GetBillData;
import com.inter.trade.ui.fragment.gamerecharge.task.BillTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 游戏充值账单Fragment
 * 
 * @author Lihaifeng
 * 
 */

@SuppressLint("ValidFragment")
public class GameRechargeBillFragment extends BaseFragment implements
		OnClickListener, SwipListener,ResponseStateListener {

	private Bundle bundle;
	private Button btnCommit;
	private EditText etBankNo;
	private TextView tvGameName;
	private TextView tvArea;
	private TextView tvAccount;
	private TextView tvPrice;
	private TextView tvNumber;
	private TextView tvTotal;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private TableRow trArea;

	private GetBillData data;
	
	
	private BillTask task;
	

	public static GameRechargeBillFragment create(double value, String couponId) {
		return new GameRechargeBillFragment();
	}

	public GameRechargeBillFragment() {
	}

	public GameRechargeBillFragment(Bundle b) {
		this.bundle = b;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState!=null){
			bundle=savedInstanceState.getBundle("data");
			bill=(BillData) savedInstanceState.getSerializable("bill");
		}
		PayApp.mSwipListener = this;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Bundle bu=new  Bundle();
		outState.putBundle("data", bu);
		outState.putSerializable("bill", bill);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initSwipPic(PayApp.isSwipIn);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.fragment_game_charge_bill,
				container, false);

		setTitle("游戏充值账单");
		setBackVisible();
		initView(view);
		updateView();
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task!=null){
			task.cancel(true);
		}
	}

	/**
	 * 刷新页面数据
	 * 
	 * @throw
	 * @return void
	 */
	private void updateView() {
		String price = bundle.getString("price");
		String number = bundle.getString("quantity");
		String area = bundle.getString("area");

		tvGameName.setText(bundle.getString("gamename"));
		tvAccount.setText(bundle.getString("account"));
		tvPrice.setText(price);
		tvNumber.setText(number);
		if(price!=null&& number!=null){
			tvTotal.setText(Double.parseDouble(price) * Integer.parseInt(number)
					+ "");
		}
		if(!TextUtils.isEmpty(area)){
			tvArea.setText(area);
			trArea.setVisibility(View.VISIBLE);
		}else{
			trArea.setVisibility(View.GONE);
		}
	}

	private void initView(View view) {
		btnCommit = (Button) view.findViewById(R.id.btn_commit);
		btnCommit.setOnClickListener(this);
		swip_card = (ImageView) view.findViewById(R.id.swip_card);
		swip_prompt = (TextView) view.findViewById(R.id.swip_prompt);
		card_edit = (EditText) view.findViewById(R.id.card_edit);

		tvGameName = (TextView) view.findViewById(R.id.tv_game_name);
		tvArea = (TextView) view.findViewById(R.id.tv_game_area);
		tvAccount = (TextView) view.findViewById(R.id.tv_game_account);
		tvPrice = (TextView) view.findViewById(R.id.tv_game_price);
		tvNumber = (TextView) view.findViewById(R.id.tv_game_number);
		tvTotal = (TextView) view.findViewById(R.id.tv_game_count);
		trArea=(TableRow) view.findViewById(R.id.tr_area);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_commit:// 前往银联支付
			if (checkInput()) {
				collectData();
				task=new BillTask(getActivity(), this);
				task.execute(data.getGameId(),
							 data.getGameName(),
							 data.getArea(),
							 data.getServer(),
							 data.getQuantity(),
							 data.getPrice(),
							 data.getUserCount(),
							 data.getPaycardid(),
							 data.getRechabkcardno(),
							 data.getCost());
			}

			break;

		default:
			break;
		}
	}

	private boolean checkInput() {

		String cardNumber = card_edit.getText().toString();
		if (null == cardNumber || "".equals(cardNumber)) {
			PromptUtil.showToast(getActivity(), "请刷卡或输入卡号");
			return false;
		}
		if (!UserInfoCheck.checkBankCard(cardNumber)) {
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		return true;
	}

	private void collectData() {
		data = new GetBillData();
		String quantity = bundle.getString("quantity");
		String price = bundle.getString("price");
		String totalPrice=Double.parseDouble(price)*Integer.parseInt(quantity)+"";
		String cost =bundle.getString("cost");
		String totalCost=Double.parseDouble(cost)*Integer.parseInt(quantity)+"";
		data.setGameId(bundle.getString("gameId"));
		data.setGameName(bundle.getString("gamename"));
		data.setArea(bundle.getString("area"));
		data.setServer(bundle.getString("server"));
		data.setQuantity(quantity);
		data.setPrice(totalPrice);
		data.setPaycardid(PayApp.mKeyCode==null?"":PayApp.mKeyCode);
		data.setRechabkcardno(card_edit.getText() + "");
		data.setUserCount(bundle.getString("account"));
		data.setCost(totalCost);
		bankno=card_edit.getText()+"";
	}

	private void initSwipPic(boolean flag) {
		if (flag) {
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.swip_enable));
		} else {
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.swip_card_bg));
		}
	}

	@Override
	public void recieveCard(CardData data) {
		Toast.makeText(getActivity(), "接受到了卡号", Toast.LENGTH_SHORT).show();
		card_edit.setText(data.pan);
	}

	@Override
	public void checkedCard(boolean flag) {
		initSwipPic(flag);
	}

	@Override
	public void progress(int status, String message) {
		switch (status) {
		case SWIPING_START:
			PromptUtil.showToast(getActivity(), message);
			break;
		case SWIPING_FAIL:
			PromptUtil.showToast(getActivity(), message);
			break;
		case SWIPING_SUCCESS:
			PromptUtil.showToast(getActivity(), message);

			break;
		default:
			break;
		}
	}
	private BillData bill;
	private String bankno;
	/**
	 * 重载方法
	 * @param obj
	 * @param cla
	 */
	@Override
	public void onSuccess(Object obj, Class cla) {
			if(cla.equals(BillData.class)){
				bill=(BillData) obj;
				System.out.println("流水号:"+bill.getBkntno()+"钱数:"+bill.getTotalPrice());
				UnionpayUtil.startUnionPlungin(bill.getBkntno(), getActivity());//跳转到银联支付
			}
	}
	public BillData getBillData(){
		return bill;
	}
	
	public String getBankNo(){
		return bankno;
	}
}
