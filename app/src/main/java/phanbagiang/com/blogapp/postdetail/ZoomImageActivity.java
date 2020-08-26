package phanbagiang.com.blogapp.postdetail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import phanbagiang.com.blogapp.R;

public class ZoomImageActivity extends AppCompatActivity {

    private Intent intent;
    private ImageView imageZoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        addControls();
    }
    private void addControls(){
        intent=getIntent();
        imageZoom=findViewById(R.id.zoom_imageView5);
        String imageMain=intent.getStringExtra("iamge_main");
        Glide.with(getApplicationContext()).load(imageMain).into(imageZoom);
    }
}