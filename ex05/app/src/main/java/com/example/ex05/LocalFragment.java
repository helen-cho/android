package com.example.ex05;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LocalFragment extends Fragment {
    JSONArray array = new JSONArray();
    LocalAdapter adapter=new LocalAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_local, container, false);
        new LocalThread().execute();

        RecyclerView list= view.findViewById(R.id.list);
        list.setAdapter(adapter);
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(5,
                StaggeredGridLayoutManager.HORIZONTAL);
        list.setLayoutManager(manager);
        return view;
    }

    class LocalThread extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            String url="https://dapi.kakao.com/v2/local/search/keyword.json?size=10&query=루원시티";
            String result=KakaoAPI.connect(url);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                array = new JSONObject(s).getJSONArray("documents");
                Log.i("size", array.length() + "");
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            super.onPostExecute(s);
        }
    }

    class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.ViewHolder>{
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView name, phone, address;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.name);
                phone=itemView.findViewById(R.id.phone);
            }
        }

        @NonNull
        @Override
        public LocalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=getLayoutInflater().inflate(R.layout.item_local, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LocalAdapter.ViewHolder holder, int position) {
            try {
                JSONObject obj=array.getJSONObject(position);
                holder.name.setText(obj.getString("place_name"));
                //holder.phone.setText(obj.getString("phone"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public int getItemCount() {
            return array.length();
        }
    }

}//Activity