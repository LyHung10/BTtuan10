package vn.iotstart.bttuan10.ThuchanhActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import vn.iotstart.bttuan10.R;

public class UploadVideoActivity extends AppCompatActivity {

    private Uri selectedVideoUri;
    private VideoView videoView;
    private Button btnChoose, btnUpload;
    private ProgressDialog progressDialog;

    private Cloudinary cloudinary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video); // layout bạn gửi

        videoView = findViewById(R.id.videoView2);
        btnChoose = findViewById(R.id.add);
        btnUpload = findViewById(R.id.upload);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        configCloudinary();

        btnChoose.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .galleryOnly()
                    .galleryMimeTypes(new String[]{"video/*"})
                    .start();
        });

        btnUpload.setOnClickListener(v -> {
            if (selectedVideoUri != null) {
                uploadVideoToCloudinary(selectedVideoUri); // Gửi Uri trực tiếp
            } else {
                Toast.makeText(this, "Vui lòng chọn video", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            selectedVideoUri = data.getData();
            videoView.setVideoURI(selectedVideoUri);
            videoView.start(); // auto play
        }
    }

    private void configCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", "dzeiza7ea");
        config.put("api_key", "456647796593115");
        config.put("api_secret", "7guuvbP7hc2fo2tarLQfxHDZbxg");
        cloudinary = new Cloudinary(config);

        MediaManager.init(this, config);
    }

    private void uploadVideoToCloudinary(Uri videoUri) {
        if (videoUri == null) {
            Toast.makeText(this, "Video URI is null!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        MediaManager.get().upload(videoUri)
                .option("resource_type", "video")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(UploadVideoActivity.this, "Bắt đầu upload...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Có thể cập nhật UI tiến trình nếu muốn
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String videoUrl = resultData.get("secure_url").toString();
                        Toast.makeText(UploadVideoActivity.this, "Upload thành công!", Toast.LENGTH_SHORT).show();
                        Log.d("CloudinaryUpload", "Video URL: " + videoUrl);

                        // Gọi hàm lưu Firebase (có thể thay đổi title và desc tùy bạn)
                        saveVideoToFirebase(videoUrl, "Tom and Jerry", "");
                    }


                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadVideoActivity.this, "Lỗi upload: " + error.getDescription(), Toast.LENGTH_LONG).show();
                        Log.e("CloudinaryUpload", "Upload error: " + error.toString());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadVideoActivity.this, "Upload bị hoãn lại!", Toast.LENGTH_LONG).show();
                        Log.e("CloudinaryUpload", "Reschedule: " + error.toString());
                    }
                }).dispatch();
    }

    private void saveVideoToFirebase(String videoUrl, String title, String desc) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userVideosRef = FirebaseDatabase.getInstance().getReference("videos"); // Lưu chung vào node 'videos'

        userVideosRef.get().addOnSuccessListener(dataSnapshot -> {
            // Đếm số lượng video đã có
            long videoCount = dataSnapshot.getChildrenCount();
            String newKey = "v" + (videoCount + 1); // ví dụ: v1, v2, v3...

            Map<String, Object> videoData = new HashMap<>();
            videoData.put("url", videoUrl);
            videoData.put("title", title);
            videoData.put("desc", desc);

            userVideosRef.child(newKey).setValue(videoData)
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Đã lưu Firebase!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Lỗi lưu Firebase", Toast.LENGTH_SHORT).show();
                    });

        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(this, "Không thể truy cập Firebase", Toast.LENGTH_SHORT).show();
        });
    }

}
