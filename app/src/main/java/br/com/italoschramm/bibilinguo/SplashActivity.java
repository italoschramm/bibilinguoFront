package br.com.italoschramm.bibilinguo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.Map;

import br.com.italoschramm.bibilinguo.client.RequestClientLevelInter;
import br.com.italoschramm.bibilinguo.client.RequestClientLoginInter;
import br.com.italoschramm.bibilinguo.components.MessageBox;
import br.com.italoschramm.bibilinguo.config.ServerConfig;
import br.com.italoschramm.bibilinguo.contract.LoginDbHelper;
import br.com.italoschramm.bibilinguo.model.User;
import br.com.italoschramm.bibilinguo.model.rest.Level;
import br.com.italoschramm.bibilinguo.model.rest.Login;
import br.com.italoschramm.bibilinguo.model.rest.Token;
import br.com.italoschramm.bibilinguo.service.LevelService;
import br.com.italoschramm.bibilinguo.service.LoginService;

public class SplashActivity extends AppCompatActivity implements RequestClientLoginInter, RequestClientLevelInter {

    private LoginService loginService;
    private LevelService levelService;
    private MessageBox message;
    private String tokenCod;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginDbHelper data = new LoginDbHelper(SplashActivity.this);
                Map<String,String> loginPassword = data.existsData();
                if(loginPassword.size() > 0){
                    Login login = new Login();
                    for(Map.Entry<String, String> result : loginPassword.entrySet()) {
                        login.setUsername(result.getKey());
                        login.setPassword(result.getValue());
                    }
                    loginService = new LoginService(SplashActivity.this);
                    loginService.login(login);
                }else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        }, 2000);

    }

    @Override
    public void onTaskDoneLogin(Token token, User user) {
        if(loginService.erros.isHasErro()){
            message = new MessageBox(this);
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            message.generateAlertWithOkNewIntent(loginService.erros.getMessage(), "Erro", intent, SplashActivity.this);
        }else{
            tokenCod = token.getToken();
            this.user = user;

            levelService = new LevelService(SplashActivity.this);
            levelService.getLevels(token.getToken());
        }
    }

    @Override
    public void onTaskDone(Level[] level) {
        if(levelService.erros.isHasErro()) {
            message = new MessageBox(this);
            message.generateAlert(levelService.erros.getMessage(), "Erro");
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            Intent mainActivity = new Intent(SplashActivity.this, MainActivity.class);
            mainActivity.putExtra("User", user);
            mainActivity.putExtra("Level", level);
            mainActivity.putExtra("Token", tokenCod);
            startActivity(mainActivity);
        }
    }
}