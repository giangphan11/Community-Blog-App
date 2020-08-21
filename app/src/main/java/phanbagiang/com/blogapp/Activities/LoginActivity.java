package phanbagiang.com.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import phanbagiang.com.blogapp.R;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;

public class LoginActivity extends AppCompatActivity {

    EditText edtMail, edtPass;
    Button btnLogin, btnRegister;
    ProgressBar progressBar;
    Toolbar toolbar;
    // firebase
    private FirebaseAuth mAuth;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setSupportActionBar(toolbar);
        addControls();
        addEvents();
    }
    private void addControls(){
        progressBar=findViewById(R.id.login_progress);
        mAuth=FirebaseAuth.getInstance();
        edtMail=findViewById(R.id.log_edtEmail);
        edtPass=findViewById(R.id.log_edtPass);
        btnLogin=findViewById(R.id.log_btnLogin);
        btnRegister=findViewById(R.id.btnRegister);
        intent=new Intent(LoginActivity.this,MainActivity.class);
    }
    private void addEvents(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
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
                    showMessage("Vui lòng điền đầy đủ các trường !!!");
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
            updateUI();
        }
    }

    private void updateUI(){
        startActivity(intent);
        finish();
    }
    private void showMessage(String mess){
        Snackbar.make(btnLogin,mess,Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show();
        //Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }
    private void logIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            showMessage("Đăng nhập thành công !");
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            showMessage(task.getException().getMessage());
                            btnLogin.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            // ...
                        }

                        // ...
                    }
                });
    }
}