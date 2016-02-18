/** By Geir Turtum and Torgeir Lien
 * 
 */

package com.example.volumemeter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;


public class MainActivity extends Activity {

	//The abstract class KnockDetector requires the implementation of void knockDetected(int) method
	KnockDetector mKnockDetector = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(mKnockDetector == null){
			mKnockDetector = new KnockDetector(this){
				@Override
				void knockDetected(int knockCount) {
					switch (knockCount){
						case 1:
							Log.d("knockDetected", "1 knock");
							break;
						case 2:
							Log.d("knockDetected", "2 knock");
							break;
						case 3:
							Log.d("knockDetected", "3 knock");
							break;
						default:
							break;
					}
				}
			};
		}
		mKnockDetector.init();
	}

	public void onBackPressed (){
		super.onBackPressed();
		Log.d("Activity Life", "on invoked");
	}

	public void onResume(){
		super.onResume();
	}

	public void onRestart(){
		super.onRestart();
	}

	public void onStop(){
		Log.d("Activity Life", "onStop invoked");
		super.onStop();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;

	}

}
