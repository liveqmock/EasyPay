package com.inter.trade.ui.func.childfragment;

import java.io.File;

import com.inter.trade.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateAppFragment extends FragmentActivity
{
	private ProgressBar downloadbar;
	private TextView resultView;
	private Button title_back_btn;
	private String url = "";
	private final Handler handler = new UIHandler();
	private DownloadService servcie;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_update_app);
		downloadbar = (ProgressBar) this.findViewById(R.id.downloadbar);
		resultView = (TextView) this.findViewById(R.id.result);
		title_back_btn = (Button) this.findViewById(R.id.title_back_btn);
		title_back_btn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (null != bundle)
		{
			url = bundle.getString("Url");
		}
		autoDownload();
	}

	private final class UIHandler extends Handler
	{

		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1:
				int downloaded_size = msg.getData().getInt("size");
				downloadbar.setProgress(downloaded_size);
				int result = (int) ((float) downloaded_size
						/ downloadbar.getMax() * 100);
				resultView.setText("已下载:" + result + "%");
				if (downloadbar.getMax() == downloadbar.getProgress())
				{
					Toast.makeText(getApplicationContext(), "下载完成",
							Toast.LENGTH_LONG).show();
					update();
				}
			}
		}
	}

	private void update()
	{
		String filename = url.substring(url.lastIndexOf('/') + 1);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), filename)),
				"application/vnd.android.package-archive");
		startActivity(intent);
		finish();
	}

	private void autoDownload()
	{
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			DownloadTask task;
			try
			{
				task = new DownloadTask(url);
				servcie.isPause = false;
				new Thread(task).start();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(UpdateAppFragment.this, R.string.sdcarderror, 1)
					.show();
		}
	}

	private final class DownloadTask implements Runnable
	{

		public DownloadTask(String target) throws Exception
		{
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED))
			{
				File destination = Environment.getExternalStorageDirectory();
				servcie = new DownloadService(target, destination, 5,
						getApplicationContext());
				downloadbar.setMax(servcie.fileSize);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "SD卡不存在或写保护!",
						Toast.LENGTH_LONG).show();
			}
		}

		public void run()
		{
			try
			{
				servcie.download(new DownloadListener()
				{

					public void onDownload(int downloaded_size)
					{
						Message message = new Message();
						message.what = 1;
						message.getData().putInt("size", downloaded_size);
						handler.sendMessage(message);
					}

				});
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}
}
