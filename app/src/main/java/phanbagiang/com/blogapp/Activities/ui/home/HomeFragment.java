package phanbagiang.com.blogapp.Activities.ui.home;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import phanbagiang.com.blogapp.Adapter.PostAdapter;
import phanbagiang.com.blogapp.R;
import phanbagiang.com.blogapp.model.Post;
import phanbagiang.com.blogapp.model.User;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FloatingActionButton pop_fab;
    private Dialog dialogPost;

    //firebase
    FirebaseUser mUser;
    DatabaseReference mReference;

    private Uri uriPictureSelected;
    static int requestCode=113;
    private static final String TAG = "HomeFragment";
    private EditText edtTitle, edtDes;
    private ImageView img_Avatarr, imgMain, imgPost;

    private ProgressBar progressBar;
    private RecyclerView listPost;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayoutManager linearLayoutManager;
    PostAdapter postAdapter;
    List<Post>mData;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


//        final TextView textView = root.findViewById(R.id.text_home);
//
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        shimmerFrameLayout=root.findViewById(R.id.shimmer_layout);
        pop_fab=root.findViewById(R.id.home_fab);
        listPost=root.findViewById(R.id.list_post);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        mReference=FirebaseDatabase.getInstance().getReference("Users");

        initPopup();
        addControls();
        addEvents();
        return root;
    }

    private void addControls(){
        mData=new ArrayList<>();
        postAdapter=new PostAdapter(getContext(),mData);
        listPost.setAdapter(postAdapter);
        listPost.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        listPost.setLayoutManager(linearLayoutManager);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Post");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData.clear();
                for(DataSnapshot data :dataSnapshot.getChildren()){
                    Post post=data.getValue(Post.class);
                    mData.add(post);
                }
                shimmerFrameLayout.stopShimmer();
                listPost.setVisibility(View.VISIBLE);
                pop_fab.setVisibility(View.VISIBLE);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onStop() {
        super.onStop();
        shimmerFrameLayout.stopShimmer();
    }

    private void addEvents(){
        pop_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPost.show();
            }
        });
        imgMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=22){
                    checkAndRequestForPermission();
                }
                else{
                    openGallery();
                }
            }
        });
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

        mReference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Glide.with(getContext()).load(user.getPhotoImage()).into(img_Avatarr);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPost.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                if(edtTitle.getText().toString().isEmpty()||
                        edtDes.getText().toString().isEmpty()||
                        uriPictureSelected==null){
                    showMessage("Please input in the field!!!");
                    progressBar.setVisibility(View.INVISIBLE);
                    imgPost.setVisibility(View.VISIBLE);
                }
                else{
                    mReference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final User user=dataSnapshot.getValue(User.class);
                            StorageReference mStorageReference= FirebaseStorage.getInstance().getReference("blog_images");
                            final StorageReference imageFilePath=mStorageReference.child(uriPictureSelected.getLastPathSegment());
                            imageFilePath.putFile(uriPictureSelected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageDowloadLink=uri.toString();
                                            Post post=new Post(edtTitle.getText().toString(),
                                                    edtDes.getText().toString(),
                                                    imageDowloadLink,
                                                    user.getUserId(),
                                                    user.getPhotoImage(),user.getUserName());
                                            // add post with firebase database
                                            addPost(post);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showMessage(e.getMessage());
                                            progressBar.setVisibility(View.INVISIBLE);
                                            imgPost.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    // do something


                }
            }
        });
    }
    private void addPost(Post post){
        DatabaseReference mReference=FirebaseDatabase.getInstance().getReference("Post").push();
        String postKey=mReference.getKey();
        post.setPostKey(postKey);
        mReference.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post successfully !");
                progressBar.setVisibility(View.INVISIBLE);
                imgPost.setVisibility(View.VISIBLE);
                dialogPost.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage(e.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
                imgPost.setVisibility(View.VISIBLE);
            }
        });
    }
    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //Note: Intent.ACTION_PICK
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, requestCode);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //Toast.makeText(this, "Please accept the required for permission!", Toast.LENGTH_SHORT).show();
                showMessage("Please accept the required for permission!");
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            openGallery();
        }
    }
    private void showMessage(String mess){
        Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==requestCode && resultCode==RESULT_OK &&data!=null){
            uriPictureSelected=data.getData();
            imgMain.setImageURI(uriPictureSelected);
        }
    }
}