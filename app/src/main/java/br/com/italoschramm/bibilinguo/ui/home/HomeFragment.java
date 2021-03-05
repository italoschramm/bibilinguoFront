package br.com.italoschramm.bibilinguo.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.italoschramm.bibilinguo.LessonActivity;
import br.com.italoschramm.bibilinguo.LoginActivity;
import br.com.italoschramm.bibilinguo.R;
import br.com.italoschramm.bibilinguo.client.RequestClientQuestionInter;
import br.com.italoschramm.bibilinguo.components.MessageBox;
import br.com.italoschramm.bibilinguo.config.ServerConfig;
import br.com.italoschramm.bibilinguo.model.User;
import br.com.italoschramm.bibilinguo.model.rest.Level;
import br.com.italoschramm.bibilinguo.model.rest.Question;
import br.com.italoschramm.bibilinguo.model.rest.Subject;
import br.com.italoschramm.bibilinguo.service.LoginService;
import br.com.italoschramm.bibilinguo.service.QuestionService;

public class HomeFragment extends Fragment implements RequestClientQuestionInter {

    private HomeViewModel homeViewModel;

    public User user;

    private String token;

    private Level[] level;

    private QuestionService questionService;

    private MessageBox message;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        token = getActivity().getIntent().getExtras().getString("Token");
        loadSubjects(root);

        //final TextView textView = root.findViewById(R.id.text_home);
        //homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
        //    @Override
        //    public void onChanged(@Nullable String s) {
        //        textView.setText(s);
        //    }
        //});
        return root;
    }

    private void loadSubjects(View root){
        Parcelable[] parceble = getActivity().getIntent().getExtras().getParcelableArray("Level");
        if(parceble != null)
            level = Arrays.copyOf(parceble, parceble.length, Level[].class);

        loadComponents(root, level);
    }

    private void loadComponents(View root, Level[] levelArray){

        int i = 1;
        boolean first = true;
        for(Level level: levelArray){
            if(level.getSubject() == null)
                return;
            for(Subject subject : level.getSubject()){
                if(first) {
                    generateLayout(root, subject);
                    first = false;
                }else {
                    generateCopyLayout(root, subject, i);
                    i++;
                }
           }
        }
    }

    private void generateLayout(View root, Subject subject){
        TextView textView = (TextView) root.findViewById(R.id.textLessonHome);
        textView.setText(subject.getDescription());

        ImageView imageView = (ImageView) root.findViewById(R.id.imageLessonHome);
        Context context = imageView.getContext();
        Picasso.with(context).load(subject.getImage()).into(imageView);

        LinearLayout line = (LinearLayout) root.findViewById(R.id.linearLayoutLine);
        loadActionClick(line, subject.getId());
    }

    private void generateCopyLayout(View root, Subject subject, int index){
        LinearLayout main = (LinearLayout)root.findViewById(R.id.layoutMainHomeSub);
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        ConstraintLayout mainInflater = (ConstraintLayout) inflater.inflate(R.layout.fragment_home, null, false);
        LinearLayout line = mainInflater.findViewById(R.id.linearLayoutLine);
        if(line.getParent() != null)
            ((ViewGroup)line.getParent()).removeView(line);

        TextView textLesson = line.findViewById(R.id.textLessonHome);
        textLesson.setText(subject.getDescription());

        ImageView imageView = line.findViewById(R.id.imageLessonHome);
        Context context = imageView.getContext();
        Picasso.with(context).load(subject.getImage()).into(imageView);

        main.addView(line, index);
        loadActionClick(line, subject.getId());
    }

    private void loadActionClick(LinearLayout line, long idSubject){
        ImageView imgLesson = (ImageView) line.findViewById(R.id.imageLessonHome);
        imgLesson.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getQuestions(idSubject);
            }
        });
    }

    public void getQuestions(long idQuestion){
        questionService = new QuestionService(this);
        questionService.getQuestionsBySubject(token, idQuestion);
    }

    @Override
    public void onTaskDone(Question[] questions) {
        if(questionService.erros.isHasErro()) {
            message = new MessageBox(this.getContext());
            message.generateAlert(questionService.erros.getMessage(), "Erro");
        }else {
            Intent registerActivity = new Intent(getActivity(), LessonActivity.class);
            registerActivity.putExtra("Questions", questions);
            startActivity(registerActivity);
        }

    }
}