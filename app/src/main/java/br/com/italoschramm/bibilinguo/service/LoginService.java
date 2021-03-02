package br.com.italoschramm.bibilinguo.service;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.com.italoschramm.bibilinguo.client.RequestClient;
import br.com.italoschramm.bibilinguo.client.RequestClientInter;
import br.com.italoschramm.bibilinguo.client.RequestClientLoginInter;
import br.com.italoschramm.bibilinguo.config.ServerClient;
import br.com.italoschramm.bibilinguo.model.rest.Login;
import br.com.italoschramm.bibilinguo.model.rest.Token;
import br.com.italoschramm.bibilinguo.util.Erro;

public class LoginService implements RequestClientInter{

    private RequestClient request;
    private Context context;
    public Erro erros = new Erro();
    private Token token;
    private String pictureProfile;
    private WeakReference<RequestClientLoginInter> mCallBack;

    public LoginService(Context context){
        this.context = context;
        this.mCallBack = new WeakReference(context);
    }

    public void login(Login login){
        Gson gson = new Gson();
        request = new RequestClient(LoginService.this);
        String jsonString = gson.toJson(login);
        request.requestPost(jsonString, ServerClient.URL_API + ServerClient.LOGIN, context);
    }

    public void getImageProfile(Long id){
        request = new RequestClient(LoginService.this);
        request.requestGetString(ServerClient.URL_API + ServerClient.USER_GETIMAGE + id, context);
    }

    @Override
    public void onTaskDone(JSONObject jsonObject) {
        if(request.erros.isHasErro()) {
            erros = request.erros;
            callBack();
            return;
        }   else{

            Gson gson = new Gson();
            token = gson.fromJson(jsonObject.toString(), Token.class);
            callBack();

        }
    }

    @Override
    public void onTaskDone(String result) {
        if(request.erros.isHasErro()) {
            erros = request.erros;
            callBack();
            return;
        }   else{

            pictureProfile = result;
            callBack();

        }
    }

    private void callBack(){
        final RequestClientLoginInter callBack = mCallBack.get();
        if (callBack != null) {
            callBack.onTaskDoneLogin(token);
        }
    }

}
