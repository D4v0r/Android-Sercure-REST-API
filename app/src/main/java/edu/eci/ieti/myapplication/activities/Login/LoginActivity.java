package edu.eci.ieti.myapplication.activities.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.eci.ieti.myapplication.R;
import edu.eci.ieti.myapplication.activities.Main.MainActivity;
import edu.eci.ieti.myapplication.model.LoginWrapper;
import edu.eci.ieti.myapplication.model.Token;
import edu.eci.ieti.myapplication.service.authService.AuthClient;
import edu.eci.ieti.myapplication.storage.TokenStorage;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private Button loginButton;
    private EditText emailView;
    private EditText passwordView;
    private TokenStorage tokenStorage;
    private final AuthClient authClient = new AuthClient();
    private final ExecutorService executorService = Executors.newFixedThreadPool( 1 );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tokenStorage = new TokenStorage(this);
        loginButton = (Button) findViewById(R.id.logoutButton);
        emailView = (EditText) findViewById(R.id.editTextTextEmailAddress);
        passwordView = (EditText) findViewById(R.id.editTextTextPassword);

        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin(){

        emailView.setError(null);
        passwordView.setError(null);
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        LoginWrapper loginWrapper = validateViews(email, password);
        if(loginWrapper != null){
            executorService.execute(authProcess(loginWrapper));
        }
    }

    private Runnable authProcess (LoginWrapper loginWrapper){

        return new Runnable() {
            @Override
            public void run() {
                try {
                    Call<Token> apiCall = authClient.getService().login(loginWrapper);
                    Response<Token> response = apiCall.execute();
                    if (response.isSuccessful()) {
                        Token token = response.body();
                        tokenStorage.saveToken(token);
                        startActivity(
                                new Intent(LoginActivity.this, MainActivity.class)
                        );
                        finish();
                    }
                    else{
                        showInvalidDataError();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showInvalidDataError();
                }
            }
        };
    }

    private void showInvalidDataError(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emailView.setError("Verify Your email!");
                passwordView.setError("verify Your Password!");
            }
        });
    }

    private LoginWrapper validateViews(String email, String password){

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

        if (!(focusView == null)) {
            focusView.requestFocus();
            return null;
        }

        return  new LoginWrapper(email, password);
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