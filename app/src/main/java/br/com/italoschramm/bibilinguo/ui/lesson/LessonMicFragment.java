package br.com.italoschramm.bibilinguo.ui.lesson;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import br.com.italoschramm.bibilinguo.LessonActivity;
import br.com.italoschramm.bibilinguo.R;
import br.com.italoschramm.bibilinguo.config.ServerConfig;
import br.com.italoschramm.bibilinguo.model.rest.Answer;
import br.com.italoschramm.bibilinguo.model.rest.Question;
import br.com.italoschramm.bibilinguo.util.Voice;
import br.com.italoschramm.bibilinguo.util.VoiceInter;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LessonMicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LessonMicFragment extends Fragment implements VoiceInter {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Question question;
    private View mView;
    private MediaPlayer mediaPlayerQuestion;
    private MediaPlayer mediaPlayerAnswer;
    private MediaRecorder mediaMic;
    private int qtyErros = 0;
    private long selected;
    private boolean micActive;
    private String fileNameMic;
    public static final int RECORD_AUDIO = 0;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private int progress;

    public LessonMicFragment(){

    }

    public LessonMicFragment(Question question, int progress) {
        // Required empty public constructor
        this.question = question;
        this.progress = progress;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LessonMicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LessonMicFragment newInstance(String param1, String param2) {
        LessonMicFragment fragment = new LessonMicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lesson_mic, container, false);
        mView = view;
        loadComponents();
        mediaPlayerQuestion.start();
        loadSpeak();
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        loadMic();
        return view;
    }

    /*
    private void loadMic(){
            ImageView imageMic = (ImageView) mView.findViewById(R.id.imageLessonMic);
            imageMic.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (hasPermission()) {
                        if (!micActive) {
                            try {
                                    mediaMic = new MediaRecorder();;
                                    mediaMic.setAudioSource(MediaRecorder.AudioSource.MIC);
                                    mediaMic.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                                    mediaMic.setOutputFile(getFileNameMic());
                                    mediaMic.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                                mediaMic.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mediaMic.start();
                            micActive = true;
                        } else {
                            mediaMic.stop();
                            micActive = false;
                        }
                    }
                }
            });
    }

     */

    private void loadMic(){
        ImageView imageMic = (ImageView) mView.findViewById(R.id.imageLessonMic);
        imageMic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (hasPermission()) {
                    Voice voice = new Voice(getActivity(), LessonMicFragment.this::onTaskDone,false);
                    voice.startListener();
                }
            }
        });
    }



    /*
    private String getFileNameMic() {
            fileNameMic = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mic";
            File dir = new File(fileNameMic);
            if (!dir.exists())
                dir.mkdirs();
            return fileNameMic + "/mic.mp4";

    }

     */

    private boolean hasPermission(){
        if ((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(getActivity(), permissions, RECORD_AUDIO);
            return false;
        } else {
            return true;
        }
    }

    private void loadSpeak(){
        ImageView image = (ImageView) mView.findViewById(R.id.imageLessonSound);
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayerQuestion.start();
            }
        });
    }

    private void loadComponents(){

        ProgressBar progressBar = (ProgressBar) mView.findViewById(R.id.lessonProgressBar);
        progressBar.setProgress(progress);

        TextView textQuestion = mView.findViewById(R.id.textLessonQuestion);
        textQuestion.setText(question.getDescription());

        mediaPlayerQuestion = new MediaPlayer();
        try {
            mediaPlayerQuestion.setDataSource(question.getSound());
            mediaPlayerQuestion.prepare();
        } catch (IOException ignored) {
        }

        for(int i = 1; i < 5; i++){
            if(question.getAnswer().size() >= i)
                setImageLesson(question.getAnswer().get(i-1), i);
        }
    }

    private void setImageLesson(Answer answer, int index){
        ImageView image = mView.findViewById(R.id.imageAnswerMic);
        Picasso.with(getContext()).load(answer.getImage()).into(image);
        setAudio(answer);
    }

    private void setAudio(Answer answer){
        ImageView image = (ImageView) mView.findViewById(R.id.imageAnswerMic);
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayerAnswer = new MediaPlayer();
                try {
                    mediaPlayerAnswer.setDataSource(answer.getSound());
                    mediaPlayerAnswer.prepare();
                } catch (IOException ignored) {
                }
                mediaPlayerAnswer.start();
                selected = answer.getId();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) getActivity().finish();

    }

    @Override
    public void onTaskDone(String result) {
        String filename = "";
        boolean newQuestion = false;
        Button button = (Button) mView.findViewById(R.id.buttonAnswerContinue);
        ImageView imageAnswer = (ImageView) mView.findViewById(R.id.imageAnswerWrongRight);
        RelativeLayout relativeLayout = (RelativeLayout) mView.findViewById(R.id.layoutLessonAnswer);
        TextView textView = (TextView) mView.findViewById(R.id.textLessonReturnAnswer);
            if(question.getRightAnswer().getDescription().equalsIgnoreCase(result)){
                imageAnswer.setImageResource(R.drawable.backgroundgreen);
                textView.setText("Resposta correta");
                button.setText("Continuar");
                filename = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.success;
                newQuestion = true;
            }else if(qtyErros < 3){
                imageAnswer.setImageResource(R.drawable.backgroundorange);
                textView.setText("Não conseguimos entender. Tente novamente");
                button.setText("Tentar Novamente");
                qtyErros++;
                filename = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.error;
                newQuestion = false;
            }else{
                imageAnswer.setImageResource(R.drawable.backgroundred);
                textView.setText("Não conseguimos entender. Vamos continuar");
                button.setText("Continuar");
            newQuestion = true;
            }

        relativeLayout.setVisibility(View.VISIBLE);
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(filename));
            mediaPlayer.start();
        } catch (Exception e) {
        }


        Button buttonContinue = (Button) mView.findViewById(R.id.buttonAnswerContinue);
        boolean finalNewQuestion = newQuestion;
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(finalNewQuestion)
                    ((LessonActivity)getActivity()).loadNewQuestion(question, true);
                else
                    relativeLayout.setVisibility(View.GONE);
            }
        });

    }
}