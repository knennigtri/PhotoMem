package com.example.photomemory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PracticeActivity extends Activity {
    	private static final String TAG = "PracticeActivity";
    	private int photoIndex = 0;
        private boolean randomize = true; 
        
    	@Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_practice);
            
            Bundle bundle = this.getIntent().getExtras();
            final String selectedFolder = bundle.getString("dbPath");

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
            
            TextView tv = (TextView) findViewById(R.id.practice_photoName);
			tv.setText(photoPaths[photoIndex].substring(0, photoPaths[photoIndex].length()-4));
            nextPhoto(memPath, photoPaths[photoIndex]);
            photoIndex++;
                        
            final Button nextButton = (Button) findViewById(R.id.practice_nextButton);
            nextButton.setOnClickListener(new Button.OnClickListener() {
    			@Override
    			public void onClick(View arg0) {
    				if(photoIndex < photoPaths.length-1)
    				{
    					LinearLayout ll = (LinearLayout) findViewById(R.id.practice_controlsFrame);
    	 				ll.setVisibility(0);
    	 				TextView tv = (TextView) findViewById(R.id.practice_photoName);
         				tv.setText(photoPaths[photoIndex].substring(0, photoPaths[photoIndex].length()-4));
         				nextPhoto(memPath, photoPaths[photoIndex]);		
    					Log.v(TAG,"Photo Not Memorized PhotoIndex=" + photoIndex);
    					photoIndex++;
    				}
    				else
    				{
    					finish();
    				}
    			}
            	
            });
        }
        
        private void nextPhoto(String memPath, String name){
        	final String fullName = name;
        	Bitmap bitmapImage = BitmapFactory.decodeFile(memPath + "/" + fullName); 
            ImageView myImageView = (ImageView)findViewById(R.id.practice_imageView);
             myImageView.setImageBitmap(bitmapImage);
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
        	switch(item.getItemId()){
        	case R.id.menu_memory_menu:
        		startActivity(new Intent(PracticeActivity.this, StartMemoryActivity.class));
        		finish();
        		return true;
        	case R.id.menu_edit: //TODO Create Preview Edit
        		//Edit the current file
        	case R.id.menu_settings: //TODO Settings page
        		//startActivity(new Intent(PracticeActivity.this, Settings.class));
        		finish();
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        	}
        }
    }
