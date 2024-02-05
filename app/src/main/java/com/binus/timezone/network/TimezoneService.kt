package com.binus.timezone.network

import com.binus.timezone.ui.zonawaktu.model.TimezoneResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface TimezoneService {
    @GET("b/65bf14c1266cfc3fde857a02")
    @Headers("Content-Type: application/json")
    fun getList(
        @Header("X-Master-Key") masterKey: String = ApiKey.MASTER_KEY,
        @Header("X-Access-Key") accessKey: String = ApiKey.ACCESS_KEY
    ): Flowable<TimezoneResponse>
}