package com.nennig.photomem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import com.nennig.photomem.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.provider.MediaStore.Files;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FileManagement {
	private static final String TAG = "FileManagement";
	private static String[] acceptedExtensions = {".jpg",".png"};
	private Activity _activity;
	private String memFolder;
	private String memFolderPath;
	
	public FileManagement(Activity a, String folder){
		memFolder = folder;
		_activity = a;
		memFolderPath = Mem.ROOT_FOLDER + "/" + _activity.getString(R.string.app_name) + 
    			"/" + memFolder;
		Log.d(TAG, "Constructor: " + memFolderPath);
	}

    public void renamePhoto(String newName, String prevName){
    	Log.d(TAG, "Current image path: " + memFolderPath + "/" + prevName);
    	Log.d(TAG, "New image path: " + memFolderPath + "/" + newName);
    	File file = new File(memFolderPath + "/" + prevName);
    	if(file.exists())
    	{
    		//Simple for loop to run through all of the accepted extensions for the new name
    		for(int i = 0; i<acceptedExtensions.length; i++)
    		{
	    		if(prevName.contains(acceptedExtensions[i]))
	    		{
		    		if(newName.contains(acceptedExtensions[i])){
		    			file.renameTo(new File(memFolderPath + "/" + newName));
		        		Log.d(TAG, "Changed to: " + memFolderPath + "/" + newName);
		    		}
		    		else
		    		{
		    			file.renameTo(new File(memFolderPath + "/" + newName + acceptedExtensions[i]));
		        		Log.d(TAG, "Changed to: " + memFolderPath + "/" + newName + acceptedExtensions[i]);
		    		}
	    		}
    		}
    	}
    	else
    	{
    		Log.e(TAG, memFolderPath + "/" + prevName + 
    				" does not exist!");
    	}
	}
    
    public void copyPhoto(String origPath){
    	try{
    		File f1 = new File(origPath);
    		String [] splits = origPath.split("/");
    		String name = splits[splits.length-1];
    		
    		File f2 = new File(memFolderPath + "/" + name);
    		InputStream in = new FileInputStream(f1);

    		//For Overwrite the file.
    		OutputStream out = new FileOutputStream(f2);

    		byte[] buf = new byte[1024];
    		int len;
    		while ((len = in.read(buf)) > 0){
    			out.write(buf, 0, len);
    		}
    		in.close();
    		out.close();
    		Log.d(TAG, name + " File copied.");
    	}
		catch(FileNotFoundException ex){
		  Log.e(TAG, ex.getMessage() + " in the specified directory.");
		}
    	catch(IOException e){
    		Log.e(TAG, e.getMessage());  
    	}	
    		  
    }
    
    public void deletePhoto(String name){
    	File f = new File(memFolderPath + "/" + name);
    	if(f.exists()){
    		f.delete();
    	}
    }
    
    public boolean hasMemPhotos(){
    	File f = new File(memFolderPath);
    	String[] list = f.list(new FilenameFilter(){  
            @Override  
            public boolean accept(File dir, String name)  
            {  
            	for(int i = 0;i<acceptedExtensions.length;i++)
            	{
            		if(name.endsWith(acceptedExtensions[i]))
            			return true;  
            	}
            	return false;
            }  
        });
    	Log.d(TAG, "list len: " + list.length);
    	if(list.length > 0)
    		return true;
    	return false;
    }
    
    public String[] getMemPhotos(){
    	File folder = new File(memFolderPath);
    	return folder.list(new FilenameFilter(){  
            @Override  
            public boolean accept(File dir, String name)  
            {  
            	for(int i = 0;i<acceptedExtensions.length;i++)
            	{
            		if(name.endsWith(acceptedExtensions[i]))
            			return true;  
            	}
            	return false;  
            }  
        });
    }
    
    public String[] getShuffledMemPhotos(){
    	String[] photos = getMemPhotos();
    	shuffleArray(photos);
    	return photos;
    }
    
    public static int calculateInSampleSize(BitmapFactory.Options o, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = o.outHeight;
	    final int width = o.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	        if (width > height) {
	            inSampleSize = Math.round((float)height / (float)reqHeight);
	        } else {
	            inSampleSize = Math.round((float)width / (float)reqWidth);
	        }
	    }
	    return inSampleSize;
    }
    
    public Bitmap drawNextPhoto(String name, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(memFolderPath + "/" + name, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(memFolderPath + "/" + name, options);
    }
    
    public static String getPhotoName(String name){
    	
    	for(int i = 0; i<acceptedExtensions.length;i++){
    		if(name.contains(acceptedExtensions[i]))
    		{
    			return name.substring(0, name.length()-4);
    		}
    	}   	
    	return name;
    }
    
    /**
     * This is a simple method to randomize the array
     * @param arr - Array to be randomized
     */
    private static void shuffleArray(String[] arr) {
        int n = arr.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
          int change = i + random.nextInt(n - i);
          swap(arr, i, change);
        }
      }

    /**
     * Simple swapping method
     * @param arr - Array being randomized
     * @param i - position of one element
     * @param change - position of second element
     */
      private static void swap(String[] a, int i, int change) {
        String helper = a[i];
        a[i] = a[change];
        a[change] = helper;
      }
}
