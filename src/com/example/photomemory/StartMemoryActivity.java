package com.example.photomemory;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class StartMemoryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_memory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_start_memory, menu);
        return true;
    }
}
