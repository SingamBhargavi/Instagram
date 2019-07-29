package com.bhargavi.instagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    EditText username,email,password,fullname;
    Button signup;
    TextView txt_login;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        fullname=findViewById(R.id.fullname);
        password=findViewById(R.id.password);
        signup=findViewById(R.id.signup);
        txt_login=findViewById(R.id.txt_login);

        auth=FirebaseAuth.getInstance();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(SignupActivity.this);
                pd.setMessage("Please Wait!");
                pd.show();

                String str_username = username.getText().toString();
                String str_password = password.getText().toString();
                String str_email = email.getText().toString();
                String str_fullname = fullname.getText().toString();


                if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname) ||
                        TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)) {
                    Toast.makeText(SignupActivity.this, "All Fields are Required!", Toast.LENGTH_SHORT).show();
                } else if (str_password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Password must be atleast 6 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    signup(str_username,str_fullname,str_email,str_password);
                }
            }
            });

                }
                private void signup(final String username, final String fullname, String email, String password){
                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = auth.getCurrentUser();
                                        String userid = firebaseUser.getUid();

                                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                                        HashMap<String,Object>hashMap=new HashMap<>();
                                        hashMap.put("id",userid);
                                        hashMap.put("username",username.toLowerCase());
                                        hashMap.put("fullname",fullname);
                                        hashMap.put("bio","");
                                        hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/instagram-e5e54.appspot.com/o/Users%2Fprofile.jpg?alt=media&token=97cbdef3-64df-460f-99c4-9158bd651c96");

                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    pd.dismiss();
                                                    Intent intent=new Intent(SignupActivity.this,MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                    else
                                    {
                                        pd.dismiss();
                                        Toast.makeText(SignupActivity.this,"You cannot register with the email!!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
