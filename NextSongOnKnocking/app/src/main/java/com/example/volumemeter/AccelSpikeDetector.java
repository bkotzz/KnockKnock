package com.example.volumemeter;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import java.lang.Math;

/**
 * Detects spikes in accelerometer data (only in z axis) and generates accelerometer events
 * the volatile spikeDetected boolean will be set when a spike is detected
 */
public class AccelSpikeDetector implements SensorEventListener{		
	
	private SensorManager mSensorManager;

	private Callback callbackMethod;
	
	// Forces are in m/s^2
	final public float minForceZ = 5;
	final public float maxForceX = 5;
	final public float maxForceY = 5;

	// Hint, in microseconds, of desired delay between events
	final public int updatePeriod = 100;

	//For high pass filter
	private float currentZVal = 0;
	private float currentXVal = 0;
	private float currentYVal = 0;

	AccelSpikeDetector(SensorManager sm){
		mSensorManager = sm;
	}

	public void registerCallback(Callback cb){
		callbackMethod = cb;
	}

	public void unregisterCallback(){
		callbackMethod = null;
	}

	public void stopAccSensing(){
		mSensorManager.unregisterListener(this);
	}

	public void resumeAccSensing(){
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), updatePeriod);
	}

	public void onSensorChanged(SensorEvent event) {
		currentXVal = Math.abs(event.values[0]); // X-axis
		currentYVal = Math.abs(event.values[1]); // Y-axis
		currentZVal = Math.abs(event.values[2]); // Z-axis

		// Z force must be above some limit, the other forces below some limit to filter out shaking motions
		if (currentZVal > minForceZ && currentXVal < maxForceX && currentYVal < maxForceY){
			String log = "currXVal:" + currentXVal + " currYVal:" + currentYVal + " currZVal:" + currentZVal;
			Log.d("AccelSpikeDetector", "onSensorChanged " + log);

                    callbackMethod.knockEvent();
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
}
