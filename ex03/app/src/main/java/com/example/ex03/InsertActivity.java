package com.example.ex03;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InsertActivity extends AppCompatActivity {
    DBHelper helper;
    SQLiteDatabase db;
    EditText name, juso, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        getSupportActionBar().setTitle("주소등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name= findViewById(R.id.name);
        juso= findViewById(R.id.juso);
        phone = findViewById(R.id.phone);

        helper=new DBHelper(this);
        db = helper.getWritableDatabase();
        findViewById(R.id.btnInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName = name.getText().toString();
                String strJuso = juso.getText().toString();
                String strPhone = phone.getText().toString();
                if(strName.equals("") || strJuso.equals("") || strPhone ==""){
                    Toast.makeText(
                            InsertActivity.this,"모든내용을 입력하세요!",Toast.LENGTH_SHORT).show();
                }else{
                    String sql="insert into address(name,juso,phone) values(";
                    sql += "'" + strName + "',";
                    sql += "'" + strJuso + "',";
                    sql += "'" + strPhone + "')";
                    db.execSQL(sql);
                    finish();
                }
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