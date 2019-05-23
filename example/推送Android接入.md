# U-Push集成

## 要求 
+ Android SDK4.0.0及以上
+ Android Studio集成
+ 本文档使用gradle自动导入

## 创建应用
+ [友盟官网](http://message.umeng.com)使用包名创建应用，并获取对应的AppKey和Umeng Message Secret

## 导入推送SDK
```
// 友盟基础组件(必须导入，推送依赖基础库)
compile 'com.umeng.sdk:common:latest.integration'
// 包含阿里UTDID服务的补充包 设备标识UTDID组件
compile 'com.umeng.sdk:utdid:1.1.5.3'
// 友盟推送 PushSDK必须依赖基础组件库、utdid库
compile 'com.umeng.sdk:push:4.1.0'
```

如果无法正常集成请在工程目录下的build.gradle添加如下配置：
```
repositories {
    mavenCentral()
}
```

## 初始化推送SDK
```
UMConfigure.init(Context context, String appkey, String channel, int deviceType, String pushSecret);
```

在AndroidManifest.xml添加：
```
<meta-data
    android:name="UMENG_APPKEY"
    android:value="xxxxxxxxxxxxxxxxxxxxxxxxxxxx" />
<!-- Channel ID用来标识App的推广渠道，作为推送消息时给用户分组的一个维度。-->
<meta-data
    android:name="UMENG_CHANNEL"
    android:value="Channel ID" />
```

## 注册推送服务
```
PushAgent mPushAgent = PushAgent.getInstance(this);
//注册推送服务，每次调用register方法都会回调该接口
mPushAgent.register(new IUmengRegisterCallback() {
    @Override
    public void onSuccess(String deviceToken) {
        //注册成功会返回device token
    }
    @Override
    public void onFailure(String s, String s1) {
    }
});
```

## 配置ApplicationId
请确保Application Module的build.gradle文件中的applicationId与资源包名package一致，若不一致，需要自定义包名
```
PushAgent mPushAgent = PushAgent.getInstance(this);
mPushAgent.setResourcePackageName("packageString");
```

## 小米、华为、魅族通道推送

### 创建应用
+ [小米平台创建应用](https://dev.mi.com/console/)，并获取对应的Appkey和AppSecret，在友盟推送后台配置
+ [华为平台创建应用](http://developer.huawei.com/cn/consumer/devunion/ui/server/PUSH.html)，并获取对应的Appkey和AppSecret，在友盟推送后台配置
+ [魅族平台创建应用](https://open.flyme.cn/open-web/views/push.html)，并获取对应的AppID、Appkey和AppSecret，在友盟推送后台配置

### 导入推送SDK
```
// 小米Push通道
compile 'com.umeng.sdk:push-xiaomi:1.0.0'
// 华为Push通道
compile 'com.umeng.sdk:push-huawei:1.0.0'
// 魅族Push通道
compile 'com.umeng.sdk:push-meizu:1.0.0'
```

### 初始化
```
// 小米推送初始化
MiPushRegistar.register(final Context context, final String XIAOMI_ID, final String XIAOMI_KEY);

// 华为推送初始化
HuaWeiRegister.register(final Context context);

// 魅族推送初始化
MeizuRegister.register(Context context, String meizuAppId, String meizuAppKey);
```

### 使用自定义弹窗功能
创建Activity继承自UmengNotifyClickActivity
```
public class PlatformPushTestActivity extends UmengNotifyClickActivity {
    private static String TAG = MipushTestActivity.class.getName();
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mipush);
    }
    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Log.i(TAG, body);
    }
}
```

在AndroidManifest.xml中配置该Activity，并设置launchMode=”singleTask”和exported=”true”
```
<activity
            android:name="packagename.PlatformPushTestActivity"
            android:exported="true"
            android:launchMode="singleTask"/>
```

在【友盟+】推送后台发送通知时，勾选若设备离线转为系统通道下发，并填写Activity的完整包路径（该Activity需继承自UmengNotifyClickActivity）

### 魅族特殊设置
自定义Recevier组件受魅族接入方式限制，必须在包名目录实现一个自定义Recevier，继承自UmengMeizuPushReceiver
```
public class MeizuTestReceiver extends UmengMeizuPushReceiver {
}
```

在AndroidManifest.xml中配置该Recevier
```
<!--魅族push应用定义消息receiver声明 -->
<receiver android:name="${applicationId}.MeizuTestReceiver">
        <intent-filter>
            <!-- 接收push消息 -->
            <action android:name="com.meizu.flyme.push.intent.MESSAGE" />
            <!-- 接收register消息 -->
            <action android:name="com.meizu.flyme.push.intent.REGISTER.FEEDBACK" />
            <!-- 接收unregister消息-->
            <action android:name="com.meizu.flyme.push.intent.UNREGISTER.FEEDBACK" />
            <!-- 兼容低版本Flyme3推送服务配置 -->
            <action android:name="com.meizu.c2dm.intent.REGISTRATION" />
            <action android:name="com.meizu.c2dm.intent.RECEIVE" />
            <category android:name="${applicationId}"></category>
        </intent-filter>
</receiver>
```

详情参考：[友盟安卓SDK集成指南](https://developer.umeng.com/docs/66632/detail/66744)

