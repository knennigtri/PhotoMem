package com.example.photomemory;

import java.io.File;
import java.io.FilenameFilter;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        
        Bundle bundle = this.getIntent().getExtras();
        final String selectedFolder = bundle.getString("dbPath");

        String memPath = Environment.getExternalStorageDirectory().toString() + "/" + selectedFolder;
        
        
        //Single file
        Bitmap bitmap = BitmapFactory.decodeFile(memPath + "/1 - Jeremiah Aleman.jpg");
        
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        LinearLayout picLayout = (LinearLayout) findViewById(R.id.photoFrame);
 /*       int resizeW = picLayout.getWidth();
        int resizeH = picLayout.getHeight();
        
        Matrix matrix = new Matrix();
        matrix.setScale(resizeW, resizeH);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);
*/
        
        ImageView myImageView = (ImageView)findViewById(R.id.imageView);
        myImageView.setImageBitmap(bitmap);
        
        final Button showButton = (Button) findViewById(R.id.showButton);
        showButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//TextView name = (TextView) findViewById(R.id.photoName);
				
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
