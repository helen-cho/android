package com.example.ex04;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

public class LocalActivity extends AppCompatActivity {
    JSONArray array = new JSONArray();
    LocalAdapter adapter=new LocalAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        getSupportActionBar().setTitle("지역검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new LocalThread().execute();
        ListView list=findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class LocalThread extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            String url="https://dapi.kakao.com/v2/local/search/keyword.json?query=롯데리아";
            String result=KakaoAPI.connect(url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                array= new JSONObject(s).getJSONArray("documents");
                adapter.notifyDataSetChanged();
                Log.i("size", array.length() + "");
            }catch (Exception e){
                Log.e("PostExecute:" , e.toString());
            }
            super.onPostExecute(s);
        }
    }

    class LocalAdapter extends BaseAdapter{
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
            View item = getLayoutInflater().inflate(R.layout.item_local, viewGroup, false);
            return item;
        }
    }
}//Activity