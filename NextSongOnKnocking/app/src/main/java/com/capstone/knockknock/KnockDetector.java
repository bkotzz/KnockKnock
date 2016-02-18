package com.capstone.knockknock;

import android.content.Context;
import android.hardware.SensorManager;

interface Callback{
	void knockEvent();
}


abstract public class KnockDetector implements Callback {
	
	/**
	 * Manages starting, pausing and resuming the Spike Detector and Pattern Recognizer
	 */
	
	private MainActivity parentActivity;	

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
