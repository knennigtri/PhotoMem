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
    private CustomAlerts cAlerts;
    private FileManagement fManagement;
    private Bitmap bitmapImage;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
		//TODO Put up a counter on the screen 
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_viewer);
        cAlerts = new CustomAlerts(this, this.getIntent().getExtras().getString("memFolder"));
        fManagement = new FileManagement(this, this.getIntent().getExtras().getString("memFolder"));
        boolean randomize = this.getIntent().getExtras().getBoolean("memRandomize");
        
        if(randomize) 
        	photoPaths = fManagement.getShuffledMemPhotos();
        else
        	photoPaths = fManagement.getMemPhotos();
        
        Log.v(TAG, "#" + photoPaths.length);
        
        nextPhoto();
        
        final Button memorizedButton = (Button) findViewById(R.id.viewer_memorizedButton);
        memorizedButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				memorizedCount++;
				TextView memorized = (TextView) findViewById(R.id.viewer_memorized);
				memorized.setText("" + memorizedCount);
				
				Log.v(TAG,"Photo Memorized. PhotoIndex=" + photoIndex);
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
				
				Log.v(TAG,"Photo Not Memorized PhotoIndex=" + photoIndex);
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
    	Log.v(TAG, "Total Photos Iterated: " + photoIndex);
		Log.v(TAG, "Total correct: " + memorizedCount);
		Log.v(TAG, "Total wrong: " + wrongCount);
		
		
		TextView tv = (TextView) findViewById(R.id.viewer_photoName);
		tv.setText("Final Score: Memorized: " + memorizedCount + " Wrong: " + wrongCount);

		
		Bundle extras = new Bundle();
    	extras.putString("memFolder", this.getIntent().getExtras().getString("memFolder"));
    	extras.putBoolean("memRandomize", this.getIntent().getExtras().getBoolean("memRandomize"));
    	
    	DecimalFormat twoDec = new DecimalFormat("#.##");
    	String[] stats = {String.valueOf(memorizedCount),
    			String.valueOf(wrongCount),
    			String.valueOf(twoDec.format(((double) memorizedCount/photoPaths.length)*100))};
    	
    	extras.putStringArray("memStats", stats);
    	Intent intent = new Intent(ViewerActivity.this,MemScoreActivity.class);
    	intent.putExtras(extras);          
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
    		extras = new Bundle();
        	extras.putString("memFolder", this.getIntent().getExtras().getString("memFolder"));
        	extras.putBoolean("memRandomize", this.getIntent().getExtras().getBoolean("memRandomize"));
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
