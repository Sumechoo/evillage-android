package nl.worth.clangnotifications.data.repository

import nl.worth.clangnotifications.data.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interface where all API calls are declared
 */
interface ClangApiService {

    @POST("api/v1/account/register")
    fun createAccount(@Body clangAccount: ClangAccount): Call<ClangAccountResponse>

    @POST("api/v1/token/save")
    fun storeFirebaseToken(@Body clangToken: ClangTokenUpdate): Call<ResponseBody>

    @POST("/api/v1/notification/action")
    fun logNotificationAction(@Body clangActionRequest: ClangActionRequest): Call<ResponseBody>

    @POST("/api/v1/notification/event")
    fun logEvent(@Body clangEventLog: ClangEvent): Call<ResponseBody>

    @POST("/api/v1/properties")
    fun updateProperties(@Body clangProperties: ClangProperties): Call<ResponseBody>
}