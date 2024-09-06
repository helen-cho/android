package com.example.ex08;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class ReviewReadActivity extends AppCompatActivity {
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();

    TextView email, date, rating;
    EditText contents;
    RatingBar ratingBar;
    String id="";
    ReviewVO vo = new ReviewVO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_read);
        getSupportActionBar().setTitle("리뷰정보");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");

        email=findViewById(R.id.email);
        date=findViewById(R.id.date);
        rating=findViewById(R.id.rating);
        contents=findViewById(R.id.contents);
        ratingBar=findViewById(R.id.ratingBar);

        getRead();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBar.setRating(v);
                rating.setText(String.valueOf(v));
                vo.setRating(v);
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box=new AlertDialog.Builder(ReviewReadActivity.this);
                box.setTitle("질의");
                box.setMessage(id + "번 리뷰를 삭제하실래요?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("review").document(id).delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        finish();
                                    }
                                });
                    }
                });
                box.setNegativeButton("아니오", null);
                box.show();
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box=new AlertDialog.Builder(ReviewReadActivity.this);
                box.setTitle("질의");
                box.setMessage(id + "번 리뷰를 수정하실래요?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        vo.setContents(contents.getText().toString());
                        db.collection("review")
                            .document(id)
                            .set(vo)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    finish();
                                }
                            });
                    }
                });
                box.setNegativeButton("아니오", null);
                box.show();
            }
        });
    }//onCreate

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getRead(){
        db.collection("review")
            .document(id)
            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot doc=task.getResult();
                    String strEmail=doc.getData().get("email").toString();
                    email.setText(strEmail);
                    if(!user.getEmail().equals(strEmail)){
                        contents.setEnabled(false);
                        findViewById(R.id.buttons).setVisibility(View.INVISIBLE);
                        ratingBar.setIsIndicator(true);
                    }
                    contents.setText(doc.getData().get("contents").toString());
                    date.setText(doc.getData().get("date").toString());

                    String strRating=doc.getData().get("rating").toString();
                    DecimalFormat df=new DecimalFormat("#.0");
                    float fltRating=Float.parseFloat(strRating);
                    ratingBar.setRating(fltRating);
                    rating.setText(df.format(fltRating));

                    vo.setEmail(email.getText().toString());
                    vo.setContents(contents.getText().toString());
                    vo.setDate(date.getText().toString());
                    vo.setRating(fltRating);
                    vo.setId(id);
                    String index=doc.getData().get("index").toString();
                    vo.setIndex(Integer.parseInt(index));
                }
            });
    }
}//Activity