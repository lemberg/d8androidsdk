package com.ls.sampleapp;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;

import com.ls.sampleapp.adapters.ArticlesAdapter;

public class MainActivity extends ActionBarActivity implements TabListener{
		
	private ViewPager mViewPager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    

        final ActionBar bar = this.getSupportActionBar();
        bar.setTitle(R.string.app_name);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
               
        this.mViewPager = (ViewPager)this.findViewById(R.id.pager);   
        this.mViewPager.setAdapter(new ArticlesAdapter(this.getSupportFragmentManager()));
        this.mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {                       
                        bar.setSelectedNavigationItem(position);
                    }
                });
               
        for (int i = 0; i < AppConstants.NODE_IDS.length; i++) {
        	bar.addTab(
        			bar.newTab()
                            .setText("Article " + AppConstants.NODE_IDS[i])
                            .setTabListener(this));
        }               
           
       
    }

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1)
	{
		int position = arg0.getPosition();
		mViewPager.setCurrentItem(position);		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
	{
		// TODO Auto-generated method stub
		
	}   
}
