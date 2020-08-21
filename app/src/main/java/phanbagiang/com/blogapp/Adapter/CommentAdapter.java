package phanbagiang.com.blogapp.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import phanbagiang.com.blogapp.R;
import phanbagiang.com.blogapp.model.Comment;
import phanbagiang.com.blogapp.model.User;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context mContext;
    private List<Comment>mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.row_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Comment comment=mData.get(position);
        Glide.with(mContext)
                .load(comment.getUserImage())
                .into(holder.imgUser);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(comment.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if(user.isCheck()){
                    holder.img_check.setVisibility(View.VISIBLE);
                }
                else{
                    holder.img_check.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.txtUserName.setText(comment.getUserName());
        holder.txtContent.setText(comment.getContent());
        long time= (long) comment.getTimeStamp();
        holder.txtTime.setText(convertLongTimeToStringTime(time));
    }
    private String convertLongTimeToStringTime(long time){
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date= DateFormat.format("c. HH:mm",calendar).toString();
        return date;
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imgUser;
        ImageView img_check;
        TextView txtContent, txtUserName, txtTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser=itemView.findViewById(R.id.comment_imguser);
            txtContent=itemView.findViewById(R.id.comment_Content);
            txtTime=itemView.findViewById(R.id.comment_time);
            txtUserName=itemView.findViewById(R.id.comment_UserName);
            img_check=itemView.findViewById(R.id.comment_check);
        }
    }
}
