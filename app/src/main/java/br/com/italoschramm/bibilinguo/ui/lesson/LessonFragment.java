package br.com.italoschramm.bibilinguo.ui.lesson;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import br.com.italoschramm.bibilinguo.LessonActivity;
import br.com.italoschramm.bibilinguo.R;
import br.com.italoschramm.bibilinguo.components.MessageBox;
import br.com.italoschramm.bibilinguo.model.rest.Answer;
import br.com.italoschramm.bibilinguo.model.rest.Question;
import br.com.italoschramm.bibilinguo.util.Util;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LessonFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LessonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;
    private int progress;

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private Question question;
    private MediaPlayer mediaPlayerQuestion;
    private long selected;
    private MessageBox message;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LessonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LessonFragment newInstance(String param1, String param2) {
        LessonFragment fragment = new LessonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LessonFragment(){

    }

    public LessonFragment(Question question, int progress) {

        this.question = question;
        this.progress = progress;
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
        View view = inflater.inflate(R.layout.fragment_lesson, container, false);
        mView = view;
        loadComponents();
        mediaPlayerQuestion.start();
        loadSpeak();
        return view;
    }

    private void loadSpeak(){
        ImageView image = (ImageView) mView.findViewById(R.id.imageLessonSound);
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayerQuestion.start();
            }
        });
    }

    private void loadButton(Question question){
        Button btSend = (Button) mView.findViewById(R.id.buttonLessonSend);
        ImageView imageAnswer = (ImageView) mView.findViewById(R.id.imageAnswerWrongRight);
        RelativeLayout relativeLayout = (RelativeLayout) mView.findViewById(R.id.layoutLessonAnswer);
        TextView textView = (TextView) mView.findViewById(R.id.textLessonReturnAnswer);
        btSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean answerRight = false;
                String filename = null;
                if(question.getRightAnswer().getId() == selected){
                    imageAnswer.setImageResource(R.drawable.backgroundgreen);
                    textView.setText("Resposta correta");
                    filename = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.success;
                    answerRight = true;
                }else{
                    imageAnswer.setImageResource(R.drawable.backgroundred);
                    textView.setText("Resposta errada");
                    filename = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.error;
                }
                relativeLayout.setVisibility(View.VISIBLE);
                try {
                    MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(filename));
                    mediaPlayer.start();
                } catch (Exception e) {
                }

                Button buttonContinue = (Button) mView.findViewById(R.id.buttonAnswerContinue);
                boolean finalAnswerRight = answerRight;
                buttonContinue.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ((LessonActivity)getActivity()).loadNewQuestion(question, finalAnswerRight);
                    }
                });

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

        loadButton(question);

        for(int i = 1; i < 5; i++){
            if(question.getAnswer().size() >= i)
                setImageLesson(question.getAnswer().get(i-1), i);
        }
    }

    private void setImageLesson(Answer answer, int index){
        ImageView image = null;
        switch (index){
            case 1:
                image = mView.findViewById(R.id.imageLesson1);
                break;
            case 2:
                image = mView.findViewById(R.id.imageLesson2);
                break;
            case 3:
                image = mView.findViewById(R.id.imageLesson3);
                break;
            case 4:
                image = mView.findViewById(R.id.imageLesson4);
                break;
        }
        Picasso.with(getContext()).load(answer.getImage()).into(image);
        setAudio(answer, image);
    }

    private void setAudio(Answer answer, ImageView image){

        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(answer.getSound());
                    mediaPlayer.prepare();
                } catch (IOException ignored) {
                }
                mediaPlayer.start();
                selected = answer.getId();
            }
        });
    }
}