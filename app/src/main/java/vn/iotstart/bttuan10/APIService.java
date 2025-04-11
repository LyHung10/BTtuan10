package vn.iotstart.bttuan10;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import vn.iotstart.bttuan10.Model.MessageVideoModel;

public interface APIService {

    // Tạo Gson để parse JSON
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    // Tạo Retrofit instance
    APIService serviceApi = new Retrofit.Builder()
            .baseUrl("http://app.iotstar.vn:8081/appfoods/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    // API endpoint
    @GET("getvideos.php")
    Call<MessageVideoModel> getVideos();
}
