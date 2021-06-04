package br.com.italoschramm.bibilinguo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Button btRegister = (Button) findViewById(R.id.btRegister);
        btRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent registerActivity = new Intent(LoadActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        });
    }
}