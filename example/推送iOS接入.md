# U-Push集成

## 要求 
+ iOS 7.0及以上
+ 本文档使用手动集成

## 创建应用
+ [友盟官网](http://message.umeng.com)使用包名创建应用，并获取对应的AppKey和Umeng Message Secret

## 工程配置
1、选择SDK功能组件并下载，解压.zip文件得到相应组件包(例如：UMCommon.framework，UMAnalytics.framework， UMPush.framework等)。  
2、XcodeFile —> Add Files to "Your Project"，在弹出Panel选中所下载组件包－>Add。（注：选中“Copy items if needed”）
3、添加依赖库，在项目设置target -> 选项卡General ->Linked Frameworks and Libraries如下
```
SystemConfiguration.framework
libsqlite3.tbd
libz.tbd
CoreTelephony.framework
UserNotifications.framework
```

## 打开推送开关
在Capabilities->Push Notifications打开

## 打开后台推送权限设置
在Capabilities->Background Modes打开，勾选Remote notifications

## 初始化
```
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    ...
  
    [UMessage startWithAppkey:@"umkey" launchOptions:launchOptions];
    // 注册通知
    [UMessage registerForRemoteNotifications];
    if (iOS10) {
        UNUserNotificationCenter *center = [UNUserNotificationCenter currentNotificationCenter];
        center.delegate = delegate;
        UNAuthorizationOptions types10 = UNAuthorizationOptionBadge | UNAuthorizationOptionAlert | UNAuthorizationOptionSound;
        [center requestAuthorizationWithOptions:types10 completionHandler:^(BOOL granted, NSError * _Nullable error) {
            if (granted) {
                // 允许
                QQMLog(@"注册成功");
            } else {
                // 不允许
                QQMLog(@"注册失败");
            }
        }];
    }

    #ifdef DEBUG
    [UMessage setLogEnabled:YES];
    #endif
    // 关闭友盟自带弹框
    //    [UMessage setAutoAlert:NO];

    // 点击推送跳转到指定页面UIApplicationLaunchOptionsLocalNotificationKey
    QQMLog(@"推送内容-------%@", launchOptions[UIApplicationLaunchOptionsRemoteNotificationKey]);
    if (!iOS10) {
        // 由推送打开应用
        if (launchOptions[UIApplicationLaunchOptionsRemoteNotificationKey]) {
        }
    }


  return YES;
}

```

## 推送相关API
```
/**
 *  当设备接收到远程推送通知时，如果程序是处于关闭状态，系统会在给用户暂时远程推送通知的同时，将程序启动到后台，调用如下方法
 *
 *  @param application       application description
 *  @param userInfo          <#userInfo description#>
 *  @param completionHandler <#completionHandler description#>
 */
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
}

/**
 *  授权成功后调用
 *
 *  @param application <#application description#>
 *  @param deviceToken <#deviceToken description#>
 */
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
  NSLog(@"-----------%@", deviceToken);
  [UMessage registerDeviceToken:deviceToken];
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
  NSLog(@"Failed to get token, error:%@", error);
}

#pragma mark - iOS10推送
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
}

// 处理前台收到通知的代理方法
- (void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler {
  
  NSDictionary *userInfo = notification.request.content.userInfo;
  
  if([notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
    
  }else{
    // 应用处于前台时的本地推送接受
  }
}

// 处理后台点击通知的代理方法
- (void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)())completionHandler {
  NSDictionary *userInfo = response.notification.request.content.userInfo;
  if([response.notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
    
  }else{
    // 应用处于后台时的本地推送接受
  }
  completionHandler();
}
```

详情参考：[友盟iOS SDK集成指南](https://developer.umeng.com/docs/66632/detail/66734)

