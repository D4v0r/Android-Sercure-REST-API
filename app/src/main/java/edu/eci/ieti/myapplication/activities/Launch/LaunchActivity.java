package edu.eci.ieti.myapplication.activities.Launch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.eci.ieti.myapplication.R;
import edu.eci.ieti.myapplication.activities.Login.LoginActivity;
import edu.eci.ieti.myapplication.activities.Main.MainActivity;

public class LaunchActivity extends AppCompatActivity {

    public static final String TOKEN_KEY = "TOKEN_KEY";

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        SharedPreferences sharedPref =
                getSharedPreferences( getString( R.string.PREFERENCE_FILE_KEY ), Context.MODE_PRIVATE );

        if(sharedPref.contains(TOKEN_KEY)){
            //TODO go to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            //TODO go to LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}