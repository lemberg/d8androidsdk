package com.ls.sampleapp;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class MainActivity extends ActionBarActivity {
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }   
        

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Integer", Integer.valueOf(1));
        map.put("String", null);
        map.put("Custom Item", new Test());
        String[]array = new String[]{"test1","test2"};
//        List<String> array = new LinkedList<String>();
//        array.add("test1");
//        array.add("test2");
        map.put("array", array);
        
        Gson gson = new Gson();
        JsonElement obj = gson.toJsonTree(map);
        String json = gson.toJson(map);
        Log.e("test",json);
        
//        Gson gson2 = new Gson();
//        Map map2 = gson2.fromJson(json, HashMap.class);
//        Log.e("result:",((List<String>) map2.get("array")).get(0).toString());
    }

    private class Test{
    	String test = "test";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }    
}
