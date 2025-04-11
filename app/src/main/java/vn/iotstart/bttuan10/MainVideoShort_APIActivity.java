package vn.iotstart.bttuan10;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.iotstart.bttuan10.Adapter.VideosAdapter;
import vn.iotstart.bttuan10.Model.MessageVideoModel;
import vn.iotstart.bttuan10.Model.VideoModel;

public class MainVideoShort_APIActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private VideosAdapter videosAdapter;
    private List<VideoModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_video_short_fire_base); // sửa lại từ R.Layout -> R.layout

        viewPager2 = findViewById(R.id.vpager);
        list = new ArrayList<>();

        getVideos(); // Gọi hàm lấy video
    }

    private void getVideos() {
        APIService.serviceApi.getVideos().enqueue(new Callback<MessageVideoModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageVideoModel> call,
                                   @NonNull Response<MessageVideoModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list = response.body().getResult();

                    videosAdapter = new VideosAdapter(MainVideoShort_APIActivity.this, list);
                    viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
                    viewPager2.setAdapter(videosAdapter);
                } else {
                    Log.d("TAG", "Response error or empty body");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageVideoModel> call, @NonNull Throwable t) {
                Log.e("TAG", "API call failed: " + t.getMessage());
            }
        });
    }
}

