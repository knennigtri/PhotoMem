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
import android.widget.Toast;

public class ViewerActivity extends Activity {
	private static final String TAG = "ViewerActivity";
	private int photoIndex = 0;
	private int correctCount = 0;
	private int wrongCount = 0;
    private boolean randomize = true; 
    private CustomAlerts cAlerts;
    private FileManagement fManagement;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        
        cAlerts = new CustomAlerts(this, this.getIntent().getExtras().getString("memFolder"));
        fManagement = new FileManagement(this, this.getIntent().getExtras().getString("memFolder"));
        
        final String[] photoPaths = fManagement.getMemPhotos();
        
        if(randomize) shuffleArray(photoPaths);
       
        for(int i = 0; i<photoPaths.length; i++)
        	Log.v(TAG, i + " - " + photoPaths[i]);
        
        fManagement.nextPhotoWithOnTouchListener(photoPaths[photoIndex]);
        photoIndex++;
        
        final Button memorizedButton = (Button) findViewById(R.id.viewer_memorizedButton);
        memorizedButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				correctCount++;
				if(photoIndex < photoPaths.length)
				{
					//TODO Fix Bitmap Problem  "Displaying Bitmaps Efficiently"
					LinearLayout ll = (LinearLayout) findViewById(R.id.viewer_controlsFrame);
	 				ll.setVisibility(4);
					fManagement.nextPhotoWithOnTouchListener(photoPaths[photoIndex]);
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
				wrongCount++;
				if(photoIndex < photoPaths.length)
				{
					//TODO Fix Bitmap Problem  "Displaying Bitmaps Efficiently"
					LinearLayout ll = (LinearLayout) findViewById(R.id.viewer_controlsFrame);
	 				ll.setVisibility(4);
					fManagement.nextPhotoWithOnTouchListener(photoPaths[photoIndex]);		
					Log.v(TAG,"Photo Not Memorized PhotoIndex=" + photoIndex);
					photoIndex++;
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
    	Bundle extras;
    	Intent intent;
    	switch(item.getItemId()){
    	case R.id.menu_start_over:
    		extras = new Bundle();
        	extras.putString("memFolder", this.getIntent().getExtras().getString("memFolder"));
        	intent = new Intent(ViewerActivity.this,ViewerActivity.class);
        	intent.putExtras(extras);          
        	startActivity(intent);
        	Toast.makeText(this, "Mem Restarted. Good Luck!", Toast.LENGTH_LONG).show();
    		finish();
    		return true;
    	case R.id.menu_memory_menu:
    		extras = new Bundle();
        	extras.putString("memFolder", this.getIntent().getExtras().getString("memFolder"));
        	intent = new Intent(ViewerActivity.this,StartMemoryActivity.class);
        	intent.putExtras(extras);          
        	startActivity(intent);
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
