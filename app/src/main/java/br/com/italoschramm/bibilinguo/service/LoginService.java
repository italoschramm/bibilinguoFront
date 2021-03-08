package br.com.italoschramm.bibilinguo.service;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.com.italoschramm.bibilinguo.client.RequestClient;
import br.com.italoschramm.bibilinguo.client.RequestClientInter;
import br.com.italoschramm.bibilinguo.client.RequestClientLoginInter;
import br.com.italoschramm.bibilinguo.config.ServerClient;
import br.com.italoschramm.bibilinguo.model.User;
import br.com.italoschramm.bibilinguo.model.rest.Login;
import br.com.italoschramm.bibilinguo.model.rest.Token;
import br.com.italoschramm.bibilinguo.util.Erro;

public class LoginService implements RequestClientInter{

    private RequestClient request;
    private Context context;
    public Erro erros = new Erro();
    private Token token;
    private User user = new User();
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
        request.request(jsonString, ServerClient.URL_API + ServerClient.LOGIN, context, Request.Method.POST);
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


            user.setId(token.getUser().getId());
            user.setName(token.getUser().getName());
            user.setEmail(token.getUser().getEmail());
            user.setActive(token.getUser().isActive());
            user.setImageProfile(token.getUser().getUserImage());

            callBack();

        }
    }

    @Override
    public void onTaskDone(JSONArray jsonObject) {

    }

    private void callBack(){
        final RequestClientLoginInter callBack = mCallBack.get();
        if (callBack != null) {
            callBack.onTaskDoneLogin(token, user);
        }
    }

}
