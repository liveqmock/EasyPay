package com.inter.trade.ui.fragment.agent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.agent.task.BindAgentTask;
import com.inter.trade.ui.fragment.agent.task.GetAgentTask;
import com.inter.trade.ui.fragment.agent.task.GetAgentTask.AgentInfo;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.PromptUtil;

/**
 *  区域绑定（绑定代理商）
 * @author zhichao.huang
 *
 */
public class BindAgentFragment extends BaseFragment implements OnClickListener,AsyncLoadWorkListener
			,ResponseStateListener{
	
	private LinearLayout agent_no_layout;
	private LinearLayout agent_area_layout;
	
	private EditText agent_no;
	private TextView agent_area;
	
	private Button bind_agent_submit;
	
	public static String mProtocolType="4";
	private String agentID;
	
	public BindAgentFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(getActivity().getString(R.string.left_bind_agent));
		setBackVisible();
		new GetAgentTask(getActivity(),this).execute("");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.bind_agent_layout, container, false);
		
		
		agent_no_layout = (LinearLayout)view.findViewById(R.id.agent_no_layout);
		agent_area_layout = (LinearLayout)view.findViewById(R.id.agent_area_layout);
		agent_no_layout.setVisibility(View.GONE);
		agent_area_layout.setVisibility(View.GONE);
		agent_no = (EditText)view.findViewById(R.id.agent_no);
		agent_area = (TextView)view.findViewById(R.id.agent_area);
		
	//	agent_id = (EditText)view.findViewById(R.id.agent_id);
		bind_agent_submit = (Button)view.findViewById(R.id.bind_agent_submit);
		bind_agent_submit.setOnClickListener(this);
		
		agent_no.setEnabled(false);
		
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setTitle("绑定代理商");
		setTitle(getActivity().getString(R.string.left_bind_agent));
	}
	
	private boolean checkInfo() {
		if(agent_no == null) return false;
		agentID = agent_no.getText().toString();
		
		if(agentID == null || agentID.trim().equals("")) {
			PromptUtil.showToast(getActivity(), "请输入服务代号");
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bind_agent_submit:
			if(checkInfo()) {
				//调用接口
				new BindAgentTask(getActivity(),this).execute(agentID);
			}
			break;
		case R.id.title_right_btn://编辑按钮
			
			bind_agent_submit.setVisibility(View.VISIBLE);//显示提交按钮
			agent_no.setEnabled(true);
			agent_no.requestFocus();
			agent_no.setText("");
			agent_area_layout.setVisibility(View.GONE);
			showSoftBoard(agent_no);
			
		default:
			break;
		}
		
	}

	@Override
	public void onSuccess(Object protocolDataList, Bundle bundle) {
		AgentInfo info=(AgentInfo) protocolDataList;
		agent_no_layout.setVisibility(View.VISIBLE);
		agent_area_layout.setVisibility(View.VISIBLE);
		bind_agent_submit.setVisibility(View.GONE);
		agent_no.setEnabled(false);
		agent_no.setText(info.getAgentNo());
		agent_area.setText(info.getAgentArea());
		if("020001".equals(info.getAgentNo())){
			setRightVisible(this, "编辑");
		}else{
			Button right = (Button) getActivity()
					.findViewById(R.id.title_right_btn);
			right.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onFailure(String error) {
		
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		new GetAgentTask(getActivity(),this).execute("");
	}
	
	
	
}
