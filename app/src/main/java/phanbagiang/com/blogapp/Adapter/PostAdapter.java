package phanbagiang.com.blogapp.Adapter;

import android.content.Context;
import android.content.Intent;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import phanbagiang.com.blogapp.Activities.PostDetailActivity;
import phanbagiang.com.blogapp.R;
import phanbagiang.com.blogapp.model.Post;
import phanbagiang.com.blogapp.model.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context mContext;
    private List<Post>mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_row_home_fragment,parent,false);
        return new ViewHolder(view);
    }

    private String convertLongTimeToStringTime(long time){
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        Date date=calendar.getTime();
        String date2="";
        if(date.getMonth()>10){
            date2 = DateFormat.format("dd 'tháng' MM 'lúc' HH:mm",calendar).toString();
        }
        else{
            date2 = DateFormat.format("dd 'tháng' M 'lúc' HH:mm",calendar).toString();
        }
        return date2;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Post post=mData.get(position);
        Glide.with(mContext).load(post.getPicture()).into(holder.imgMain);
        Glide.with(mContext).load(post.getUserPhoto()).into(holder.imgUser);
        holder.txtTitle.setText(post.getTitle());
        holder.txtUsername.setText(post.getUserName());
        final long timeSt=(long)post.getTimeStamp();
        holder.txtDate.setText(convertLongTimeToStringTime(timeSt));
        DatabaseReference mReference= FirebaseDatabase.getInstance().getReference("Users");
        mReference.child(post.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if(user.isCheck()){
                    holder.imgTick.setVisibility(View.VISIBLE);
                }
                else{
                    holder.imgTick.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.imgMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("pic",post.getPicture());
                intent.putExtra("pic2",post.getUserPhoto());
                intent.putExtra("title",post.getTitle());
                intent.putExtra("des",post.getDes());

                intent.putExtra("timeStamp",timeSt);
                intent.putExtra("postKey",post.getPostKey());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgMain, imgUser, imgTick;
        TextView txtTitle, txtUsername, txtDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMain=itemView.findViewById(R.id.row_imgMain);
            imgUser=itemView.findViewById(R.id.row_imgUser);
            imgTick=itemView.findViewById(R.id.row_tick);
            txtTitle=itemView.findViewById(R.id.row_txtUser);
            txtUsername=itemView.findViewById(R.id.row_username);
            txtDate=itemView.findViewById(R.id.row_date);
        }
    }
}
