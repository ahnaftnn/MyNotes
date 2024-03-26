package com.ahnaftn.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailEdit,passEdit;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtn;
    public static final String shared_pref= "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit=findViewById(R.id.email_edit);
        passEdit=findViewById(R.id.password_edit_text);
        createAccountBtn= findViewById(R.id.create_account_btn);
        progressBar=findViewById(R.id.progress_bar);
        loginBtn=findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailEdit.getText().toString();
                String password=passEdit.getText().toString();

                boolean isValidated= validateData(email,password);
                if(!isValidated){
                    return;
                }
                loginAccountinFirebase(email,password);
            }
        });
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

        checkBox();
    }

    private void checkBox() {
        SharedPreferences sharedPreferences = getSharedPreferences(shared_pref,MODE_PRIVATE);
        String check = sharedPreferences.getString("name","");
        if(check.equals("true")){
            Intent intent= new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        }
    }

    void loginAccountinFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){

                    //storing login data
                    SharedPreferences sharedPreferences = getSharedPreferences(shared_pref,MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences.edit();

                    editor.putString("name","true");
                    editor.apply();

                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        startActivity(intent);

                    }else {
                        Utility.showToast(LoginActivity.this,"Please verify your Email.");
                    }
                }else{
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }

            }
        });
    }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email,String password)
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEdit.setError("Wrong Email");
            return false;
        }
        if(password.length()<6){
            passEdit.setError("Password is too short");
            return false;
        }
        return true;
    }
}