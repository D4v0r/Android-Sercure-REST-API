package edu.eci.ieti.myapplication.service.authService;

import edu.eci.ieti.myapplication.model.LoginWrapper;
import edu.eci.ieti.myapplication.model.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("/auth")
    public Call<Token> login(@Body LoginWrapper wrapper);
}
