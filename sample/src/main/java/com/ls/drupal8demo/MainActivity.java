/**
 * The MIT License (MIT)
 * Copyright (c) 2014 Lemberg Solutions Limited
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ls.drupal8demo;


import com.ls.drupal8demo.adapters.CategoriesAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity implements TabListener {

	private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

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
}
