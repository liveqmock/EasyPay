package com.inter.trade.qrsacncode.util;

import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.inter.trade.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 二维码帮助类
 * 
 * @see 
 * <ImageView
        android:id="@+id/dimencode"
        android:layout_width="250dp"
        android:layout_height="250dp"
        android:layout_centerInParent="true"
        android:scaleType="fitXY"/>
 * 建议显示二维码图片的ImageView配置
 * @author zhichao.huang
 *
 */
public class QRCodeUtils {
	
	// 图片的一般宽度
    private static final int IMAGE_HALFWIDTH = 30;
    
// // 需要插图图片的大小 这里设定为40*40
//    int[] pixels = new int[2*IMAGE_HALFWIDTH * 2*IMAGE_HALFWIDTH];
	
	/**
     * 生成普通的二维码（不带Logo）
     *
     * @param content 将要生成二维码的内容
     * @return 返回生成好的二维码事件
     * @throws WriterException WriterException异常
     */
    public static Bitmap createCommonQRCode(String content) throws WriterException {
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, 600, 600);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 生成一维码 
     * 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
     *
     * @param content 将要生成一维码的内容
     * @return 返回生成好的一维码bitmap
     * @throws WriterException WriterException异常
     */
    public static Bitmap createOneDCode(String content) throws WriterException {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.CODE_128, 500, 200);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    
    /**
     * 生成附加在二维码图片上的icon
     * @return
     */
    public static Bitmap getQRCenterBitmap(Context context){
    	// 插入到二维码里面的图片对象
        Bitmap mBitmap;
        
        Drawable d = context.getResources().getDrawable(R.drawable.easy_pay_logo);
        mBitmap = ((BitmapDrawable)d).getBitmap();
        
        // 缩放图片
        Matrix m = new Matrix();
        float sx = (float) 2*IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2*IMAGE_HALFWIDTH / mBitmap.getHeight();
        m.setScale(sx, sy);
        
        //重新构造一个40*40的图片
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                        mBitmap.getHeight(), m, false);
    	
    	return mBitmap;
    }
    
    /**
     * 创建二维码（带Logo）
     * @param str
     * @return
     * @throws WriterException 
     */
    public static Bitmap createAddLogoQRCode(Context context, String str) throws WriterException {
    	// 插入到二维码里面的图片对象
    	Bitmap mBitmap = getQRCenterBitmap(context);
    	
    	//关键语句，不然生成出带logo的二维码识别不出来
    	Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
        hints.put(EncodeHintType.MARGIN, 1); 
    	
    	  // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                        BarcodeFormat.QR_CODE, 300, 300, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                        if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH
                                        && y < halfH + IMAGE_HALFWIDTH) {
                                pixels[y * width + x] = mBitmap.getPixel(x - halfW + IMAGE_HALFWIDTH, y
                                                - halfH + IMAGE_HALFWIDTH);
                        } else {
                                if (matrix.get(x, y)) {
                                        pixels[y * width + x] = 0xff000000;
                                }
                        }

                }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                        Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }

}
