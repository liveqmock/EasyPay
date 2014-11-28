package com.inter.trade.volley.util;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inter.trade.volley.Cache;
import com.inter.trade.volley.VolleyLog;

import android.os.SystemClock;


public class DiskBasedCache implements Cache
{

	private final Map<String, CacheHeader> mEntries = new LinkedHashMap<String, CacheHeader>(
			16, .75f, true);

	private long mTotalSize = 0;

	private final File mRootDirectory;

	private final int mMaxCacheSizeInBytes;

	private static final int DEFAULT_DISK_USAGE_BYTES = 5 * 1024 * 1024;

	private static final float HYSTERESIS_FACTOR = 0.9f;

	private static final int CACHE_MAGIC = 0x20120504;

	public DiskBasedCache(File rootDirectory, int maxCacheSizeInBytes)
	{
		mRootDirectory = rootDirectory;
		mMaxCacheSizeInBytes = maxCacheSizeInBytes;
	}

	public DiskBasedCache(File rootDirectory)
	{
		this(rootDirectory, DEFAULT_DISK_USAGE_BYTES);
	}

	@Override
	public synchronized void clear()
	{
		File[] files = mRootDirectory.listFiles();
		if (files != null)
		{
			for (File file : files)
			{
				file.delete();
			}
		}
		mEntries.clear();
		mTotalSize = 0;
		VolleyLog.d("Cache cleared.");
	}

	@Override
	public synchronized Entry get(String key)
	{
		CacheHeader entry = mEntries.get(key);
		if (entry == null)
		{
			return null;
		}

		File file = getFileForKey(key);
		CountingInputStream cis = null;
		try
		{
			cis = new CountingInputStream(new FileInputStream(file));
			CacheHeader.readHeader(cis);
			byte[] data = streamToBytes(cis,
					(int) (file.length() - cis.bytesRead));
			return entry.toCacheEntry(data);
		}
		catch (IOException e)
		{
			VolleyLog.d("%s: %s", file.getAbsolutePath(), e.toString());
			remove(key);
			return null;
		}
		finally
		{
			if (cis != null)
			{
				try
				{
					cis.close();
				}
				catch (IOException ioe)
				{
					return null;
				}
			}
		}
	}

	@Override
	public synchronized void initialize()
	{
		if (!mRootDirectory.exists())
		{
			if (!mRootDirectory.mkdirs())
			{
				VolleyLog.e("Unable to create cache dir %s",
						mRootDirectory.getAbsolutePath());
			}
			return;
		}

		File[] files = mRootDirectory.listFiles();
		if (files == null)
		{
			return;
		}
		for (File file : files)
		{
			FileInputStream fis = null;
			try
			{
				fis = new FileInputStream(file);
				CacheHeader entry = CacheHeader.readHeader(fis);
				entry.size = file.length();
				putEntry(entry.key, entry);
			}
			catch (IOException e)
			{
				if (file != null)
				{
					file.delete();
				}
			}
			finally
			{
				try
				{
					if (fis != null)
					{
						fis.close();
					}
				}
				catch (IOException ignored)
				{
				}
			}
		}
	}

	@Override
	public synchronized void invalidate(String key, boolean fullExpire)
	{
		Entry entry = get(key);
		if (entry != null)
		{
			entry.softTtl = 0;
			if (fullExpire)
			{
				entry.ttl = 0;
			}
			put(key, entry);
		}

	}

	@Override
	public synchronized void put(String key, Entry entry)
	{
		pruneIfNeeded(entry.data.length);
		File file = getFileForKey(key);
		try
		{
			FileOutputStream fos = new FileOutputStream(file);
			CacheHeader e = new CacheHeader(key, entry);
			e.writeHeader(fos);
			fos.write(entry.data);
			fos.close();
			putEntry(key, e);
			return;
		}
		catch (IOException e)
		{
		}
		boolean deleted = file.delete();
		if (!deleted)
		{
			VolleyLog.d("Could not clean up file %s", file.getAbsolutePath());
		}
	}

