package phanbagiang.com.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

import phanbagiang.com.blogapp.R;

public class RegisterActivity extends AppCompatActivity {
    Button regBtn;
    ImageView regImage;
    EditText regName,regMail, regPassword1, regPassword2;
    ProgressBar progressBar;

    static int requestCode=113;

    Uri uriPictureSelected;
    //FireBase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addControls();
        addEvents();
    }

    private void addControls() {
        regImage=findViewById(R.id.regImage);
        regName=findViewById(R.id.regName);
        regMail=findViewById(R.id.regEmail);
        regPassword1=findViewById(R.id.regPassword1);
        regPassword2=findViewById(R.id.regPassword2);
        regBtn=findViewById(R.id.regbtn);
        progressBar=findViewById(R.id.progressBar);

        // firebase
        mAuth=FirebaseAuth.getInstance();
    }
    private void addEvents(){
        regImage.setOnClickListener(new View.OnClickListener() {
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

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                final String name=regName.getText().toString();
                final String email=regMail.getText().toString();
                final String passWord1=regPassword1.getText().toString();
                final String passWord2=regPassword2.getText().toString();
                if(name.isEmpty()||email.isEmpty()||!passWord1.equals(passWord2)){
                    //Toast.makeText(RegisterActivity.this, "Please verify all Fields!", Toast.LENGTH_SHORT).show();
                    showMessage("Please verify all Fields!");
                    progressBar.setVisibility(View.INVISIBLE);
                    regBtn.setVisibility(View.VISIBLE);
                }
                else if(uriPictureSelected==null){
                    //Toast.makeText(RegisterActivity.this, "please select an Image from Gallery!", Toast.LENGTH_SHORT).show();
                    showMessage("please select an Image from Gallery!");
                    progressBar.setVisibility(View.INVISIBLE);
                    regBtn.setVisibility(View.VISIBLE);
                }
                else{
                    CreateUserAccount(name,email,passWord1);
                }
            }
        });
    }

    private void CreateUserAccount(final String name, final String email, final String passWord1) {
        mAuth.createUserWithEmailAndPassword(email, passWord1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Toast.makeText(RegisterActivity.this, "Register completed!", Toast.LENGTH_SHORT).show();
                            showMessage("Register completed!");
                            // update User
                            updateUser(email,passWord1,name,uriPictureSelected,mAuth.getCurrentUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            regBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
    private void showMessage(String mess){
        Snackbar.make(regBtn,mess,Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show();
    }
    private void updateUser(final String email, final String passWord1, final String name, Uri uriPictureSelected, final FirebaseUser currentUser) {
       // currentUser.get
        StorageReference mStorageRef=FirebaseStorage.getInstance().getReference().child("user_photos");
        final StorageReference imageFilepath=mStorageRef.child(uriPictureSelected.getLastPathSegment());
        imageFilepath.putFile(uriPictureSelected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Image upload successfully
                // now we can get our image url
                imageFilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri contain image uri
                        UserProfileChangeRequest updateProfile=new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(updateProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //Toast.makeText(RegisterActivity.this, "Register completed!", Toast.LENGTH_SHORT).show();
                                    showMessage(task.getException().getMessage());
                                    UpdateUI();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void UpdateUI() {
        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //Note: Intent.ACTION_PICK
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, requestCode);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //Toast.makeText(this, "Please accept the required for permission!", Toast.LENGTH_SHORT).show();
                showMessage("Please accept the required for permission!");
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(RegisterActivity.this,
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==requestCode && resultCode==RESULT_OK &&data!=null){
            uriPictureSelected=data.getData();
            regImage.setImageURI(uriPictureSelected);
        }
    }
}