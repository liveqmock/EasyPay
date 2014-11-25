package com.inter.trade.ui.fragment.gamerecharge;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.inter.trade.R;
import com.inter.trade.adapter.GameRechargeCompanyAdapter;
import com.inter.trade.ui.GameRechargeSelectActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.gamerecharge.data.CompanyListData;
import com.inter.trade.ui.fragment.gamerecharge.data.GameListData;
import com.inter.trade.ui.fragment.gamerecharge.task.CompanyListTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;

/**
 * 游戏充值主Fragment
 * @author Lihaifeng
 *
 */
public class GameRechargeGameFragment extends BaseFragment implements OnItemClickListener
 		,ResponseStateListener{
	private ListView mListView;
	private GameRechargeCompanyAdapter mAdapter;
	private ArrayList<String> mArrayList = new ArrayList<String>();
	
	/**网络获取的数据*/
	private ArrayList<CompanyListData> netData;
	
	private CompanyListTask task;
	
	
	public GameRechargeGameFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.game_recharge_company_list, container,false);
		initView(view);
		if(savedInstanceState!=null){
			netData=(ArrayList<CompanyListData>) savedInstanceState.getSerializable("data");
		}
		
		if(netData==null){
			task=new CompanyListTask(getActivity(),this);
			task.execute();
		}else{
				GameRechargeCompanyAdapter adapter=new GameRechargeCompanyAdapter(getActivity(), netData);
				mListView.setAdapter(adapter);
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
		if(task!=null){
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
		
		CompanyListData companyListData = netData.get(position);
		
		GameListData gameListData=new GameListData();
		gameListData.setGameId(companyListData.getCompanyId());
		gameListData.setGameName(companyListData.getCompanyName());
		
		
		Bundle bundle=new Bundle();
		bundle.putSerializable("data", gameListData);
		
		Intent intent=new Intent(getActivity(),GameRechargeSelectActivity.class);
		intent.putExtra("gameSelect", bundle);
		startActivity(intent);
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		netData=(ArrayList<CompanyListData>) obj;
		if(netData!=null){
			GameRechargeCompanyAdapter adapter=new GameRechargeCompanyAdapter(getActivity(), netData);
			mListView.setAdapter(adapter);
		}
		
	}

}
