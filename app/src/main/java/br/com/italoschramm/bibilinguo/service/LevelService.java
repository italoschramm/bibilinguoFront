package br.com.italoschramm.bibilinguo.service;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.com.italoschramm.bibilinguo.client.RequestClient;
import br.com.italoschramm.bibilinguo.client.RequestClientInter;
import br.com.italoschramm.bibilinguo.client.RequestClientLevelInter;
import br.com.italoschramm.bibilinguo.client.RequestClientLoginInter;
import br.com.italoschramm.bibilinguo.config.ServerClient;
import br.com.italoschramm.bibilinguo.model.rest.Level;
import br.com.italoschramm.bibilinguo.model.rest.Login;
import br.com.italoschramm.bibilinguo.model.rest.Token;
import br.com.italoschramm.bibilinguo.util.Erro;

public class LevelService implements RequestClientInter {

    private RequestClient request;
    private Context context;
    private WeakReference<RequestClientLevelInter> mCallBack;
    public Erro erros = new Erro();
    private Level[] level;

    public LevelService(Context context){
        this.context = context;
        this.mCallBack = new WeakReference(context);
    }


    public void getLevels(String token){
        Gson gson = new Gson();
        request = new RequestClient(LevelService.this);
        String jsonString = "";
        request.requestArray(jsonString, ServerClient.URL_API + ServerClient.USER_GETLEVELS, context, Request.Method.GET, token);
    }


    @Override
    public void onTaskDone(JSONObject jsonObject) {
        if(request.erros.isHasErro()) {
            erros = request.erros;
            callBack();
            return;
        }   else{

            Gson gson = new Gson();
            //level = gson.fromJson(jsonObject.toString(), Level.class);
            callBack();

        }
    }

    @Override
    public void onTaskDone(JSONArray jsonObject) {
        if(request.erros.isHasErro()) {
            erros = request.erros;
            callBack();
            return;
        }   else{

            Gson gson = new Gson();
            level = gson.fromJson(jsonObject.toString(), Level[].class);
            callBack();

        }
    }

    private void callBack(){
        final RequestClientLevelInter callBack = mCallBack.get();
        if (callBack != null) {
            callBack.onTaskDone(level);
        }
    }

}
