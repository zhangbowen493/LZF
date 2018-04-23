package com.wanda.pay.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;

import com.wanda.pay.bean.Constant_data;
import com.wanda.pay.util.BitmapUtils;
import com.wanda.pay.util.Tools;

public class CropImageView extends View {  
    // 在touch重要用到的点，  
    private float mX_1 = 0;  
    private float mY_1 = 0;  
    // 触摸事件判断  
    private final int STATUS_SINGLE = 1;  
    private final int STATUS_MULTI_START = 2;  
    private final int STATUS_MULTI_TOUCHING = 3;  
    // 当前状态  
    private int mStatus = STATUS_SINGLE;  
    // 默认裁剪的宽高  
    private int cropWidth;  
    private int cropHeight;  
    // 浮层Drawable的四个点  
    private final int EDGE_LT = 1;  
    private final int EDGE_RT = 2;  
    private final int EDGE_LB = 3;  
    private final int EDGE_RB = 4;  
    private final int EDGE_MOVE_IN = 5;  
    private final int EDGE_MOVE_OUT = 6;  
    private final int EDGE_NONE = 7;  
  
    public int currentEdge = EDGE_NONE;  
  
    protected float oriRationWH = 0;  
    protected final float maxZoomOut = 5.0f;  
    protected final float minZoomIn = 0.333333f;  
  
    protected Drawable mDrawable;  
    protected FloatDrawable mFloatDrawable;  
  
    protected Rect mDrawableSrc = new Rect();// 图片Rect变换时的Rect  
    protected Rect mDrawableDst = new Rect();// 图片Rect  
    protected Rect mDrawableFloat = new Rect();// 浮层的Rect  
    protected boolean isFrist = true;  
    private boolean isTouchInSquare = true;  
  
    protected Context mContext;  
    /**是否全屏裁剪*/
    private boolean allScreen;
    
    
    /**是否可以改变裁剪大小*/
    private boolean canChangeSize=true;
    /**是否可以拖动矩形框*/
    private boolean canMove=true;
    /**拍照*/
    private Camera camera;
    /**屏幕方向*/
    private IOrientationEventListener iOrientationEventListener;
    /**当前屏幕方向*/
    private int currentOrientation=0;
    /**屏幕旋转0度*/
    private final int DEGREE_0=0;
    /**屏幕旋转90度*/
    private final int DEGREE_90=90;
    /**屏幕旋转180度*/
    private final int DEGREE_180=180;
    /**屏幕旋转270度*/
    private final int DEGREE_270=270;
    
    
    
    public CropImageView(Context context) {  
        super(context);  
        init(context);  
    }  
  
