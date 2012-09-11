package com.nennig.photomem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mem {
	//Variables for Bundles
	public static final String CURRENT_MEM = "com.nennig.photomem.CURRENT_MEM";
	public static final String RANDOMIZE = "com.nennig.photomem.RANDOMIZE";
	public static final String MEM_STATS = "com.nennig.photomem.MEM_STATS";
	
	//Variables for Shared Preferences
	public static final String MY_PREFS = "myPrefs";
	public static final String MEM_NAME = "MemName";
	public static final String DESCRIPTION = "Description";
	public static final String TIME_CREATED = "TimeCreated";
	public static final String TIMES_COMPLETED = "TimesMemCompleted";
	public static final String COMPLETION_RATE = "CompletionRate";
	public static final String PERFECT_MEM = "PerfectMem";
	
	//Variables native to this class
	private String _name;
	private String _timeCreated;
	private int _timesMemCompleted;
	private double _completionRate;
	private boolean _randomize;
	
	public Mem(String name){
		_name = name;
	}
	
	public void create(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = new Date();
		
		_timeCreated = dateFormat.format(date);
		_timesMemCompleted = 0;
		_completionRate = 0;
		_randomize = false;
	}
	
	public void recreate(String timeCreated, int timesMemCompleted, double completionRate, boolean randomize){
		_timeCreated = timeCreated;
		_timesMemCompleted = timesMemCompleted;
		_completionRate = completionRate;
		_randomize = randomize;
	}
	
	public String getName(){
		return _name;
	}
	public String getTimeCreated(){
		return _timeCreated;
	}
	public int getTimesMemCompleted(){
		return _timesMemCompleted;
	}
	public double getCompletionRate(){
		return _completionRate;
	}
	public boolean getRandomize(){
		return _randomize;
	}
	
	public static String[] getMemColumns(){
		String[] fields = {MEM_NAME, TIME_CREATED, TIMES_COMPLETED, COMPLETION_RATE, RANDOMIZE};
		return fields;
	}
	
	public void incrementMemCompleted(double newCompletionRate){
		double oldAverage = (_completionRate*_timesMemCompleted);
		_completionRate = (oldAverage + newCompletionRate) / (_timesMemCompleted + 1);
		_timesMemCompleted++;
	}
	
	public void setRandomize(boolean val){
		_randomize = val;
	}
}
