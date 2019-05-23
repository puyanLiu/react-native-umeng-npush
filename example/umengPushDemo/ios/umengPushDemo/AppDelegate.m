/**
 * Copyright (c) 2015-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import "AppDelegate.h"

#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>
#import "RCTUmengPush.h"


@interface AppDelegate()  <UNUserNotificationCenterDelegate>

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  NSURL *jsCodeLocation;

  jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index" fallbackResource:nil];

  RCTRootView *rootView = [[RCTRootView alloc] initWithBundleURL:jsCodeLocation
                                                      moduleName:@"umengPushDemo"
                                               initialProperties:nil
                                                   launchOptions:launchOptions];
  rootView.backgroundColor = [[UIColor alloc] initWithRed:1.0f green:1.0f blue:1.0f alpha:1];

  self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
  UIViewController *rootViewController = [UIViewController new];
  rootViewController.view = rootView;
  self.window.rootViewController = rootViewController;
  [self.window makeKeyAndVisible];
  
  [self setupPushWithOptions: launchOptions];
  
  return YES;
}

#pragma mark - 推送
- (void)setupPushWithOptions:(NSDictionary *)launchOptions {
  [RCTUmengPush pushNotificationDidFinishLaunchingWithOptions:launchOptions umKey:@"xxx" delegate:self];
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
@end
