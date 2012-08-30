package com.example.photomemory;

import java.io.File;
import java.util.Currency;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class StartMemoryActivity extends Activity {
	private static final String TAG = "StartMemoryActivity";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private String selectedFolder;
	private String rootFolder = Environment.getExternalStorageDirectory().toString();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        Bundle bundle = this.getIntent().getExtras();
        selectedFolder = bundle.getString("dbPath");        
        this.setTitle(selectedFolder);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_memory);
        
        final Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new Button.OnClickListener() {
        	@Override
        	public void onClick(View arg0){
        		Bundle extras = new Bundle();
            	extras.putString("dbPath", selectedFolder);
            	Intent intent = new Intent(StartMemoryActivity.this,ViewerActivity.class);
            	intent.putExtras(extras);          
            	startActivity(intent);
        	}
        });
        
        final Button practiceButton = (Button) findViewById(R.id.practiceButton);
        practiceButton.setOnClickListener(new Button.OnClickListener() {
        	@Override
        	public void onClick(View arg0){
        		Bundle extras = new Bundle();
            	extras.putString("dbPath", selectedFolder);
            	Intent intent = new Intent(StartMemoryActivity.this,PracticeActivity.class);
            	intent.putExtras(extras);          
            	startActivity(intent);
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
				 // create Intent to take a picture and return control to the calling application
			    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    			    
			    File imagesFolder = new File(rootFolder + "/" + getString(R.string.app_name) + "/" + selectedFolder + "/");
			    
			    File image = new File(imagesFolder, getString(R.string.default_photo_name));
			    Uri uriSavedImage = Uri.fromFile(image);
			    
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage); // set the image file name

			    // start the image capture Intent
			    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
        	
        });        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
            	AlertDialog.Builder alert = new AlertDialog.Builder(this); 

                alert.setTitle("New Photo"); 
                alert.setMessage("What should be remembered?"); 
                
                final EditText name = new EditText(this);
                name.setHint("Photo Description");
                
                alert.setView(name); 

                alert.setPositiveButton("Add Photo", new DialogInterface.OnClickListener() { 
                    public void onClick(DialogInterface dialog, int whichButton) { 
                    	renamePhoto(name.getText().toString(), getString(R.string.default_photo_name));
                    	Log.v(TAG, name.getText().toString());
                    	Toast.makeText(StartMemoryActivity.this, "Photo Added to Mem!", Toast.LENGTH_LONG).show();
                    } 
                }); 
                alert.show();               
            }
        }
    }
    
    private void renamePhoto(String newName, String prevName){
    	File file = new File(rootFolder + "/" + getString(R.string.app_name) + 
    			"/" + selectedFolder + "/" + prevName);
    	if(file.exists())
    	{
    		file.renameTo(new File(rootFolder + "/" + getString(R.string.app_name) + 
    				"/" + selectedFolder + "/" + newName + ".jpg"));
    		Log.v(TAG, "Changed to: " + rootFolder + "/" + getString(R.string.app_name) + 
    				"/" + selectedFolder + "/" + newName + ".jpg");
    	}
    	else
    	{
    		Log.v(TAG, rootFolder + "/" + getString(R.string.app_name) + 
    				"/" + selectedFolder + "/" + prevName + 
    				" does not exist!");
    	}
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
    		//TODO Create Add Mem Process\
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
