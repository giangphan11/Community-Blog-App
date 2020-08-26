package phanbagiang.com.blogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import phanbagiang.com.blogapp.R;

public class UpdateActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;
    private final String url="https://drive.google.com/drive/folders/1HPWRMtkg_3mYoSjjpPVAiS7PQnsLjSks";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        toolbar=findViewById(R.id.update_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Runtime External storage permission for saving download files
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                //Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            }
        }
        addControls();
        test2();
        //addEvents();
    }

    private void test2() {
        webView.setDownloadListener(new DownloadListener()
        {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                request.setMimeType(mimeType);
                String cookies = CookieManager.getInstance().getCookie(url);

                String fileName = contentDisposition.replace("inline; filename=", "");
                fileName = fileName.replaceAll(".+UTF-8''", "");
                fileName = fileName.replaceAll("\"", "");
                try {
                    fileName = URLDecoder.decode(fileName, "UTF-8");
                    request.addRequestHeader("cookie", cookies);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading File");
                    request.setTitle(fileName);
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                    url, contentDisposition, mimeType));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    //install(fileName);
                } catch (Exception ex) {
                    if (ContextCompat.checkSelfPermission(UpdateActivity.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED){
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateActivity.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            Toast.makeText(getBaseContext(), "Để tải xuống tập tin\ncần cấp quyền.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        }
                        else{
                            Toast.makeText(getBaseContext(), "Để tải xuống tệp đính kèm\nCần có sự đồng ý.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        }
                    }
                }



            }});
    }

    private void addControls(){
        webView=findViewById(R.id.web_view);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
    private void addEvents(){

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try{
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType(mimetype);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file");
                    String fileName = contentDisposition.replace("inline; filename=", "");
                    fileName = fileName.replaceAll(".+UTF-8''", "");
                    fileName = fileName.replaceAll("\"", "");
                    fileName = URLDecoder.decode(fileName, "UTF-8");
                    request.setTitle(fileName);
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Đang tải tập tin...", Toast.LENGTH_LONG).show();
                }
                catch (Exception ex){
                    if (ContextCompat.checkSelfPermission(UpdateActivity.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED){
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateActivity.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            Toast.makeText(getBaseContext(), "Để tải xuống tập tin\ncần cấp quyền.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        }
                        else{
                            Toast.makeText(getBaseContext(), "Để tải xuống tệp đính kèm\nCần có sự đồng ý.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        }
                    }
                }
            }
        });
    }
    private void install(String fileName) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.fromFile(new File(fileName)),
                "application/vnd.android.package-archive");
        startActivity(install);
    }
}