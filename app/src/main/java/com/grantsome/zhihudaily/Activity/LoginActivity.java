package com.grantsome.zhihudaily.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.grantsome.zhihudaily.R;

public class LoginActivity extends AppCompatActivity {

    private EditText accountText;

    private EditText passwordText;

    private Button loginButton;

    private SharedPreferences sharedPreference;

    private SharedPreferences.Editor sharedPreferenceEditor;

    private CheckBox rememberPassword;

    private boolean isLogin;

    private Toolbar toolbar;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toobar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,android.R.color.holo_blue_light));
        setSupportActionBar(toolbar);
        setupActionBar();
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        accountText = (EditText) findViewById(R.id.account);
        passwordText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);
        rememberPassword = (CheckBox) findViewById(R.id.remember_password);
        boolean isRemember = sharedPreference.getBoolean("remember_password",false);
        if(isRemember){
            String account = sharedPreference.getString("account","");
            String password = sharedPreference.getString("password","");
            accountText.setText(account);
            passwordText.setText(password);
            rememberPassword.setChecked(true);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = accountText.getText().toString();
                String password = passwordText.getText().toString();
                if(account.equals("redRock666")&&password.equals("redRock666")){
                    isLogin = true;
                    sharedPreferenceEditor = sharedPreference.edit();
                    if(rememberPassword.isChecked()){
                        sharedPreferenceEditor.putBoolean("remember_password",true);
                        sharedPreferenceEditor.putString("account",account);
                        sharedPreferenceEditor.putString("password",password);
                    } else {
                        sharedPreferenceEditor.clear();
                    }
                    sharedPreferenceEditor.apply();
                    Intent intent = new Intent(LoginActivity.this,UserUIActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "账号或密码错误,请重新输入", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.loginmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
            int id = menuItem.getItemId();
            if(id == android.R.id.home) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        return super.onOptionsItemSelected(menuItem);
    }

    public boolean isLogin(){
        return isLogin;
    }

}
