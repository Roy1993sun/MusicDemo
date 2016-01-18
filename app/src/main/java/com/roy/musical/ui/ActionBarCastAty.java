package com.roy.musical.ui;

import com.roy.musical.R;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by roy on 16-1-15.
 */
public abstract class ActionBarCastAty extends AppCompatActivity {
    private DrawerLayout          mDrawerLayout;
    private Toolbar               mtToolbar;
    private ActionBarDrawerToggle mToggle;

    int mItemWhenOpenWhenDrawerClosed = -1;

    DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            if (mToggle != null) {
                mToggle.onDrawerSlide(drawerView, slideOffset);
            }
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            if (mToggle != null) {
                mToggle.onDrawerOpened(drawerView);
            }

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.app_name);
            }
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            if (mToggle != null) {
                mToggle.onDrawerClosed(drawerView);
            }

            if (mItemWhenOpenWhenDrawerClosed >= 0) {
                Bundle extras = ActivityOptions.makeCustomAnimation(ActionBarCastAty.this, R.anim.fade_in, R.anim.fade_out)
                                               .toBundle();

                Class atyClass = null;
                switch (mItemWhenOpenWhenDrawerClosed) {
                    case R.id.nav_all_music:
                        Toast.makeText(ActionBarCastAty.this, "点击了all music", Toast.LENGTH_SHORT)
                             .show();
                        atyClass = MusicPlayerActivity.class;
                        break;

                    case R.id.nav_playlist:
                        Toast.makeText(ActionBarCastAty.this, "点击了play list", Toast.LENGTH_SHORT)
                             .show();
                        atyClass = PlayListActivity.class;
                        break;
                }
                if (atyClass != null) {
                    startActivity(new Intent(ActionBarCastAty.this, atyClass), extras);
                    finish();
                }
            }
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            if (mToggle != null) {
                mToggle.onDrawerStateChanged(newState);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mtToolbar.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        mtToolbar.setTitle(titleId);
    }

    protected void initToolbar() {
        mtToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mtToolbar.inflateMenu(R.menu.main);

        if (mDrawerLayout != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


            mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mtToolbar, R.string.open_content_drawer, R.string.close_content_drawer);
            mDrawerLayout.setDrawerListener(mDrawerListener);
            populateDrawerItems(navigationView);
            setSupportActionBar(mtToolbar);
            updateDrawerToggle();
        }

    }

    private void populateDrawerItems(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                mItemWhenOpenWhenDrawerClosed = item.getItemId();
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        if (MusicPlayerActivity.class.isAssignableFrom(getClass())) {
            navigationView.setCheckedItem(R.id.nav_all_music);
        } else if (PlayListActivity.class.isAssignableFrom(getClass())) {
            navigationView.setCheckedItem(R.id.nav_playlist);
        }
    }

    private void updateDrawerToggle() {
        if (mtToolbar == null) {
            return;
        }
        boolean isRoot = getFragmentManager().getBackStackEntryCount() == 0;
        mToggle.setDrawerIndicatorEnabled(isRoot);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(!isRoot);
            getSupportActionBar().setDisplayShowHomeEnabled(!isRoot);
            getSupportActionBar().setHomeButtonEnabled(!isRoot);
        }
        if (isRoot) {
            mToggle.syncState();
        }

    }
}
