package com.example.ex03;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    DBHelper helper;
    SQLiteDatabase db;
    MyAdpter adpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DBHelper(this);
        db = helper.getReadableDatabase();
        String sql="select _id, name, juso, phone, photo from address";
        //데이터생성
        Cursor cursor=db.rawQuery(sql, null);
        adpter = new MyAdpter(this, cursor);
        ListView list=findViewById(R.id.list);
        list.setAdapter(adpter);
    }//onCreate

    class MyAdpter extends CursorAdapter{
        public MyAdpter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View view=getLayoutInflater().inflate(R.layout.item,viewGroup,false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }
    }
}//Activity