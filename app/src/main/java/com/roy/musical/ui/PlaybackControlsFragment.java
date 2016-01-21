package com.roy.musical.ui;

import android.content.Intent;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.roy.musical.R;


public class PlaybackControlsFragment extends Fragment {

    private static final String TAG = "PlaybackControls";

    private ImageButton mPlayOrPause;
    private TextView    mTitle;
    private TextView    mSubTitle;
    //    private String       mArturl;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_playback_container, container, false);

        mPlayOrPause = (ImageButton) rootView.findViewById(R.id.play_pause);
        mPlayOrPause.setEnabled(true);

        mPlayOrPause.setOnClickListener(mButtonListener);

        mTitle = (TextView) rootView.findViewById(R.id.fm_tv_title);
        mSubTitle = (TextView) rootView.findViewById(R.id.fm_tv_artist);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FullScreenPlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                MediaMetadata metadata = getActivity().getMediaController()
                                                      .getMetadata();

                if (metadata != null) {
                    intent.putExtra(MusicPlayerActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION, metadata.getDescription());
                }
                startActivity(intent);
            }
        });
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity().getMediaController() != null) {
            onConnected();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (getActivity().getMediaController() != null) {
            getActivity().getMediaController()
                         .unregisterCallback(mCallback);
        }
    }

    public void onConnected() {
        MediaController controller = getActivity().getMediaController();

        if (controller != null) {
            onMetadataChanged(controller.getMetadata());
            onPlaybackStateChanged(controller.getPlaybackState());
            controller.registerCallback(mCallback);
        }
    }


    /**
     * 按钮点击事件的监听 切换不同的播放状态
     */
    private final View.OnClickListener mButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            PlaybackState stateObj = getActivity().getMediaController()
                                                  .getPlaybackState();

            final int state = stateObj == null ? PlaybackState.STATE_NONE : stateObj.getState();
            Log.d(TAG, "onPlaybackStateChanged: " + state);

            switch (v.getId()) {
                case R.id.play_pause:
                    Log.d(TAG, "play btn pressed: " + state);
                    if (state == PlaybackState.STATE_NONE ||
                            state == PlaybackState.STATE_STOPPED ||
                            state == PlaybackState.STATE_PAUSED) {
                        playMedia();
                    } else if (state == PlaybackState.STATE_PLAYING ||
                            state == PlaybackState.STATE_CONNECTING ||
                            state == PlaybackState.STATE_BUFFERING) {
                        pauseMedia();
                    }
                    break;
            }
        }
    };

    private void pauseMedia() {
        MediaController controller = getActivity().getMediaController();
        if (controller != null) {
            controller.getTransportControls()
                      .pause();
        }
    }

    private void playMedia() {
        MediaController controller = getActivity().getMediaController();
        if (controller != null) {
            controller.getTransportControls()
                      .play();
        }
    }

    private MediaController.Callback mCallback = new MediaController.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackState state) {
            PlaybackControlsFragment.this.onPlaybackStateChanged(state);

        }


        @Override
        public void onMetadataChanged(MediaMetadata metadata) {
            if (metadata == null) {
                return;
            }

            PlaybackControlsFragment.this.onMetadataChanged(metadata);
        }
    };

    private void onMetadataChanged(MediaMetadata metadata) {
        if (getActivity() == null) {
            System.out.println("onMetadataChanged如果正确调用则不会提示,有可能是callback未注册");
            return;
        }

        if (metadata == null) {
            return;
        }

        mTitle.setText(metadata.getDescription()
                               .getTitle());

        mSubTitle.setText(metadata.getDescription()
                                  .getSubtitle());

        // // TODO: 16-1-20 149行 需要获取歌曲图片的缓存 考虑是否需要
    }


    /**
     * 播放状态与图标的改变
     *
     * @param state 获取当前的状态
     */
    private void onPlaybackStateChanged(PlaybackState state) {

        if (getActivity() == null) {
            System.out.println("onPlaybackStateChanged如果正确调用则不会提示,有可能是callback未注册");
            return;
        }
        if (state == null) {
            return;
        }

        boolean enablePlay = false;

        switch (state.getState()) {

            case PlaybackState.STATE_PAUSED:
            case PlaybackState.STATE_STOPPED:
                enablePlay = true;
                break;

            case PlaybackState.STATE_ERROR:
                Toast.makeText(getActivity(), state.getErrorMessage(), Toast.LENGTH_LONG)
                     .show();
                break;
        }

        if (enablePlay) {
            mPlayOrPause.setImageDrawable(getActivity().getDrawable(R.mipmap.ic_play_arrow_black_36dp));
        } else {
            mPlayOrPause.setImageDrawable(getActivity().getDrawable(R.mipmap.ic_pause_black_36dp));
        }
    }

}
