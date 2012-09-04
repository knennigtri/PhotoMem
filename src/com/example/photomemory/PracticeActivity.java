package com.example.photomemory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PracticeActivity extends Activity {
    	private static final String TAG = "PracticeActivity";
    	private int photoIndex = 0;
        private boolean randomize = true; 
        private String[] photoPaths;
        private CustomAlerts cAlerts;
        private FileManagement fManagement;
        
    	@Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_practice);

            cAlerts = new CustomAlerts(this, this.getIntent().getExtras().getString("memFolder"));
            fManagement = new FileManagement(this, this.getIntent().getExtras().getString("memFolder"));
            
            photoPaths = fManagement.getMemPhotos(); 
            
            Log.v(TAG, "#" + photoPaths.length);
            
            if(randomize) shuffleArray(photoPaths);
            nextPhoto();
                        
            final Button nextButton = (Button) findViewById(R.id.practice_nextButton);
            nextButton.setOnClickListener(new Button.OnClickListener() {
    			@Override
    			public void onClick(View arg0) {
    				photoIndex++;
    				nextPhoto();
    			}
            	
            });
        }
        
    	private void nextPhoto(){
    		if(photoIndex < photoPaths.length)
			{
				//TODO Fix Bitmap Problem  "Displaying Bitmaps Efficiently"
				Log.v(TAG, "index after next iteration: "+photoIndex);
				LinearLayout ll = (LinearLayout) findViewById(R.id.practice_controlsFrame);
 				ll.setVisibility(0);
 				TextView tv = (TextView) findViewById(R.id.practice_photoName);
 				tv.setText(FileManagement.getPhotoName(photoPaths[photoIndex]));
 				fManagement.drawNextPhoto(photoPaths[photoIndex]);		
 				Log.v(TAG,"PhotoIndex=" + photoIndex);
			}
			else
			{
				finish();
			}
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
            getMenuInflater().inflate(R.menu.activity_practice, menu);
            return true;
        }
        
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
        	Bundle extras;
        	Intent intent;
        	switch(item.getItemId()){
        	case R.id.menu_memory_menu:
        		extras = new Bundle();
        		extras.putString("memFolder", this.getIntent().getExtras().getString("memFolder"));
        		intent = new Intent(PracticeActivity.this, StartMemoryActivity.class);
        		intent.putExtras(extras);
        		startActivity(intent);
        		finish();
        		return true;
        	case R.id.menu_edit: 
        		cAlerts.editPhoto(this, fManagement, photoPaths[photoIndex]);
        		return true;
        	case R.id.menu_rotate_left:
        		//TODO Create rotate Left
        		return true;
        	case R.id.menu_rotate_right:
        		//TODO Create rotate Right
        		return true;
        	case R.id.menu_delete:
        		//TODO Picture changes before deletion
        		cAlerts.deletePhoto(this, fManagement,photoPaths[photoIndex]);
        		photoIndex++;
        		nextPhoto();
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        	}
        }
    }
