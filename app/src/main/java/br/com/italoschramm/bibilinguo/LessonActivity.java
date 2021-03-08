package br.com.italoschramm.bibilinguo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.italoschramm.bibilinguo.config.ServerConfig;
import br.com.italoschramm.bibilinguo.model.rest.Answer;
import br.com.italoschramm.bibilinguo.model.rest.Level;
import br.com.italoschramm.bibilinguo.model.rest.Question;
import br.com.italoschramm.bibilinguo.ui.lesson.LessonFinishFragment;
import br.com.italoschramm.bibilinguo.ui.lesson.LessonFragment;
import br.com.italoschramm.bibilinguo.ui.lesson.LessonMicFragment;

public class LessonActivity extends AppCompatActivity {

    private List<Question> questionsList = new ArrayList<Question>();
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private int progressTotal;
    private int progressIncrement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        manager = getSupportFragmentManager();
        transaction  = manager.beginTransaction();
        //LinearLayout layoutMain = (LinearLayout) findViewById(R.id.layoutMainLesson);
        loadParcebleQuestions();
        loadQuestion();

    }

    private void loadParcebleQuestions(){
        Parcelable[] parceble = getIntent().getExtras().getParcelableArray("Questions");
        Question[] questionsArray = null;
        if(parceble != null)
            questionsArray = Arrays.copyOf(parceble, parceble.length, Question[].class);

        for(Question question : questionsArray){
            questionsList.add(question);
        }
        progressIncrement = Math.round(100/(questionsList.size() + 1));
        progressTotal = progressIncrement;

    }

    public void loadNewQuestion(Question questionReturn, boolean remove){
        if(remove) {
            for (Question question : questionsList) {
                if (question.getId() == questionReturn.getId()) {
                    questionsList.remove(question);
                    progressTotal = progressTotal + progressIncrement;
                    break;
                }
            }
            if(questionsList.size() >= 1)
                loadFragment(questionsList.get(0));
            else
                loadFragment(null);
        }else
            if(questionsList.size() >= 2) {
                loadFragment(questionsList.get(1));
                changeList(questionsList.get(1));
            }
            else if(questionsList.size() >= 1) {
                loadFragment(questionsList.get(0));
                changeList(questionsList.get(0));
            }

    }

    @Override
    public void finishAndRemoveTask() {
        super.finishAndRemoveTask();
    }

    private void changeList(Question questionGet){
        for (Question question : questionsList) {
            if (question.getId() == questionGet.getId()) {
                questionsList.remove(question);
                questionsList.add(question);
                break;
            }
        }

    }

    private void loadQuestion(){
        if(questionsList.size() != 0)
            loadFragment(questionsList.get(0));
    }

    private void loadFragment(Question question){

        if(question != null) {
            if (question.getTypeQuestion().getId() == 1) {
                LessonFragment fragmentLesson = new LessonFragment(question, Math.round(progressTotal));
                loadTransaction(fragmentLesson);
            } else if (question.getTypeQuestion().getId() == 2) {
                LessonMicFragment fragmentLesson = new LessonMicFragment(question, Math.round(progressTotal));
                loadTransaction(fragmentLesson);
            }
        }else {
            LessonFinishFragment fragmentLesson = new LessonFinishFragment();
            loadTransaction(fragmentLesson);
        }
    }

    private void loadTransaction(Fragment fragment){
        if(transaction == null)
            transaction  = manager.beginTransaction();

        if(!transaction.isEmpty()) {
            transaction  = manager.beginTransaction();
            transaction.remove(getSupportFragmentManager().findFragmentByTag("FragLesson")).commit();
            transaction  = manager.beginTransaction();
        }
        transaction.add(R.id.layoutActivityLesson,fragment,"FragLesson");
        transaction.commit();
    }

}