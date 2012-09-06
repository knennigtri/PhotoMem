package com.example.photomemory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAlerts{
	private static final String TAG = "CustomAlerts";
	private String memFolder;
	private Activity _activity;
	
	public CustomAlerts(Activity a, String folder){
		folder = memFolder;
		_activity = a;
	}
	
	public CustomAlerts(Activity a){
		this(a, "");
	}
	
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
            	Log.v(TAG, "Name changed to: " + name.getText().toString());
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
	
	public void namePhotoAfterImport(final Context c, final FileManagement f){
		AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle("New Photo"); 
        alert.setMessage("What should be remembered?"); 
        
        final EditText name = new EditText(c);
        name.setHint("Photo Description");
        
        alert.setView(name); 

        alert.setPositiveButton("Add Photo", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	f.renamePhoto(name.getText().toString(), _activity.getString(R.string.default_photo_name));
            	Log.v(TAG, name.getText().toString());
            	Toast.makeText(c, "Photo Added to Mem!", Toast.LENGTH_LONG).show();
            } 
        }); 
        alert.show(); 
	}
}
