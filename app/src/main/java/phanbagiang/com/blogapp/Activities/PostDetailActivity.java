package phanbagiang.com.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import phanbagiang.com.blogapp.R;

public class PostDetailActivity extends AppCompatActivity {
    private CircleImageView imgUser, imgCurrentUser;
    private TextView txtTitle, txtDes, txtDate;
    private ImageView imgMain;
    private EditText edtComment;
    private MaterialButton btnSend;
    private String postKey;

    //Firebase
    FirebaseUser mUser;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Window window=getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        addControls();
    }
    private void addControls(){
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        intent=getIntent();
        imgUser=findViewById(R.id.detail_user);
        imgCurrentUser=findViewById(R.id.detail_imaCurrenUser);
        txtTitle=findViewById(R.id.detail_title);
        txtDes=findViewById(R.id.detail_des);
        txtDate=findViewById(R.id.detail_date);
        imgMain=findViewById(R.id.detail_img);
        edtComment=findViewById(R.id.detail_edt_comment);
        btnSend=findViewById(R.id.detail_btnSend);

        Glide.with(getApplicationContext())
                .load(mUser.getPhotoUrl())
                .into(imgCurrentUser);
        String pictureMain=intent.getStringExtra("pic");
        String pictureUser=intent.getStringExtra("pic2");
        String title=intent.getStringExtra("title");
        String des=intent.getStringExtra("des");
        txtTitle.setText(title);
        txtDes.setText(des);
        long timeStamp=intent.getLongExtra("timeStamp",11);
        txtDate.setText(timeStampToString(timeStamp));
        postKey=intent.getStringExtra("postKey");
        Glide.with(getApplicationContext())
                .load(pictureMain)
                .into(imgMain);
        Glide.with(getApplicationContext())
                .load(pictureUser)
                .into(imgUser);
    }

    private String timeStampToString(long timeStamp){
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timeStamp);
        String date= DateFormat.format("dd-MM-yyyy, HH:mm",calendar).toString();
        return date;
}
}