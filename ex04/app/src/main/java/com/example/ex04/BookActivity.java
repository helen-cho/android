package com.example.ex04;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class BookActivity extends AppCompatActivity {
    JSONArray array = new JSONArray(); //데이터
    BookAdapter adapter = new BookAdapter();
    String query="안드로이드";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        getSupportActionBar().setTitle("도서검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new BookThread().execute();
        ListView list=findViewById(R.id.list);
        list.setAdapter(adapter);
    }//onCreate

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class BookThread extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            String url="https://dapi.kakao.com/v3/search/book?target=title&query=" + query;
            String result=KakaoAPI.connect(url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                array=new JSONObject(s).getJSONArray("documents");
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            super.onPostExecute(s);
        }
    }

    //어댑터정의
    class BookAdapter extends BaseAdapter{
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
            View item= getLayoutInflater().inflate(R.layout.item_book, viewGroup, false);
            try {
                JSONObject obj = array.getJSONObject(i);
                TextView title=item.findViewById(R.id.title);
                title.setText(obj.getString("title"));

                TextView price=item.findViewById(R.id.price);
                int intPrice = obj.getInt("sale_price");
                DecimalFormat df=new DecimalFormat("#,###원");
                price.setText(df.format(intPrice));

                String strImage =obj.getString("thumbnail");
                ImageView image= item.findViewById(R.id.image);
                if(strImage.equals("")){
                    image.setImageResource(R.drawable.no_image);
                }else {
                    Picasso.with(BookActivity.this).load(strImage).into(image);
                }

                TextView authors = item.findViewById(R.id.authors);
                authors.setText(obj.getString("authors"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return item;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        SearchView searchView=(SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                query = s;
                new BookThread().execute();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}//Activity