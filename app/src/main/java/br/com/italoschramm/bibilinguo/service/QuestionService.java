package br.com.italoschramm.bibilinguo.service;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.com.italoschramm.bibilinguo.client.RequestClient;
import br.com.italoschramm.bibilinguo.client.RequestClientInter;
import br.com.italoschramm.bibilinguo.client.RequestClientQuestionInter;
import br.com.italoschramm.bibilinguo.config.ServerClient;
import br.com.italoschramm.bibilinguo.config.ServerConfig;
import br.com.italoschramm.bibilinguo.model.rest.Question;
import br.com.italoschramm.bibilinguo.util.Erro;

public class QuestionService implements RequestClientInter {

    private RequestClient request;
    private Context context;
    private WeakReference<RequestClientQuestionInter> mCallBack;
    public Erro erros = new Erro();
    private Question[] questions;

    public QuestionService(Fragment fragment){
        this.context = fragment.getContext();
        this.mCallBack = new WeakReference(fragment);
    }

    public void getQuestionsBySubject(String token, long idSubject){
        Gson gson = new Gson();
        request = new RequestClient(QuestionService.this);
        String jsonString = "";
        request.requestArray(jsonString, ServerClient.URL_API + ServerClient.GETQUESTIONSBYSUBJECT + idSubject, context, Request.Method.GET, token);
    }

    @Override
    public void onTaskDone(JSONObject jsonObject) {

    }

    @Override
    public void onTaskDone(JSONArray jsonObject) {
        if(request.erros.isHasErro()) {
            erros = request.erros;
            callBack();
            return;
        }   else{

            Gson gson = new Gson();
            Log.d(ServerConfig.TAG, gson.toString());
            questions = gson.fromJson(jsonObject.toString(), Question[].class);
            callBack();

        }
    }

    private void callBack(){
        final RequestClientQuestionInter callBack = mCallBack.get();
        if (callBack != null) {
            callBack.onTaskDone(questions);
        }
    }
}
