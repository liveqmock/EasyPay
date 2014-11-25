package com.inter.trade.ui.fragment.checking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.fragment.checking.LockPatternView.Cell;
import com.inter.trade.ui.fragment.checking.LockPatternView.DisplayMode;
import com.inter.trade.ui.fragment.checking.util.AuthorPwdModifyParser;
import com.inter.trade.ui.fragment.checking.util.LockPatternUtil;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;

/*
 * Author: Ruils 心怀产品梦的安卓码农 
 * Blog: http://blog.csdn.net/ruils
 * QQ: 5452781
 * Email: 5452781@qq.com
 */

public class LockSetupActivity extends Activity implements
        LockPatternView.OnPatternListener, OnClickListener {

    private static final String TAG = "LockSetupActivity";
    private LockPatternView lockPatternView;
    private Button leftButton;
    private Button rightButton;
    //解锁图案文字提示
    private TextView lockText;
    
    /**跳过文字和重新设置 */
    private TextView tv_jump,tv_hint_bottom;

    private static final int STEP_1 = 1; // 开始
    private static final int STEP_2 = 2; // 第一次设置手势完成
    private static final int STEP_3 = 3; // 按下继续按钮
    private static final int STEP_4 = 4; // 第二次设置手势完成
    // private static final int SETP_5 = 4; // 按确认按钮

    private int step;

    private List<Cell> choosePattern;

    private boolean confirm = false;
    
    /**
     * 记录是否为设置手势密码界面进入
     */
    private boolean isSetInPwd=false;
    
    /**
     * 手势密码输入提示
     */
    private LockHintIndicator lockHintIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_lock_setup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);
        leftButton = (Button) findViewById(R.id.left_btn);
        rightButton = (Button) findViewById(R.id.right_btn);
        lockText = (TextView)findViewById(R.id.my_lock_text);
        lockHintIndicator = (LockHintIndicator)findViewById(R.id.lockIndicator);
        tv_jump=(TextView) findViewById(R.id.tv_jump);
        tv_hint_bottom=(TextView) findViewById(R.id.tv_hint_bottom);
        tv_hint_bottom.setVisibility(View.INVISIBLE);
        
        tv_jump.setOnClickListener(this);
        tv_hint_bottom.setOnClickListener(this);
        
        if("set".equals(getIntent().getStringExtra("mode"))){//设置手势密码进入
        	tv_jump.setVisibility(View.GONE);
        	isSetInPwd=true;
        }

//        step = STEP_1;
//        updateView();
        lockPatternView.clearPattern();
        lockPatternView.enableInput();
    }

    private void updateView() {
        switch (step) {
        case STEP_1:
            leftButton.setText(R.string.cancel);
            rightButton.setText("");
            rightButton.setEnabled(false);
            choosePattern = null;
            confirm = false;
            lockPatternView.clearPattern();
            lockPatternView.enableInput();
            break;
        case STEP_2:
            leftButton.setText(R.string.try_again);
            rightButton.setText(R.string.goon);
            rightButton.setEnabled(true);
            lockPatternView.disableInput();
            break;
        case STEP_3:
            leftButton.setText(R.string.cancel);
            rightButton.setText("");
            rightButton.setEnabled(false);
            lockPatternView.clearPattern();
            lockPatternView.enableInput();
            break;
        case STEP_4:
            leftButton.setText(R.string.cancel);
            if (confirm) {
                rightButton.setText(R.string.confirm);
                rightButton.setEnabled(true);
                lockPatternView.disableInput();
            } else {
                rightButton.setText("");
                lockPatternView.setDisplayMode(DisplayMode.Wrong);
                lockPatternView.enableInput();
                rightButton.setEnabled(false);
            }

            break;

        default:
            break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.left_btn:
            if (step == STEP_1 || step == STEP_3 || step == STEP_4) {
                finish();
            } else if (step == STEP_2) {
                step = STEP_1;
                updateView();
            }
            break;

        case R.id.right_btn:
            if (step == STEP_2) {
                step = STEP_3;
                updateView();
            } else if (step == STEP_4) {

                SharedPreferences preferences = getSharedPreferences(
                		"lock", MODE_PRIVATE);
                preferences
                        .edit()
                        .putString("lock_key",
                                LockPatternView.patternToString(choosePattern))
                        .commit();

                Intent intent = new Intent(this, LockActivity.class);
                startActivity(intent);
                finish();
            }

            break;
        case R.id.tv_jump://跳过
        	Intent intent = new Intent();
    		intent.setClass(LockSetupActivity.this, MainActivity.class);
    		intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
    		setResult(Constants.ACTIVITY_FINISH);
    		finish();
    		
        	break;
        case R.id.tv_hint_bottom://重新设置手势
        	tv_hint_bottom.setVisibility(View.INVISIBLE);
        	choosePattern=null;
        	 lockPatternView.clearPattern();
             lockPatternView.enableInput();
             setInfoText("绘制解锁图案");
             lockHintIndicator.setSeletions("");
        	break;

        default:
            break;
        }

    }

    @Override
    public void onPatternStart() {
        Log.d(TAG, "onPatternStart");
    }

    @Override
    public void onPatternCleared() {
        Log.d(TAG, "onPatternCleared");
    }

    @Override
    public void onPatternCellAdded(List<Cell> pattern) {
        Log.d(TAG, "onPatternCellAdded");
    }

    @Override
    public void onPatternDetected(List<Cell> pattern) {
        Log.d(TAG, "onPatternDetected");

        if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
//            Toast.makeText(this,
//                    R.string.lockpattern_recording_incorrect_too_short,
//                    Toast.LENGTH_SHORT).show();
        	lockText.setText(this.getResources().getString(R.string.lockpattern_recording_incorrect_too_short));
            clearPattern();
            return;
        }

        if (choosePattern == null) {
            choosePattern = new ArrayList<Cell>(pattern);
 //           Log.d(TAG, "choosePattern = "+choosePattern.toString());
//            Log.d(TAG, "choosePattern.size() = "+choosePattern.size());
            Log.d(TAG, "choosePattern = "+Arrays.toString(choosePattern.toArray()));
            
            runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					lockHintIndicator.setSeletions(LockPatternUtil.lockPatternToString(choosePattern));
				}
			});
            
         
