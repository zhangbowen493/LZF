/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sitech.oncon.barcode.core;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.sitech.oncon.barcode.camera.CameraManager;
import com.wanda.pay.R;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 * 
 * @author gaotaiwen@gmail.com ()
 */
public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {
            0, 64, 128, 192, 255, 192, 128, 64
    };
    private static final long ANIMATION_DELAY = 100L;
    private static final int OPAQUE = 0xFF;
	private static final int SPEEN_DISTANCE = 10;
	private static final int MIDDLE_LINE_PADDING = 5;
	private static final int MIDDLE_LINE_WIDTH = 5;

    private final Paint paint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int frameColor;
    private final int resultPointColor;
    private final int whiteColor;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private boolean isFirst;
    private int slideTop;
	private int slideBottom;

	private Bitmap qrLineBitmap;//΢�ŵ�ɨ������һ��ͼƬ
    private int qrWidth;//ɨ���ߵĳ�
    private int qrHeight;//ɨ���ߵĸ�
    private Rect qrSrc;
    private Rect qrDst;
    private CameraManager cameraManager;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every
        // time in onDraw().
        paint = new Paint();
        Resources resources = getResources();
        
        qrLineBitmap = BitmapFactory.decodeResource(resources, R.drawable.qrcode_scan_line);
        qrWidth = qrLineBitmap.getWidth();
        qrHeight = qrLineBitmap.getHeight();
        qrSrc=new Rect(0, 0, qrWidth, qrHeight);

        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        frameColor = resources.getColor(R.color.viewfinder_frame);
        resultPointColor = resources.getColor(R.color.possible_result_points);
        whiteColor = resources.getColor(R.color.white);
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }
    public void setCameraManager(CameraManager cameraManager) {
		this.cameraManager = cameraManager;
	}
    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = cameraManager.getFramingRect();
        if (frame == null){
            return;
        }
        
      //��ʼ���м��߻��������ϱߺ����±�  
        if(!isFirst){  
            isFirst = true;  
            slideTop = frame.top;  
            slideBottom = frame.bottom;  
        }  
        
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {
        	
        	//�����м����,ÿ��ˢ�½��棬�м���������ƶ�SPEEN_DISTANCE  
            slideTop += SPEEN_DISTANCE;  
            if(slideTop >= frame.bottom){  
                slideTop = frame.top;  
            }

            qrDst=new Rect(frame.left, slideTop, frame.right, slideTop+qrHeight);
            canvas.drawBitmap(qrLineBitmap,qrSrc,qrDst ,null);

            paint.setColor(whiteColor);
            paint.setStrokeWidth(2);
            canvas.drawLine(frame.left, frame.top-1, frame.right, frame.top-1, paint);
            canvas.drawLine(frame.left-1, frame.top, frame.left-1, frame.bottom, paint);
            canvas.drawLine(frame.right+1, frame.top, frame.right+1, frame.bottom, paint);
            canvas.drawLine(frame.left, frame.bottom+1, frame.right, frame.bottom+1, paint);
            
            int linewidth = 10;
            paint.setColor(frameColor);

            // draw rect
            canvas.drawRect(-10 + frame.left, -10 + frame.top,
                    -10 + (linewidth + frame.left), -10 + (50 + frame.top), paint);
            canvas.drawRect(-10 + frame.left, -10 + frame.top,
                    -10 + (50 + frame.left), -10 + (linewidth + frame.top), paint);
            
            canvas.drawRect(10 + ((0 - linewidth) + frame.right),
                    -10 + frame.top, 10 + (1 + frame.right),
                    -10 + (50 + frame.top), paint);
            canvas.drawRect(10 + (-50 + frame.right), -10 + frame.top, 10
                    + frame.right, -10 + (linewidth + frame.top), paint);
           
            canvas.drawRect(-10 + frame.left, 10 + (-49 + frame.bottom),
                    -10 + (linewidth + frame.left), 10 + (1 + frame.bottom),
                    paint);
            canvas.drawRect(-10 + frame.left, 10
                    + ((0 - linewidth) + frame.bottom), -10 + (50 + frame.left),
                    10 + (1 + frame.bottom), paint);
            
            canvas.drawRect(10 + ((0 - linewidth) + frame.right), 10
                    + (-49 + frame.bottom), 10 + (1 + frame.right), 10
                    + (1 + frame.bottom), paint);
            canvas.drawRect(10 + (-50 + frame.right), 10
                    + ((0 - linewidth) + frame.bottom), 10 + frame.right, 10
                    + (linewidth - (linewidth - 1) + frame.bottom), paint);

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }

            // Request another update at the animation interval, but only
            // repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom);
        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     * 
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}
