package com.nennig.photomem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nennig.photomem.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAlerts{
	private static final String TAG = "CustomAlerts";
	private Activity _activity;
	
	public CustomAlerts(Activity a){
		_activity = a;
	}
	
    public void aboutAlert(Context c){
    	//TODO Finish ABOUT Alert
    	AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle("About"); 
        alert.setMessage("Copywrite @ 2012 Kevin Nennig");
        
        alert.setPositiveButton("View Site", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	String url = "https://sites.google.com/site/nennigk/personal-projects/photomem-app";
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	_activity.startActivity(i);
            } 
        }); 
        
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
              // Canceled. 
            } 
      }); 
        
      alert.show();
    }
	
	/**********************************************************
	 *********Custom Alerts for PracticeActivity START*********
	 **********************************************************/
	
	public void editPhoto(Context c, final FileManagement f, final String prevName){
    	AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle("Edit Photo"); 
        
        final EditText name = new EditText(c);
        name.setText(prevName);
        
        alert.setView(name); 

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	f.renamePhoto(name.getText().toString(), prevName);
            	TextView tv = (TextView) _activity.findViewById(R.id.practice_photoName);
            	tv.setText(FileManagement.getPhotoName(name.getText().toString()));
            	Log.d(TAG, "Name changed to: " + name.getText().toString());
            } 
        }); 
        alert.show();  
    }
	
	public void deletePhoto(final Context c, final FileManagement f, final String photoName){
		AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle("Delete " + photoName + "?"); 

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	f.deletePhoto(photoName);
            	Toast.makeText(c, "Photo Deleted", Toast.LENGTH_SHORT).show();
            } 
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	//Cancel Deletion 
            } 
        });
        alert.show(); 
	}
	
	/**********************************************************
	 *********Custom Alerts for PracticeActivity END***********
	 **********************************************************/
	
	/**********************************************************
	 ******Custom Alerts for StartMemoryActivity START*********
	 **********************************************************/
	
	public void namePhotoAfterImport(final Context c, final FileManagement f, final String oldName){
		AlertDialog.Builder alert = new AlertDialog.Builder(c); 

		
			
		
        alert.setTitle("New Photo"); 
        alert.setMessage("What should be remembered?"); 
        
        final EditText name = new EditText(c);
        
        if(!oldName.equals(_activity.getString(R.string.default_photo_name)))
        	name.setText(oldName);
        else
        	name.setHint("Memory Description with Photo");
        
        alert.setView(name); 

        alert.setPositiveButton("Add Photo", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	f.renamePhoto(name.getText().toString(), oldName);
            	Log.d(TAG, name.getText().toString());
            	Intent intent = new Intent(_activity, StartMemoryActivity.class);
            	intent.putExtra(Mem.CURRENT_MEM, _activity.getIntent().getExtras().getString(Mem.CURRENT_MEM));
            	_activity.startActivity(intent);
            	_activity.finish();
            } 
        }); 
        alert.show(); 
	}
	
    public void AddPhotoAlert(Context c, final String memFolder){
    	AlertDialog.Builder alert = new AlertDialog.Builder(c);  

        LinearLayout ll = new LinearLayout(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        final Button takePhoto = new Button(c);
        takePhoto.setBackgroundResource(R.drawable.blue_button);
        takePhoto.setText("Take Photo");
        takePhoto.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d(TAG,"Take Photo Clicked");
				takePhoto(memFolder);
			}
        });
        ll.addView(takePhoto);
        
        final Button selectPhoto = new Button(c);
        selectPhoto.setBackgroundResource(R.drawable.blue_button);
        selectPhoto.setText("select Photo");
        
        selectPhoto.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d(TAG,"select Photo Clicked");
				selectPhoto();
			}
        });
        ll.addView(selectPhoto);
        
/*        final Button selectFolder = new Button(this);
        selectFolder.setBackgroundResource(R.drawable.blue_button);
        selectFolder.setText("select Folder");
        selectFolder.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d(TAG,"select Folder Clicked");
				selectMultiPhotos();
			}	
        });
        ll.addView(selectFolder);
*/        
        alert.setView(ll); 
        alert.show(); 
    
    }
    
    private void takePhoto(String memFolder){
    	// create Intent to take a picture and return control to the calling application
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    			    
	    File imagesFolder = new File(Mem.ROOT_FOLDER + "/" + _activity.getString(R.string.app_name) + "/" + memFolder + "/");
	    
	    File image = new File(imagesFolder, _activity.getString(R.string.default_photo_name));
	    Uri uriSavedImage = Uri.fromFile(image);
	    
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage); // set the image file name

	    // start the image capture Intent
	    _activity.startActivityForResult(intent, Mem.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
    
    private void selectPhoto(){
    	Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, Images.Media.INTERNAL_CONTENT_URI);
    	_activity.startActivityForResult(choosePictureIntent, Mem.REQUEST_CHOOSE_IMAGE_REQUEST_CODE);
    }
    
