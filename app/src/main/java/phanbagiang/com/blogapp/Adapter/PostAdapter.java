package phanbagiang.com.blogapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import phanbagiang.com.blogapp.R;
import phanbagiang.com.blogapp.model.Post;

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post=mData.get(position);
        Glide.with(mContext).load(post.getPicture()).into(holder.imgMain);
        Glide.with(mContext).load(post.getUserPhoto()).into(holder.imgUser);
        holder.txtTitle.setText(post.getTitle());
        holder.txtUsername.setText(post.getUserName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgMain, imgUser;
        TextView txtTitle, txtUsername;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMain=itemView.findViewById(R.id.row_imgMain);
            imgUser=itemView.findViewById(R.id.row_imgUser);
            txtTitle=itemView.findViewById(R.id.row_txtUser);
            txtUsername=itemView.findViewById(R.id.row_username);
        }
    }
}