package com.inter.trade.volley.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PoolingByteArrayOutputStream extends ByteArrayOutputStream
{

	private static final int DEFAULT_SIZE = 256;

	private final ByteArrayPool mPool;

	public PoolingByteArrayOutputStream(ByteArrayPool pool)
	{
		this(pool, DEFAULT_SIZE);
	}

	public PoolingByteArrayOutputStream(ByteArrayPool pool, int size)
	{
		mPool = pool;
		buf = mPool.getBuf(Math.max(size, DEFAULT_SIZE));
	}

	@Override
	public void close() throws IOException
	{
		mPool.returnBuf(buf);
		buf = null;
		super.close();
	}

	@Override
	public void finalize()
	{
		mPool.returnBuf(buf);
	}

	private void expand(int i)
	{
		if (count + i <= buf.length)
		{
			return;
		}
		byte[] newbuf = mPool.getBuf((count + i) * 2);
		System.arraycopy(buf, 0, newbuf, 0, count);
		mPool.returnBuf(buf);
		buf = newbuf;
	}

	@Override
	public synchronized void write(byte[] buffer, int offset, int len)
	{
		expand(len);
		super.write(buffer, offset, len);
	}

	@Override
	public synchronized void write(int oneByte)
	{
		expand(1);
		super.write(oneByte);
	}
}
