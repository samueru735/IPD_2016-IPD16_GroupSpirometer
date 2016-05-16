package com.group4.ipd16.spirometer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.jjoe64.graphview.GraphView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bob on 28/04/16.
 */
public class ImageHandler {
//    public static String PACKAGE_NAME;
    private String TAG;
    public ImageHandler() {
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,"Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + BaseActivity.PACKAGE_NAME
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="Results_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public void saveViewtoStorage(GraphView view) {
        //view.getRootView();
        view.setDrawingCacheEnabled(true);

        /*view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
*/
        view.buildDrawingCache(true);
       // getViewDimensions(view);

        //Bitmap returnedBitmap = Bitmap.createBitmap(1024/*viewWidth*/, 1980/*viewHeight*/, Bitmap.Config.ARGB_8888);
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getDrawingCache());
        //Bitmap returnedBitmap = Bitmap.createBitmap(viewWidth,viewHeight,Bitmap.Config.ARGB_8888);

        view.setDrawingCacheEnabled(false); // clear drawing cache


        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        storeImage(returnedBitmap);
    }

    public int viewWidth;
    public int viewHeight;
    public void getViewDimensions(final GraphView view){
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = view.getWidth();
                    viewHeight = view.getHeight();
                }
            });
        }
    }
}
