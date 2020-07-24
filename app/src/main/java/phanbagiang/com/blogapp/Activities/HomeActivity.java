package phanbagiang.com.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import phanbagiang.com.blogapp.R;

public class HomeActivity extends AppCompatActivity {
    private Button btnLogout;
    private TextView txtTen;

    //firebase
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addControls();
        addEvents();
    }
    private void addControls(){
        txtTen=findViewById(R.id.home_txtTen);
        btnLogout=findViewById(R.id.home_btnLogout);
        Intent intent=getIntent();
        txtTen.setText(intent.getStringExtra("name"));
        mAuth=FirebaseAuth.getInstance();
    }
    private void addEvents(){
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}