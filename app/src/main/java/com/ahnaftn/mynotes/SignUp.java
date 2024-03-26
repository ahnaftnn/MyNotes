package com.ahnaftn.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    EditText emailEdit,passEdit,confirmPassEdit;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEdit=findViewById(R.id.email_edit);
        passEdit=findViewById(R.id.password_edit_text);
        confirmPassEdit=findViewById(R.id.confirm_password_edit_text);
        createAccountBtn= findViewById(R.id.create_account_btn);
        progressBar=findViewById(R.id.progress_bar);
        loginBtn=findViewById(R.id.login_btn);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailEdit.getText().toString();
                String password=passEdit.getText().toString();
                String confirmPassword=confirmPassEdit.getText().toString();

                boolean isValidated= validateData(email,password,confirmPassword);
                if(!isValidated){
                    return;
                }
                createAccountinFirebase(email,password);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignUp.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    void createAccountinFirebase(String email,String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            startActivity(new Intent(SignUp.this,Email_Check.class));
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();

                            finish();
                        } else{
                            Utility.showToast(SignUp.this,task.getException().getLocalizedMessage());
                        }
                    }
                });
    }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        } else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email,String password, String confirmPassword)
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
        if(!password.equals(confirmPassword)){
            confirmPassEdit.setError("Password not matched");
            return false;
        }
        return true;
    }
}