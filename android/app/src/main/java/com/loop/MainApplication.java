package com.loop;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.vfi.smartpos.deviceservice.aidl.IDeviceService;
import com.vfi.smartpos.deviceservice.aidl.IPrinter;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

    IDeviceService idevice;
    public static IPrinter printer;

    private final ReactNativeHost mReactNativeHost =
            new ReactNativeHost(this) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return BuildConfig.DEBUG;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    return Arrays.<ReactPackage>asList(
                            new MainReactPackage(),
                            new HelloWorldPackage()
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

        bindServices();

//    initializeFlipper(this); // Remove this line if you don't want Flipper enabled
    }

    public void bindServices(){
        Intent intent = new Intent();
        intent.setAction("com.vfi.smartpos.device_service");
        intent.setPackage("com.vfi.smartpos.deviceservice");
        boolean isSucc = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        if (!isSucc) {
            Log.i("TAG", "deviceService connect fail!");
        } else {
            Log.i("TAG", "deviceService connect success");
        }
        Toast.makeText(this,isSucc?"deviceService connect success":"deviceService connect fail!",Toast.LENGTH_LONG).show();

    }

   /* @Override
    public void onTerminate() {
        super.onTerminate();
        if(conn!=null){
            unbindService(conn);
        }
    }*/

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            idevice = IDeviceService.Stub.asInterface(service);
            try {
                printer = idevice.getPrinter();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            // Toast.makeText(this, "bind service sucess", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };



    /**
     * Loads Flipper in React Native templates.
     *
     * @param context
     */
    private static void initializeFlipper(Context context) {
        if (BuildConfig.DEBUG) {
            try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
                Class<?> aClass = Class.forName("com.facebook.flipper.ReactNativeFlipper");
                aClass.getMethod("initializeFlipper", Context.class).invoke(null, context);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
