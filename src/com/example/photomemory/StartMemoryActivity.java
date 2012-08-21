package com.example.photomemory;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartMemoryActivity extends Activity {
	private static final String TAG = "StartMemoryActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        Bundle bundle = this.getIntent().getExtras();
        final String selectedFolder = bundle.getString("dbPath");
        String[] parsePath = selectedFolder.split("/");
        
        this.setTitle(parsePath[parsePath.length-1]);
        
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
        
        
        
        final Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(StartMemoryActivity.this, MainActivity.class);
				startActivity(intent);
				StartMemoryActivity.this.finish();
				
			}
        	
        });
        
        
        Log.v(TAG, "Path: " + selectedFolder);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_start_memory, menu);
        return true;
    }
}
