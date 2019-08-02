package com.example.yzs.customcollection.network.okhttp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.yzs.customcollection.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpActivity extends Activity implements View.OnClickListener {
  private static final MediaType MEDIA_TYPE_MARKDOWN =
      MediaType.parse("text/x-markdown;charset=utf-8");
  private TextView tvOkhttp;
  private Button btnGet, btnPost, btnUpload, btnDownload;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_okhttp);
    tvOkhttp = findViewById(R.id.tvOkhttp);
    btnGet = findViewById(R.id.btnGet);
    btnPost = findViewById(R.id.btnPost);
    btnUpload = findViewById(R.id.btnUpload);
    btnDownload = findViewById(R.id.btnDownload);

    btnGet.setOnClickListener(this);
    btnPost.setOnClickListener(this);
    btnUpload.setOnClickListener(this);
    btnDownload.setOnClickListener(this);
  }

  @RequiresApi(api = Build.VERSION_CODES.M) @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnGet:
        doGet();
        break;
      case R.id.btnPost:
        doPost();
        break;
      case R.id.btnUpload:
        checkSelfPermission();

        break;
      case R.id.btnDownload:
        doDownLoad();
        break;
      default:
        break;
    }
  }

  private void doDownLoad() {
    OkHttpClient client = new OkHttpClient.Builder()
        .build();
    Request request =
        new Request.Builder().url("http://pic24.nipic.com/20120922/10898738_143746326185_2.jpg")
            .build();
    Call call = client.newCall(request);
    call.enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {

      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        InputStream inputStream = response.body().byteStream();
        String filePath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
          filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
          filePath = getFilesDir().getAbsolutePath();
        }

        File file = new File(filePath, "test.jpg");
        if (file != null) {
          FileOutputStream fileOutputStream = new FileOutputStream(file);
          byte[] bytes = new byte[2048];
          int len = 0;
          while ((len = inputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, len);
          }
          runOnUiThread(new Runnable() {
            @Override public void run() {
              Toast.makeText(OkhttpActivity.this, "下载成功", Toast.LENGTH_LONG).show();
            }
          });
          fileOutputStream.flush();
          fileOutputStream.close();
          inputStream.close();
        }
      }
    });
  }

  @RequiresApi(api = Build.VERSION_CODES.M) private void checkSelfPermission() {
    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_DENIED) {
      requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0x110);
    } else {
      try {
        doUpload();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 0x110) {
      if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
        Toast.makeText(OkhttpActivity.this, "没有权限", Toast.LENGTH_LONG).show();
        return;
      } else {
        try {
          doUpload();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void doUpload() throws IOException {
    String filePath = "";
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    } else {
      return;
    }
    File fileDirectory = new File(filePath);
    if (fileDirectory.isDirectory()) {
      if (!fileDirectory.exists()) {
        fileDirectory.mkdirs();
      }
    }
    File file = new File(fileDirectory, "test.txt");
    if (!file.exists()) {
      file.createNewFile();
      OutputStream outputStream = new FileOutputStream(file);
      outputStream.write("okhttp test".getBytes());
      outputStream.flush();
      outputStream.close();
    }
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url("https://api.github.com/markdown/raw")
        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
        .build();
    Call call = client.newCall(request);
    call.enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        final String result = e.getMessage();
        runOnUiThread(new Runnable() {
          @Override public void run() {
            Toast.makeText(OkhttpActivity.this, "upload request", Toast.LENGTH_LONG).show();
            tvOkhttp.setText(result);
          }
        });
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        runOnUiThread(new Runnable() {
          @Override public void run() {
            Toast.makeText(OkhttpActivity.this, "upload request", Toast.LENGTH_LONG).show();
            tvOkhttp.setText(result);
          }
        });
      }
    });
  }

  private void doGet() {
    OkHttpClient client = new OkHttpClient();
    Request request =
        new Request.Builder().url("https://unitoy.hivoice.cn/karpro/userAgreement.html")
            .get()
            .build();
    Call call = client.newCall(request);
    call.enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {

      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        runOnUiThread(new Runnable() {
          @Override public void run() {
            Toast.makeText(OkhttpActivity.this, "get request", Toast.LENGTH_LONG).show();
            tvOkhttp.setText(result);
          }
        });
      }
    });
  }

  private void doPost() {
    OkHttpClient client = new OkHttpClient();
    FormBody formBody = new FormBody.Builder().add("ip", "192.168.6.142").build();
    Request request = new Request.Builder().url("http://ip.taobao.com/service/getIpInfo.php")
        .post(formBody)
        .build();
    Call call = client.newCall(request);
    call.enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {

      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        runOnUiThread(new Runnable() {
          @Override public void run() {
            Toast.makeText(OkhttpActivity.this, "post request", Toast.LENGTH_LONG).show();
            tvOkhttp.setText(result);
          }
        });
      }
    });
  }
}