	@Override
	public synchronized void remove(String key)
	{
		boolean deleted = getFileForKey(key).delete();
		removeEntry(key);
		if (!deleted)
		{
			VolleyLog.d("Could not delete cache entry for key=%s, filename=%s",
					key, getFilenameForKey(key));
		}
	}

	private String getFilenameForKey(String key)
	{
		int firstHalfLength = key.length() / 2;
		String localFilename = String.valueOf(key.substring(0, firstHalfLength)
				.hashCode());
		localFilename += String.valueOf(key.substring(firstHalfLength)
				.hashCode());
		return localFilename;
	}

	public File getFileForKey(String key)
	{
		return new File(mRootDirectory, getFilenameForKey(key));
	}

	private void pruneIfNeeded(int neededSpace)
	{
		if ((mTotalSize + neededSpace) < mMaxCacheSizeInBytes)
		{
			return;
		}
		if (VolleyLog.DEBUG)
		{
			VolleyLog.v("Pruning old cache entries.");
		}

		long before = mTotalSize;
		int prunedFiles = 0;
		long startTime = SystemClock.elapsedRealtime();

		Iterator<Map.Entry<String, CacheHeader>> iterator = mEntries.entrySet()
				.iterator();
		while (iterator.hasNext())
		{
			Map.Entry<String, CacheHeader> entry = iterator.next();
			CacheHeader e = entry.getValue();
			boolean deleted = getFileForKey(e.key).delete();
			if (deleted)
			{
				mTotalSize -= e.size;
			}
			else
			{
				VolleyLog.d(
						"Could not delete cache entry for key=%s, filename=%s",
						e.key, getFilenameForKey(e.key));
			}
			iterator.remove();
			prunedFiles++;

			if ((mTotalSize + neededSpace) < mMaxCacheSizeInBytes
					* HYSTERESIS_FACTOR)
			{
				break;
			}
		}

		if (VolleyLog.DEBUG)
		{
			VolleyLog.v("pruned %d files, %d bytes, %d ms", prunedFiles,
					(mTotalSize - before), SystemClock.elapsedRealtime()
							- startTime);
		}
	}

	private void putEntry(String key, CacheHeader entry)
	{
		if (!mEntries.containsKey(key))
		{
			mTotalSize += entry.size;
		}
		else
		{
			CacheHeader oldEntry = mEntries.get(key);
			mTotalSize += (entry.size - oldEntry.size);
		}
		mEntries.put(key, entry);
	}

	private void removeEntry(String key)
	{
		CacheHeader entry = mEntries.get(key);
		if (entry != null)
		{
			mTotalSize -= entry.size;
			mEntries.remove(key);
		}
	}

	private static byte[] streamToBytes(InputStream in, int length)
			throws IOException
	{
		byte[] bytes = new byte[length];
		int count;
		int pos = 0;
		while (pos < length
				&& ((count = in.read(bytes, pos, length - pos)) != -1))
		{
			pos += count;
		}
		if (pos != length)
		{
			throw new IOException("Expected " + length + " bytes, read " + pos
					+ " bytes");
		}
		return bytes;
	}

	static class CacheHeader
	{

		public long size;

		public String key;

		public String etag;

		public long serverDate;

		public long ttl;

		public long softTtl;

		public Map<String, String> responseHeaders;

		private CacheHeader()
		{
		}

		public CacheHeader(String key, Entry entry)
		{
			this.key = key;
			this.size = entry.data.length;
			this.etag = entry.etag;
			this.serverDate = entry.serverDate;
			this.ttl = entry.ttl;
			this.softTtl = entry.softTtl;
			this.responseHeaders = entry.responseHeaders;
		}

		public static CacheHeader readHeader(InputStream is) throws IOException
		{
			CacheHeader entry = new CacheHeader();
			int magic = readInt(is);
			if (magic != CACHE_MAGIC)
			{
				throw new IOException();
			}
			entry.key = readString(is);
			entry.etag = readString(is);
			if (entry.etag.equals(""))
			{
				entry.etag = null;
			}
			entry.serverDate = readLong(is);
			entry.ttl = readLong(is);
			entry.softTtl = readLong(is);
			entry.responseHeaders = readStringStringMap(is);
			return entry;
		}

