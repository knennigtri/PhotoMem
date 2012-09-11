package com.nennig.photomem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.nennig.photomem.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
        		Log.d(TAG, folderPaths[i].getName());
        		listItems.add(folderPaths[i].getName());
        	}
        }
        
        final ArrayAdapter<String> aa = new ArrayAdapter<String>(this,R.layout.main_list_item,listItems);
        setListAdapter(aa);
        
        //This creates a listener for the Files listed
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	String memFolder = aa.getItem(position);
                Log.d(TAG, memFolder);
                if(memFolder.contains(newDBString)){
                	createMemAlert();
                	Log.d(TAG, "Creating new Mem");
                }
                else
                {
                	Bundle extras = new Bundle();
                	extras.putString(Mem.CURRENT_MEM, memFolder);
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
				manageMemAlert(aa.getItem(position));
				return false;
			}
        	
		});
    }
    
    public SQLiteDatabase openDatebase(){
    	PhotoMemOpenHelper db = new PhotoMemOpenHelper(this);
    	
    	return null;
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
            	String _name = name.getText().toString();
            	String _desc = desc.getText().toString();
            	setMemPrefs(_name, _desc);
            	createMem(_name,_desc);
            	Log.d(TAG, _name + " " + _desc);
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
    		Log.d(TAG, "New Mem Created: " + name);
    	}
    	else
    	{
    		Toast.makeText(this, "Mem already Exists!", Toast.LENGTH_SHORT).show();
    	}
    }
    
    private void setMemPrefs(String name, String desc){
    	SharedPreferences settings =  getSharedPreferences(Mem.MY_PREFS,MODE_PRIVATE);
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
    
    private void manageMemAlert(final String memName){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this); 

        alert.setTitle(memName); 
        alert.setPositiveButton("Delete Mem", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	deleteMemAlert(memName);
            } 
        }); 
        
        alert.setNegativeButton("Edit Mem", new DialogInterface.OnClickListener() { 
              public void onClick(DialogInterface dialog, int whichButton) { 
                editMemAlert(memName); 
              } 
        }); 
        alert.show();
    }
    
    private void editMemAlert(final String memName){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this); 
        alert.setTitle("Edit Mem Description"); 
        
        final EditText desc = new EditText(this);
        
        final SharedPreferences settings = getSharedPreferences(Mem.MY_PREFS, MODE_PRIVATE);
        desc.setText(settings.getString(memName + "." + Mem.DESCRIPTION, ""));
      
        desc.setHint("description");
        
        LinearLayout ll = new LinearLayout(this);
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
    private void deleteMemAlert(final String memName){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this); 

        alert.setTitle(memName); 
        alert.setMessage("Delete Mem?"); 
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	Log.d(TAG, rootFolder + "/" + getString(R.string.app_name) + "/" + memName);
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
    	Log.d(TAG,folder.toString());
    	if(!folder.exists()){
    		folder.mkdirs();
    		Log.d(TAG, "Folder Created");
    	}
    	else
    	{
    		Log.d(TAG, "The folder is already present");
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
    		createMemAlert();
    		//if (p != null) showPopup(MainActivity.this, p);          	
    	case R.id.menu_about: //TODO Settings page
    		//TODO Create About Me Page
    		return true;
    	case R.id.menu_rate_this:
    		String str ="https://play.google.com/store/apps/details?id=com.nennig.photomem";
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
