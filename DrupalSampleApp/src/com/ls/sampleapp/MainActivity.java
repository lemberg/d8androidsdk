package com.ls.sampleapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.ls.drupal.DrupalClient;
import com.ls.http.base.ResponseData;
import com.ls.sampleapp.article.Article;
import com.ls.sampleapp.article.Page;

public class MainActivity extends ActionBarActivity implements TabListener {
		
	private ViewPager mViewPager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    
        
        requestArticle();
        final ActionBar bar = this.getSupportActionBar();
        bar.setTitle("Test");
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
               
        this.mViewPager = (ViewPager)this.findViewById(R.id.pager);   
        this.mViewPager.setAdapter(new ArticlesPageAdapter(this.getSupportFragmentManager()));
        this.mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {                       
                        bar.setSelectedNavigationItem(position);
                    }
                });
               
        for (int i = 0; i < 5; i++) {
        	bar.addTab(
        			bar.newTab()
                            .setText("Tab " + (i + 1))
                            .setTabListener(this));
        }
    }

    private void requestArticle()
    {
    	new AsyncTask<Void, Void, Void>()
		{
    		@Override
    		protected Void doInBackground(Void... params)
    		{      			
//    			Page page = new Page(new DrupalClient("http://vh324.dev-ls.co.uk/",MainActivity.this), 1);
//    	    	ResponseData response = page.getDataFromServer(true);
//    	    	Article article = page.get(0);
//    	    	Log.e("Article body:",article.getBody());
//    	      	Log.e("Article coment:",article.getComent().toString());
    			return null;
    		}
		}.execute();    	
    			
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
