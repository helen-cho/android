package com.example.ex03;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

public class KakaoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao);
        getSupportActionBar().setTitle("카카오검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new BookThread().execute();
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
            super.onPostExecute(s);
        }
    }

    //결과파싱
    public void bookParser(String result){
        try{
            JSONArray jArray = new JSONObject(result).getJSONArray("documents");
            Log.i("size", jArray.length() + "");
        }catch (Exception e){
            Log.i("파싱오류:", e.toString());
        }
    }
}//Activity