package com.example.photomemory;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NextScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_screen);
        
        String imageInSD = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/1338634608906.jpg";
        System.out.println(Environment.getExternalStorageDirectory().getPath());
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        LinearLayout picLayout = (LinearLayout) findViewById(R.id.pictureLayout);
        int resizeW = 100;//picLayout.getWidth();
        int resizeH = 100;//picLayout.getHeight();
        
        Matrix matrix = new Matrix();
        matrix.postScale(resizeW, resizeH);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, false);

        
        ImageView myImageView = (ImageView)findViewById(R.id.imageViewPhoto);
        myImageView.setImageBitmap(resizedBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_next_screen, menu);
        return true;
    }
}
