package com.inter.trade.ui.fragment.gamerecharge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.adapter.GameRechargeGameAdapter;
import com.inter.trade.ui.GameRechargeSelectActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.gamerecharge.data.GameListData;
import com.inter.trade.ui.fragment.gamerecharge.task.GameListTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.view.sortview.CharacterParser;
import com.inter.trade.view.sortview.PinyinComparator;
import com.inter.trade.view.sortview.SideBar;
import com.inter.trade.view.sortview.SideBar.OnTouchingLetterChangedListener;
import com.inter.trade.view.sortview.SortAdapter;
import com.inter.trade.view.sortview.SortModel;

/**
 * 游戏充值主Fragment
 * @author Lihaifeng
 *
 */
public class GameRechargeCompanyFragment extends BaseFragment implements OnItemClickListener
 	,ResponseStateListener	{
	private ListView mListView;
	/**右侧的a-z*/
	private SideBar sideBar;
	/**显示A-Z的弹窗*/
	private TextView dialog;
	
	private SortAdapter adapter;
	
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	
	private GameRechargeGameAdapter mAdapter;
	
	/**网络获取的数据*/
	private ArrayList<GameListData> netData;
	
	private GameListTask task;
	
	
	public GameRechargeCompanyFragment(){}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.game_recharge_game_list, container,false);
		initView(view);
		System.out.println(savedInstanceState);
		if(savedInstanceState!=null){
			netData=(ArrayList<GameListData>) savedInstanceState.getSerializable("data");
		}
		if(netData==null){
			task=new GameListTask(getActivity(), this);
			task.execute();
		}else {
			updateCompanyList(netData);
		}
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("data", netData);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task!=null && !task.isCancelled()){
			task.cancel(true);
		}
	}
	
	private void initView(View view){
		mListView = (ListView) view.findViewById(R.id.lv_company);
		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		
	    mListView.setOnItemClickListener(this);
	    
	    characterParser=CharacterParser.getInstance();
	    pinyinComparator = new PinyinComparator();
	    sideBar.setTextView(dialog);
	    
	 	//设置右侧触摸监听
	 	sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
	 				
	 				@Override
	 				public void onTouchingLetterChanged(String s) {
	 					//该字母首次出现的位置
	 					if(adapter!=null){
	 						int position = adapter.getPositionForSection(s.charAt(0));
		 					if(position != -1){
		 						mListView.setSelection(position);
		 					}
	 					}
	 				}
	 			});
	}
	
	private void updateCompanyList(ArrayList<GameListData> list) {
		ArrayList<String> mArrayList = new ArrayList<String>();
		for(GameListData data:list){
			mArrayList.add(data.getGameName());
		}
		SourceDateList = filledData(mArrayList);
	    
	 // 根据a-z进行排序源数据
	 	Collections.sort(SourceDateList, pinyinComparator);
	 	adapter = new SortAdapter(getActivity(), SourceDateList);
	 	mListView.setAdapter(adapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
		String gameName = SourceDateList.get(position).getName();
		
		GameListData gameListData=new GameListData();
		
		for(int i=0;i<netData.size();i++){
			GameListData data = netData.get(i);
			if(gameName.equals(data.getGameName())){
				gameListData.setGameName(gameName);
				gameListData.setGameId(data.getGameId());
			}
		}
		Bundle bundle=new Bundle();
		bundle.putSerializable("data", gameListData);
		
		Intent intent=new Intent(getActivity(),GameRechargeSelectActivity.class);
		intent.putExtra("gameSelect", bundle);
		startActivity(intent);
	}
	
	/**
	 * 为ListView填充数据
	 * @param data
	 * @return
	 */
	private List<SortModel> filledData(ArrayList<String> data){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<data.size(); i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(data.get(i));
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(data.get(i));
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		netData=(ArrayList<GameListData>) obj;
		if(netData!=null){
			updateCompanyList(netData);
		}
	}
}
