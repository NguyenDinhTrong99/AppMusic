package com.trongdeptrai.musicapp;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.trongdeptrai.musicapp.model.Song;
import com.trongdeptrai.musicapp.utils.ManagerPlay;
import java.text.SimpleDateFormat;

import static com.trongdeptrai.musicapp.utils.VariablePublic.isPlay;
import static com.trongdeptrai.musicapp.utils.VariablePublic.sListSong;
import static com.trongdeptrai.musicapp.utils.VariablePublic.sPosition;

public class ManagerSongUI implements View.OnClickListener {
    private static final long TIME_DELAY = 100;
    private Context mContext;
    private Dialog dialog;
    ImageButton btnClose, btnPlay, btnNext, btnPrevious;
    SeekBar seekDuration;
    TextView tvNameSong, tvTimeStart, tvTimeEnd;
    ImageView imgAvatar;
    private ManagerPlay mManagerPlay;

    public ManagerSongUI(Context context) {
        mContext = context;
        mManagerPlay = new ManagerPlay(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_hide:
                dialog.dismiss();
                break;
            case R.id.btn_PlaySong:
                isPlay = !isPlay;
                setTimeDuration();
                PlayStopLintenser();
                break;
            case R.id.btn_NextSong:
                mManagerPlay.next();
                setNameSong();
                PlayStopLintenser();
                setTimeDuration();
                break;
            case R.id.btn_PreviousSong:
                mManagerPlay.previous();
                setNameSong();
                PlayStopLintenser();
                setTimeDuration();
                break;
        }
    }


    // hàm hiển thị ui chơi nhạc
    public void showUiPlaySong() {
        dialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ui_play_song);
        //set full màn hình
        Window window = dialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        //ánh xạ
        initView(dialog);
        PlayStopLintenser();
        //sự kiện khi nhấn seekbar
        seekDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mManagerPlay.fastForWard(seekDuration.getProgress());
            }
        });
        dialog.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
    }
    
    public void PlayStopLintenser(){
        if(isPlay){
            ManagerPlay.isPlay();
            setImagesButton();
            mManagerPlay.setSong(sPosition);
            mManagerPlay.play();
            updateTimeDuration();
        }else {
            ManagerPlay.isPlay();
            setImagesButton();
            mManagerPlay.stop();
        }
    }
    
    private void initView(Dialog dialog) {
        btnClose = dialog.findViewById(R.id.btn_hide);
        btnPlay = dialog.findViewById(R.id.btn_PlaySong);
        btnNext = dialog.findViewById(R.id.btn_NextSong);
        btnPrevious = dialog.findViewById(R.id.btn_PreviousSong);
        tvNameSong = dialog.findViewById(R.id.text_NamSongPlay);
        tvTimeStart = dialog.findViewById(R.id.text_TimeStart);
        tvTimeEnd = dialog.findViewById(R.id.text_TimeEnd);
        seekDuration = dialog.findViewById(R.id.seekBar);
        imgAvatar = dialog.findViewById(R.id.image_AvatarPlay);

        setNameSong();
        setTimeDuration();

        //set anim app music
        setAnimationMusic();
        btnClose.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
    }

    private void setAnimationMusic() {
        Animation animator = AnimationUtils.loadAnimation(mContext, R.anim.anim_cricle_music);
        imgAvatar.setAnimation(animator);
    }

    private void updateTimeDuration(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                tvTimeStart.setText(sdf.format(mManagerPlay.getCurrentTimeDuration()));
                new Handler().postDelayed(this, 300);
            }
        }, TIME_DELAY);
    }

    private void setImagesButton() {
        if (isPlay) {
            btnPlay.setImageResource(R.drawable.ic_pause);
        } else {
            btnPlay.setImageResource(R.drawable.ic_play);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setTimeDuration(){
        Song song = sListSong.get(sPosition);
        String time = convertDurationToTime(song.getDuration());
        tvTimeEnd.setText(time);
        tvTimeStart.setText("00:00");
        seekDuration.setProgress(0);
    }

    private void setNameSong(){
        Song song = sListSong.get(sPosition);
        tvNameSong.setText(song.getName());
    }

    //hàm đổi thời gian về đúng định dạng phút giây của bài hát
    private String convertDurationToTime(String duration) {
        int temp = Integer.parseInt(duration);
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        time = sdf.format(temp);
        seekDuration.setMax(Integer.parseInt(duration));
        return time;
    }


}
