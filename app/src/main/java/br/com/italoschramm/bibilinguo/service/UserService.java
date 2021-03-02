package br.com.italoschramm.bibilinguo.service;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.com.italoschramm.bibilinguo.client.RequestClient;
import br.com.italoschramm.bibilinguo.client.RequestClientInter;
import br.com.italoschramm.bibilinguo.client.RequestClientRegisterUserInter;
import br.com.italoschramm.bibilinguo.config.ServerClient;
import br.com.italoschramm.bibilinguo.model.rest.UserRegister;
import br.com.italoschramm.bibilinguo.model.rest.UserReturn;
import br.com.italoschramm.bibilinguo.util.Erro;

public class UserService implements RequestClientInter {

    private RequestClient request;
    private UserReturn user;
    private Context context;
    public Erro erros = new Erro();
    private WeakReference<RequestClientRegisterUserInter> mCallBack;

    public UserService(Context context){
        this.context = context;
        this.mCallBack = new WeakReference(context);
    }

    public void register(UserRegister user){
        Gson gson = new Gson();
        String jsonString = gson.toJson(user);

        String url = ServerClient.URL_API + ServerClient.USER_REGISTER;
        request = new RequestClient(UserService.this);
        request.requestPost(jsonString, url, context);
    }

    @Override
    public void onTaskDone(JSONObject jsonObject) {
        if(request.erros.isHasErro()) {
            erros = request.erros;
            callBack();
            return;
        }   else{

            Gson gson = new Gson();
            user = gson.fromJson(jsonObject.toString(), UserReturn.class);
            callBack();
        }
    }

    @Override
    public void onTaskDone(String result) {

    }

    private void callBack(){
        final RequestClientRegisterUserInter callBack = mCallBack.get();
        if (callBack != null) {
            callBack.onTaskDoneRegister(user);
        }
    }
}
