package com.example.android_speech_to_text;

import java.util.ArrayList;
import com.example.android_speech_to_text.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
 
public class MainActivity extends Activity {
 
    private TextView txtSpeechInput;
    private ScrollView scrollInput;
    private ImageButton btnSpeak;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private String finaltxt = "";
    private boolean firstStart = true;
    private boolean isListening = false;
    private String ready = "Spracherkennung aktiv... ";
    private String finalPartialTxt = "";

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInputField);
        scrollInput = (ScrollView) findViewById(R.id.scrollView1);
        txtSpeechInput.setMovementMethod(new ScrollingMovementMethod());
        
        scrollInput = (ScrollView) findViewById(R.id.scrollView1);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
     
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                         RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                                         this.getPackageName());
 
        btnSpeak.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                
            	if(!isListening){
            		startRecognition();
            		isListening = true;
            	}
            	else if(isListening){
                	mSpeechRecognizer.stopListening();
                	mSpeechRecognizer.destroy();
            		isListening = false;
            		firstStart = true;
            		finaltxt = "";
            		txtSpeechInput.setText("Spracherkennung Ende.");
            		ready = "Spracherkennung aktiv... ";
            	}
            }
        });
 
    }
    
    public void startRecognition(){
		SpeechRecognitionListener listener = new SpeechRecognitionListener(); 
		mSpeechRecognizer.setRecognitionListener(listener);
		mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }
    
    public void stopRecognition(ArrayList<String> text){
    	finaltxt = finaltxt.concat(text.get(0));
    	finaltxt = finaltxt.concat(" ");
    	txtSpeechInput.setText(finaltxt);
    	scrollInput.fullScroll(View.FOCUS_DOWN);
        txtSpeechInput.append("\n");
        scrollInput.post(new Runnable()
        {
        	public void run(){
        	scrollInput.fullScroll(View.FOCUS_DOWN);	
        	}
        });
        
    	mSpeechRecognizer.stopListening();
    	mSpeechRecognizer.destroy();
    	ready = "";
    	startRecognition();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {   }

        @Override
        public void onBufferReceived(byte[] buffer)
        {   }

        @Override
        public void onEndOfSpeech()
        {   }
        
        @Override
        public void onError(int error)
        {
        	startRecognition();
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {   }

        @Override
        public void onPartialResults(Bundle partialResults)
        {
            ArrayList<String> text = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            finalPartialTxt = finaltxt.concat(text.get(0));
        	finalPartialTxt = finalPartialTxt.concat(" ");
            txtSpeechInput.setText(finalPartialTxt);
        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
        	if(firstStart){
        	txtSpeechInput.setText(ready);
        	firstStart = false;
        	}
        }

        @Override
        public void onResults(Bundle results)
        {
            ArrayList<String> text = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            stopRecognition(text);
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {   }
        
    }
    
}
