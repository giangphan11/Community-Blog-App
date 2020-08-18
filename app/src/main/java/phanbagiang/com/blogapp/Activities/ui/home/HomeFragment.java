package phanbagiang.com.blogapp.Activities.ui.home;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import phanbagiang.com.blogapp.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FloatingActionButton pop_fab;
    private Dialog dialogPost;

    FirebaseUser mUser;

    private EditText edtTitle, edtDes;
    private ImageView img_Avatarr, imgMain, imgPost;
    private ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        initPopup();
        pop_fab=root.findViewById(R.id.home_fab);
        pop_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPost.show();
            }
        });
        return root;
    }


    private void initPopup(){
        dialogPost=new Dialog(getContext());
        dialogPost.setContentView(R.layout.popup_add_post);
        dialogPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        dialogPost.getWindow().getAttributes().gravity= Gravity.TOP;

        // init view
        edtDes=dialogPost.findViewById(R.id.edt_desc);
        edtTitle=dialogPost.findViewById(R.id.edt_Title);
        img_Avatarr=dialogPost.findViewById(R.id.imageView);
        imgMain=dialogPost.findViewById(R.id.pop_image_main);
        imgPost=dialogPost.findViewById(R.id.pop_add);
        progressBar=dialogPost.findViewById(R.id.pop_progressBar2);

        Glide.with(getContext()).load(mUser.getPhotoUrl()).into(img_Avatarr);
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPost.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
}