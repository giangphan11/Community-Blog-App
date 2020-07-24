package phanbagiang.com.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import phanbagiang.com.blogapp.R;

public class RegisterActivity extends AppCompatActivity {
    Button regBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addControls();
        addEvents();
    }

    private void addControls() {
        regBtn=findViewById(R.id.regbtn);
        progressBar=findViewById(R.id.progressBar);
    }
    private void addEvents(){
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                regBtn.setVisibility(View.INVISIBLE);
            }
        });
    }
}