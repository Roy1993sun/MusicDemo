package com.roy.musical.ui;

import android.app.ActivityManager;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.roy.musical.R;
import com.roy.musical.utils.ResourceHelper;

/**
 * 用来控制和显示正在播放的音乐的基类
 * Created by Roy_Sun on 2016/1/17 0017.
 */
public abstract class BaseActivity extends ActionBarCastAty implements MediaBrowserProvider {

    private MediaBrowser             mMediaBrowser;
    private PlaybackControlsFragment mControlsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getTitle().toString(), BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_white), ResourceHelper.getThemeColor(this, R.attr.colorPrimary, android.R.color.darker_gray));

        setTaskDescription(taskDescription);


        //mMediaBrowser = new MediaBrowser(this, new ComponentName(this,MusicService.class),mConnectionCallback,null);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (getMediaController() != null) {
            getMediaController().unregisterCallback(mMediaControllerCallback);
        }
    }

    public MediaBrowser getmMediaBrowser() {
        return mMediaBrowser;
    }


//    protected void hidePlaybackControls() {
//        getFragmentManager().beginTransaction()
//                            .hide(mControlsFragment)
//                            .commit();
//
//    }

    /**
     * 判断是否需要显示音乐播放控制器,如果是switch中的状态则不显示
     *
     * @return 如果是true 则显示
     */
    private boolean shouldShowControls() {
        MediaController mediaController = getMediaController();
        if (mediaController == null ||
                mediaController.getMetadata() == null ||
                mediaController.getPlaybackState() == null) {
            return false;
        }

        switch (mediaController.getPlaybackState()
                               .getState()) {
            case PlaybackState.STATE_NONE:
            case PlaybackState.STATE_ERROR:
            case PlaybackState.STATE_STOPPED:
                return false;
            default:
                return true;
        }
    }

    //    private final MediaBrowser.ConnectionCallback mConnectionCallback =
    //            new MediaBrowser.ConnectionCallback() {
    //                @Override
    //                public void onConnected() {
    //                    connectToSession(mMediaBrowser.getSessionToken());
    //                }
    //            };
    private final MediaController.Callback mMediaControllerCallback =
            new MediaController.Callback() {
                @Override
                public void onPlaybackStateChanged(@NonNull PlaybackState state) {
                    if (shouldShowControls()) {
                        shouldShowControls();
                    } else {
                       // hidePlaybackControls();
                    }
                }

                @Override
                public void onMetadataChanged(MediaMetadata metadata) {
                    super.onMetadataChanged(metadata);
                }
            };
}
