package com.example.ex08;

import static com.example.ex08.RemoteService.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReadActivity extends AppCompatActivity {
    Retrofit retrofit;
    RemoteService service;

    int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_wine);

        ImageView image=findViewById(R.id.image);
        TextView name=findViewById(R.id.name);
        TextView txtIndex=findViewById(R.id.index);
        RatingBar ratingBar=findViewById(R.id.rating);

        Intent intent=getIntent();
        index = intent.getIntExtra("index", 0);
        getSupportActionBar().setTitle("와인정보: " + index);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RemoteService.class);
        Call<HashMap<String, Object>> call=service.read(index);
        call.enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                HashMap<String,Object> vo=response.body();
                name.setText(vo.get("wine_name").toString());
                Picasso.with(ReadActivity.this)
                        .load(vo.get("wine_image").toString()).into(image);
                txtIndex.setText(index + "");
                Float rating =Float.parseFloat(vo.get("rating").toString());
                ratingBar.setRating(rating);
            }

            @Override
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}