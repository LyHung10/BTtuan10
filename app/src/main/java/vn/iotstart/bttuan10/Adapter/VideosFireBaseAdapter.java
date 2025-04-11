package vn.iotstart.bttuan10.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import vn.iotstart.bttuan10.Model.Video1Model;
import vn.iotstart.bttuan10.R;

public class VideosFireBaseAdapter extends FirebaseRecyclerAdapter<Video1Model, VideosFireBaseAdapter.MyHolder> {

    private boolean isFav = false;

    public VideosFireBaseAdapter(@NonNull FirebaseRecyclerOptions<Video1Model> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_video_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull Video1Model model) {
        holder.textVideoTitle.setText(model.getTitle());
        holder.textVideoDescription.setText(model.getDesc());

        holder.videoProgressBar.setVisibility(View.VISIBLE);
        holder.videoView.setVideoURI(Uri.parse(model.getUrl()));

        holder.videoView.setOnPreparedListener(mp -> {
            holder.videoProgressBar.setVisibility(View.GONE);
            mp.start();

            float videoRatio = (float) mp.getVideoWidth() / mp.getVideoHeight();
            float screenRatio = (float) holder.videoView.getWidth() / holder.videoView.getHeight();
            float scale = videoRatio / screenRatio;

            if (scale >= 1f) {
                holder.videoView.setScaleX(scale);
            } else {
                holder.videoView.setScaleY(1f / scale);
            }

            mp.setLooping(true);
        });

        holder.videoView.setOnCompletionListener(mp -> mp.start());

        holder.favorites.setOnClickListener(v -> {
            if (!isFav) {
                holder.favorites.setImageResource(R.drawable.ic_fill_favorite);
            } else {
                holder.favorites.setImageResource(R.drawable.favorites);
            }
            isFav = !isFav;
        });

        // Bạn có thể xử lý sự kiện imShare, imMore, imPerson tại đây nếu cần
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        VideoView videoView;
        ProgressBar videoProgressBar;
        TextView textVideoTitle, textVideoDescription;
        ImageView imPerson, favorites, imShare, imMore;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            videoProgressBar = itemView.findViewById(R.id.videoProgressBar);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription = itemView.findViewById(R.id.textVideoDescription);
            imPerson = itemView.findViewById(R.id.imPerson);
            favorites = itemView.findViewById(R.id.favorites);
            imShare = itemView.findViewById(R.id.imShare);
            imMore = itemView.findViewById(R.id.imMore);
        }
    }
}

