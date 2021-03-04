package br.com.italoschramm.bibilinguo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.italoschramm.bibilinguo.client.RequestClient;
import br.com.italoschramm.bibilinguo.client.RequestClientLoginInter;
import br.com.italoschramm.bibilinguo.client.RequestClientRegisterUserInter;
import br.com.italoschramm.bibilinguo.components.MessageBox;
import br.com.italoschramm.bibilinguo.config.ServerConfig;
import br.com.italoschramm.bibilinguo.model.User;
import br.com.italoschramm.bibilinguo.model.rest.Login;
import br.com.italoschramm.bibilinguo.model.rest.Token;
import br.com.italoschramm.bibilinguo.model.rest.UserRegister;
import br.com.italoschramm.bibilinguo.model.rest.UserReturn;
import br.com.italoschramm.bibilinguo.service.LoginService;
import br.com.italoschramm.bibilinguo.service.UserService;
import br.com.italoschramm.bibilinguo.util.Util;

public class RegisterActivity extends AppCompatActivity implements RequestClientRegisterUserInter, RequestClientLoginInter {

    private String name;
    private String email;
    private String password1;
    private String password2;

    private RequestClient request;
    private LoginService loginService;
    private UserService userService;
    private MessageBox message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button btnRegister = (Button) findViewById(R.id.btSaveRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveRegister();
            }
        });
    }

    private void saveRegister(){
        UserRegister user = new UserRegister();

        EditText edTxtName = (EditText)findViewById(R.id.edName);
        EditText edTxtEmail = (EditText)findViewById(R.id.edEmail);
        EditText edTxtPassword1 = (EditText)findViewById(R.id.edPassword1);
        EditText edTxtPassword2 = (EditText)findViewById(R.id.edPassword2);

        name = edTxtName.getText().toString();
        email = edTxtEmail.getText().toString();
        password1 = edTxtPassword1.getText().toString();
        password2 = edTxtPassword2.getText().toString();

        if(!validators()){
            return;
        }else{
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password1);
            user.setActive(true);

            userService = new UserService(this);
            userService.register(user);
        }
    }

    private boolean validators() {
        message = new MessageBox(this);
        Log.d(ServerConfig.TAG, String.valueOf(password1.length()));
        if(name.length() < 3) {
            message.generateAlert("Nome inválido!", "Erro");
            return false;
        }else if (!password1.equals(password2)) {
            message.generateAlert("Senhas não são iguais", "Erro");
            return false;
        }else if(password1.length() < 6) {
            message.generateAlert("A senha precisa ter no mínimo 6 caracteres.", "Erro");
            return false;
        }else if(!Util.isValidEmailAddress(email)){
            message.generateAlert("E-mail inválido!", "Erro");
            return false;
        }else{
            return true;
        }

    }

    @Override
    public void onTaskDoneLogin(Token token) {
        if(loginService.erros.isHasErro()){
            message = new MessageBox(this);
            message.generateAlert(loginService.erros.getMessage(), "Erro");
        }else{
            User user = new User();
            user.setName(token.getUser().getName());
            user.setEmail(token.getUser().getEmail());
            user.setActive(token.getUser().isActive());
            user.setImageProfile(token.getUser().getUserImage());

            Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
            mainActivity.putExtra("User", user);
            startActivity(mainActivity);
            Log.d(ServerConfig.TAG, token.getToken());
        }
    }

    @Override
    public void onTaskDoneRegister(UserReturn userReturn) {
        if(userService.erros.isHasErro())
            message.generateAlert(request.erros.getMessage(), "Erro");
        else {
            Login login = new Login();
            login.setUsername(userReturn.getEmail());
            login.setPassword(password1);

            loginService = new LoginService(RegisterActivity.this);
            loginService.login(login);
        }
    }
}