package com.mp3player.searchonline;
/**
 * Created by Usman Jamil on 02/02/2017.
 * Usmans.net
 * Skype usman.jamil78
 * email usmanjamil547@gmail.com
 */
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;
import com.onesignal.OneSignal;
import java.util.ArrayList;
import java.util.List;

import android.support.design.widget.CoordinatorLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements constants  {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;
    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    String[] Final_Suggestions=null;
    private SimpleCursorAdapter mAdapter;
        String SearchText=null;
    SongFragment fragment;
    ProgressDialog pDialog;
    String TabFragmentB;
    Boolean Is = false;
    FloatingActionButton Sharebutton;
    String urs;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        int id = viewPager.getCurrentItem();
        OneSignal.startInit(this).init();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
         Sharebutton = (FloatingActionButton) findViewById(R.id.fav);
        Sharebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            onshare();
            }
        });

        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    Snackbar snackbar = Snackbar .make(coordinatorLayout, getString(R.string.WelcomeMsg), Snackbar.LENGTH_LONG);
    SongFragment toy1 = (SongFragment) getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + viewPager.getId() + ":" + 0);
        setView();
        snackbar.show();
    }
    public void setView(){
       Sharebutton.setVisibility(View.VISIBLE);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongFragment(), "Search");
            adapter.addFragment(new DownloadFragment(), "Downloaded");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);


    }
    @Override
    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if ((paramInt == 4) && (paramKeyEvent.getRepeatCount() == 0)) {
          onexit();
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    public void onexit() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("Rate Us");
        localBuilder
                .setMessage(getString(R.string.rating)).setNeutralButton("Rate",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface paramAnonymousDialogInterface,
                                    int paramAnonymousInt) {
                                MainActivity.this.ratee(MainActivity.this
                                        .getApplicationContext()
                                        .getPackageName());
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface paramAnonymousDialogInterface,
                            int paramAnonymousInt) {

                        paramAnonymousDialogInterface.dismiss();
                        MainActivity.this.finish();

                    }
                });
        localBuilder.show();
    }
    public void onshare() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("Share");
        localBuilder
                .setMessage(getString(R.string.share)).setNeutralButton("Share",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface paramAnonymousDialogInterface,
                            int paramAnonymousInt) {
                        MainActivity.this.share(getString(R.string.ShareMsg)+MainActivity.this
                                .getApplicationContext()
                                .getPackageName());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            DialogInterface paramAnonymousDialogInterface,
                            int paramAnonymousInt) {
                        paramAnonymousDialogInterface.dismiss();
                    }
                });
        localBuilder.show();
    }

    public void ratee(String paramString) {
        try {
            Intent localIntent = new Intent("android.intent.action.VIEW");
            localIntent
                    .setData(Uri.parse("market://details?id=" + paramString));
            startActivity(localIntent);
            return;
        } catch (Exception localException) {

        }
    }
    public void share(String paramString) {
    try {
            Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,paramString);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
            return;
        } catch (Exception localException) {

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

 }
