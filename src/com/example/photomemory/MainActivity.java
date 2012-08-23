package com.example.photomemory;

import java.io.File;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.Environment;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;



public class MainActivity extends ListActivity {
	private static final String appName = "PhotoMemory";
	private static final String TAG = "MainActivity";
	private static final String newDBString = "+Create First PhotoMem";
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ArrayList<String> listItems=new ArrayList<String>();
        
        File[] folderPaths;
    	folderPaths = findPMFolders();
        if(folderPaths == null)
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
        
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	String selectedFolder = appName + "/" + aa.getItem(position);
                Log.v(TAG, selectedFolder);
                if(selectedFolder.equals(newDBString)){
                	//Go to Creating new PhotoMem
                	Log.v(TAG, "There are no PhotoMems to select.");
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
    }
    
    public File[] findPMFolders(){
    	File folder = new File(Environment.getExternalStorageDirectory().toString() + "/"+appName+"/");
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
    	case R.id.menu_memory_menu:
    		startActivity(new Intent(this, StartMemoryActivity.class));
    		finish();
    		return true;
    	case R.id.menu_edit: //TODO Create Preview Edit
    		//Edit the current file
    	case R.id.add_mem:
    		//TODO Create Add Mem Process
    	case R.id.menu_settings: //TODO Settings page
    		//startActivity(new Intent(PracticeActivity.this, Settings.class));
    		finish();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    
    
}
