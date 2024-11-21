package dev.md19303.lab6;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

    @Multipart
    @POST("/add_cake")  // Đảm bảo endpoint của bạn đúng
    Call<Void> uploadCakeWithImage(
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("distributor") RequestBody distributor,
            @Part MultipartBody.Part image
    );
}
