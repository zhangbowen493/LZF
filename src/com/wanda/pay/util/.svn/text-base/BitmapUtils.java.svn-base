package com.wanda.pay.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
/**
 * bitmap工具类
 * @author liuzhipeng
 *
 */
public class BitmapUtils {

	/**
	 * 根据图片路径压缩并返回bitmap
	 * @param path  图片路径
	 * @param maxLength  压缩后bitmap宽高较大值
	 * @param maxScale  压缩后bitmap宽高较小值比较大值
	 * @param maxSize  压缩后bitmap大小
	 * @return  图片压缩后的bitmap对象
	 */
	public static Bitmap getCompressBitmapByPath(String path,int maxLength,float maxScale,int maxSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int maxWidth=0;
        int maxHeight=0;
        if(height>width){
        	maxHeight=maxLength;
        	maxWidth=(int) (maxLength*maxScale);
        }else{
        	maxWidth=maxLength;
        	maxHeight=(int) (maxLength*maxScale);
        }
		
		Bitmap bitmap = compressBitmapSize(path, maxWidth, maxHeight);
        Bitmap bmS=Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, false);
		Bitmap bbb = compressBitmapQuality(bmS,maxSize);
        bitmap.recycle();
        bitmap=null;
        bmS.recycle();
        bmS=null;
        return bbb;
	}
	
	public static Bitmap getCompressBitmapByBitmap(Bitmap bitmap,int width,int height,int maxSize){
		Bitmap bm=Bitmap.createScaledBitmap(bitmap, width, height, false);
		Bitmap bbb=compressBitmapQuality(bm, maxSize);
		bm.recycle();
		bm=null;
		return bbb;
	}

	/**
	 * 根据图片路径压缩图片大小
	 * @param path  图片路径
	 * @param maxWidth  压缩后bitmap最大宽度
	 * @param maxHeight  压缩后bitmap最大高度
	 * @return  压缩后的bitmap
	 */
	private static Bitmap compressBitmapSize(String path,
                int maxWidth, int maxHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;
        
        // 在内存中创建bitmap对象，这个对象按照缩放大小创建的
        int inSampleSize = 1;

        if (height > maxHeight || width > maxWidth) {
                if (width > height) {
                        inSampleSize = Math.round((float) height / (float) maxHeight);
                } else {
                        inSampleSize = Math.round((float) width / (float) maxWidth);
                }
        }
        options.inJustDecodeBounds = false;
        
        return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * 压缩bitmap质量
	 * @param image  压缩对象
	 * @param maxSize  bitmap压缩后的最大大小
	 * @return  压缩后的bitmap
	 */
	private static Bitmap compressBitmapQuality(Bitmap image,int maxSize) {
		int options = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        
        while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于maxSize kb,大于继续压缩
                options -= 10;// 每次都减少10
                baos.reset();// 重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                Log.i("压缩图片质量", "op="+options);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        
        try {
			baos.close();
			isBm.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return bitmap;
	}

	/**  
	 * 读取图片属性：旋转的角度  
	 * @param path 图片绝对路径  
	 * @return degree旋转的角度  
	 */   
	 public static int readPictureDegree(String path) {
	  int degree = 0;
	  try {
	   ExifInterface exifInterface = new ExifInterface(path);
	   int orientation = exifInterface.getAttributeInt(
	     ExifInterface.TAG_ORIENTATION,
	     ExifInterface.ORIENTATION_NORMAL);
	   switch (orientation) {
	   case ExifInterface.ORIENTATION_ROTATE_90:
	    degree = 90;
	    break;
	   case ExifInterface.ORIENTATION_ROTATE_180:
	    degree = 180;
	    break;
	   case ExifInterface.ORIENTATION_ROTATE_270:
	    degree = 270;
	    break;
	   }
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
	  return degree;

	 }
	 
	 /**
	  * 图片旋转
	  * 
	  * @param degree
	  * @param bitmap
	  * @return
	  */
	 public static Bitmap rotaingImageView(int degree, Bitmap bitmap) {
	  // 旋转图片 动作
	  Matrix matrix = new Matrix();
	  matrix.postRotate(degree);
	  // 创建新的图片
	  Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
	    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	  return resizedBitmap;
	 }
	 
	/**
	 * 图片保存
	 * @param bm 保存的bitmap对象
	 * @param path 文件路径
	 */
	 public static void saveBitmap(Bitmap bm,String path) {
		File f = new File(path);
	     if (f.exists()) {
	      f.delete();
	     }
	     try {
	      FileOutputStream out = new FileOutputStream(f);
	      bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
	      out.flush();
	      out.close();
	     } catch (FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	     } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	     } 
	  }

}