//            step = STEP_2;
//            updateView();
            //第一次输入手势密码成功，清理之前的手势密码，启动输入
            //Toast.makeText(this, "请设置确认密码", Toast.LENGTH_SHORT).show();
            setInfoText("再次绘制解锁图案");
            lockPatternView.clearPattern();
            lockPatternView.enableInput();
            return;
        }
//[(row=1,clmn=0), (row=2,clmn=0), (row=1,clmn=1), (row=0,clmn=2)]
//[(row=1,clmn=0), (row=2,clmn=0), (row=1,clmn=1), (row=0,clmn=2)]    
        
        Log.d(TAG, "choosePattern = "+Arrays.toString(choosePattern.toArray()));
        Log.d(TAG, "pattern = "+Arrays.toString(pattern.toArray()));
        
        if (choosePattern.equals(pattern)) {
//            Log.d(TAG, "pattern = "+pattern.toString());
//            Log.d(TAG, "pattern.size() = "+pattern.size());
            Log.d(TAG, "pattern = "+Arrays.toString(pattern.toArray()));
            
            SharedPreferences preferences = getSharedPreferences(
            		"lock", MODE_PRIVATE);
            
//            preferences
//                    .edit()
//                    .putString("lock_key",
//                    		LockPatternView.patternToString(choosePattern))
//                    .commit();
            lock_key = LockPatternUtil.lockPatternToString(choosePattern);
            preferences
            .edit()
            .putString("lock_key","x58abfghfghgf"
            		)//V3.0.0版本之前把密码保存在本地SharedPreferences，现随机生成一个密码保存至本地，以清除其他版本的密码
            .commit();
            
            
            //把手势密码结果转为一串数字，对应012345678
            
            Log.i(TAG, "choosePattern -> str:"+LockPatternUtil.lockPatternToString(choosePattern));
            
            //手势密码设置成功，则异步修改密码
            AuthorPwdModifyTask authorPwdModifyTask = new AuthorPwdModifyTask();
            authorPwdModifyTask.execute("");
            
//            Intent intent = new Intent(this, LockActivity.class);
//            startActivity(intent);
//            finish();
//            confirm = true;
        } else {
//            confirm = false;
        	//Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
        	setWarnText("与上一次绘制的不一致,请重新绘制");
        	tv_hint_bottom.setVisibility(View.VISIBLE);
        	clearPattern();
        }
      
