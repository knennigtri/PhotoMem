package com.nennig.photomem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.nennig.photomem.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartMemoryActivity extends Activity {
	private static final String TAG = "StartMemoryActivity";

//	private static final int REQUEST_CHOOSE_MULTI_IMAGE = 3;
	private String memFolder;
	private CustomAlerts cAlerts;
	private FileManagement fManagement;
	
	private boolean _randomize;
	private int _stats_timesCompleted;
	private float _stats_completionRate;
	private int _stats_perfectMem;
	private String _mem_description;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	if(savedInstanceState != null){
        	memFolder = savedInstanceState.getString(Mem.CURRENT_MEM);
        	LoadPreferences(memFolder);
        	_randomize = savedInstanceState.getBoolean(Mem.RANDOMIZE);
        }
        else
        {
	        Bundle bundle = this.getIntent().getExtras();
	        memFolder = bundle.getString(Mem.CURRENT_MEM);  
	        LoadPreferences(memFolder);
        }
        
        this.setTitle(_mem_description);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_memory);
        
        cAlerts = new CustomAlerts(StartMemoryActivity.this);
        fManagement = new FileManagement(this, memFolder);
        
        setMemTexts();
        
        final CheckBox randButton = (CheckBox) findViewById(R.id.start_random_checkbox);
        randButton.setOnClickListener(new CheckBox.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(_randomize)
					_randomize = false;
				else
					_randomize = true;
				getPreferences(MODE_PRIVATE).edit().putBoolean(
						memFolder + "." + Mem.RANDOMIZE, _randomize).commit();
			}

        });
        
        final Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new Button.OnClickListener() {
        	@Override
        	public void onClick(View arg0){
        		if(fManagement.hasMemPhotos()){
	        		Bundle extras = new Bundle();
	            	extras.putString(Mem.CURRENT_MEM, memFolder);
	            	extras.putBoolean(Mem.RANDOMIZE, _randomize);
	            	Intent intent = new Intent(StartMemoryActivity.this,ViewerActivity.class);
	            	intent.putExtras(extras);          
	            	startActivity(intent);
	            	finish();
        		}
        		else
        		{
        			Toast.makeText(StartMemoryActivity.this, "No Photos found, click Add Photo",Toast.LENGTH_LONG).show();
        		}
        	}
        });
        
        final Button practiceButton = (Button) findViewById(R.id.practiceButton);
        practiceButton.setOnClickListener(new Button.OnClickListener() {
        	@Override
        	public void onClick(View arg0){
        		if(fManagement.hasMemPhotos()){
	        		Bundle extras = new Bundle();
	            	extras.putString(Mem.CURRENT_MEM, memFolder);
	            	extras.putBoolean(Mem.RANDOMIZE, _randomize);
	            	Intent intent = new Intent(StartMemoryActivity.this,PracticeActivity.class);
	            	intent.putExtras(extras);          
	            	startActivity(intent);
	            	finish();
        		}
        		else
        		{
        			Toast.makeText(StartMemoryActivity.this, "No Photos found. To add photos to this Mem, click " + getString(R.string.startMem_button_add_photo),Toast.LENGTH_LONG).show();
        		}
        	}
        });
        
        final Button addPhotoButton = (Button) findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cAlerts.AddPhotoAlert(StartMemoryActivity.this, memFolder);
			}
        	
        });
        
/*        final Button editButton = (Button) findViewById(R.id.editButton);
        editButton.setVisibility(Button.INVISIBLE);
        editButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				toastMessage("Currently not implemented. You can individually edit each photo in Practice mode.", "long");
			}
        	
        });
*/
        final Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(StartMemoryActivity.this, MainActivity.class);
				startActivity(intent);
				StartMemoryActivity.this.finish();
			}
        	
        });       
    }
    
    private void setMemTexts(){
    	TextView desc = (TextView) findViewById(R.id.viewer_mem_description);
    	desc.setText(memFolder);
    	
    	TextView timesCompleted = (TextView) findViewById(R.id.viewer_stats_completed);
    	timesCompleted.setText("Mem completed " + _stats_timesCompleted + " times");
   
     	TextView completionRate = (TextView) findViewById(R.id.viewer_stats_memorized);
    	completionRate.setText(_stats_completionRate + "% of Mem Memorized");
    	
    	TextView perfectMem = (TextView) findViewById(R.id.viewer_stats_perfect_mem);
    	perfectMem.setText("Perfect Mem Completed " + _stats_perfectMem + " Times");
    
    	CheckBox cb = (CheckBox) findViewById(R.id.start_random_checkbox);
    	cb.setChecked(_randomize);
    }
    
