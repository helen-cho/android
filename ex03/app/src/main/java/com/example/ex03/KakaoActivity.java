package com.example.ex03;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class KakaoActivity extends AppCompatActivity {
    ArrayList<HashMap<String,Object>> array=new ArrayList<>();
    BookAdapter adapter = new BookAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao);
        getSupportActionBar().setTitle("카카오검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new BookThread().execute();
        ListView list=findViewById(R.id.list);
        list.setAdapter(adapter);
    }//onCreate

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //카카오 API 호출 쓰레드
    class BookThread extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            String url="https://dapi.kakao.com/v3/search/book?target=title&query=자바";
            String result = KakaoAPI.connect(url);
            Log.i("result", result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            bookParser(s);
            adapter.notifyDataSetChanged();
            super.onPostExecute(s);
        }
    }

    //결과파싱
    public void bookParser(String result){
        try{
            JSONArray jArray = new JSONObject(result).getJSONArray("documents");
            Log.i("size", jArray.length() + "");
            for(int i=0; i<jArray.length(); i++){
                JSONObject obj=jArray.getJSONObject(i);
                HashMap<String,Object> map=new HashMap<>();
                map.put("title", obj.getString("title"));
                map.put("price", obj.getString("sale_price"));
                map.put("contents", obj.getString("contents"));
                map.put("authors", obj.getString("authors"));
                map.put("image", obj.getString("thumbnail"));
                map.put("publisher", obj.getString("publisher"));
                array.add(map);
            }
        }catch (Exception e){
            Log.i("파싱오류:", e.toString());
        }
    }

    //Adapter정의
    class BookAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return array.size();
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
            HashMap<String, Object> map=array.get(i);
            View item = getLayoutInflater().inflate(R.layout.item_book,viewGroup,false);
            TextView title=item.findViewById(R.id.title);
            title.setText(map.get("title").toString());
            TextView price=item.findViewById(R.id.price);
            int intPrice =Integer.parseInt(map.get("price").toString());
            DecimalFormat df=new DecimalFormat("#,###원");
            price.setText(df.format(intPrice));
            TextView authors=item.findViewById(R.id.authors);
            authors.setText(map.get("authors").toString());
            String strImage = map.get("image").toString();
            ImageView image = item.findViewById(R.id.image);
            if(strImage.equals("")){
                image.setImageResource(R.drawable.no_image);
            }else{
                Picasso.with(KakaoActivity.this).load(strImage).into(image);
            }
            return item;
        }
    }
}//Activity