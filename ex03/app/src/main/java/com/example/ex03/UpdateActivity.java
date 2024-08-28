package com.example.ex03;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateActivity extends AppCompatActivity {
    DBHelper helper;
    SQLiteDatabase db;
    EditText name, juso, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Intent intent=getIntent();
        int id = intent.getIntExtra("id", 0);

        name = findViewById(R.id.name);
        juso = findViewById(R.id.juso);
        phone = findViewById(R.id.phone);

        helper = new DBHelper(this);
        db = helper.getWritableDatabase();
        String sql = "select _id, name, juso, phone, photo from address where _id=" + id;
        Cursor cursor=db.rawQuery(sql, null);
        if(cursor.moveToNext()){
            name.setText(cursor.getString(1));
            juso.setText(cursor.getString(2));
            phone.setText(cursor.getString(3));
        }
        getSupportActionBar().setTitle("정보수정:" + id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}