//    private void selectMultiPhotos(Context c){
//    	//TODO Complete Select Folder - Need to create custom Gallery
//    //	Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE, Images.Media.INTERNAL_CONTENT_URI);
//    //	startActivityForResult(shareIntent, REQUEST_CHOOSE_MULTI_IMAGE);
//    	Toast.makeText(c, "This is not implemented yet.", Toast.LENGTH_SHORT).show();
//    	restartActivity("This is not implemented yet.");
//    }
	
	/**********************************************************
	 ******Custom Alerts for StartMemoryActivity END***********
	 **********************************************************/
	
	/**********************************************************
	 *********Custom Alerts for MainActivity START*************
	 **********************************************************/
	   
    /**
     * This creates an alert that gives input for Mems
     */
    public void createMemAlert(final Context c){           
    	AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle("Create a new Mem"); 
        alert.setMessage("Mem Name and Description"); 
        
        final EditText name = new EditText(c);
        final EditText desc = new EditText(c);
        name.setHint("Name");
        desc.setHint("description");
        
        LinearLayout ll = new LinearLayout(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(name);
        ll.addView(desc);
        alert.setView(ll); 

        alert.setPositiveButton("Create", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	String _name = name.getText().toString();
            	String _desc = desc.getText().toString();
            	setMemPrefs(_name, _desc);
            	createMem(c, _name,_desc);
            	Log.d(TAG, _name + " " + _desc);
            	_activity.startActivity(new Intent(_activity, MainActivity.class));
            	_activity.finish();
            } 
        }); 
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
              public void onClick(DialogInterface dialog, int whichButton) { 
                // Canceled. 
              } 
        }); 
        alert.show();  
    }
    
    /**
     * This creates a folder for the Mem
     * @param name - name of the folder
     * @param desc - description of the Mem
     */
    private void createMem(Context c, String name, String desc){
    	File folder = new File(Mem.ROOT_FOLDER + "/" + _activity.getString(R.string.app_name) + "/" + name);
    	if(!folder.exists())
    	{
    		folder.mkdirs();
    	}
    	else
    	{
    		Toast.makeText(c, "Mem already Exists!", Toast.LENGTH_SHORT).show();
    	}
    }

    private void setMemPrefs(String name, String desc){
    	SharedPreferences settings =  _activity.getSharedPreferences(Mem.MY_PREFS,Context.MODE_PRIVATE);
    	SharedPreferences.Editor e = settings.edit();
    	e.putString(name + "." + Mem.MEM_NAME, name);
    	e.putString(name + "." + Mem.DESCRIPTION, desc);
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
    	
    	e.putString(name + "." + Mem.TIME_CREATED, dateFormat.format(date));
    	e.putInt(name + "." + Mem.TIMES_COMPLETED, 0);
    	e.putFloat(name + "." + Mem.COMPLETION_RATE, 0);
    	e.commit();
    }
    
    public void manageMemAlert(final Context c, final String memName){
    	AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle(memName); 
        alert.setPositiveButton("Delete Mem", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	deleteMemAlert(c, memName);
            } 
        }); 
        
        alert.setNegativeButton("Edit Mem", new DialogInterface.OnClickListener() { 
              public void onClick(DialogInterface dialog, int whichButton) { 
                editMemAlert(c, memName); 
              } 
        }); 
        alert.show();
    }
    
    private void editMemAlert(Context c, final String memName){
    	AlertDialog.Builder alert = new AlertDialog.Builder(c); 
        alert.setTitle("Edit Mem Description"); 
        
        final EditText desc = new EditText(c);
        
        final SharedPreferences settings = _activity.getSharedPreferences(Mem.MY_PREFS, Context.MODE_PRIVATE);
        desc.setText(settings.getString(memName + "." + Mem.DESCRIPTION, ""));
      
        desc.setHint("description");
        
        LinearLayout ll = new LinearLayout(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(desc);
        alert.setView(ll); 

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	String str = desc.getText().toString();;
            	settings.edit().putString(memName + "." + Mem.DESCRIPTION, str).commit();
            } 
        }); 
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
              public void onClick(DialogInterface dialog, int whichButton) { 
                // Canceled. 
              } 
        }); 
        alert.show();  
    }
    
    /**
     * This creates an alert to confirm the deletion of the Mem
     * @param memName - Name of Mem
     */
    private void deleteMemAlert(final Context c, final String memName){
    	AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle(memName); 
        alert.setMessage("Delete Mem?"); 
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	Log.d(TAG, Mem.ROOT_FOLDER + "/" + _activity.getString(R.string.app_name) + "/" + memName);
            	deleteMem(memName);
            	_activity.startActivity(new Intent(_activity, MainActivity.class));
            	_activity.finish();
            } 
        }); 
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
              public void onClick(DialogInterface dialog, int whichButton) { 
                // Canceled. 
              } 
        }); 
        alert.show();
    }
    
    /**
     * This deletes a folder and all the files underneath it
     * @param memName - Name of folder that will be deleted
     */
    private void deleteMem(String memName){
    	File folder = new File(Mem.ROOT_FOLDER + "/" + _activity.getString(R.string.app_name) + "/" + memName);
    	File[] files = folder.listFiles();
    	for(int i = 0; i< files.length; i++)
    		files[i].delete();
    	folder.delete();
    }
	
	/**********************************************************
	 *********Custom Alerts for MainActivity END*************
	 **********************************************************/
}
