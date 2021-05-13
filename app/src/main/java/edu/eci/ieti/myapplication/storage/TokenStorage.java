package edu.eci.ieti.myapplication.storage;
import edu.eci.ieti.myapplication.R;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;

import edu.eci.ieti.myapplication.model.Token;

public class TokenStorage {

    private final String TOKEN_KEY = "TOKEN_KEY";

    private final SharedPreferences sharedPreferences;

    public TokenStorage( Context context )
    {
        this.sharedPreferences =
                context.getSharedPreferences( context.getString( R.string.preference_file_key ), Context.MODE_PRIVATE );
    }

    public String getToken()
    {
        return sharedPreferences.getString( TOKEN_KEY, null );
    }

    public void saveToken( Token token )
    {
        sharedPreferences.edit().putString( TOKEN_KEY, token.getToken() ).apply();
    }

    public void clearToken()
    {
        sharedPreferences.edit().remove( TOKEN_KEY ).apply();
    }
}
