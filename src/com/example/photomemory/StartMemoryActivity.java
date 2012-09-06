package com.example.photomemory;

import java.io.File;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class StartMemoryActivity extends Activity {
	private static final String TAG = "StartMemoryActivity";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	private static final int REQUEST_CHOOSE_IMAGE = 2;
	private static final int REQUEST_CHOOSE_MULTI_IMAGE = 3;
	private String memFolder;
	private CustomAlerts cAlerts;
	private FileManagement fManagement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        Bundle bundle = this.getIntent().getExtras();
        memFolder = bundle.getString("memFolder");        
        this.setTitle(memFolder);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_memory);
        cAlerts = new CustomAlerts(this, memFolder);
        fManagement = new FileManagement(this, memFolder);
        
        final Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new Button.OnClickListener() {
        	@Override
        	public void onClick(View arg0){
        		if(fManagement.hasMemPhotos()){
	        		Bundle extras = new Bundle();
	            	extras.putString("memFolder", memFolder);
	            	Intent intent = new Intent(StartMemoryActivity.this,ViewerActivity.class);
	            	intent.putExtras(extras);          
	            	startActivity(intent);
        		}
        		else
        		{
        			toastMessage("No Photos found, click Add Photo","long");
        		}
        	}
        });
        
        final Button practiceButton = (Button) findViewById(R.id.practiceButton);
        practiceButton.setOnClickListener(new Button.OnClickListener() {
        	@Override
        	public void onClick(View arg0){
        		if(fManagement.hasMemPhotos()){
	        		Bundle extras = new Bundle();
	            	extras.putString("memFolder", memFolder);
	            	Intent intent = new Intent(StartMemoryActivity.this,PracticeActivity.class);
	            	intent.putExtras(extras);          
	            	startActivity(intent);
        		}
        		else
        		{
        			toastMessage("No Photos found. To add photos to this Mem, click " + getString(R.string.startMem_button_add_photo),"long");
        		}
        	}
        });
        
        final Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(StartMemoryActivity.this, MainActivity.class);
				startActivity(intent);
				StartMemoryActivity.this.finish();
			}
        	
        });
        
        final Button addPhotoButton = (Button) findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PhotoSelectorInflator();
			}
        	
        });        
    }
    
    private void toastMessage(String m, String len){
    	if(len.equals("long")){
    		Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    	}
    	else
    	{
    		Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    	}
    }
    
    private void PhotoSelectorInflator(){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);  

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        final Button takePhoto = new Button(this);
        takePhoto.setBackgroundResource(R.drawable.red_button);
        takePhoto.setText("Take Photo");
        takePhoto.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.v(TAG,"Take Photo Clicked");
				takePhoto();
			}
        });
        final Button selectPhoto = new Button(this);
        selectPhoto.setBackgroundResource(R.drawable.red_button);
        selectPhoto.setText("select Photo");
        
        selectPhoto.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.v(TAG,"select Photo Clicked");
				selectPhoto();
			}
        });
        final Button selectFolder = new Button(this);
        selectFolder.setBackgroundResource(R.drawable.red_button);
        selectFolder.setText("select Folder");
        selectFolder.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.v(TAG,"select Folder Clicked");
				selectFolder();
			}	
        });
        
        ll.addView(takePhoto);
        ll.addView(selectPhoto);
        ll.addView(selectFolder);
        
        alert.setView(ll); 
        alert.show(); 
    
    }
    
    private void takePhoto(){
    	// create Intent to take a picture and return control to the calling application
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    			    
	    File imagesFolder = new File(FileManagement.ROOTFOLDER + "/" + getString(R.string.app_name) + "/" + memFolder + "/");
	    
	    File image = new File(imagesFolder, getString(R.string.default_photo_name));
	    Uri uriSavedImage = Uri.fromFile(image);
	    
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage); // set the image file name

	    // start the image capture Intent
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
    
    private void selectPhoto(){
    	Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, Images.Media.INTERNAL_CONTENT_URI);
    	startActivityForResult(choosePictureIntent, REQUEST_CHOOSE_IMAGE);
    }
    
    private void selectFolder(){
    	//TODO Complete Select Folder - Need to create custom Gallery
    //	Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE, Images.Media.INTERNAL_CONTENT_URI);
    //	startActivityForResult(shareIntent, REQUEST_CHOOSE_MULTI_IMAGE);
    }
    
    private void restartActivity(){
    	Bundle extras = new Bundle();
    	extras.putString("memFolder", this.getIntent().getExtras().getString("memFolder"));
    	Intent intent = new Intent(StartMemoryActivity.this,StartMemoryActivity.class);
    	intent.putExtras(extras);          
    	startActivity(intent);
    	Toast.makeText(this, "Photo added to Mem", Toast.LENGTH_SHORT).show();
		finish();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
            	cAlerts.namePhotoAfterImport(this, fManagement);
            }
        }
        else if (requestCode == REQUEST_CHOOSE_IMAGE) {
            if (resultCode == RESULT_OK) {
                Log.v(TAG, "Picture Choosen: " + getPath(data.getData()));
                fManagement.copyPhoto(getPath(data.getData()));
            }
        }
        else if (requestCode == REQUEST_CHOOSE_MULTI_IMAGE) {
            if (resultCode == RESULT_OK) {
            	//TODO onActivityResult Folder
            	if (Intent.ACTION_SEND_MULTIPLE.equals(data.getAction()) && data.hasExtra(Intent.EXTRA_STREAM)) { 
            	    ArrayList<Parcelable> list = data.getParcelableArrayListExtra(Intent.EXTRA_STREAM); 
            	    for (Parcelable p : list) { 
            	       Uri uri = (Uri) p; 
            	       Log.v(TAG, "URI: " + uri.getPath());
            	   } 
            	} 
            }
        }
        restartActivity();
    }
    
    public String getPath(Uri uri) {
        int columnIndex = 0;

        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, proj, null, null, null);

        try {
            columnIndex = cursor.getColumnIndexOrThrow
                           (MediaStore.Images.Media.DATA);
        } catch (Exception e) {
            Toast.makeText(this, "Exception in getRealPathFromURI",
                           Toast.LENGTH_SHORT).show();
            finish();  
            return null;
        }       
        cursor.moveToFirst();
        return cursor.getString(columnIndex);               

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_start_memory, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_settings:
    		//TODO Create Settings Activity
    		Toast.makeText(this, "Settings Inflate", Toast.LENGTH_SHORT).show();
    		return true;
    	case R.id.menu_about: //TODO Settings page
    		//TODO Create About Me Page
    		Toast.makeText(this, "About Inflate", Toast.LENGTH_SHORT).show();  
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
