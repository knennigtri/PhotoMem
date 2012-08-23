package com.example.photomemory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewerActivity extends Activity {
	private static final String TAG = "ViewerActivity";
	private int photoIndex = 0;
	private int correctCount = 0;
	private int wrongCount = 0;
    private boolean randomize = true; 
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        
        Bundle bundle = this.getIntent().getExtras();
        final String selectedFolder = bundle.getString("dbPath");
        final String mode = bundle.getString("mode");

        final String memPath = Environment.getExternalStorageDirectory().toString() + "/" + selectedFolder;
        
        File folder = new File(memPath);
        
        final String[] photoPaths = folder.list(new FilenameFilter(){  
            @Override  
            public boolean accept(File dir, String name)  
            {  
                return ((name.endsWith(".jpg"))||(name.endsWith(".png")));  
            }  
        }); 
        
        if(randomize) shuffleArray(photoPaths);
       
        for(int i = 0; i<photoPaths.length; i++)
        	Log.v(TAG, i + " - " + photoPaths[i]);
        
        nextPhoto(memPath, photoPaths[photoIndex]);
        photoIndex++;
        
        final Button memorizedButton = (Button) findViewById(R.id.viewer_memorizedButton);
        memorizedButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(photoIndex < photoPaths.length)
				{
					LinearLayout ll = (LinearLayout) findViewById(R.id.viewer_controlsFrame);
	 				ll.setVisibility(4);
					nextPhoto(memPath, photoPaths[photoIndex]);
					correctCount++;
					Log.v(TAG,"Photo Memorized. PhotoIndex=" + photoIndex);
					photoIndex++;
				}
				else
				{
					photoMemoryEnd();
				}
			}
        	
        });
        
        final Button notMemorizedButton = (Button) findViewById(R.id.viewer_notMemorizedButton);
        notMemorizedButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(photoIndex < photoPaths.length-1)
				{
					LinearLayout ll = (LinearLayout) findViewById(R.id.viewer_controlsFrame);
	 				ll.setVisibility(4);
					nextPhoto(memPath, photoPaths[photoIndex++]);		
					wrongCount++;
					Log.v(TAG,"Photo Not Memorized PhotoIndex=" + photoIndex);
				}
				else
				{
					photoMemoryEnd();
				}
			}
        	
        });
    }
    
    private void photoMemoryEnd(){
    	Log.v(TAG, "Total Photos Iterated: " + photoIndex);
		Log.v(TAG, "Total correct: " + correctCount);
		Log.v(TAG, "Total wrong: " + wrongCount);
		TextView tv = (TextView) findViewById(R.id.viewer_photoName);
		tv.setText("Final Score: Memorized: " + correctCount + " Wrong: " + wrongCount);
    }
    
    private void nextPhoto(String memPath, String name){
    	final String fullName = name;
    	Bitmap bitmapImage = BitmapFactory.decodeFile(memPath + "/" + fullName); 
        ImageView myImageView = (ImageView)findViewById(R.id.viewer_imageView);
         myImageView.setImageBitmap(bitmapImage);
         myImageView.setOnTouchListener(new OnTouchListener() {
 			@Override
 			public boolean onTouch(View arg0, MotionEvent arg1) {
 				TextView tv = (TextView) findViewById(R.id.viewer_photoName);
 				tv.setText(fullName.substring(0, fullName.length()-4));
 				LinearLayout ll = (LinearLayout) findViewById(R.id.viewer_controlsFrame);
 				ll.setVisibility(0);
 				return false;
 			}
         	
         });
    }

    /**
     * This is a simple method to randomize the array
     * @param arr - Array to be randomized
     */
    public static void shuffleArray(String[] arr) {
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_viewer, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_start_over:
    		startActivity(new Intent(ViewerActivity.this, ViewerActivity.class));
    		finish();
    		return true;
    	case R.id.menu_memory_menu:
    		startActivity(new Intent(ViewerActivity.this, StartMemoryActivity.class));
    		finish();
    		return true;
    	case R.id.menu_settings:
    		//startActivity(new Intent(ViewerActivity.this, Settings.class));
    		finish();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