    public CropImageView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init(context);  
    }  
  
    public CropImageView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init(context);  
  
    }  
  
    @SuppressLint("NewApi")  
    private void init(Context context) {  
        this.mContext = context;  
        try {  
            if (android.os.Build.VERSION.SDK_INT >= 11) {  
                this.setLayerType(LAYER_TYPE_SOFTWARE, null);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        mFloatDrawable = new FloatDrawable(context);  
    }  
  
    /**
     * 裁剪图片
     * @param mDrawable 要裁剪的图片
     * @param cropWidth
     * @param cropHeight
     */
    public void setDrawable(Drawable mDrawable, int cropWidth, int cropHeight) {  
        this.mDrawable = mDrawable;  
        //this.cropWidth = cropWidth;  
        //this.cropHeight = cropHeight;  
        this.isFrist = true;  
        invalidate();  
    }  
    
    /**
     * 设置拍照模式
     * @param camera
     * @param cropWidth
     * @param cropHeight
     */
    public void setCamera(Camera camera, int cropWidth, int cropHeight,boolean screen){
    	this.mDrawable =new BitmapDrawable(Bitmap.createBitmap(getWidth(), getHeight(), Config.ALPHA_8));  
        this.cropWidth = cropWidth;  
        this.cropHeight = cropHeight;  
        this.isFrist = true; 
        this.camera=camera;
        this.allScreen=screen;
        iOrientationEventListener = new IOrientationEventListener(mContext);
        iOrientationEventListener.enable();
        if(allScreen)setFloatDrawableDraw(false);
        invalidate();
    }
  
    @SuppressLint("ClickableViewAccessibility")  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
  
        if (event.getPointerCount() > 1) {  
            if (mStatus == STATUS_SINGLE) {  
                mStatus = STATUS_MULTI_START;  
            } else if (mStatus == STATUS_MULTI_START) {  
                mStatus = STATUS_MULTI_TOUCHING;  
            }  
        } else {  
            if (mStatus == STATUS_MULTI_START  
                    || mStatus == STATUS_MULTI_TOUCHING) {  
                mX_1 = event.getX();  
                mY_1 = event.getY();  
            }  
  
            mStatus = STATUS_SINGLE;  
        }  
  
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            mX_1 = event.getX();  
            mY_1 = event.getY();  
            currentEdge = getTouch((int) mX_1, (int) mY_1);  
            isTouchInSquare = mDrawableFloat.contains((int) event.getX(),  
                    (int) event.getY());  
  
            break;  
  
        case MotionEvent.ACTION_UP:  
            checkBounds();  
            break;  
  
        case MotionEvent.ACTION_POINTER_UP:  
            currentEdge = EDGE_NONE;  
            break;  
  
        case MotionEvent.ACTION_MOVE:  
            if (mStatus == STATUS_MULTI_TOUCHING) {  
  
            } else if (mStatus == STATUS_SINGLE) {  
                int dx = (int) (event.getX() - mX_1);  
                int dy = (int) (event.getY() - mY_1);  
  
                mX_1 = event.getX();  
                mY_1 = event.getY();  
                // 根據得到的那一个角，并且变换Rect  
                if (!(dx == 0 && dy == 0)) {  
                	
                    if(canChangeSize){
                    	int[] d=checkMove(dx, dy, currentEdge);
                    	dx=d[0];
                    	dy=d[1];
                    	switch (currentEdge) {  
                        case EDGE_LT:  
                            mDrawableFloat.set(mDrawableFloat.left + dx,  
                                    mDrawableFloat.top + dy, mDrawableFloat.right,  
                                    mDrawableFloat.bottom);  
                            break;  
      
                        case EDGE_RT:  
                            mDrawableFloat.set(mDrawableFloat.left,  
                                    mDrawableFloat.top + dy, mDrawableFloat.right  
                                            + dx, mDrawableFloat.bottom);  
                            break;  
      
                        case EDGE_LB:  
                            mDrawableFloat.set(mDrawableFloat.left + dx,  
                                    mDrawableFloat.top, mDrawableFloat.right,  
                                    mDrawableFloat.bottom + dy);  
                            break;  
      
                        case EDGE_RB:  
                            mDrawableFloat.set(mDrawableFloat.left,  
                                    mDrawableFloat.top, mDrawableFloat.right + dx,  
                                    mDrawableFloat.bottom + dy);  
                            break;  
      
                        case EDGE_MOVE_IN:  
                            if (isTouchInSquare && canMove) {  
                                mDrawableFloat.offset(dx, dy);  
                            }  
                            break;  
      
                        case EDGE_MOVE_OUT:  
                            break;  
                        }  
                    }else{
                    	switch (currentEdge) {
						case EDGE_MOVE_OUT:
							
							break;

						default:
							if (isTouchInSquare && canMove) {  
                            	int[] d=checkMove(dx, dy,currentEdge);
                                mDrawableFloat.offset(d[0], d[1]);  
                            } 
							
							break;
						}
                    }
                    mDrawableFloat.sort();  
                    invalidate();  
                }  
            }  
            break;  
        }  
        //手动对焦
        if(camera!=null && event.getAction()==MotionEvent.ACTION_DOWN){
        	float x = event.getX();
            float y = event.getY();
            float touchMajor = event.getTouchMajor();
            float touchMinor = event.getTouchMinor();

            Rect touchRect = new Rect((int) (x - touchMajor / 2),
                            (int) (y - touchMinor / 2), (int) (x + touchMajor / 2),
                            (int) (y + touchMinor / 2));

            try {
            	this.submitFocusAreaRect(touchRect);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
        }
  
        return true;  
    }  
    
    /**
     * 判断mFloatDrawable是否会拖拽出原图
     * @param x x轴改变量
     * @param y y轴改变量
     * @return true 会超出屏幕
     */
    private int[] checkMove(int dx,int dy,int currentEdge){
    	switch (currentEdge) {
		case EDGE_LT:
			if(mDrawableFloat.left + dx<mDrawableSrc.left) dx=mDrawableSrc.left-mDrawableFloat.left;
			if(mDrawableFloat.top + dy<mDrawableSrc.top) dy=mDrawableSrc.top-mDrawableFloat.top;
			break;
		case EDGE_RT:
			if(mDrawableFloat.right + dx>mDrawableSrc.right) dx=mDrawableSrc.right-mDrawableFloat.right;
			if(mDrawableFloat.top + dy<mDrawableSrc.top) dy=mDrawableSrc.top-mDrawableFloat.top;
			break;
		case EDGE_LB:
			if(mDrawableFloat.left + dx<mDrawableSrc.left) dx=mDrawableSrc.left-mDrawableFloat.left;
			if(mDrawableFloat.bottom + dy>mDrawableSrc.bottom) dy=mDrawableSrc.bottom-mDrawableFloat.bottom;
			break;
		case EDGE_RB:
			if(mDrawableFloat.right + dx>mDrawableSrc.right) dx=mDrawableSrc.right-mDrawableFloat.right;
			if(mDrawableFloat.bottom + dy>mDrawableSrc.bottom) dy=mDrawableSrc.bottom-mDrawableFloat.bottom;
			break;
		case EDGE_MOVE_IN:
			
			if(mDrawableFloat.left + dx<mDrawableSrc.left) dx=mDrawableSrc.left-mDrawableFloat.left;
			if(mDrawableFloat.right + dx>mDrawableSrc.right) dx=mDrawableSrc.right-mDrawableFloat.right;
			if(mDrawableFloat.top + dy<mDrawableSrc.top) dy=mDrawableSrc.top-mDrawableFloat.top;
			if(mDrawableFloat.bottom + dy>mDrawableSrc.bottom) dy=mDrawableSrc.bottom-mDrawableFloat.bottom;
			break;

		default:
			break;
		}
    	return new int[]{dx,dy};
    }
    
  
    // 根据初触摸点判断是触摸的Rect哪一个角  
    public int getTouch(int eventX, int eventY) {  
        if (mFloatDrawable.getBounds().left <= eventX  
                && eventX < (mFloatDrawable.getBounds().left + mFloatDrawable  
                        .getBorderWidth())  
                && mFloatDrawable.getBounds().top <= eventY  
                && eventY < (mFloatDrawable.getBounds().top + mFloatDrawable  
                        .getBorderHeight())) {  
            return EDGE_LT;  
        } else if ((mFloatDrawable.getBounds().right - mFloatDrawable  
                .getBorderWidth()) <= eventX  
                && eventX < mFloatDrawable.getBounds().right  
                && mFloatDrawable.getBounds().top <= eventY  
                && eventY < (mFloatDrawable.getBounds().top + mFloatDrawable  
                        .getBorderHeight())) {  
            return EDGE_RT;  
        } else if (mFloatDrawable.getBounds().left <= eventX  
                && eventX < (mFloatDrawable.getBounds().left + mFloatDrawable  
                        .getBorderWidth())  
                && (mFloatDrawable.getBounds().bottom - mFloatDrawable  
                        .getBorderHeight()) <= eventY  
                && eventY < mFloatDrawable.getBounds().bottom) {  
            return EDGE_LB;  
        } else if ((mFloatDrawable.getBounds().right - mFloatDrawable  
                .getBorderWidth()) <= eventX  
                && eventX < mFloatDrawable.getBounds().right  
                && (mFloatDrawable.getBounds().bottom - mFloatDrawable  
                        .getBorderHeight()) <= eventY  
                && eventY < mFloatDrawable.getBounds().bottom) {  
            return EDGE_RB;  
        } else if (mFloatDrawable.getBounds().contains(eventX, eventY)) {  
            return EDGE_MOVE_IN;  
        }  
        return EDGE_MOVE_OUT;  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
  
        if (mDrawable == null) {  
            return;  
        }  
  
        if (mDrawable.getIntrinsicWidth() == 0  
                || mDrawable.getIntrinsicHeight() == 0) {  
            return;  
        }  
  
        configureBounds();  
        // 在画布上花图片  
        mDrawable.draw(canvas);  
        canvas.save();  
        // 在画布上画浮层FloatDrawable,Region.Op.DIFFERENCE是表示Rect交集的补集  
        canvas.clipRect(mDrawableFloat, Region.Op.DIFFERENCE);  
        // 在交集的补集上画上灰色用来区分  
        canvas.drawColor(Color.parseColor("#a0000000"));  
        canvas.restore();  
        // 画浮层  
        mFloatDrawable.draw(canvas);  
    }  
  
    protected void configureBounds() {  
        // configureBounds在onDraw方法中调用  
        // isFirst的目的是下面对mDrawableSrc和mDrawableFloat只初始化一次，  
        // 之后的变化是根据touch事件来变化的，而不是每次执行重新对mDrawableSrc和mDrawableFloat进行设置  
        if (isFrist) {  
            oriRationWH = ((float) mDrawable.getIntrinsicWidth())  
                    / ((float) mDrawable.getIntrinsicHeight());  
  
            final float scale = mContext.getResources().getDisplayMetrics().density;  
            int w = Math.min(getWidth(), (int) (mDrawable.getIntrinsicWidth()  
                    * scale + 0.5f));  
            int h = (int) (w / oriRationWH);  
  
            int left = (getWidth() - w) / 2;  
            int top = (getHeight() - h) / 2;  
            int right = left + w;  
            int bottom = top + h;  
  
            mDrawableSrc.set(left, top, right, bottom);  
            mDrawableDst.set(mDrawableSrc);  
  
            //dp
            int floatWidth = Tools.dipTopx(mContext, cropWidth);  
            int floatHeight = Tools.dipTopx(mContext, cropHeight);  
            //px
            //int floatWidth = cropWidth;  
            //int floatHeight = cropHeight;  
  
            if (floatWidth > getWidth()) {  
                floatWidth = getWidth();  
                floatHeight = cropHeight * floatWidth / cropWidth;  
            }  
  
            if (floatHeight > getHeight()) {  
                floatHeight = getHeight();  
                floatWidth = cropWidth * floatHeight / cropHeight;  
            }  
  
            int floatLeft = (getWidth() - floatWidth) / 2;  
            int floatTop = (getHeight() - floatHeight) / 2;
            if(camera==null){
            	int padW=mDrawableSrc.width()/8;
            	int padH=mDrawableSrc.height()/8;
            	mDrawableFloat.set(left+padW, top+padH, right-padW, bottom-padH);
            }else{
            	if(allScreen){
            		mDrawableFloat.set(mDrawableSrc);
            		Log.i("裁剪前大小",mDrawableSrc.toString()+ mDrawableFloat.toString());
            	}else{
            		mDrawableFloat.set(floatLeft, floatTop, floatLeft + floatWidth,  
            				floatTop + floatHeight);  
            	}
            }
  
            isFrist = false;  
            
        }  
  
        mDrawable.setBounds(mDrawableDst);  
        mFloatDrawable.setBounds(mDrawableFloat);  
    }  
  
    // 在up事件中调用了该方法，目的是检查是否把浮层拖出了屏幕  
    protected void checkBounds() {  
        int newLeft = mDrawableFloat.left;  
        int newTop = mDrawableFloat.top;  
  
        boolean isChange = false;  
        if (mDrawableFloat.left < getLeft()) {  
            newLeft = getLeft();  
            isChange = true;  
        }  
  
        if (mDrawableFloat.top < getTop()) {  
            newTop = getTop();  
            isChange = true;  
        }  
  
        if (mDrawableFloat.right > getRight()) {  
            newLeft = getRight() - mDrawableFloat.width();  
            isChange = true;  
        }  
  
        if (mDrawableFloat.bottom > getBottom()) {  
            newTop = getBottom() - mDrawableFloat.height();  
            isChange = true;  
        }  
  
        mDrawableFloat.offsetTo(newLeft, newTop);  
        if (isChange) {  
            invalidate();  
        }  
    }  
  
    // 进行图片的裁剪，所谓的裁剪就是根据Drawable的新的坐标在画布上创建一张新的图片  
    public Bitmap getCropImage() {  
    	if(camera!=null)return null;
    	Bitmap tmpBitmap = Bitmap.createBitmap(getWidth(), getHeight(),  
    			Config.RGB_565);  
    	Canvas canvas = new Canvas(tmpBitmap);  
    	mDrawable.draw(canvas);  
    	Log.i("裁剪图片原大小", "width="+tmpBitmap.getWidth()+" height="+tmpBitmap.getHeight()+" size="+tmpBitmap.getRowBytes()*tmpBitmap.getHeight()/1024);
        
    	Matrix matrix = new Matrix();  
    	float scale = (float) (mDrawableSrc.width())  
    			/ (float) (mDrawableDst.width());  
    	matrix.postScale(scale, scale);  
  
        Bitmap ret = Bitmap.createBitmap(tmpBitmap, mDrawableFloat.left,  
                mDrawableFloat.top, mDrawableFloat.width(),  
                mDrawableFloat.height(), matrix, true);  
        tmpBitmap.recycle();  
        tmpBitmap = null;  
        
        Bitmap bm;
        //压缩图片
        if(ret.getWidth()>ret.getHeight()){
        	bm=BitmapUtils.getCompressBitmapByBitmap(ret, Constant_data.PHOTO_WIDTH, Constant_data.PHOTO_HEIGHT, Constant_data.PHOTO_SIZE);
        }else{
        	bm=BitmapUtils.getCompressBitmapByBitmap(ret, Constant_data.PHOTO_HEIGHT, Constant_data.PHOTO_WIDTH, Constant_data.PHOTO_SIZE);
        }
        ret.recycle();
        ret=null;
        
        //保存图片
        /*File f=mContext.getExternalCacheDir();
		if(!f.exists())f.mkdirs();
		BitmapUtils.saveBitmap(ret, new File(f,"shiming.jpg").getAbsolutePath());
  */
        Log.i("裁剪图片大小", "width="+bm.getWidth()+" height="+bm.getHeight()+" size="+bm.getRowBytes()*bm.getHeight()/1024);
        return bm;  
    }  
    
    /**
     * 拍照并返回照片
     * @param callback 返回接口
     */
    public void takePicture(final TakePictureCallback callback){
    	if(camera==null)return;
    	//拍照时屏幕方向
    	final int orientation=currentOrientation;
    	camera.takePicture(null , null, new PictureCallback() {
			
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				// TODO Auto-generated method stub
				//压缩原始图片
				Options opts = new Options();
				opts.inJustDecodeBounds=true;
				BitmapFactory.decodeByteArray(data, 0, data.length, opts);
				int scaleWidth=opts.outHeight/mDrawableSrc.width();
				int scaleHeight=opts.outWidth/mDrawableSrc.height();
				//int scaleWidth=opts.outWidth/mDrawableSrc.width();
				//int scaleHeight=opts.outHeight/mDrawableSrc.height();
				Log.i("原始图片尺寸", "wid="+opts.outWidth+" hei="+opts.outHeight);
				if(scaleHeight > 1 || scaleWidth > 1){
					opts.inSampleSize=scaleHeight>scaleWidth?scaleHeight:scaleWidth;
				}
				//opts.inSampleSize=2;
				opts.inJustDecodeBounds=false;
				Bitmap tmpBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
				
				//Bitmap tmpBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				
				//处理图片选转
				Bitmap bmR=BitmapUtils.rotaingImageView(90, tmpBitmap);
				tmpBitmap.recycle();
				tmpBitmap=null;
				Log.i("拍摄照片原大小", "width="+bmR.getWidth()+" height="+bmR.getHeight()+" size="+bmR.getRowBytes()*bmR.getHeight()/1024);
				
				Bitmap bmS = Bitmap.createScaledBitmap(bmR, mDrawableSrc.width(), mDrawableSrc.height(), false);
				bmR.recycle();
				bmR=null;
				Log.i("照片伸缩后大小", "width="+bmS.getWidth()+" height="+bmS.getHeight()+" size="+bmS.getRowBytes()*bmS.getHeight()/1024);
				//Log.i("tmpbitmap", "width="+tmpBitmap.getWidth()+" height="+tmpBitmap.getHeight()+" s w="+scaleWidth+" s h="+scaleHeight);
				Log.i("裁剪大小",mDrawableSrc.toString()+ mDrawableFloat.toString());
				
				//裁剪图片
				Bitmap ret =Bitmap.createBitmap(bmS,mDrawableFloat.left,mDrawableFloat.top,
		    			mDrawableFloat.width(),mDrawableFloat.height(),null,true);
				bmS.recycle();
				bmS=null;
				
				//压缩照片并处理拍照时旋转角度
				Bitmap bm =BitmapUtils.getCompressBitmapByBitmap(ret, Constant_data.PHOTO_HEIGHT, Constant_data.PHOTO_WIDTH, Constant_data.PHOTO_SIZE);
				ret.recycle();
				ret=null;
				bm=BitmapUtils.rotaingImageView(orientation, bm);
				
				
				//保存图片
				/*File f=mContext.getExternalCacheDir();
				if(!f.exists())f.mkdirs();
				BitmapUtils.saveBitmap(ret, new File(f,"shiming.jpg").getAbsolutePath());
				*/
		        
		        Log.i("拍摄照片大小", "width="+bm.getWidth()+" height="+bm.getHeight()+" size="+bm.getRowBytes()*bm.getHeight()/1024);
		        if(callback!=null)callback.getPictrue(bm);
		        System.gc();
			}
			
		});
    }
    
   
   
    /**
     * 拍照返回bitmap接口
     *
     */
    public interface TakePictureCallback{
    	void getPictrue(Bitmap bitmap);
    }
  
    
    /**
     * 设置是否可以改变图片裁剪大小
     * @param change 是否可以改变裁剪大小
     */
    public void setChangeAndMove(boolean change,boolean move){
    	this.canChangeSize=change;
    	this.canMove=move;
    }
    
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
    	// TODO Auto-generated method stub
    	super.onWindowVisibilityChanged(visibility);
    	if(iOrientationEventListener==null)return;
    	if(visibility==View.VISIBLE){
    		iOrientationEventListener.enable();
    	}else{
    		iOrientationEventListener.disable();;
    	}
    }
    
    private class IOrientationEventListener extends OrientationEventListener{

		public IOrientationEventListener(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onOrientationChanged(int orientation) {
			// TODO Auto-generated method stub
			//Log.i("屏幕方向", ""+orientation);
			if(orientation == IOrientationEventListener.ORIENTATION_UNKNOWN)return;
			if(orientation<=45 || orientation>315){
				currentOrientation=DEGREE_0;
			}else if(orientation>45 && orientation<=135){
				currentOrientation=DEGREE_90;
			}else if(orientation>135 && orientation<=225){
				currentOrientation=DEGREE_180;
			}else if(orientation>225 && orientation<=315){
				currentOrientation=DEGREE_270;
			}
		}
    	
    }
    
    /**
     * 设置是否画mFloatDrawable
     * @param draw
     */
    public void setFloatDrawableDraw(boolean draw){
    	mFloatDrawable.setDrawOrNot(draw);
    }
    
    
    /**
     * 手动对焦模块
     */
    private void submitFocusAreaRect(final Rect touchRect) {
        Camera.Parameters cameraParameters = camera.getParameters();

        if (cameraParameters.getMaxNumFocusAreas() == 0) {
                return;
        }
        // Convert from View's width and height to +/- 1000
        Rect focusArea = new Rect();
        focusArea.set(touchRect.left * 2000 / getWidth() - 1000,
                        touchRect.top * 2000 / getHeight() - 1000,
                        touchRect.right * 2000 / getWidth() - 1000,
                        touchRect.bottom * 2000 / getHeight() - 1000);
        // Submit focus area to camera
        ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
        focusAreas.add(new Camera.Area(focusArea, 1000));
        cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        cameraParameters.setFocusAreas(focusAreas);
        try {
			camera.setParameters(cameraParameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Start the autofocus operation
        camera.autoFocus(new AutoFocusCallback() {
			
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				try {
					Parameters parameters=camera.getParameters();
					List<String> focuseMode = parameters.getSupportedFocusModes();
					if(focuseMode.contains(parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
						parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦  
						camera.setParameters(parameters);
						camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上  
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
    }
    
    /**  
	 *  获取最大缩放级别，最大为40
	 *  @return   
	 */
	public int getMaxZoom(){
		if(camera==null) return -1;	
		try {
			Camera.Parameters parameters=camera.getParameters();
			if(!parameters.isZoomSupported()) return -1;
			return parameters.getMaxZoom()>40?40:parameters.getMaxZoom();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}
	/**  
	 *  设置相机缩放级别
	 *  @param zoom   
	 */
	public void setZoom(int zoom){
		if(camera==null) return;
		//注意此处为录像模式下的setZoom方式。在Camera.unlock之后，调用getParameters方法会引起android框架底层的异常
		//stackoverflow上看到的解释是由于多线程同时访问Camera导致的冲突，所以在此使用录像前保存的mParameters。
		if(zoom>getMaxZoom()) zoom=getMaxZoom();
		if(zoom<0) zoom=0;
		try {
			Parameters parameters=camera.getParameters();
			if(!parameters.isZoomSupported()) return;
			parameters.setZoom(zoom);
			camera.setParameters(parameters);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/**
	 * 返回当前相机缩放级别
	 * @return
	 */
	public int getZoom(){
		if(camera==null) return -1;
		try {
			Parameters parameters=camera.getParameters();
			return parameters.getZoom();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}
    
}  
