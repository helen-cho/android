package com.example.ex08;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class MypageFragment extends Fragment {
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage=FirebaseStorage.getInstance();

    EditText email, name, phone, address;
    CircleImageView photo;
    UserVO vo = new UserVO();
    String strFile="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mypage, container, false);

        email=view.findViewById(R.id.email);
        name=view.findViewById(R.id.name);
        phone=view.findViewById(R.id.phone);
        photo=view.findViewById(R.id.photo);
        address=view.findViewById(R.id.address);

        email.setText(user.getEmail());
        vo.setEmail(user.getEmail());
        readUser();

        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box=new AlertDialog.Builder(getActivity());
                box.setTitle("질의");
                box.setMessage("사용자정보를 수정하실래요?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!strFile.equals("")) {
                            String fileName = System.currentTimeMillis() + "/jpg";
                            StorageReference ref = storage.getReference("/photos/" + fileName);
                            Uri file = Uri.fromFile(new File(strFile));
                            ref.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            vo.setPhoto(uri.toString());
                                            updateUser();
                                        }
                                    });
                                }
                            });
                        }else{
                            updateUser();
                        }
                    }
                });
                box.setNegativeButton("아니오", null);
                box.show();
            }
        });

        //앨범 버튼 클릭시
        view.findViewById(R.id.album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityResult.launch(intent);
            }
        });

        return view;
    }//onCreateView


    //앨범에서 이미지 선택후
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode() == RESULT_OK) {
                    Cursor cursor=getActivity().getContentResolver().query(o.getData().getData(), null, null, null, null);
                    cursor.moveToFirst();
                    int index=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    strFile = cursor.getString(index);
                    photo.setImageBitmap(BitmapFactory.decodeFile(strFile));
                    cursor.close();
                }
            }
        }
    );  //startActivityResult

    public void updateUser(){
        vo.setName(name.getText().toString());
        vo.setPhone(phone.getText().toString());
        vo.setAddress(address.getText().toString());
        db.collection("user")
            .document(user.getUid())
            .set(vo)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getActivity(), "수정완료", Toast.LENGTH_SHORT).show();
                    readUser();
                }
            });
    }

    public void readUser(){
        db.collection("user")
            .document(user.getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot doc=task.getResult();
                    if(doc.getData() != null){
                        vo.setName(doc.getData().get("name").toString());
                        vo.setPhone(doc.getData().get("phone").toString());
                        vo.setAddress(doc.getData().get("address").toString());

                        name.setText(vo.getName());
                        phone.setText(vo.getPhone());
                        address.setText(vo.getAddress());

                        if(doc.getData().get("photo") != null) {
                            vo.setPhoto(doc.getData().get("photo").toString());
                            Picasso.with(getActivity()).load(vo.getPhoto()).into(photo);
                        }
                    }
                }
            });
    }
}//Fragment