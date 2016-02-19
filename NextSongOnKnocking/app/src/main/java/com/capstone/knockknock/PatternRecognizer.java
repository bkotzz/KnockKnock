package com.capstone.knockknock;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.lang.Runnable;

import android.util.Log;

public class PatternRecognizer {
	
	final long minWaitTime_ms = 250; // The time-window when knocks will NOT be acknowledged
	final long waitWindow_ms = 1000; // The time-window when knocks WILL be acknowledged, after minWait
	final int MAX_DETECTED = 3;
	
	private ScheduledFuture<?> timerFuture = null ;
	EventGenState_t state = EventGenState_t.Wait;
	ScheduledThreadPoolExecutor mExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
	KnockDetector p = null;
	private int detectedKnockCount = 0;
	
	PatternRecognizer(KnockDetector parent){
		p = parent;
	}
	
	private enum EventGenState_t {
		Wait,
		S1,
		S2, 
		S3,
		S4,
	} 

	Runnable waitTimer = new Runnable(){
		public void run() {
			timeOutEvent();			
		}
	};

	private void startTimer(long timeToWait){
		if (timerFuture != null){
			timerFuture.cancel(false);
		}
		timerFuture = mExecutor.schedule(waitTimer, timeToWait, TimeUnit.MILLISECONDS);
	}

	public void knockEvent(){
		Log.d("PatternRecognizer","knockEvent: " + state);

		switch(state){
		case Wait:
			detectedKnockCount++;
			startTimer(minWaitTime_ms);
			state =  EventGenState_t.S1;
			break;
		case S1:
			// In minWaitTime, ignore knock
			break;
		case S2:
			detectedKnockCount++;
			timerFuture.cancel(false);
			startTimer(minWaitTime_ms);
			state = EventGenState_t.S3;
			break;
		case S3:
			// In minWaitTime, ignore knock
			break;
		case S4:
			detectedKnockCount = Math.min(MAX_DETECTED, detectedKnockCount + 1);
			break;
		default:
			Log.d("PatternRecognizer","knockEvent: Invalid State");
			break;
		}
	}

	
	public void timeOutEvent(){
		Log.d("PatternRecognizer","timeOutEvent");
		switch(state){
		case Wait:
			Log.d("PatternRecognizer","Time out in Wait state");
			break;
		case S1:
			startTimer(waitWindow_ms);
			state = EventGenState_t.S2;
			break;
		case S2:
			p.knockDetected(detectedKnockCount);
			detectedKnockCount = 0;
			state = EventGenState_t.Wait;
			break;
		case S3:
			startTimer(waitWindow_ms);
			state = EventGenState_t.S4;
			break;
		case S4:
			p.knockDetected(detectedKnockCount);
			detectedKnockCount = 0;
			state = EventGenState_t.Wait;
			break;
		default:
			Log.d("PatternRecognizer","timeOutEvent: Invalid state");
			break;
		}
	}
}