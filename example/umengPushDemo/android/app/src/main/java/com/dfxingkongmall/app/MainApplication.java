package com.dfxingkongmall.app;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.push.LogUtil;
import com.push.UmengPushApplication;
import com.reactlibrary.RNReactNativeUmengNpushPackage;
import com.umeng.commonsdk.UMConfigure;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

import java.util.Arrays;
import java.util.List;

public class MainApplication extends UmengPushApplication implements ReactApplication {

  private static MainApplication instance;

  public static MainApplication getInstance() {
    if (instance == null) {
      // 线程安全
      synchronized (MainApplication.class) {
        if (instance == null) {
          instance = new MainApplication();
        }
      }
    }
    return instance;
  }


  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new RNReactNativeUmengNpushPackage(),
          new PushPackage()
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);

    instance = this;
    LogUtil.syncIsDebug(getApplicationContext());
    LogUtil.i("===========================================进入了嘛");
    UMConfigure.setLogEnabled(true);
    UMConfigure.init(this, getResources().getString(R.string.umeng_key), getResources().getString(R.string.umeng_channel), UMConfigure.DEVICE_TYPE_PHONE, null);
    initPush(getResources().getString(R.string.application_package));
    MeizuRegister.register(this, getResources().getString(R.string.umeng_meizu_app_id), getResources().getString(R.string.umeng_meizu_app_key));
    MiPushRegistar.register(this, getResources().getString(R.string.umeng_xiaomi_app_id), getResources().getString(R.string.umeng_xiaomi_app_key));
    HuaWeiRegister.register(this);
  }
}
