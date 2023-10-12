package com.example.getmessageforrpa;


import static android.Manifest.permission.READ_SMS;

import static com.example.util.HttpRequestHelper.sendPostRequest;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class SmsListenerService extends Service{
    SmsGetObserver mObserver;
    private static final String CHANNEL_ID = "sms_listener_channel";
    private static final int NOTIFICATION_ID = 1;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    String android_id;
    String Token;
    @Override
    public void onCreate() {
        super.onCreate();
        // 创建一个前台通知，以确保服务在后台运行
        createNotificationChannel();
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("SmsListenerService")
                .setContentText("Listening for SMS")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        startForeground(NOTIFICATION_ID, notification);
        android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        Log.i("SmsListenerService","唯一标示=="+android_id);
        //监听短信
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            int hasReadSmsPermission = checkSelfPermission(READ_SMS);
//            if (hasReadSmsPermission != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{READ_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
//                return;
//            }
//        }

        Uri uri = Uri.parse("content://sms");
        mObserver = new SmsListenerService.SmsGetObserver(this);
        Log.i("SmsListenerService","开始监听手机短信！！");
        //监听短信内容是否发送变化
        getContentResolver().registerContentObserver(uri, true, mObserver);
        // 在此处添加你的短信监听代码
        // 监听短信的逻辑可以在这里处理
        // 注意：在前台服务中监听短信可能会产生一些电量消耗
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
             Token = intent.getStringExtra("Token");

            // 在这里可以使用 Token 和 android_id 参数
        }

        // 返回适当的启动模式
        return START_STICKY; // 适用于需要长时间运行的服务
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
            Log.i("SmsListenerService",uri.toString());
            if (uri.toString().contains("content://sms/raw") ||
                    uri.toString().equals("content://sms")){
                return;
            }

            Cursor cursor = mContext.getContentResolver().query(uri, new String[]{"address", "body", "date"},
                    null, null, "date DESC");
            if(cursor.moveToNext()) {
                //短信发送地号码
                String sender = cursor.getString(cursor.getColumnIndex("address"));
                //短信内容
                String content = cursor.getString(cursor.getColumnIndex("body"));
                System.out.println("发送短信号码：" + sender + "，获取到短信内容：" + content);

                //保存短信
                //callJavascriptFunction(content);
                if (content.contains("验证码")) {
                    //发送短信
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("Msg", content);
                        jsonObject.put("PhoneIMEI", android_id);
                        jsonObject.put("PhoneNum", sender);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    new SmsListenerService.HttpPostTask().execute(Token, jsonObject.toString(), "http://console.inplusday.com/api/InsertSms");

                }
            }
            cursor.close();

        }
    }

    private class HttpPostTask extends AsyncTask<String,Void,String> {

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
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Sms Listener Service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}

