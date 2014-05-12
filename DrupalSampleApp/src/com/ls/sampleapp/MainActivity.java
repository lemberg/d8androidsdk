package com.ls.sampleapp;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;

import com.ls.sampleapp.adapters.CategoriesAdapter;

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
        this.mViewPager.setAdapter(new CategoriesAdapter(this.getSupportFragmentManager()));
        this.mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {                       
                        bar.setSelectedNavigationItem(position);
                    }
                });
               
        for (int i = 0; i < AppConstants.CATEGORIE.values().length; i++) {
        	bar.addTab(
        			bar.newTab()
                            .setText(AppConstants.CATEGORIE.values()[i].name)
                            .setTabListener(this));
        }               
           
//       Log.e("Test items:",new Gson().toJson(new TestItemArray()));
//       ArrayList<LinkedList<TestItem>> array = new Gson().fromJson("[[{\"test\":\"test1\"},{\"test\":\"test2\"}],[],[{\"test\":\"test3\"}]]",(Class) new TestItemArray().getManagedDataClassSpecifyer());
//        Log.e("Test","data:"+array.get(0).get(0).test);
    }

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1)
	{}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1)
	{
		int position = arg0.getPosition();
		mViewPager.setCurrentItem(position);		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
	{}   
//	
//	private class TestItem
//	{
//		public String test;
//		public TestItem(String test)
//		{
//			this.test = test;
//		}
//	}
//	
//	private class TestItemArray extends AbstractDrupalItemsArray<List<TestItem>>
//	{
//
//		public TestItemArray()
//		{	
//			super(new DrupalClient("test", MainActivity.this), 3,);	
//			List<TestItem> items = new LinkedList<MainActivity.TestItem>();
//			items.add(new TestItem("test1"));
//			this.add(items);
//			
//			List<TestItem> items2 = new LinkedList<MainActivity.TestItem>();
//			items.add(new TestItem("test2"));
//			this.add(items2);
//			
//			List<TestItem> items3 = new LinkedList<MainActivity.TestItem>();
//			items3.add(new TestItem("test3"));
//			this.add(items3);
//		}
//
//		@Override
//		protected String getPath()
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		protected Map<String, String> getItemRequestPostParameters()
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		protected Map<String, String> getItemRequestGetParameters(RequestMethod method)
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
//		
//		@Override
//		public Object getManagedDataClassSpecifyer()
//		{
//			// TODO Auto-generated method stub
//			return super.getManagedDataClassSpecifyer();
//		}
//	}
}
