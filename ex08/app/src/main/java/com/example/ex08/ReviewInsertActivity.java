package com.example.ex08;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewInsertActivity extends AppCompatActivity {
    ReviewVO vo=new ReviewVO();
    RatingBar ratingBar;
    TextView contents, rating;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_insert);
        getSupportActionBar().setTitle("리뷰쓰기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        vo.setIndex(intent.getIntExtra("index", 0));
        vo.setEmail(intent.getStringExtra("email"));

        ratingBar=findViewById(R.id.ratingBar);
        rating=findViewById(R.id.rating);
        contents=findViewById(R.id.contents);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating.setText(String.valueOf(v));
                vo.setRating(v);
            }
        });

        //등록버튼을 클릭한경우
        findViewById(R.id.insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contents.getText().toString().equals("") || ratingBar.getRating()==0){
                    Toast.makeText(ReviewInsertActivity.this,"내용과 평점을 입력하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //리뷰등록
                vo.setContents(contents.getText().toString());
                Date date = new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                vo.setDate(sdf.format(date));
                db.collection("review").add(vo)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ReviewInsertActivity.this,
                                        "리뷰등록성공!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
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