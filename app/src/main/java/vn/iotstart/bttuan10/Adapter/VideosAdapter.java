package vn.iotstart.bttuan10.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import vn.iotstart.bttuan10.Model.VideoModel;
import vn.iotstart.bttuan10.R;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder> {

    private Context context;
    private List<VideoModel> videoList;

    public VideosAdapter(Context context, List<VideoModel> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video_youtube_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoModel videoModel = videoList.get(position);
        holder.textVideoTitle.setText(videoModel.getTitle());
        holder.textVideoDescription.setText(videoModel.getDescription());

        // Show loading
        holder.videoProgressBar.setVisibility(View.VISIBLE);

        // Load video từ YouTube
        holder.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = extractYoutubeVideoId(videoModel.getUrl());
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0);
                    holder.videoProgressBar.setVisibility(View.GONE);
                }
            }
        });

        holder.favorites.setOnClickListener(new View.OnClickListener() {
            private boolean isFav = false;

            @Override
            public void onClick(View v) {
                if (!isFav) {
                    holder.favorites.setImageResource(R.drawable.ic_fill_favorite);
                    isFav = true;
                } else {
                    holder.favorites.setImageResource(R.drawable.favorites);
                    isFav = false;
                }
            }
        });
    }
    private String extractYoutubeVideoId(String url) {
        Uri uri = Uri.parse(url);
        String videoId = uri.getQueryParameter("v");
        if (videoId != null) return videoId;

        // Trường hợp là link rút gọn youtu.be hoặc link không có query param
        List<String> segments = uri.getPathSegments();
        if (!segments.isEmpty()) {
            return segments.get(segments.size() - 1);
        }

        return null;
    }



    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        VideoView videoView;
        YouTubePlayerView youtubePlayerView;

        ProgressBar videoProgressBar;
        TextView textVideoTitle, textVideoDescription;
        ImageView imPerson, favorites, imShare, imMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //videoView = itemView.findViewById(R.id.videoView);
            youtubePlayerView = itemView.findViewById(R.id.youtube_player_view);
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

