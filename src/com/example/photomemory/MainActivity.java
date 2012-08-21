package com.example.photomemory;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;


public class MainActivity extends ListActivity {

	private static final String TAG = "MainActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ArrayList<String> listItems=new ArrayList<String>();
        listItems.add("Hello");
        listItems.add("Kevin");
        
        Log.v(TAG, "Test");
        File[] folderPaths;
    	folderPaths = findPMFolders();
        if(folderPaths == null)
        {

        }
        else
        {
        	for(int i = 0; i < folderPaths.length; i++)
        	{
        		listItems.add(folderPaths[i].getName());
        	}
        }
        
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,R.layout.list_item,listItems);
        setListAdapter(aa);
        
  /*      
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NextScreen.class);
                startActivity(intent);
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public File[] findPMFolders(){
    	File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/"+R.string.app_name+"/");
    	Log.v(TAG,folder.getPath());
    	if(!folder.exists()){
    		folder.mkdir();
    		Log.v(TAG, "Folder Created");
    	}
    	return folder.listFiles();
    }
}
