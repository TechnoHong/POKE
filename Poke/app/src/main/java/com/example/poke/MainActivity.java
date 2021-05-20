package com.example.poke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private static final String Tag = "MainActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //로그인 되어 있지 않으면 로그인 화면으로
        if(user == null) {
            myStartActivity(LoginActivity.class);
        }

        //회원정보가 없으면 회원등록 화면 나옴
        else {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            String uid = user.getUid();

           nullStartActivity(uid,"allergy");
           nullStartActivity(uid,"users");
            nullStartActivity(uid,"preference");
        }

        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoPasswordResetButton).setOnClickListener(onClickListener);
        findViewById(R.id.revokeButton).setOnClickListener(onClickListener);
        findViewById(R.id.preButton).setOnClickListener(onClickListener);
        findViewById(R.id.myInfoButton).setOnClickListener(onClickListener);
    }

    public void scanBarcode(View view) {
        new IntentIntegrator(this).initiateScan();
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    myStartActivity(LoginActivity.class);
                    break;
                case R.id.gotoPasswordResetButton:
                    myStartActivity(PasswordResetActivity.class);
                    break;
                case R.id.revokeButton:
                    revokeAccess();
                    myStartActivity(LoginActivity.class);
                    break;
                case R.id.preButton:
                    myStartActivity(AllergyActivity.class);
                    break;
                case R.id.myInfoButton:
                    myStartActivity(MyInfoActivity.class);
//                case R.id.BarcodeScanButton:
//                    myStartActivity(BarcodeScanActivity.class);
//                    break;
            }
        }
    };

    private void nullStartActivity(String uid, String child){
        mDatabase.child(child).child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if(dataSnapshot != null) {
                        if (dataSnapshot.exists()) {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        } else {
                           if(child.equals("allergy")){
                               myStartActivity(AllergyActivity.class);
                           }
                           else if(child.equals("users")){
                               myStartActivity(MemberInitActivity.class);
                           }
                           else if(child.equals("preference")){
                               myStartActivity(preferenceActivity.class);
                           }
                        }
                    }
                }
                else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });

    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void revokeAccess() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(uid).removeValue();

        mAuth.getCurrentUser().delete();
    }
}