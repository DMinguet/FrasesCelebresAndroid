package com.daniminguet.trabajofrasescelebres.interfaces;

import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Categoria;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.models.Usuario;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IAPIService {
    @GET("frase/all")
    Call<List<Frase>> getFrases();

    @GET("frase/all/{offset}")
    Call<List<Frase>> getFrasesLimit(@Path("offset") int offset);

    @GET("frase/dia/{dia}")
    Call<Frase> getFraseDelDia(@Path("dia") Date dia);

    @POST("frase/add")
    Call<Boolean> addFrase(@Body Frase frase);

    @POST("frase/addValues")
    @FormUrlEncoded
    Call<Boolean> addFraseValues(@Field("texto") String texto,
                                 @Field("fechaProgramada") Date fechaProgramada,
                                 @Field("idAutor") int idAutor,
                                 @Field("idCategoria")int idCategoria);

    @GET("autor/all")
    Call<List<Autor>> getAutores();

    @POST("autor/add")
    Call<Boolean> addAutor(@Body Autor autor);

    @GET("categoria/all")
    Call<List<Categoria>> getCategorias();

    @POST("categoria/add")
    Call<Boolean> addCategoria(@Body Categoria categoria);

    @GET("usuario/all")
    Call<List<Usuario>> getUsers();

    @POST("usuario/add")
    Call<Boolean> addUsuario(@Body Usuario usuario);

    @POST("usuario/login")
    Call<Usuario> logUsuario (
            @Body Usuario user
    );
}
