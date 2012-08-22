package com.example.photomemory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

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
	private Bitmap bitmapImage;
	private ImageView myImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        
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
       
        for(int i = 0; i<photoPaths.length; i++)
        	Log.v(TAG, i + " - " + photoPaths[i]);
        
        nextPhoto(memPath, photoPaths[photoIndex]);
        photoIndex++;
        
        final Button memorizedButton = (Button) findViewById(R.id.memorizedButton);
        memorizedButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(photoIndex < photoPaths.length)
				{
					LinearLayout ll = (LinearLayout) findViewById(R.id.controlsFrame);
	 				ll.setVisibility(4);
	 				bitmapImage.recycle();
	 				myImageView.destroyDrawingCache();
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
        
        final Button notMemorizedButton = (Button) findViewById(R.id.notMemorizedButton);
        notMemorizedButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(photoIndex < photoPaths.length-1)
				{
					LinearLayout ll = (LinearLayout) findViewById(R.id.controlsFrame);
	 				ll.setVisibility(4);
	 				bitmapImage.recycle();
	 				myImageView.destroyDrawingCache();
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
		TextView tv = (TextView) findViewById(R.id.photoName);
		tv.setText("Final Score: Memorized: " + correctCount + " Wrong: " + wrongCount);
    }
    
    private void nextPhoto(String memPath, String name){
    	final String fullName = name;
    	bitmapImage = BitmapFactory.decodeFile(memPath + "/" + fullName); 
         myImageView = (ImageView)findViewById(R.id.imageView);
         myImageView.setImageBitmap(bitmapImage);
         myImageView.setOnTouchListener(new OnTouchListener() {
 			@Override
 			public boolean onTouch(View arg0, MotionEvent arg1) {
 				TextView tv = (TextView) findViewById(R.id.photoName);
 				tv.setText(fullName.substring(0, fullName.length()-4));
 				LinearLayout ll = (LinearLayout) findViewById(R.id.controlsFrame);
 				ll.setVisibility(0);
 				return false;
 			}
         	
         });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_viewer, menu);
        return true;
    }
}
/*
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        
        Bundle bundle = this.getIntent().getExtras();
        final String selectedFolder = bundle.getString("dbPath");
        
        File images = new File(Environment.getExternalStorageDirectory().toString() + "/" + selectedFolder);
        File[] imagelist = images.listFiles(new FilenameFilter(){  
        @Override  
        public boolean accept(File dir, String name)  
        {  
            return ((name.endsWith(".jpg"))||(name.endsWith(".png")));  
        }  
    });  
        mFiles = new String[imagelist.length];  
  
        for(int i= 0 ; i< imagelist.length; i++)  
        {  
            mFiles[i] = imagelist[i].getAbsolutePath();  
        }  
        mUrls = new Uri[mFiles.length];  
  
        for(int i=0; i < mFiles.length; i++)  
        {  
            mUrls[i] = Uri.parse(mFiles[i]);     
        }     
  
        Gallery g = (Gallery) findViewById(R.id.gallery1);  
        g.setAdapter(new ImageAdapter(this));  
        g.setFadingEdgeLength(40);  
    }  
    public class ImageAdapter extends BaseAdapter{  
          
        int mGalleryItemBackground;  
        public ImageAdapter(Context c)  {     
            mContext = c;     
        }  
        public int getCount(){  
            return mUrls.length;  
        }  
        public Object getItem(int position){  
            return position;  
        }  
        public long getItemId(int position) {  
            return position;  
        }  
        public View getView(int position, View convertView, ViewGroup parent){  
            ImageView i = new ImageView(mContext);  
  
            i.setImageURI(mUrls[position]);  
            i.setScaleType(ImageView.ScaleType.FIT_XY);  
            i.setLayoutParams(new Gallery.LayoutParams(260, 210));  
            return i;  
        }     
        private Context mContext;
		  
        }
 */
