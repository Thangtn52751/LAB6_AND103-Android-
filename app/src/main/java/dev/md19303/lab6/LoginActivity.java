package dev.md19303.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputEditText txtUser, txtPass;
    Button btnSignIn;
    TextView tvForgotPassword,tvSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        txtUser = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        btnSignIn = findViewById(R.id.btnSignIn);

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);

        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });


        btnSignIn.setOnClickListener(v -> {
            String email = txtUser.getText().toString();
            String pass = txtPass.getText().toString();

            if(email.isEmpty() || pass.isEmpty()){
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin !!", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công !!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,AddCakeActivity.class));
                            }
                            else{
                                Log.w("zzz", "onComplete: ", task.getException());
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

    }
}