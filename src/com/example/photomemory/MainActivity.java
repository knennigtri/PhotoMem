package com.example.photomemory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;



public class MainActivity extends ListActivity {
	private static final String TAG = "MainActivity";
	private static final String newDBString = "+ Create New Mem";
	private static final String rootFolder = Environment.getExternalStorageDirectory().toString();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ArrayList<String> listItems=new ArrayList<String>();
        
        File[] folderPaths;
    	folderPaths = findPMFolders(rootFolder + "/" + getString(R.string.app_name));
        if(folderPaths.length == 0)
        {
        	listItems.add(newDBString);
        }
        else
        {
        	for(int i = 0; i < folderPaths.length; i++)
        	{
        		Log.v(TAG, folderPaths[i].getName());
        		listItems.add(folderPaths[i].getName());
        	}
        }
        
        final ArrayAdapter<String> aa = new ArrayAdapter<String>(this,R.layout.list_item,listItems);
        setListAdapter(aa);
        
        //This creates a listener for the Files listed
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	String selectedFolder = aa.getItem(position);
                Log.v(TAG, selectedFolder);
                if(selectedFolder.contains(newDBString)){
                	//TODO Create Add Mem Process
                	createMemAlert();
                	Log.v(TAG, "Creating new Mem");
                }
                else
                {
                	Bundle extras = new Bundle();
                	extras.putString("dbPath", selectedFolder);
                	Intent intent = new Intent(MainActivity.this,StartMemoryActivity.class);
                	intent.putExtras(extras);          
                	startActivity(intent);
                }
            }
        });
        
        //This allows for an optional deletion of the folder
        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				deleteMemAlert(aa.getItem(position));
				return false;
			}
        	
		});
    }
    
    /**
     * This creates an alert that gives input for Mems
     */
    private void createMemAlert(){           
        AlertDialog.Builder alert = new AlertDialog.Builder(this); 

        alert.setTitle("Create a new Mem"); 
        alert.setMessage("Mem Name and Description"); 
        
        final EditText name = new EditText(this);
        final EditText desc = new EditText(this);
        name.setHint("Name");
        desc.setHint("description");
        
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(name);
        ll.addView(desc);
        alert.setView(ll); 

        alert.setPositiveButton("Create", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	createMem(name.getText().toString(),desc.getText().toString());
            	Log.v(TAG, name.getText().toString() + " " + desc.getText().toString());
            	startActivity(new Intent(MainActivity.this, MainActivity.class));
            	finish();
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
    private void createMem(String name, String desc){
    	File folder = new File(rootFolder + "/" + getString(R.string.app_name) + "/" + name);
    	if(!folder.exists())
    	{
    		folder.mkdirs();
    		File descFile = new File(folder,getString(R.string.infoFile));
    		try
    		{
    			FileWriter fstream = new FileWriter(descFile);
    			BufferedWriter out = new BufferedWriter(fstream);
    			out.write(desc);
    			out.close();
    		}
    		catch(IOException e)
    		{
    			Log.e(TAG, e.getMessage());
    		}
    		Log.v(TAG, "New Mem Created: " + name);
    	}
    	else
    	{
    		Toast.makeText(this, "Mem already Exists!", Toast.LENGTH_SHORT).show();
    	}
    }
    
    /**
     * This creates an alert to confirm the deletion of the Mem
     * @param memName - Name of Mem
     */
    private void deleteMemAlert(final String memName){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this); 

        alert.setTitle(memName); 
        alert.setMessage("Delete Mem?"); 
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	Log.v(TAG, rootFolder + "/" + getString(R.string.app_name) + "/" + memName);
            	deleteMem(memName);
            	startActivity(new Intent(MainActivity.this, MainActivity.class));
            	finish();
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
    	File folder = new File(rootFolder + "/" + getString(R.string.app_name) + "/" + memName);
    	File[] files = folder.listFiles();
    	for(int i = 0; i< files.length; i++)
    		files[i].delete();
    	folder.delete();
    }
    
    /**
     * Finds all the file names in a given folder
     * @return - List of Files that are in the given folder
     */
    private File[] findPMFolders(String folderPath){
    	File folder = new File(folderPath);
    	Log.v(TAG,folder.toString());
    	if(!folder.exists()){
    		folder.mkdirs();
    		Log.v(TAG, "Folder Created");
    	}
    	else
    	{
    		Log.v(TAG, "The folder is already present");
    	}
    	
    	return folder.listFiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.add_mem:
    		//TODO Create Add Mem Process\
    		//Open popup window
    		createMemAlert();
    		//if (p != null) showPopup(MainActivity.this, p);          	
    	case R.id.menu_about: //TODO Settings page
    		//TODO Create About Me Page
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