		public Entry toCacheEntry(byte[] data)
		{
			Entry e = new Entry();
			e.data = data;
			e.etag = etag;
			e.serverDate = serverDate;
			e.ttl = ttl;
			e.softTtl = softTtl;
			e.responseHeaders = responseHeaders;
			return e;
		}

		public boolean writeHeader(OutputStream os)
		{
			try
			{
				writeInt(os, CACHE_MAGIC);
				writeString(os, key);
				writeString(os, etag == null ? "" : etag);
				writeLong(os, serverDate);
				writeLong(os, ttl);
				writeLong(os, softTtl);
				writeStringStringMap(responseHeaders, os);
				os.flush();
				return true;
			}
			catch (IOException e)
			{
				VolleyLog.d("%s", e.toString());
				return false;
			}
		}

	}

	private static class CountingInputStream extends FilterInputStream
	{
		private int bytesRead = 0;

		private CountingInputStream(InputStream in)
		{
			super(in);
		}

		@Override
		public int read() throws IOException
		{
			int result = super.read();
			if (result != -1)
			{
				bytesRead++;
			}
			return result;
		}

		@Override
		public int read(byte[] buffer, int offset, int count)
				throws IOException
		{
			int result = super.read(buffer, offset, count);
			if (result != -1)
			{
				bytesRead += result;
			}
			return result;
		}
	}

	private static int read(InputStream is) throws IOException
	{
		int b = is.read();
		if (b == -1)
		{
			throw new EOFException();
		}
		return b;
	}

	static void writeInt(OutputStream os, int n) throws IOException
	{
		os.write((n >> 0) & 0xff);
		os.write((n >> 8) & 0xff);
		os.write((n >> 16) & 0xff);
		os.write((n >> 24) & 0xff);
	}

	static int readInt(InputStream is) throws IOException
	{
		int n = 0;
		n |= (read(is) << 0);
		n |= (read(is) << 8);
		n |= (read(is) << 16);
		n |= (read(is) << 24);
		return n;
	}

	static void writeLong(OutputStream os, long n) throws IOException
	{
		os.write((byte) (n >>> 0));
		os.write((byte) (n >>> 8));
		os.write((byte) (n >>> 16));
		os.write((byte) (n >>> 24));
		os.write((byte) (n >>> 32));
		os.write((byte) (n >>> 40));
		os.write((byte) (n >>> 48));
		os.write((byte) (n >>> 56));
	}

	static long readLong(InputStream is) throws IOException
	{
		long n = 0;
		n |= ((read(is) & 0xFFL) << 0);
		n |= ((read(is) & 0xFFL) << 8);
		n |= ((read(is) & 0xFFL) << 16);
		n |= ((read(is) & 0xFFL) << 24);
		n |= ((read(is) & 0xFFL) << 32);
		n |= ((read(is) & 0xFFL) << 40);
		n |= ((read(is) & 0xFFL) << 48);
		n |= ((read(is) & 0xFFL) << 56);
		return n;
	}

	static void writeString(OutputStream os, String s) throws IOException
	{
		byte[] b = s.getBytes("UTF-8");
		writeLong(os, b.length);
		os.write(b, 0, b.length);
	}

	static String readString(InputStream is) throws IOException
	{
		int n = (int) readLong(is);
		byte[] b = streamToBytes(is, n);
		return new String(b, "UTF-8");
	}

	static void writeStringStringMap(Map<String, String> map, OutputStream os)
			throws IOException
	{
		if (map != null)
		{
			writeInt(os, map.size());
			for (Map.Entry<String, String> entry : map.entrySet())
			{
				writeString(os, entry.getKey());
				writeString(os, entry.getValue());
			}
		}
		else
		{
			writeInt(os, 0);
		}
	}

	static Map<String, String> readStringStringMap(InputStream is)
			throws IOException
	{
		int size = readInt(is);
		Map<String, String> result = (size == 0) ? Collections
				.<String, String> emptyMap()
				: new HashMap<String, String>(size);
		for (int i = 0; i < size; i++)
		{
			String key = readString(is).intern();
			String value = readString(is).intern();
			result.put(key, value);
		}
		return result;
	}

}
