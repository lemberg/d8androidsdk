package com.ls.sampleapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.ls.drupal.DrupalClient;
import com.ls.http.base.ResponseData;
import com.ls.sampleapp.article.Article;
import com.ls.sampleapp.article.Page;

public class MainActivity extends ActionBarActivity {
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    
        
        requestArticle();
    }

    private void requestArticle()
    {
    	new AsyncTask<Void, Void, Void>()
		{
    		@Override
    		protected Void doInBackground(Void... params)
    		{    			
//    			Article article = new Article(new DrupalClient("http://vh324.dev-ls.co.uk/",MainActivity.this), 1);
//    	    	ResponseData response = article.getDataFromServer(true, Article.class);
//    	    	Log.e("Article body:",article.getBody());
//    	      	Log.e("Article coment:",article.getComent().toString());
//    			return null;
    			
    			Page page = new Page(new DrupalClient("http://vh324.dev-ls.co.uk/",MainActivity.this), 1);
    	    	ResponseData response = page.getDataFromServer(true);
    	    	Article article = page.get(0);
    	    	Log.e("Article body:",article.getBody());
    	      	Log.e("Article coment:",article.getComent().toString());
    			return null;
    		}
		}.execute();    	
    			
    }
    
    
    
}