//    private void PhotoSelectorInflator(){
//    	AlertDialog.Builder alert = new AlertDialog.Builder(this);  
//
//        LinearLayout ll = new LinearLayout(this);
//        ll.setOrientation(LinearLayout.VERTICAL);
//        final Button takePhoto = new Button(this);
//        takePhoto.setBackgroundResource(R.drawable.blue_button);
//        takePhoto.setText("Take Photo");
//        takePhoto.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Log.d(TAG,"Take Photo Clicked");
//				takePhoto();
//			}
//        });
//        ll.addView(takePhoto);
//        
//        final Button selectPhoto = new Button(this);
//        selectPhoto.setBackgroundResource(R.drawable.blue_button);
//        selectPhoto.setText("select Photo");
//        
//        selectPhoto.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Log.d(TAG,"select Photo Clicked");
//				selectPhoto();
//			}
//        });
//        ll.addView(selectPhoto);
//        
///*        final Button selectFolder = new Button(this);
//        selectFolder.setBackgroundResource(R.drawable.blue_button);
//        selectFolder.setText("select Folder");
//        selectFolder.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Log.d(TAG,"select Folder Clicked");
//				selectMultiPhotos();
//			}	
//        });
//        ll.addView(selectFolder);
//*/        
//        alert.setView(ll); 
//        alert.show(); 
//    
//    }
//    
//    private void takePhoto(){
//    	// create Intent to take a picture and return control to the calling application
//	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//	    			    
//	    File imagesFolder = new File(Mem.ROOT_FOLDER + "/" + getString(R.string.app_name) + "/" + memFolder + "/");
//	    
//	    File image = new File(imagesFolder, getString(R.string.default_photo_name));
//	    Uri uriSavedImage = Uri.fromFile(image);
//	    
//	    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage); // set the image file name
//
//	    // start the image capture Intent
//	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//    }
//    
//    private void selectPhoto(){
//    	Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, Images.Media.INTERNAL_CONTENT_URI);
//    	startActivityForResult(choosePictureIntent, REQUEST_CHOOSE_IMAGE);
//    }
//    
//    private void selectMultiPhotos(){
//    	//TODO Complete Select Folder - Need to create custom Gallery
//    //	Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE, Images.Media.INTERNAL_CONTENT_URI);
//    //	startActivityForResult(shareIntent, REQUEST_CHOOSE_MULTI_IMAGE);
//    	Toast.makeText(this, "This is not implemented yet.", Toast.LENGTH_SHORT).show();
//    	restartActivity("This is not implemented yet.");
//    }
    
    private void restartActivity(String toastStr){
    	Bundle extras = new Bundle();
    	extras.putString(Mem.CURRENT_MEM, this.getIntent().getExtras().getString(Mem.CURRENT_MEM));
    	extras.putBoolean(Mem.RANDOMIZE, this.getIntent().getExtras().getBoolean(Mem.RANDOMIZE));
    	Intent intent = new Intent(StartMemoryActivity.this,StartMemoryActivity.class);
    	intent.putExtras(extras);          
    	startActivity(intent);
    	Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show();
		finish();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Mem.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
            	cAlerts.namePhotoAfterImport(this, fManagement);
            }
        }
        else if (requestCode == Mem.REQUEST_CHOOSE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Picture Choosen: " + getPath(data.getData()));
                fManagement.copyPhoto(getPath(data.getData()));
            }
        }
/*        else if (requestCode == REQUEST_CHOOSE_MULTI_IMAGE) {
            if (resultCode == RESULT_OK) {
            	//TODO onActivityResult Folder
            	if (Intent.ACTION_SEND_MULTIPLE.equals(data.getAction()) && data.hasExtra(Intent.EXTRA_STREAM)) { 
            	    ArrayList<Parcelable> list = data.getParcelableArrayListExtra(Intent.EXTRA_STREAM); 
            	    for (Parcelable p : list) { 
            	       Uri uri = (Uri) p; 
            	       Log.d(TAG, "URI: " + uri.getPath());
            	   } 
            	} 
            }
        }
*/
        restartActivity("Photo added to Mem");
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
    
    private void LoadPreferences(String name){
    	SharedPreferences settings = getSharedPreferences(Mem.MY_PREFS,MODE_PRIVATE);
        _mem_description = settings.getString(name + "." + Mem.DESCRIPTION, "");
    	_stats_timesCompleted = settings.getInt(name + "." + Mem.TIMES_COMPLETED, 0);
        _stats_completionRate = settings.getFloat(name + "." + Mem.COMPLETION_RATE, 0);
        _stats_perfectMem = settings.getInt(name + "." + Mem.PERFECT_MEM, 0);
        _randomize = settings.getBoolean(Mem.RANDOMIZE, false);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_start_memory, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_rate_this:
    		String str ="https://play.google.com/store/apps/details?id=com.nennig.photomem";
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    		return true;
    	case R.id.menu_about: //TODO Settings page
    		cAlerts.aboutAlert(StartMemoryActivity.this);  
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
    public void onSaveInstanceState(Bundle b){
    	b.putString(Mem.CURRENT_MEM, this.getIntent().getExtras().getString(Mem.CURRENT_MEM));
    	b.putBoolean(Mem.RANDOMIZE, this.getIntent().getExtras().getBoolean(Mem.RANDOMIZE));
    	super.onSaveInstanceState(b);
    }
}
