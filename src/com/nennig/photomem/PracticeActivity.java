package com.nennig.photomem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;

import com.nennig.photomem.R;

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
        private String[] photoPaths;
        private CustomAlerts cAlerts;
        private FileManagement fManagement;
        private Bitmap bitmapImage;
        private  boolean _randomize;
        private String _current_mem;
        
    	@Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_practice);
            cAlerts = new CustomAlerts(this);
            if(savedInstanceState !=null)
            {
            	_current_mem = savedInstanceState.getString(Mem.CURRENT_MEM);
            	_randomize = savedInstanceState.getBoolean(Mem.RANDOMIZE);
                fManagement = new FileManagement(this, _current_mem);
                photoIndex = savedInstanceState.getInt("photoIndex");
                photoPaths = savedInstanceState.getStringArray("photoPaths");
            }
            else
            {
            	_current_mem = getIntent().getExtras().getString(Mem.CURRENT_MEM);
                fManagement = new FileManagement(this, _current_mem);
                _randomize = this.getIntent().getExtras().getBoolean(Mem.RANDOMIZE);
                
                if(_randomize)  
                	photoPaths = fManagement.getShuffledMemPhotos();
                else
                	photoPaths = fManagement.getMemPhotos();
            }
            
            
            Log.d(TAG, "#" + photoPaths.length);
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
				Log.d(TAG, "index after next iteration: "+photoIndex);
				LinearLayout ll = (LinearLayout) findViewById(R.id.practice_controlsFrame);
 				ll.setVisibility(0);
 				
 				TextView tv = (TextView) findViewById(R.id.practice_photoName);
 				tv.setText(FileManagement.getPhotoName(photoPaths[photoIndex]));
 					
 				ImageView photoView = (ImageView) findViewById(R.id.practice_imageView);
 		        
 				bitmapImage = fManagement.drawNextPhoto(photoPaths[photoIndex], 500, 500);	
 				photoView.setImageBitmap(bitmapImage);
 				
 				Log.d(TAG,"PhotoIndex=" + photoIndex);
			}
			else
			{
				finish();
			}
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
        		extras.putString(Mem.CURRENT_MEM, _current_mem);
        		extras.putBoolean(Mem.RANDOMIZE, _randomize);
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
        
        @Override
        public void onSaveInstanceState(Bundle b){
        	b.putString(Mem.CURRENT_MEM, this.getIntent().getExtras().getString(Mem.CURRENT_MEM));
        	b.putBoolean(Mem.RANDOMIZE, this.getIntent().getExtras().getBoolean(Mem.RANDOMIZE));
        	b.putStringArray(Mem.MEM_STATS, getIntent().getStringArrayExtra(Mem.MEM_STATS));
        	b.putInt("photoIndex", photoIndex);
        	b.putStringArray("photoPaths", photoPaths);
        	super.onSaveInstanceState(b);
        }
    }