//        step = STEP_4;
//        updateView();

    }
    
     /**
      * 设置警告文字 
      * @throw
      * @return void
      */
     private void setWarnText(String str){
    		lockText.setText(str);
        	lockText.setTextColor(this.getResources().getColor(R.color.red));
     }
     
     /**
      * 设置正常文字
      * @throw
      * @return void
      */
     private void setInfoText(String str){
    	 lockText.setText(str);
     	lockText.setTextColor(this.getResources().getColor(R.color.white));
     }
    
    
    
    /**
     * 手势密码输错了，提示用户，并延迟两秒清理当前的输入轨迹
     */
    private void clearPattern() {
    	lockPatternView.setDisplayMode(DisplayMode.Wrong);
        new Thread(){
        	public void run() {
        		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        		runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						lockPatternView.clearPattern();
						lockPatternView.enableInput();
					}
				});
        		
        	}
        }.start();
    }
    
    /**
     * 手势密码
     */
    private String lock_key;
    
    private ProtocolRsp mRsp;
    
    class AuthorPwdModifyTask extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<ProtocolData> mDatas = getRequestDatas();
			AuthorPwdModifyParser authorRegParser = new AuthorPwdModifyParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			try {
				PromptUtil.dissmiss();
				if(mRsp == null){
					PromptUtil.showToast(LockSetupActivity.this, LockSetupActivity.this.getString(R.string.net_error));
				}else{
					try {
						List<ProtocolData> mDatas = mRsp.mActions;
						parserResoponse(mDatas);
						
						if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//							PromptUtil.showToast(LockSetupActivity.this, "登录成功");
							LoginUtil.isLogin=true;
							LoginUtil.mLoginStatus.login_name=PreferenceConfig.instance(LockSetupActivity.this).getString(Constants.USER_NAME, "");
							
							PreferenceConfig.instance(LockSetupActivity.this).putString(Constants.USER_GESTURE_PWD, lock_key);
							
							Logger.d("login","登录接口"+LoginUtil.mLoginStatus.mResponseData.getReq_token());
							ProtocolUtil.printString("LoginFragment",
									 LoginUtil.mLoginStatus.mResponseData.getReq_token());
							
							//切换账户或者重新登录账户或者注册,都会进入手势密码设置页面，当手势设置成功，则设为false
							PreferenceConfig.instance(LockSetupActivity.this).putBoolean(Constants.IS_SET_PWDSAFETY, false);
							
							
							
				    		
//				    		if(SafetyLoginFragment.safetyLoginFragment != null) {//有一些bug待修复，通过setResult关闭不了activity
//				    			SafetyLoginFragment.safetyLoginFragment.getActivity().finish();
//				    		}
							String mode = getIntent().getStringExtra("mode");//set则为从修改手势密码页面进入
							if(!"set".equals(mode)){//登录进入
								setResult(Constants.ACTIVITY_FINISH);
								//登录成功进入主页面
								if(SafetyLoginActivity.isLoadMain && LockActivity.isLoadMain) {
									Intent intent = new Intent();
						    		intent.setClass(LockSetupActivity.this, MainActivity.class);
						    		intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
						    		startActivity(intent);
								}
								
							}else{//设置手势密码进入
//								Intent intent = new Intent();
//					    		intent.setClass(LockSetupActivity.this, LockActivity.class);
//					    		intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//					    		startActivity(intent);
							}
				    		finish();
				    		
							
							Log.i("Result", "LockSetupActivity finish()");
//							FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//							fragmentTransaction.replace(R.id.func_container,FragmentFactory.createFragment(FragmentFactory.current_index));
//							fragmentTransaction.commit();
							
						}else {
							PromptUtil.showToast(LockSetupActivity.this, LoginUtil.mLoginStatus.message);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						PromptUtil.showToast(LockSetupActivity.this, getString(R.string.req_error));
					}
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(LockSetupActivity.this, "请稍后...");
		}
		
	}
    
    private List<ProtocolData> getRequestDatas(){
		CommonData data = new CommonData();
		
		data.putValue("auoldpwd", "");//旧密码
		data.putValue("aunewpwd", lock_key);//新密码
		data.putValue("aumoditype", "1");//修改支付类型；1：密码 2 支付密码
		data.putValue("reset", "1");//是否重置密码；如果为1，则无视旧密码，直接设置新密码
		
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfoV2", "authorPwdModify", data);
		
		
		return mDatas;
	}
    
    
    /**
	 * 解析响应体
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> authorid = data.find("/authorid");
				if(authorid != null){
					LoginUtil.mLoginStatus.authorid = authorid.get(0).mValue;
				}
				
				List<ProtocolData> agentid = data.find("/agentid");
				if(agentid != null){
					LoginUtil.mLoginStatus.agentid = agentid.get(0).mValue;
				}
				
				List<ProtocolData> ispaypwd = data.find("/ispaypwd");
				if(ispaypwd != null){
					LoginUtil.mLoginStatus.ispaypwd = ispaypwd.get(0).mValue;
				}
				
				List<ProtocolData> gesturepasswd = data.find("/gesturepasswd");
				if(gesturepasswd != null){
					LoginUtil.mLoginStatus.gesturepasswd = gesturepasswd.get(0).mValue;
				}
			}
		}
	}
    
    
    
    

}
