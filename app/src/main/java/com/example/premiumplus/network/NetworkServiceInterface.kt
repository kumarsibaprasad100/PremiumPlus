package com.example.premiumplus.network

import com.example.premiumplus.requestModel.AddClientRequestModel
import com.example.premiumplus.requestModel.ClientRequestModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkServiceInterface {
    @POST("user/PostClientDetails")
    fun getClient(@Body body: ClientRequestModel?): Observable<Response<ResponseBody>>

    @POST("user/PostClientDetails")
    fun addClient(@Body client: AddClientRequestModel): Observable<Response<ResponseBody>>?
}