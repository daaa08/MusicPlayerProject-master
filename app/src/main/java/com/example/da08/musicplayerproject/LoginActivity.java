package com.example.da08.musicplayerproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.da08.musicplayerproject.util.PermissionControl;

public class LoginActivity extends AppCompatActivity implements PermissionControl.CallBack {

    EditText email,password;
    ImageButton btnSignIn, btnSignUp;
    ImageView imgLogo;

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionControl.checkVersion(this);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        imgLogo = (ImageView)findViewById(R.id.imgLogo);

        btnSignIn = (ImageButton)findViewById(R.id.btnSignIn);
        btnSignUp = (ImageButton)findViewById(R.id.btnSignUp);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionControl.onResult(this, requestCode, grantResults);
    }

    @Override
    public void init() {

    }
}
