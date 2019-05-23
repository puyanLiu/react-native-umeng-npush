package com.dfxingkongmall.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.push.LogUtil;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;

/**
 * Created by liupuyan on 2018/4/19.
 */

public class PlatformPushTestActivity extends UmengNotifyClickActivity {
    private static String TAG = PlatformPushTestActivity.class.getName();
    private String body;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_platform_push);

        LogUtil.i("小米/华为/魅族推送onCreate");

        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        //延时2秒发送推送，否则可能收不到
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Log.i("===============", "进入延时操作");
                MainApplication.getInstance().clickPlatformPushHandler(body);
            }
        }, 2000);
    }

    /**
     {
     "appkey": "",
     "mi_activity": "com.umeng.message.example.MipushTestActivity"
     "mipush": true,
     "timestamp": 1473225266373,
     "production_mode": "true",
     "type": "unicast",
     "device_tokens": "",
     "payload":
         {
         "body":
             {"text": "from pa36a",
             "after_open": "go_app",
             "ticker": "Hello World",
             "title": "listcastpa43"
             },
         "display_type": "notification",
         }
     }
     */
    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        LogUtil.i("小米/华为/魅族推送" + body);
    }
}
