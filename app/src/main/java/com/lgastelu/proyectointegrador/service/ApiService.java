package com.lgastelu.proyectointegrador.service;

import com.lgastelu.proyectointegrador.models.Usuarios;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    String API_BASE_URL = "http://10.0.2.2:8080"; //no acepta https:, solo http:

    @POST("/registrar")
    Call<Usuarios> createUsuario(@Body Usuarios usuarios);

}
