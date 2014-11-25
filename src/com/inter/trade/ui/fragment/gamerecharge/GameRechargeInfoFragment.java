package com.inter.trade.ui.fragment.gamerecharge;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.trade.R;
import com.inter.trade.ui.GameRechargeBillActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.gamerecharge.data.GameInfoData;
import com.inter.trade.ui.fragment.gamerecharge.data.GameSelectListData;
import com.inter.trade.ui.fragment.gamerecharge.dialog.WheelWidgetBottonView;
import com.inter.trade.ui.fragment.gamerecharge.dialog.WheelWidgetDialog;
import com.inter.trade.ui.fragment.gamerecharge.dialog.WheelWidgetDialog.ClickSureButtonListener;
import com.inter.trade.ui.fragment.gamerecharge.task.GameInfoTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.fragment.waterelectricgas.util.ListToArrayUtil;
import com.inter.trade.util.LoginUtil;

/**
 * 游戏详细信息填写Fragment
 * 
 * @author Lihaifeng
 * 
 */

@SuppressLint("ValidFragment")
public class GameRechargeInfoFragment extends BaseFragment implements
		OnClickListener, ClickSureButtonListener, ResponseStateListener {

	private Bundle bundle;

	private Button btnCommit;

	private TextView tvNumber;
	private TextView tvArea;
	private TextView tvGameName;
	private TextView tvPrice;
	private EditText etAccount;
	private TextView tvServer;

	private int mark = 1;// 记录点击的位置 1 数量 2 区 3 服务器

	private GameInfoTask task;

	private String[] items = new String[] { "1", "2", "3", "4", "5", "10",
			"20", "30", "40", "50", "100" };

	private GameSelectListData data;// 选中的充值面额的信息

	private GameInfoData game;// 游戏详细信息
	
	private ArrayList<String> serverList;

	public static GameRechargeInfoFragment create(double value, String couponId) {
		return new GameRechargeInfoFragment();
	}

	public GameRechargeInfoFragment() {
	}

	public GameRechargeInfoFragment(Bundle b) {
		this.bundle = b;
		data = (GameSelectListData) b.getSerializable("selectData");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (task != null && !task.isCancelled()) {
			task.cancel(true);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Bundle bundle=new Bundle();
		bundle.putSerializable("data", data);
		bundle.putSerializable("game", game);
		outState.putBundle("save", bundle);
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.fragment_game_charge_detail,
				container, false);

		setTitle("详细信息选择");
		setBackVisible();
		initView(view);
		updatePage();
		if(savedInstanceState!=null){
			data =(GameSelectListData) savedInstanceState.getBundle("save").getSerializable("data");
			game=(GameInfoData) savedInstanceState.getBundle("save").getSerializable("game");
			updatePage();
		}else{
			task = new GameInfoTask(getActivity(), this);
			task.execute(data.getGameId());
		}

		return view;
	}

	/**
	 * 刷新页面数据
	 * 
	 * @throw
	 * @return void
	 */
	private void updatePage() {
		if(data!=null){
			tvGameName.setText(data.getGameName());
			tvPrice.setText("￥"+data.getPrice());
		}
		if (game != null) {
			if (game.getAreaList() == null||game.getAreaList().size()==0) {// 没有区服
				tvArea.setVisibility(View.GONE);
				tvServer.setVisibility(View.GONE);
			} else {// 有区|区服
				tvArea.setVisibility(View.VISIBLE);
				//tvServer.setVisibility(View.VISIBLE);
			}
		}
	}

	private void initView(View view) {
		btnCommit = (Button) view.findViewById(R.id.btn_commit);
		btnCommit.setOnClickListener(this);

		tvNumber = (TextView) view.findViewById(R.id.tv_number);
		tvNumber.setOnClickListener(this);

		tvArea = (TextView) view.findViewById(R.id.tv_game_area);
		tvArea.setOnClickListener(this);

		tvServer = (TextView) view.findViewById(R.id.tv_game_server);
		tvServer.setOnClickListener(this);

		tvGameName = (TextView) view.findViewById(R.id.tv_game_name);
		tvPrice = (TextView) view.findViewById(R.id.tv_price);
		etAccount = (EditText) view.findViewById(R.id.et_account);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_commit:// 立即充值按钮
			if(!TextUtils.isEmpty(etAccount.getText())){
				collectInfo();
				Intent intent = new Intent(getActivity(),
						GameRechargeBillActivity.class);
				intent.putExtra("data", bundle);
				startActivity(intent);
			}else{
				Toast.makeText(getActivity(), "亲,请填写游戏账号", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_number:// 选择数量按钮
			mark = 1;
			/*WheelWidgetDialog dialog = new WheelWidgetDialog(getActivity(),
					R.style.dialog, items);
			dialog.setClickSureButtonListener(this);
			dialog.show();*/
			
			WheelWidgetBottonView bottom=new WheelWidgetBottonView(getActivity(), this);
			bottom.show(items);
			break;

		case R.id.tv_game_area:// 区
			mark = 2;
			if (game != null && game.getAreaList() != null) {
				showAreaDialog();
			}
			break;
		case R.id.tv_game_server:// 服
			mark = 3;
			if(serverList!=null&& serverList.size()>0){
				showServerDialog();
			}
			break;
		default:
			break;
		}
	}

	private void showAreaDialog() {
		String[] items = ListToArrayUtil.toAreaArray(game.getAreaList());
//		WheelWidgetDialog dialogArea = new WheelWidgetDialog(getActivity(),
//				R.style.dialog, items);
//		dialogArea.setClickSureButtonListener(this);
//		dialogArea.show();
		
		WheelWidgetBottonView view=new WheelWidgetBottonView(getActivity(), this);
		view.show(items);
	}
	
	private void showServerDialog() {
		String[] items = ListToArrayUtil.toServerArray(serverList);
		/*WheelWidgetDialog dialogArea = new WheelWidgetDialog(getActivity(),
				R.style.dialog, items);
		dialogArea.setClickSureButtonListener(this);
		dialogArea.show();*/
		WheelWidgetBottonView view=new WheelWidgetBottonView(getActivity(), this);
		view.show(items);
	}

	/**
	 * 收集用户填写信息
	 * 
	 * @throw
	 * @return void
	 */
	private void collectInfo() {
		bundle = new Bundle();
		bundle.putString("gameId", data.getGameId());
		bundle.putString("gamename", tvGameName.getText() + "");
		bundle.putString("area", tvArea.getText() + "");
		bundle.putString("server", tvServer.getText() + "");
		bundle.putString("quantity", tvNumber.getText() + "");
		bundle.putString("price", data.getPrice());
		bundle.putString("account", etAccount.getText() + "");
		bundle.putString("cost", data.getCost());
	}

	@Override
	public void clickSureBtn(int position) {
		switch (mark) {
		case 1://数量
			Toast.makeText(getActivity(), "选中的值" + items[position],
					Toast.LENGTH_SHORT).show();
			tvNumber.setText(items[position]);
			break;
		case 2://区
			tvArea.setText(game.getAreaList().get(position).getName());
			serverList=game.getAreaList().get(position).getServerList();
			if(serverList==null || serverList.size()==0){
				tvServer.setVisibility(View.GONE);
			}else{
				tvServer.setVisibility(View.VISIBLE);
				tvServer.setText(serverList.get(0));
			}
			break;
		case 3://服
			tvServer.setText(serverList.get(position));
			break;
		default:
			break;
		}
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		if (cla.equals(GameInfoData.class)) {
			game = (GameInfoData) obj;
			if (game != null) {
				if (game.getAreaList() == null||game.getAreaList().size()==0) {// 没有区服
					tvArea.setVisibility(View.GONE);
					tvServer.setVisibility(View.GONE);
				} else {// 有区|区服
					tvArea.setVisibility(View.VISIBLE);
					//tvServer.setVisibility(View.VISIBLE);
				}
			}
		}
	}
}
