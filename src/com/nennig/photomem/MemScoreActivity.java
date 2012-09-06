package com.nennig.photomem;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MemScoreActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_score);
        
        String mem = this.getIntent().getExtras().getString("memFolder");
        String[] stats = this.getIntent().getExtras().getStringArray("memStats");
        
        TextView statOne = (TextView) findViewById(R.id.mem_score_one);
        statOne.setText("Results for " + mem + " Mem:");
        
        TextView statTwo = (TextView) findViewById(R.id.mem_score_two);
        statTwo.setText("Memorized: " + stats[0]);
        
        TextView statThree = (TextView) findViewById(R.id.mem_score_three);
        statThree.setText("Still need work: " + stats[1]);
        
        TextView statFour = (TextView) findViewById(R.id.mem_score_four);
        statFour.setText("Mem Completion: " + stats[2] + "%");
        
        Button backButton = (Button) findViewById(R.id.mem_score_button);
        backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle extras = new Bundle();
		    	extras.putString("memFolder", MemScoreActivity.this.getIntent().getExtras().getString("memFolder"));
		    	extras.putBoolean("memRandomize", MemScoreActivity.this.getIntent().getExtras().getBoolean("memRandomize"));
		    	extras.putStringArray("memStats", MemScoreActivity.this.getIntent().getExtras().getStringArray("memStats"));
		    	Intent intent = new Intent(MemScoreActivity.this,StartMemoryActivity.class);
		    	intent.putExtras(extras);          
		    	startActivity(intent);
				finish();
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mem_score, menu);
        return true;
    }
}
