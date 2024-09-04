package com.example.ex07;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinActivity extends AppCompatActivity {
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser user=mAuth.getCurrentUser();
    EditText email, name, phone, address;
    String strEmail, uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        getSupportActionBar().setTitle("회원정보");

        FirebaseUser user=mAuth.getCurrentUser();
        strEmail = user.getEmail();
        uid = user.getUid();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email=findViewById(R.id.email);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        email.setText(user.getEmail());
        readUser();

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserVO vo=new UserVO();
                vo.setEmail(strEmail);
                vo.setName(name.getText().toString());
                vo.setPhone(phone.getText().toString());
                vo.setAddress(address.getText().toString());
                db.collection("user").document(uid).set(vo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(JoinActivity.this,"수정완료!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void readUser(){
        db.collection("user").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if (doc.getData() != null) {
                    Log.i("doc", doc.getId());
                    name.setText(doc.getData().get("name").toString());
                    phone.setText(doc.getData().get("phone").toString());
                    address.setText(doc.getData().get("address").toString());
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            Intent intent=new Intent(JoinActivity.this, MainActivity.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.logout){
            mAuth.signOut();
            finish();
            Intent intent=new Intent(JoinActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mypage, menu);
        return super.onCreateOptionsMenu(menu);
    }
}