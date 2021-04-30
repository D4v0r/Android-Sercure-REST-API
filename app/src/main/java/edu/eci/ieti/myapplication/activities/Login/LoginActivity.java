package edu.eci.ieti.myapplication.activities.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.eci.ieti.myapplication.R;
import edu.eci.ieti.myapplication.activities.Main.MainActivity;
import edu.eci.ieti.myapplication.models.LoginWrapper;
import edu.eci.ieti.myapplication.models.Token;
import edu.eci.ieti.myapplication.service.authService.AuthService;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    private Button loginButton;
    private EditText emailView;
    private EditText passwordView;
    private final ExecutorService executorService = Executors.newFixedThreadPool( 1 );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.button);
        emailView = (EditText) findViewById(R.id.editTextTextEmailAddress);
        passwordView = (EditText) findViewById(R.id.editTextTextPassword);

        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin(){

        emailView.setError(null);
        passwordView.setError(null);
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        View focusView = validateViews(email, password);
        if (!(focusView == null)) focusView.requestFocus();
        else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http:/10.0.2.2:8080") //localhost for emulator
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            AuthService authService = retrofit.create(AuthService.class);
            executorService.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Response<Token> response = authService.login( new LoginWrapper( email, password) ).execute();
                        Token token = response.body();
                        runOnUiThread(() -> {
                            if(!response.isSuccessful()){
                                emailView.setError("Verify Your email");
                                passwordView.setError("verify Your Password");
                            } else {
                                storeToken(token);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        finish();
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
            } );


        }

    }

    private void storeToken(Token token) {
        SharedPreferences sharedPref =
                getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("TOKEN_KEY",token.getToken());
        editor.commit();
    }

    private View validateViews(String email, String password){
        View focusView = null;
        if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
        }
        if (!isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
        }
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
        }
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = emailView;
        }
        return  focusView;
    }

    private boolean isEmailValid(String email) {
        /**Si la cadena contiene el caracter @ es un email valido*/
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        /**Si la cadena supera los 4 caracteres es una contraseña valida*/
        return password.length() > 4;
    }

}