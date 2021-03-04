package br.com.italoschramm.bibilinguo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import br.com.italoschramm.bibilinguo.client.RequestClient;
import br.com.italoschramm.bibilinguo.client.RequestClientLevelInter;
import br.com.italoschramm.bibilinguo.client.RequestClientLoginInter;
import br.com.italoschramm.bibilinguo.components.MessageBox;
import br.com.italoschramm.bibilinguo.config.ServerConfig;
import br.com.italoschramm.bibilinguo.model.User;
import br.com.italoschramm.bibilinguo.model.rest.Level;
import br.com.italoschramm.bibilinguo.model.rest.Login;
import br.com.italoschramm.bibilinguo.model.rest.Token;
import br.com.italoschramm.bibilinguo.service.LevelService;
import br.com.italoschramm.bibilinguo.service.LoginService;

public class LoginActivity extends AppCompatActivity implements RequestClientLoginInter, RequestClientLevelInter {

    private RequestClient request;
    private LoginService loginService;
    private LevelService levelService;
    private MessageBox message;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btRegister = (Button) findViewById(R.id.btRegister);
        btRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        });

        Button btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login(){
        EditText edLogin = (EditText)findViewById(R.id.edLogin);
        EditText edPassword = (EditText)findViewById(R.id.edPassword);
        Login login = new Login();
        login.setUsername(edLogin.getText().toString());
        login.setPassword(edPassword.getText().toString());
        login(login);
    }

    public void login(Login login){
        loginService = new LoginService(LoginActivity.this);
        loginService.login(login);
    }

    @Override
    public void onTaskDoneLogin(Token token) {
        if(loginService.erros.isHasErro()){
            message = new MessageBox(this);
            message.generateAlert(loginService.erros.getMessage(), "Erro");
        }else{
            user.setId(token.getUser().getId());
            user.setName(token.getUser().getName());
            user.setEmail(token.getUser().getEmail());
            user.setActive(token.getUser().isActive());
            user.setImageProfile(token.getUser().getUserImage());

            levelService = new LevelService(LoginActivity.this);
            levelService.getLevels(token.getToken());
        }
    }

    @Override
    public void onTaskDone(Level[] level) {
        if(levelService.erros.isHasErro()) {
            message = new MessageBox(this);
            message.generateAlert(levelService.erros.getMessage(), "Erro");
        }else{
                Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                mainActivity.putExtra("User", user);
                mainActivity.putExtra("Level", level);
                startActivity(mainActivity);
        }
    }
}