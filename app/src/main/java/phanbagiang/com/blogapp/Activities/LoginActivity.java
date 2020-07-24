package phanbagiang.com.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

import phanbagiang.com.blogapp.R;

public class LoginActivity extends AppCompatActivity {
    TextView login_reg;
    EditText edtMail, edtPass;
    Button btnLogin;
    ProgressBar progressBar;

    // firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addControls();
        addEvents();
    }
    private void addControls(){
        progressBar=findViewById(R.id.login_progress);
        mAuth=FirebaseAuth.getInstance();
        edtMail=findViewById(R.id.log_edtEmail);
        edtPass=findViewById(R.id.log_edtPass);
        btnLogin=findViewById(R.id.log_btnLogin);
        login_reg=findViewById(R.id.login_register);

        Intent intent=getIntent();
        if(intent!=null){
            edtMail.setText(intent.getStringExtra("em"));
            edtPass.setText(intent.getStringExtra("pass"));
        }
    }
    private void addEvents(){
        login_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail=edtMail.getText().toString();
                final String pass=edtPass.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                if(mail.isEmpty()||pass.isEmpty()){
                    showMessage("Please input your Email or Password !");
                    btnLogin.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else{
                    logIn(mail,pass);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            updateUI(user);
        }
    }

    private void updateUI(FirebaseUser user){
        if(user!=null){
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            intent.putExtra("name",user.getDisplayName());
            startActivity(intent);
            finish();
        }
    }
    private void showMessage(String mess){
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }
    private void logIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            showMessage("Login success!");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            showMessage(task.getException().getMessage());
                            btnLogin.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });
    }
}