package edu.eci.ieti.myapplication.service.authService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthClient {

    private AuthService service;

    public AuthClient(){

        Retrofit retrofit = new Retrofit.Builder().baseUrl( "http:/10.0.2.2:8080/" ) //localhost for emulator
                .addConverterFactory( GsonConverterFactory.create() ).build();

        service = retrofit.create( AuthService.class );
    }

    public AuthService getService(){
        return service;
    }
}
