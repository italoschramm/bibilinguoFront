package br.com.italoschramm.bibilinguo.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

import br.com.italoschramm.bibilinguo.client.RequestClientInter;
import br.com.italoschramm.bibilinguo.config.ServerConfig;

public class Voice extends Activity implements RecognitionListener {

    private Activity activity;
    public SpeechRecognizer speechRecognizer;
    private Intent intent;
    private boolean setence;
    private String result;
    private WeakReference<VoiceInter> mCallBack;

    public Voice(Activity activity, VoiceInter voice, boolean setence){
        this.activity = activity;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        speechRecognizer.setRecognitionListener(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, Locale.getDefault());
        this.setence = setence;
        this.mCallBack = new WeakReference<>(voice);
    }

    public void startListener(){
        speechRecognizer.startListening(intent);
    }


    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if(setence){
            for (int i = 0; i < data.size(); i++)
            {
                Log.d(ServerConfig.TAG, "result " + data.get(i));
                result += data.get(i);
            }
        }else{
            result = String.valueOf(data.get(0));
        }

        final VoiceInter callBack = mCallBack.get();
        if (callBack != null) {
            callBack.onTaskDone(result);
        }

    }

    public String getResult(){
        return result;
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}
