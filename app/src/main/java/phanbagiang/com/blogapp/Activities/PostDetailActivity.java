package phanbagiang.com.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import phanbagiang.com.blogapp.Adapter.CommentAdapter;
import phanbagiang.com.blogapp.R;
import phanbagiang.com.blogapp.model.Comment;
import phanbagiang.com.blogapp.model.User;



public class PostDetailActivity extends AppCompatActivity {
    private CircleImageView imgUser, imgCurrentUser;
    private TextView txtTitle, txtDes, txtDate;
    private ImageView imgMain;
    private EditText edtComment;
    private MaterialButton btnSend;
    private String postKey;

    private RecyclerView list_comment;
    private CommentAdapter commentAdapter;
    private List<Comment>mData;
    //Firebase
    FirebaseUser mUser;
    DatabaseReference mReference;
    DatabaseReference mRefUserCurrent;

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Window window=getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        addControls();
        addEvents();
    }
    private void addEvents(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content=edtComment.getText().toString();
                if(content.isEmpty()){
                    Snackbar.make(btnSend,"Comment is empty !", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                else{

                    mRefUserCurrent.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user=dataSnapshot.getValue(User.class);
                            Comment comment=new Comment(content,user.getUserName(),user.getUserId(),user.getPhotoImage());
                            mReference.child(postKey).push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Snackbar.make(btnSend,"OK", BaseTransientBottomBar.LENGTH_SHORT).show();
                                    edtComment.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(btnSend,e.getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    ///




                }

            }
        });
    }
    private void addControls(){
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        mReference= FirebaseDatabase.getInstance().getReference("Comments");
        mRefUserCurrent=FirebaseDatabase.getInstance().getReference("Users");
        intent=getIntent();
        imgUser=findViewById(R.id.detail_user);
        imgCurrentUser=findViewById(R.id.detail_imaCurrenUser);
        txtTitle=findViewById(R.id.detail_title);
        txtDes=findViewById(R.id.detail_des);
        txtDate=findViewById(R.id.detail_date);
        imgMain=findViewById(R.id.detail_img);
        edtComment=findViewById(R.id.detail_edt_comment);
        btnSend=findViewById(R.id.detail_btnSend);

        mRefUserCurrent.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext())
                        .load(user.getPhotoImage())
                        .into(imgCurrentUser);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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
        mData=new ArrayList<>();
        list_comment=findViewById(R.id.list_comment);
        commentAdapter=new CommentAdapter(PostDetailActivity.this,mData);
        list_comment.setHasFixedSize(true);
        list_comment.setAdapter(commentAdapter);
        list_comment.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Comments");
        reference.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Comment comment=data.getValue(Comment.class);
                    mData.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private String timeStampToString(long timeStamp){
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timeStamp);
        String date= DateFormat.format("c. dd/MM/yyyy,' lúc' HH:mm",calendar).toString();
        return date;
}
}