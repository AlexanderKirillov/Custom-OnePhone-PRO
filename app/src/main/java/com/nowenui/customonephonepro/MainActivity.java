package com.nowenui.customonephonepro;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity
{
    public static SlidingMenu menu;
    public static FragmentManager fragmentManager;
    public static ActionBar actionBar;
    public static Fragment mContent;
    View mDecorView;
    public static boolean isShowContentOpen;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isShowContentOpen = false;
        
        if (Build.VERSION.SDK_INT >= 15) {
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
		}
        
		if (Build.VERSION.SDK_INT>=11)
		{
        mDecorView = getWindow().getDecorView();
		}
        fragmentManager = getSupportFragmentManager();
        menu = new SlidingMenu(this);
        actionBar = getSupportActionBar();
        if (findViewById(R.id.menu_frame) == null)
		{
            menu.setSlidingEnabled(true);
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            menu.setMenu(R.layout.menu_frame);
			if (Build.VERSION.SDK_INT>=14)
			{
			menu.setBehindCanvasTransformer(new CanvasTransformer() {
					@Override
					public void transformCanvas(Canvas canvas, float percentOpen) {
						float scale = (float) (percentOpen*0.25 + 0.75);
						canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
					}
				});
				}
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        }
		else
		{
            View v = new View(this);
            menu.setMenu(v);
            menu.setSlidingEnabled(false);
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null)
            mContent = new TitleListFragment();
        setFragment(mContent);
        setMenuFragment(new MenuFragment());
        initSlidingMenu();

	}
	
    public void onClick(View v) {
		hideSystemUI();
	}

	public void onClick2(View v) {
		showSystemUI();
	}
	private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

	private void showSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	        super.onWindowFocusChanged(hasFocus);
	    if ((hasFocus)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mDecorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
	}
	
	
    public void initSlidingMenu()
	{
		
        menu.setBackgroundColor(Color.parseColor("#ff212121"));
        menu.setMode(SlidingMenu.LEFT);
        menu.setBehindOffsetRes(R.dimen.behind_offset_Res);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		
    }
    public static void setFragment(Fragment ft)
	{
        mContent = ft;
		fragmentManager.beginTransaction()
			.replace(R.id.fragmentContainer, ft)
			.commit();
        menu.showContent();
    }
    public void setMenuFragment(Fragment ft)
	{
        getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.menu_frame, ft)
			.commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
        switch (item.getItemId())
		{
            case android.R.id.home:
                if (isShowContentOpen)
                    setFragment(new TitleListFragment());
                else
                    menu.toggle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
	
    @Override
    public void onSaveInstanceState(Bundle outState)
	{
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }
    @Override
    public void onBackPressed()
	{
        if (menu.isMenuShowing())
		{
            menu.showContent();
        }
		else if (isShowContentOpen)
		{
            setFragment(new TitleListFragment());
        }
		else
		{
            super.onBackPressed();
        }
    }
}
