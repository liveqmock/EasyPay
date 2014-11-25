package com.inter.trade.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.inter.trade.R;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.qrsacncode.util.QRCodeUtils;
import com.inter.trade.ui.fragment.agent.task.GetAgentTask;
import com.inter.trade.ui.fragment.agent.task.GetAgentTask.AgentInfo;

/**
 * 下载通付宝
 * @author apple
 *
 */
public class DownloadFragment extends BaseFragment implements AsyncLoadWorkListener{
	
	/**
	 * 二维码路径
	 */
	private static final String qrPath = "http://www.tfbpay.cn/mobilepay/download/agent.php?agentcode=";
	
	/**
	 * 默认代理商代号
	 */
	private static final String defaultAgentNo = "020001";
	
	private ImageView qrImageView;
	
	public DownloadFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.about_title));
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_download, container, false);
		qrImageView = (ImageView)view.findViewById(R.id.dimencode);
//		try {
//			qrImageView.setImageBitmap(QRCodeUtils.createAddLogoQRCode(getActivity(), "http://www.tfbpay.cn/mobilepay/download/agent.php?agentcode=020001"));
//		} catch (WriterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		getAgentCode ();
		return view;
	}
	
	private void getAgentCode () {
		new GetAgentTask(getActivity(),this).execute("");
	}
	
	/**
	 * 生成代理商二维码图片
	 */
	private void setAgentQRImage(String agentno) {
		try {
			qrImageView.setImageBitmap(QRCodeUtils.createAddLogoQRCode(getActivity(), qrPath + agentno));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//获取代理商监听
	@Override
	public void onSuccess(Object protocolDataList, Bundle bundle) {
		if(protocolDataList == null) {
			qrImageView.setImageResource(R.drawable.qrcode);
			return;
		}
		
		AgentInfo info=(AgentInfo) protocolDataList;
		String agentNo = info.getAgentNo();//当前用户所绑的代理商代号
		
		setAgentQRImage(agentNo==null || agentNo.equals("") ? defaultAgentNo : agentNo);
	}

	@Override
	public void onFailure(String error) {
		//当无网情况下或没有获取到agentID时，设置一张默认代理商的图片，
		qrImageView.setImageResource(R.drawable.qrcode);
	}

	@Override
	public void onResume() {
		super.onResume();
		setTitle("APP下载");
	}
}
