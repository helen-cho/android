package com.example.ex05;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BlogFragment extends Fragment {
    JSONArray array=new JSONArray();
    BlogAdapter adapter=new BlogAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_blog, container, false);
        new BlogThread().execute();
        ListView list=view.findViewById(R.id.list);
        list.setAdapter(adapter);
        return view;
    }

    class BlogThread extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            String url="https://dapi.kakao.com/v2/search/blog?query=커피";
            String result=KakaoAPI.connect(url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                array= new JSONObject(s).getJSONArray("documents");
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            super.onPostExecute(s);
        }
    }

    class BlogAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return array.length();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View item = getLayoutInflater().inflate(R.layout.item_blog, viewGroup, false);
            try {
                JSONObject obj=array.getJSONObject(i);
                TextView title= item.findViewById(R.id.title);
                title.setText(Html.fromHtml(obj.getString("title")));
                TextView contents=item.findViewById(R.id.contents);
                contents.setText(Html.fromHtml(obj.getString("contents")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return item;
        }
    }
}//Flagment