package com.nennig.photomem;

import java.io.File;
import java.util.ArrayList;
import com.nennig.photomem.R;
import android.net.Uri;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;


public class MainActivity extends ListActivity {
	
	private static final String TAG = "MainActivity";
	private static CustomAlerts cAlerts;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> listItems=new ArrayList<String>();
        
        File[] MemPaths;
    	MemPaths = getMemFolders(Mem.ROOT_FOLDER + "/" + getString(R.string.app_name));
        if(MemPaths.length == 0)
        {
        	listItems.add(Mem.DEFAULT_CREATE_MEM);
        }
        else
        {
        	for(int i = 0; i < MemPaths.length; i++)
        	{
        		Log.d(TAG, MemPaths[i].getName());
        		listItems.add(MemPaths[i].getName());
        	}
        }
        
        
        cAlerts = new CustomAlerts(this);
        final ArrayAdapter<String> aa = new ArrayAdapter<String>(this,R.layout.main_list_item,listItems);
        setListAdapter(aa);
        
        //This creates a listener for the Files listed
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	String memFolder = aa.getItem(position);
                Log.d(TAG, memFolder);
                if(memFolder.contains(Mem.DEFAULT_CREATE_MEM)){
                	cAlerts.createMemAlert(MainActivity.this);
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
				cAlerts.manageMemAlert(MainActivity.this, aa.getItem(position));
				return false;
			}
        	
		});
    }
    
    /**
     * Finds all the file names in a given folder
     * @return - List of Files that are in the given folder
     */
    private File[] getMemFolders(String folderPath){
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
    		cAlerts.createMemAlert(MainActivity.this);
    		return true;
    	case R.id.menu_about: //TODO Settings page
    		cAlerts.aboutAlert(MainActivity.this);
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
