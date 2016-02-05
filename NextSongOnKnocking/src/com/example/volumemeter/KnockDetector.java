package com.example.volumemeter;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.hardware.SensorManager;

interface Callback{
	void knockEvent();
}


abstract public class KnockDetector implements Callback {
	
	/**
	 * Makes sure that accelerometer event and sound event only triggers a knock event 
	 * if and only if they happen at the same time or very close together in time
	 */
	
	private MainActivity parentActivity;	
	private TimerTask eventGen = null;
	private Timer mTimer = new Timer();
	private final int MaxTimeBetweenEvents = 64;
	private int period = MaxTimeBetweenEvents; 
	
	private AccelSpikeDetector mAccelSpikeDetector;
	private PatternRecognizer mPatt = new PatternRecognizer(this);
	
	abstract void knockDetected(int knockCount);

	KnockDetector(MainActivity parent){
		parentActivity = parent;
	}

	public void init(){
		mAccelSpikeDetector = new AccelSpikeDetector((SensorManager) parentActivity.getSystemService(Context.SENSOR_SERVICE));
		mAccelSpikeDetector.resumeAccSensing();
		mAccelSpikeDetector.registerCallback(this);
	}
	
	public void pause(){
		mAccelSpikeDetector.stopAccSensing();
		mAccelSpikeDetector.unregisterCallback();
	}
	
	public void resume(){
		mAccelSpikeDetector.resumeAccSensing();
		mAccelSpikeDetector.registerCallback(this);
	}
	
	public void knockEvent(){
		mPatt.knockEvent();
	}
}
