package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = (v) -> {
            switch (v.getId()){
                case R.id.signUpButton:
                    signUp();
                    break;
        }
    };

    private void signUp(){
        String email=((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password=((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck=((EditText)findViewById(R.id.passwordCheckEditText)).getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0){
            if(password.equals(passwordCheck)){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("??????????????? ?????????????????????.");
                                    myStartActivity(MemberInitActivity.class);
                                }
                                //????????? ?????? ?????? ??? ???????????????????????? ??????
                                else {
                                    if(password.length() < 6) {
                                        startToast("??????????????? 6?????? ???????????? ??????????????????.");
                                    }
                                    else {
                                        startToast("???????????? ?????? ??????????????????");
                                    }
                                }
                            }
                        });
            }else{
                startToast("??????????????? ???????????? ????????????.");
            }

        } else{
            startToast("????????? ?????? ??????????????? ???????????????.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
