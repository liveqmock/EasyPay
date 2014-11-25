package com.inter.trade.ui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpStatus;

import com.inter.trade.data.DLCell;
import com.inter.trade.log.Logger;

import android.graphics.BitmapFactory;
import android.util.Log;

public class UploadPic {
	
	public static final int DL_START=0;//开始下载
	public static final int DL_ING=1;//下载中
	public static final int DL_FAIL=2;//下载失败
	public static final int DL_FINISH=3;//下载完成
	
	private ProgressListener mProgressListener;
	
	public void setListener(ProgressListener progressListener){
		this.mProgressListener = progressListener;
	}
	
	
	public   boolean   uploadFile(String uploadUrl,String srcPath,String filename) {
		boolean flag = false;
		Log.d("UploadfileActivity", "url: "+uploadUrl);
		Log.d("UploadfileActivity", "srcPath: "+srcPath);
		Log.d("UploadfileActivity", "filename: "+filename);
		DLCell cell = new DLCell();
		if(mProgressListener != null){
			cell.current=0l;
			cell.total=0l;
			cell.state = DL_START;
			mProgressListener.progress(DL_START, cell);
		}
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try {
			if(!uploadUrl.endsWith("/")){
				uploadUrl+="/";
			}
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			// 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
			// 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
			httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
			// 允许输入输出流
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			// 使用POST方法
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
					+ filename
					+ "\""
					+ end);
			dos.writeBytes(end);
			File file = new File(srcPath);
			long total = file.length();
			
			Logger.d("file","total"+total);
			if(mProgressListener != null){
				cell.current=0l;
				cell.total=total;
				cell.state = DL_START;
				mProgressListener.progress(DL_START, cell);
			}
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			// 读取文件
			int temp =0;
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
				temp+=count;
				if(mProgressListener != null){
					cell.current=temp;
					cell.total=total;
					cell.state = DL_ING;
					mProgressListener.progress(DL_ING, cell);
				}
			}
			fis.close();

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();
			Log.d("UploadfileActivity", result);
			int code = httpURLConnection.getResponseCode();
			if(code == HttpStatus.SC_OK){
				flag = true;
				if(mProgressListener != null){
					cell.current=temp;
					cell.total=total;
					cell.state = DL_FINISH;
					mProgressListener.progress(DL_FINISH, cell);
				}
			}else{
				if(mProgressListener != null){
					cell.current=temp;
					cell.total=total;
					cell.state = DL_FAIL;
					mProgressListener.progress(DL_FAIL, cell);
				}
			}
			dos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public interface ProgressListener{
		public void progress(int state,DLCell cell);
	}
}
