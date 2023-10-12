package com.example.getmessageforrpa;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import java.util.Properties;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 101;
    private TelephonyManager telephonyManager;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    TextView m_address;
    TextView m_content;
    SmsGetObserver mObserver;

    String android_id = null;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授予了权限，获取 IMEI 号码
                getIMEI();
            } else {
                // 用户拒绝了权限请求，显示提示消息
                Toast.makeText(this, "需要权限才能获取 IMEI 号码", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getIMEI() {
        String imei = telephonyManager.getImei();
      return imei;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android_id = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);


//        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//
//        // 检查并请求权限
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION);
//        } else {
//            // 已获得权限，获取 IMEI 号码
//            android_id = getIMEI();
//        }
        Log.i("MainActivity","唯一标示=="+android_id);
        String android_model = android.os.Build.MODEL;
        String android_brand = Build.BRAND;
        Log.i("MainActivity","手机品牌=="+android_brand);
        Log.i("MainActivity","手机型号=="+android_model);

        WebView webView = findViewById(R.id.webview);
        JavaScriptInterface jsInterface = new JavaScriptInterface();
        webView.addJavascriptInterface(jsInterface, "Android");
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

        webView.loadUrl("http://sms.inplusday.com/sms/login.html?ieme="+android_id);  // 假设 index.html 是您的入口文件

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

                // 调用JavaScript中的函数
                webView.loadUrl("javascript:saveMessage("+content+","+android_id+")");


            }
            cursor.close();

        }
    }
    private class SendEmailTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return sendEmail();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(MainActivity.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Failed to send email", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean sendEmail() {
        final String username = getString(R.string.email_username);
        final String password = getString(R.string.email_password);
        String recipientEmail = getString(R.string.recipient_email);
        String subject = m_address.getText().toString();
        String messageText = m_content.getText().toString();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.port", "25");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageText);
            Transport.send(message);
            Log.d("Hunter", "发送邮件成功！邮件标题: "+m_address+", 邮件内容: "+ m_content);
            return true;
        } catch (MessagingException e) {
            Log.e("SendEmail", e.getMessage(), e);
            return false;
        }
    }


    // 定义 JavaScript 接口
    public class JavaScriptInterface {

        @JavascriptInterface
        public void redirectToNewPage(String token) {
            // 在 JavaScript 中调用此方法，触发页面跳转
            Intent intent = new Intent(MainActivity.this, IndexActivity.class); // NewActivity 是你要跳转的 Activity
            intent.putExtra("Token", token);
            startActivity(intent);
        }


        @JavascriptInterface
        public void saveData(String key,String value) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }

        @JavascriptInterface
        public String getData(String key) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, ""); // 第二个参数是默认值
        }

    }
}