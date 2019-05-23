# react-native-umeng-npush

## 安装
```
npm install react-native-umeng-npush

从项目目录安装
npm install ./cbridge/react-native-umeng-npush --save
```

## 集成到iOS
### 链接库
+ Linked Frameworks and Libraries 链接 libRCTUmengPush库
+ Header Search Paths 添加分享库的路径
```
$(SRCROOT)/../node_modules/@cbridge/react-native-umeng-npush/ios/RCTUmengPush
```

注：以上路径以自己实际路径为准

### 添加API
在`Appdelegate.m`中对应的位置添加如下API

```
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  ...
  
  [self setupPushWithOptions: launchOptions];
  
  return YES;
}

#pragma mark - 推送

- (void)setupPushWithOptions:(NSDictionary *)launchOptions {
  [RCTUmengPush pushNotificationDidFinishLaunchingWithOptions:launchOptions umKey:@"umeng key" delegate:self];
}

/**
 *  当设备接收到远程推送通知时，如果程序是处于关闭状态，系统会在给用户暂时远程推送通知的同时，将程序启动到后台，调用如下方法
 *
 *  @param application       application description
 *  @param userInfo          <#userInfo description#>
 *  @param completionHandler <#completionHandler description#>
 */
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
  [RCTUmengPush pushNotificationApplication:application didReceiveRemoteNotification:userInfo fetchCompletionHandler:completionHandler];
}

/**
 *  授权成功后调用
 *
 *  @param application <#application description#>
 *  @param deviceToken <#deviceToken description#>
 */
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
  NSLog(@"-----------%@", deviceToken);
  [RCTUmengPush pushRegisterDeviceToken: deviceToken];
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
  NSLog(@"Failed to get token, error:%@", error);
}

#pragma mark - iOS10推送
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
  [RCTUmengPush didReceiveRemoteNotification:userInfo];
}

// 处理前台收到通知的代理方法
- (void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler {
  
  NSDictionary *userInfo = notification.request.content.userInfo;
  
  if([notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
    
    [RCTUmengPush pushNotificationWillPresentNotification:userInfo];
    
  }else{
    // 应用处于前台时的本地推送接受
  }
}

// 处理后台点击通知的代理方法
- (void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)())completionHandler {
  NSDictionary *userInfo = response.notification.request.content.userInfo;
  if([response.notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
    [RCTUmengPush pushNotificationDidReceiveNotification:userInfo];
  }else{
    // 应用处于后台时的本地推送接受
  }
  completionHandler();
}
```
 
## 集成到Android
注意：0.29版本以后，reactNative会自动创建MainApplication，并且将添加原生模块从MainActivity移到了MainApplication，详情请见[http://reactnative.cn/post/1774](http://reactnative.cn/post/1774)，所以我们的这里继承也有些变化，如果你的reactNative版本是0.29以下，请点击[README-pre.md](https://github.com/liuchungui/react-native-umeng-push/blob/master/README-pre.md)

### 设置Application
新版本的react-native工程已经存在`MainApplication`，我们只需要将`MainApplication`继承`UmengApplication`，然后添加对应的`UmengPushPackage`进去就行了，如下所示：

```
public class MainApplication extends UmengPushApplication implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    protected boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new RNReactNativeUmengNpushPackage()
      );
    }

    @Override
    public void onCreate() {
      super.onCreate();
      SoLoader.init(this, /* native exopackage */ false);

      LogUtil.syncIsDebug(getApplicationContext());
      UMConfigure.setLogEnabled(true);
      UMConfigure.init(this, "umeng_key", "umeng_channel", UMConfigure.DEVICE_TYPE_PHONE, null);
      initPush("application_package");
      // 设置魅族分享
      MeizuRegister.register(this, "umeng_meizu_app_id", "umeng_meizu_app_key");
      // 设置小米分享
      MiPushRegistar.register(this, "umeng_xiaomi_app_id", "umeng_xiaomi_app_key");
      // 设置华为分享
      HuaWeiRegister.register(this);
    }
  };
}
```
   
注：这一步主要是因为友盟推送需要在Application当中接收推送，`UmengPushApplication`封装了友盟推送的内容。如果友盟推送如果不放在Application当中，退出应用之后是无法接收到推送的。

### 添加AppKey
在项目工程的`AndroidManifest.xml`中的<Application>标签下添加:

```
<meta-data
    android:name="UMENG_APPKEY"
    android:value="umeng_key" >
</meta-data>
```

详情参考：[友盟安卓SDK集成指南](https://developer.umeng.com/docs/66632/detail/66744)

## API

| API | Note |    
|---|---|
| `getDeviceToken` | 获取DeviceToken |
| `didReceiveMessage` | 接收到推送消息回调的方法 |
| `didOpenMessage` | 点击推送消息打开应用回调的方法 |
| `addAlias` | 设置别名 |
| `deleteAlias` | 移除别名 |


## Usage

```
import UmengPush from 'react-native-umeng-npush';

// 获取DeviceToken
UmengPush.getDeviceToken(deviceToken => {
    console.log("deviceToken: ", deviceToken);
});

// 接收到推送消息回调
UmengPush.didReceiveMessage(message => {
    console.log("didReceiveMessage:", message);
});

// 点击推送消息打开应用回调
UmengPush.didOpenMessage(message => {
    console.log("didOpenMessage:", message);
});
// 设置别名
UmengPush.addAlias("13434", "userPhone", (isSuccess, message) => {
            console.log("是否成功", isSuccess, message);
          });

UmengPush.deleteAlias("13434", "userPhone", (isSuccess, message) => {
            console.log("是否成功", isSuccess, message);
          });      

```

## 注意
* 安卓如果获取不到deviceToken也接收不到推送，请查看友盟后台的包名是否一致，当前设备是否添加到测试设备当中
