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
import br.com.italoschramm.bibilinguo.ui.lesson.LessonFragment;

public class LessonActivity extends AppCompatActivity {

    private List<Question> questionsList = new ArrayList<Question>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
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

    }

    private void loadQuestion(){
        if(questionsList.size() != 0)
            loadFragment(questionsList.get(0));
    }

    private void loadFragment(Question question){
        LessonFragment fragmentLesson = new LessonFragment(question);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction  = manager.beginTransaction();
        transaction.add(R.id.layoutActivityLesson,fragmentLesson,"FragLesson");
        transaction.commit();
    }


}