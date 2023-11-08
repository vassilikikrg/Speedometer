package com.karag.speedometer;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
public class CustomTTS {
    private TextToSpeech tts;
    TextToSpeech.OnInitListener  initListener= status -> {
        if (status==TextToSpeech.SUCCESS){
            //tts.setLanguage(Locale.forLanguageTag("el"));
        }
    };
    public CustomTTS(Context context){
        tts = new TextToSpeech(context,initListener);
    }
    public void speak(String message){
        tts.speak(message,TextToSpeech.QUEUE_ADD,null,null);
    }
}
