package com.example.ex07;

import static com.example.ex07.RemoteService.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    RemoteService service;
    Retrofit retrofit;
    JSONArray array=new JSONArray();
    WineAdapter adapter=new WineAdapter();
    int total=0;
    int page=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("쇼핑몰");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service=retrofit.create(RemoteService.class);
        getList();

        RecyclerView list=findViewById(R.id.list);
        list.setAdapter(adapter);
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        list.setLayoutManager(manager);

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(total > array.length() && !list.canScrollVertically(1)){
                    page++;
                    getList();
                }
            }
        });
    }//onCreate

    public void getList(){
        Call<HashMap<String, Object>> call=service.list(page);
        call.enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    total = object.getInt("total");
                    JSONArray arr=object.getJSONArray("list");
                    for(int i=0; i<arr.length(); i++){
                        array.put(arr.getJSONObject(i));
                    }
                    adapter.notifyDataSetChanged();
                }catch(Exception e){
                    Log.i("getList", e.toString());
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }else if(item.getItemId()==R.id.login){
            finish();
            Intent intent=new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.mypage){
            finish();
            Intent intent=new Intent(this, JoinActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        FirebaseUser user=mAuth.getCurrentUser();
        if(user == null){
            menu.findItem(R.id.mypage).setVisible(false);
            menu.findItem(R.id.login).setVisible(true);
        }else{
            menu.findItem(R.id.mypage).setVisible(true);
            menu.findItem(R.id.login).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    class WineAdapter extends RecyclerView.Adapter<WineAdapter.ViewHolder>{
        @NonNull
        @Override
        public WineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item=getLayoutInflater().inflate(R.layout.item_wine,parent,false);
            return new ViewHolder(item);
        }

        @Override
        public void onBindViewHolder(@NonNull WineAdapter.ViewHolder holder, int position) {
            try {
                JSONObject obj=array.getJSONObject(position);
                int intIndex = obj.getInt("index");
                String strImage=obj.getString("wine_image");
                String strName=obj.getString("wine_name");
                String strCountry=obj.getString("wine_country");
                //String strPrice=obj.getString("wine_price");
                String strType=obj.getString("wine_type");
                holder.index.setText(String.valueOf(intIndex));
                holder.name.setText(strName);
                holder.country.setText(strCountry);
                //holder.price.setText(strPrice);
                holder.type.setText(strType);
                Picasso.with(MainActivity.this).load(strImage).into(holder.image);
            } catch (JSONException e) {
                Log.i("error", e.toString());
                throw new RuntimeException(e);
            }
        }

        @Override
        public int getItemCount() {
            return array.length();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView image;
            TextView index, name, country, type, price;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                index=itemView.findViewById(R.id.index);
                image=itemView.findViewById(R.id.image);
                name=itemView.findViewById(R.id.name);
                country=itemView.findViewById(R.id.country);
                type=itemView.findViewById(R.id.type);
                price=itemView.findViewById(R.id.price);
            }
        }
    }//Adapter
}//Activity