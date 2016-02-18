/** By Geir Turtum and Torgeir Lien
 * 
 */

package com.capstone.knockknock;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class MainActivity extends Activity {

	//The abstract class KnockDetector requires the implementation of void knockDetected(int) method
	KnockDetector mKnockDetector = null;
    TextToSpeech mTextToSpeech;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        mTextToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if( TextToSpeech.SUCCESS == status )
                {
                    int result = mTextToSpeech.setLanguage(Locale.US);
                    if( TextToSpeech.LANG_MISSING_DATA  == result || TextToSpeech.LANG_NOT_SUPPORTED == result)
                    {
                        Log.e("TextToSpeech", "This Language is not supported");
                    }
                    else
                    {
                        Log.e("TextToSpeech", "Success");
                    }
                }
                else
                {
                    Log.e("TextToSpeech", "Initialization Failed!");
                }
            }
        });

		if(mKnockDetector == null){
			mKnockDetector = new KnockDetector(this){
				@Override
				void knockDetected(int knockCount) {
					switch (knockCount){
						case 1:
							Log.d("knockDetected", "1 knocks");
                            speakText("1 knock");
							break;
						case 2:
							Log.d("knockDetected", "2 knocks");
                            speakText("2 knocks");
							break;
						case 3:
							Log.d("knockDetected", "3 knocks");
                            speakText("3 knocks");
							break;
						default:
							break;
					}
				}
			};
		}

		mKnockDetector.init();
	}

    public synchronized void speakText(final String text){
        Log.d("speakText", text);
        mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

	public void onBackPressed (){
		super.onBackPressed();
		Log.d("MainActivity", "onBackPressed");
	}

	public void onResume(){
        Log.d("MainActivity", "onResume");
		super.onResume();
	}

	public void onRestart(){
        Log.d("MainActivity", "onRestart");
		super.onRestart();
	}

	public void onStop(){
		Log.d("MainActivity", "onStop");
		super.onStop();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;

	}

}
