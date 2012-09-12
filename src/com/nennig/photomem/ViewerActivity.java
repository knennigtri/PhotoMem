package com.nennig.photomem;

import java.text.DecimalFormat;
import java.util.Random;

import com.nennig.photomem.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

public class ViewerActivity extends Activity {

	private static final String TAG = "ViewerActivity";
	private int photoIndex = 0;
	private String[] photoPaths;
	private int memorizedCount = 0;
	private int wrongCount = 0;
    private FileManagement fManagement;
    private Bitmap bitmapImage;
    private  boolean _randomize;
    private String _current_mem;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_viewer);

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
        	_current_mem =getIntent().getStringExtra(Mem.CURRENT_MEM);
        	_randomize = getIntent().getExtras().getBoolean(Mem.RANDOMIZE);
	        fManagement = new FileManagement(this, getIntent().getExtras().getString(Mem.CURRENT_MEM));
	        
	        if(_randomize) 
	        	photoPaths = fManagement.getShuffledMemPhotos();
	        else
	        	photoPaths = fManagement.getMemPhotos();
        }
        
        Log.d(TAG, "#" + photoPaths.length);
        
        nextPhoto();
        
        final Button memorizedButton = (Button) findViewById(R.id.viewer_memorizedButton);
        memorizedButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				memorizedCount++;
				TextView memorized = (TextView) findViewById(R.id.viewer_memorized);
				memorized.setText("" + memorizedCount);
				
				Log.d(TAG,"Photo Memorized. PhotoIndex=" + photoIndex);
				nextPhoto();
			}
        	
        });
        
        final Button notMemorizedButton = (Button) findViewById(R.id.viewer_notMemorizedButton);
        notMemorizedButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				wrongCount++;
				TextView wrong = (TextView) findViewById(R.id.viewer_wrong);
				wrong.setText("" + wrongCount);
				
				Log.d(TAG,"Photo Not Memorized PhotoIndex=" + photoIndex);
				nextPhoto();
			}
        	
        });
    }
    
	private void nextPhoto(){
		if(photoIndex < photoPaths.length)
		{
			TextView counter = (TextView) findViewById(R.id.viewer_picture_counter);
			counter.setText((photoIndex + 1) + "/" + photoPaths.length);
			
			LinearLayout ll = (LinearLayout) findViewById(R.id.viewer_controlsFrame);
			ll.setVisibility(4);
			ImageView photoView = (ImageView) findViewById(R.id.viewer_imageView);
		        
			bitmapImage = fManagement.drawNextPhoto(photoPaths[photoIndex], 500, 500);	
			photoView.setImageBitmap(bitmapImage);
			photoView.setOnTouchListener(new OnTouchListener() {
	 			@Override
	 			public boolean onTouch(View arg0, MotionEvent arg1) {
	 				TextView tv = (TextView) findViewById(R.id.viewer_photoName);
	 				tv.setText(FileManagement.getPhotoName(photoPaths[photoIndex-1]));
	 				LinearLayout ll = (LinearLayout) findViewById(R.id.viewer_controlsFrame);
	 				ll.setVisibility(0);
	 				return false;
	 			}
	         	
	         });
			photoIndex++;
		}
		else
		{
			photoMemoryEnd();
		}
	}
	
    private void photoMemoryEnd(){
    	Log.d(TAG, "Total Photos Iterated: " + photoIndex);
		Log.d(TAG, "Total correct: " + memorizedCount);
		Log.d(TAG, "Total wrong: " + wrongCount);
		
		
		TextView tv = (TextView) findViewById(R.id.viewer_photoName);
		tv.setText("Final Score: Memorized: " + memorizedCount + " Wrong: " + wrongCount);
    	
    	DecimalFormat twoDec = new DecimalFormat("#.##");
    	String[] stats = {String.valueOf(memorizedCount),
    			String.valueOf(wrongCount),
    			String.valueOf(twoDec.format(((double) memorizedCount/photoPaths.length)*100))};
    	
    	Intent intent = new Intent(ViewerActivity.this,MemScoreActivity.class);
    	intent.putExtra(Mem.CURRENT_MEM, _current_mem);
    	intent.putExtra(Mem.RANDOMIZE, _randomize);  
    	intent.putExtra(Mem.MEM_STATS, stats);
    	startActivity(intent);
		finish();
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
        	intent = new Intent(ViewerActivity.this,ViewerActivity.class);
        	intent.putExtra(Mem.CURRENT_MEM, _current_mem);
        	intent.putExtra(Mem.RANDOMIZE, _randomize);     
        	startActivity(intent);
        	Toast.makeText(this, "Mem Restarted. Good Luck!", Toast.LENGTH_LONG).show();
    		finish();
    		return true;
    	case R.id.menu_memory_menu:
        	intent = new Intent(ViewerActivity.this,StartMemoryActivity.class);
        	intent.putExtra(Mem.CURRENT_MEM, _current_mem);
        	intent.putExtra(Mem.RANDOMIZE, _randomize);          
        	startActivity(intent);
    		finish();
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
