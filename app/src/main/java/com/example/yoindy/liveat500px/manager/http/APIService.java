package com.example.yoindy.liveat500px.manager.http;

import com.example.yoindy.liveat500px.dao.PhotoItemCollectionDao;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by YoInDy on 4/6/2560.
 */

public interface APIService {

    @POST("list")
    Call<PhotoItemCollectionDao> loadPhotoList();

    @POST("list/after/{id}")
    Call<PhotoItemCollectionDao> loadPhotoListAfterId(@Path("id") int id);

    @POST("list/before/{id}")
    Call<PhotoItemCollectionDao> loadPhotoListฺBeforeId(@Path("id") int id);
}
