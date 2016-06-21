package it.dantar.barcodehunt.app;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

public class Utils {

	public static Bitmap loadBitmap(String path) {
		File imgFile = new  File(path);
		if(imgFile.exists()){
			Log.i("BarcodeHunt", String.format("Image %s was found", path));
			return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}
		Log.w("Utils", String.format("Image %s was *not* found", path));
		return null;
	}
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    Log.i("BarcodeHunt", String.format("SD state: %s", state));
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    	    Log.i("BarcodeHunt", "Readable ok!");
    	    File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			Log.i("BarcodeHunt", sd.getAbsolutePath());
			if(sd.isDirectory()) {
				for (String filename : sd.list()) {
					Log.i("BarcodeHunt", "...contains " + filename);
				}
			}
			Log.i("BarcodeHunt", sd.getAbsolutePath());
	        return true;
	    }
	    Log.i("BarcodeHunt", "Not readable...");
	    return false;
	}
	
	public static int toPixels(int howmany, DisplayMetrics metrics) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, howmany, metrics);
	}
	
}
