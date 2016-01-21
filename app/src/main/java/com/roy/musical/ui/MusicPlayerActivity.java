package com.roy.musical.ui;

import android.media.browse.MediaBrowser;
import android.os.Bundle;

import com.roy.musical.R;

public class MusicPlayerActivity extends BaseActivity {

    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION = "com.roy.musical.EXTRA_CURRENT_MEDIA_DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initToolbar();
    }

    @Override
    public MediaBrowser getMediaBrowser() {
        return null;
    }
}
