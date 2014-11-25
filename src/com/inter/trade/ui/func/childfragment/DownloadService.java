package com.inter.trade.ui.func.childfragment;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DownloadService
{

	public static final String TAG = "tag";
	/* 用于查询数据库 */
	private DBHelper dbHelper;
	/* 要下载的文件大小 */
	public int fileSize;
	/* 每条线程需要下载的数据量 */
	private int block;
	/* 保存文件地目录 */
	private File savedFile;
	/* 下载地址 */
	private String path;
	/* 是否停止下载 */
	public boolean isPause;
	/* 线程数 */
	private MultiThreadDownload[] threads;
	/* 各线程已经下载的数据量 */
	private Map<Integer, Integer> downloadedLength = new ConcurrentHashMap<Integer, Integer>();

	public DownloadService(String target, File destination, int thread_size,
			Context context) throws Exception
	{
		dbHelper = new DBHelper(context);
		this.threads = new MultiThreadDownload[thread_size];
		this.path = target;
		URL url = new URL(target);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");

		if (conn.getResponseCode() != 200) { throw new RuntimeException(
				"server no response!"); }

		fileSize = conn.getContentLength();
		if (fileSize <= 0) { throw new RuntimeException("file is incorrect!"); }
		String fileName = getFileName(conn);
		if (!destination.exists()) destination.mkdirs();
		// 构建一个同样大小的文件
		this.savedFile = new File(destination, fileName);
		RandomAccessFile doOut = new RandomAccessFile(savedFile, "rwd");
		doOut.setLength(fileSize);
		doOut.close();
		conn.disconnect();

		// 计算每条线程需要下载的数据长度
		this.block = fileSize % thread_size == 0 ? fileSize / thread_size
				: fileSize / thread_size + 1;
		// 查询已经下载的记录
		downloadedLength = this.getDownloadedLength(path);
	}

	private Map<Integer, Integer> getDownloadedLength(String path)
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String sql = "SELECT threadId,downLength FROM fileDownloading WHERE downPath=?";
		Cursor cursor = db.rawQuery(sql, new String[]
		{ path });
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		while (cursor.moveToNext())
		{
			data.put(cursor.getInt(0), cursor.getInt(1));
		}
		db.close();
		return data;
	}

	private String getFileName(HttpURLConnection conn)
	{
		String fileName = path.substring(path.lastIndexOf("/") + 1,
				path.length());
		if (fileName == null || "".equals(fileName.trim()))
		{
			String content_disposition = null;
			for (Entry<String, List<String>> entry : conn.getHeaderFields()
					.entrySet())
			{
				if ("content-disposition".equalsIgnoreCase(entry.getKey()))
				{
					content_disposition = entry.getValue().toString();
				}
			}
			try
			{
				Matcher matcher = Pattern.compile(".*filename=(.*)").matcher(
						content_disposition);
				if (matcher.find()) fileName = matcher.group(1);
			}
			catch (Exception e)
			{
				fileName = UUID.randomUUID().toString() + ".tmp"; // 默认名
			}
		}
		return fileName;
	}

	public void download(DownloadListener listener) throws Exception
	{
		this.deleteDownloading(); // 先删除上次的记录,再重新添加
		for (int i = 0; i < threads.length; i++)
		{
			threads[i] = new MultiThreadDownload(i, savedFile, block, path,
					downloadedLength.get(i), this);
			new Thread(threads[i]).start();
		}
		this.saveDownloading(threads);

		while (!isFinish(threads))
		{
			Thread.sleep(900);
			if (listener != null) listener
					.onDownload(getDownloadedSize(threads));
			this.updateDownloading(threads);
		}
		if (!this.isPause) this.deleteDownloading();// 完成下载之后删除本次下载记录
	}

	private void saveDownloading(MultiThreadDownload[] threads)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try
		{
			db.beginTransaction();
			for (MultiThreadDownload thread : threads)
			{
				String sql = "INSERT INTO fileDownloading(downPath,threadId,downLength) values(?,?,?)";
				db.execSQL(sql, new Object[]
				{ path, thread.id, 0 });
			}
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
			db.close();
		}
	}

	private void deleteDownloading()
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "DELETE FROM fileDownloading WHERE downPath=?";
		db.execSQL(sql, new Object[]
		{ path });
		db.close();
	}

	private void updateDownloading(MultiThreadDownload[] threads)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try
		{
			db.beginTransaction();
			for (MultiThreadDownload thread : threads)
			{
				String sql = "UPDATE fileDownloading SET downLength=? WHERE threadId=? AND downPath=?";
				db.execSQL(sql, new String[]
				{ thread.currentDownloadSize + "", thread.id + "", path });
			}
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
			db.close();
		}
	}

	private int getDownloadedSize(MultiThreadDownload[] threads)
	{
		int sum = 0;
		for (int len = threads.length, i = 0; i < len; i++)
		{
			sum += threads[i].currentDownloadSize;
		}
		return sum;
	}

	private boolean isFinish(MultiThreadDownload[] threads)
	{
		try
		{
			for (int len = threads.length, i = 0; i < len; i++)
			{
				if (!threads[i].finished) { return false; }
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}