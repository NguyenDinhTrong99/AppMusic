package com.trongdeptrai.musicapp.adpater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trongdeptrai.musicapp.R;
import com.trongdeptrai.musicapp.model.Song;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private static ClickListener sClickListener;
    private ArrayList<Song> mSongList;

    public SongAdapter(ArrayList<Song> songList) {
        mSongList = songList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_song, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Song song = mSongList.get(position);
        viewHolder.tvNameSong.setText(cutNameSong(song.getName()));
        viewHolder.tvArtistSong.setText(song.getArtistName());
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvNameSong, tvArtistSong;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvNameSong = itemView.findViewById(R.id.text_NameSong);
            tvArtistSong = itemView.findViewById(R.id.text_ArtistSong);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            layoutParams.setMargins(3,3,3,3);
        }

        @Override
        public void onClick(View v) {
            sClickListener.onItemClickListener(getAdapterPosition(), v);
        }
    }

    // hàm cắt chuỗi tên bài hát. nếu tên bài hát quá dài (hơn 30 kí tự) thì cắt đi rồi thêm "..."
    private String cutNameSong(String nameSong) {
        String song = nameSong;
        if (nameSong.length() > 30) {
            String temp = nameSong.substring(0, 30);
            song = temp + "...";
        }
        return song;
    }

    // sự kiện click button
    public interface ClickListener{
        void onItemClickListener(int position, View v);
    }
    public void setOnItemClickListener(ClickListener clickListener){
        sClickListener = clickListener;
    }
}
