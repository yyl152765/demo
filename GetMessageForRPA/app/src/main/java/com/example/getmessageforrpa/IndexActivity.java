package com.example.getmessageforrpa;

import static android.Manifest.permission.READ_SMS;

import static com.example.util.HttpRequestHelper.sendPostRequest;

import android.os.AsyncTask;
import android.view.View;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import org.json.JSONException;
import org.json.JSONObject;

public class IndexActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 101;
    private TelephonyManager telephonyManager;
    SmsGetObserver mObserver;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String android_id = null;
    String Token = null;

    private WebView webView;

    /**
     * 调用JavaScript中的saveMessage方法(此方法用来存储短信内容)
     * @param content
     */
    public void callJavascriptFunction(String content) {
        webView = findViewById(R.id.webview);
        String script = "saveMessage('" + content + "');"; // 调用 JavaScript 函数 showAlert
        Log.i("IndexActivity","调用方法"+script);
        webView.evaluateJavascript(script, null);
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Token = getIntent().getStringExtra("Token");

        android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        Log.i("IndexActivity","唯一标示=="+android_id);

        String android_model = android.os.Build.MODEL;
        String android_brand = Build.BRAND;
        Log.i("IndexActivity","手机品牌=="+android_brand);
        Log.i("IndexActivity","手机型号=="+android_model);



        WebView webView = findViewById(R.id.webview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.getSettings().setJavaScriptEnabled(true);  // 如果您的 H5 项目使用了 JavaScript

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                // 检查消息的级别，仅记录错误级别的消息
                if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
                    // 将 JavaScript 错误消息记录到应用的日志中
                    String errorMessage = "JavaScript Error: " + consoleMessage.message();
                    Log.e("WebViewError", errorMessage);
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });

        webView.loadUrl("http://sms.inplusday.com/sms/main.html?Token="+Token+"&IEME="+android_id);  // 假设 index.html 是您的入口文件
        //监听短信
        Intent serviceIntent = new Intent(this, SmsListenerService.class);
        serviceIntent.putExtra("Token",Token);
        startService(serviceIntent);



    }

    public class SmsGetObserver extends ContentObserver {
        private final Context mContext;

        public SmsGetObserver(Context context){
            super(new Handler(Looper.getMainLooper()));
            this.mContext = context;
        }

        @SuppressLint("Range")
        @Override
        public void onChange(boolean selfChange, @Nullable Uri uri) {

            super.onChange(selfChange, uri);
            if(uri == null){
                return;
            }
            Log.i("IndexActivity",uri.toString());
            if (uri.toString().contains("content://sms/raw") ||
                    uri.toString().equals("content://sms")){
                return;
            }

            Cursor cursor = mContext.getContentResolver().query(uri, new String[]{"address", "body", "date"},
                    null, null, "date DESC");
            if(cursor.moveToNext()){
                //短信发送地号码
                String sender = cursor.getString(cursor.getColumnIndex("address"));
                //短信内容
                String content = cursor.getString(cursor.getColumnIndex("body"));
                System.out.println("发送短信号码："+sender+"，获取到短信内容："+content);

                //保存短信
                //callJavascriptFunction(content);

                //判断短信是否有验证码
                if (content.contains("验证码")){
                    //发送短信
                    JSONObject jsonObject =new JSONObject();
                    try {
                        jsonObject.put("Msg",content);
                        jsonObject.put("PhoneIMEI",android_id);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    new HttpPostTask().execute(Token, jsonObject.toString(),"http://console.inplusday.com/api/InsertSms");

                }

            }
            cursor.close();

        }
    }


    private class HttpPostTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String authToken = strings[0];
            String jsonPayload = strings[1];
            String url = strings[2];
            String response = "";
            try {
                 response = sendPostRequest(url, jsonPayload, authToken);
                 return response;
            } catch (Exception e) {
                Log.e("HttpPostTask", "Error: " + e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // 在这里处理成功的响应
                Log.d("HttpPostTask", "Response: " + result);
                try {
                    JSONObject res = new JSONObject(result);
                    if (res.get("status").equals("Succeed"))
                        Log.d("HttpPostTask", "短信写入成功！ ");
                    else
                        Log.d("HttpPostTask", "短信写入失败！ ");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // 处理错误情况
                Log.e("HttpPostTask", "HTTP POST请求失败");
            }
        }
    }
}
