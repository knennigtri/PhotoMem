package com.nennig.photomem;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MemScoreActivity extends Activity {
	private  boolean _randomize;
    private String _current_mem;
    private String[] _stats;
    private static final String TAG = "MemScoreActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_score);
        
        _current_mem = getIntent().getExtras().getString(Mem.CURRENT_MEM);
        _stats = getIntent().getExtras().getStringArray(Mem.MEM_STATS);
        _randomize = getIntent().getBooleanExtra(Mem.RANDOMIZE, false);
        SavePreferences(_current_mem, Float.valueOf(_stats[2]));
        
        TextView statOne = (TextView) findViewById(R.id.mem_score_one);
        statOne.setText("Results for " + _current_mem + " Mem:");
        
        TextView statTwo = (TextView) findViewById(R.id.mem_score_two);
        statTwo.setText("Memorized: " + _stats[0]);
        
        TextView statThree = (TextView) findViewById(R.id.mem_score_three);
        statThree.setText("Still need work: " + _stats[1]);
        
        TextView statFour = (TextView) findViewById(R.id.mem_score_four);
        statFour.setText("Mem Completion: " + _stats[2] + "%");
        
        Button backButton = (Button) findViewById(R.id.mem_score_button);
        backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
		     	Intent intent = new Intent(MemScoreActivity.this,StartMemoryActivity.class);
		    	intent.putExtra(Mem.CURRENT_MEM, getIntent().getStringExtra(Mem.CURRENT_MEM));      
		    	startActivity(intent);
				finish();
			}
        	
        });
    }
    
    private void SavePreferences(String name, float newCompletionRatePart){
    	SharedPreferences settings = getSharedPreferences(Mem.MY_PREFS,MODE_PRIVATE);
    	SharedPreferences.Editor e = settings.edit();
		
		float curCompletionRate = settings.getFloat(name + "." + Mem.COMPLETION_RATE, 0);
    	int curTimesCompleted = settings.getInt(name + "." + Mem.TIMES_COMPLETED, 0);
		int curPerfectMem = settings.getInt(name + "." + Mem.PERFECT_MEM, 0);
    	
    	float oldAverage = curCompletionRate*curTimesCompleted;
		float newCompletionRate = (oldAverage + newCompletionRatePart) / (curTimesCompleted + 1);
		
		if(newCompletionRatePart == 100)
			e.putInt(name + "." + Mem.PERFECT_MEM, curPerfectMem+1);
		
    	e.putInt(name + "." + Mem.TIMES_COMPLETED, curTimesCompleted + 1);
        e.putFloat(name + "." + Mem.COMPLETION_RATE, newCompletionRate);
        e.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mem_score, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_rate_this:
    		String str ="https://play.google.com/store/apps/details?id=com.nennig.photomem";
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
    public void onSaveInstanceState(Bundle b){
    	b.putString(Mem.CURRENT_MEM, this.getIntent().getExtras().getString(Mem.CURRENT_MEM));
    	b.putBoolean(Mem.RANDOMIZE, this.getIntent().getExtras().getBoolean(Mem.RANDOMIZE));
    	b.putStringArray(Mem.MEM_STATS, getIntent().getStringArrayExtra(Mem.MEM_STATS));
    	super.onSaveInstanceState(b);
    }
    
    public void onRestoreInstanceState(Bundle b) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(b);
       
        _current_mem = b.getString(Mem.CURRENT_MEM);
        _stats = b.getStringArray(Mem.MEM_STATS);
        _randomize = b.getBoolean(Mem.RANDOMIZE);
    }
}

