package com.example.photomemory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	public static String ROOTFOLDER = Environment.getExternalStorageDirectory().toString();
	public String memFolder;
	public String memFolderPath;
	
	public FileManagement(Activity a, String folder){
		Log.e(TAG, "FileManagement Constructor");
		memFolder = folder;
		_activity = a;
		memFolderPath = ROOTFOLDER + "/" + _activity.getString(R.string.app_name) + 
    			"/" + memFolder;
		Log.v(TAG, "Constructor: " + memFolderPath);
	}

    public void renamePhoto(String newName, String prevName){
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
		        		Log.v(TAG, "Changed to: " + memFolderPath + "/" + newName);
		    		}
		    		else
		    		{
		    			file.renameTo(new File(memFolderPath + "/" + newName + acceptedExtensions[i]));
		        		Log.v(TAG, "Changed to: " + memFolderPath + "/" + newName + acceptedExtensions[i]);
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
    		Log.v(TAG, name + " File copied.");
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
    
    public String[] getMemPhotos(){
    	File folder = new File(memFolderPath);
    	return folder.list(new FilenameFilter(){  
            @Override  
            public boolean accept(File dir, String name)  
            {  
                return ((name.endsWith(".jpg")));//||(name.endsWith(".png")));  
            }  
        });
    }
    
    public void drawNextPhoto(String name){
    	final String fullName = name;
    	Bitmap bitmapImage = BitmapFactory.decodeFile(memFolderPath + "/" + fullName); 
        ImageView myImageView = (ImageView)_activity.findViewById(R.id.practice_imageView);
         myImageView.setImageBitmap(bitmapImage);
    }
    
    public void nextPhotoWithOnTouchListener(String name){
    	final String fullName = name;
    	Bitmap bitmapImage = BitmapFactory.decodeFile(memFolderPath + "/" + fullName); 
        ImageView myImageView = (ImageView)_activity.findViewById(R.id.viewer_imageView);
         myImageView.setImageBitmap(bitmapImage);
         myImageView.setOnTouchListener(new OnTouchListener() {
 			@Override
 			public boolean onTouch(View arg0, MotionEvent arg1) {
 				TextView tv = (TextView) _activity.findViewById(R.id.viewer_photoName);
 				tv.setText(fullName.substring(0, fullName.length()-4));
 				LinearLayout ll = (LinearLayout) _activity.findViewById(R.id.viewer_controlsFrame);
 				ll.setVisibility(0);
 				return false;
 			}
         	
         });
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
}
