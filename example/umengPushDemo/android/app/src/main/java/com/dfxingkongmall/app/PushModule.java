package com.dfxingkongmall.app;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by liupuyan on 2018/4/19.
 */

public class PushModule extends ReactContextBaseJavaModule {

    public static final String DidOpenMessage = "DidOpenMessage";

    public PushModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "PushModule";
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DidOpenMessage, DidOpenMessage);
        return constants;
    }
}
