package capacitor.plugin.get.app.info;

import java.io.ByteArrayOutputStream;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.graphics.Canvas;
import android.content.Intent;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

@NativePlugin
public class GetAppInfo extends Plugin {

    private static final String TAG = "GetAppInfoPlugin";
    // public void echo(PluginCall call) {
    //     String value = call.getString("value");

    //     JSObject ret = new JSObject();
    //     ret.put("value", value);
    //     call.success(ret);
    // }

    // public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    //     super.initialize(cordova, webView);

    //     Log.d(TAG, "Initializing GetAppInfo");
    // }

    // @PluginMethod
    // public boolean execute(PluginCall call) throws JSONException {
    //     String value = call.getString("getAppIcon");
    //     String packageName = call.getString("packageName");

    //     if (value.equals("getAppIcon")) {
    //         this.getAppIcon(packageName, call);
    //     } else if (value.equals("getAppLabel")) {
    //         this.getAppLabel(packageName, call);
    //     }
    //     call.success(true);
    // }

    @PluginMethod
  public void getAppIcon(PluginCall call) {
    final PackageManager packageManager = getActivity().getPackageManager();

    String packageName = call.getString("packageName");
    //Log.d(TAG, packageName);
    try {
      Drawable icon = packageManager.getApplicationIcon(packageName);
      //Bitmap iconBitmap = ((BitmapDrawable) icon).getBitmap();
      Bitmap iconBitmap = getBitmapFromDrawable(icon);
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
      final String iconBase64 = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

        JSObject ret = new JSObject();
        ret.put("value", "data:image/png;base64," + iconBase64);
      call.success(ret);
    } catch (final NameNotFoundException e) {
      //Log.d(TAG, e.getMessage());
      call.reject("Cannot get app icon");
    }
  }

    @PluginMethod
  public void getAppLabel(PluginCall call) {
    final PackageManager packageManager = getActivity().getPackageManager();
    String packageName = call.getString("packageName");

    try {
      final ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
      final String appLabel = packageManager.getApplicationLabel(appInfo).toString();

      
        JSObject ret = new JSObject();
        ret.put("value", appLabel);
      call.success(ret);
    } catch (final NameNotFoundException e) {
      call.reject("Cannot get app label");
    }
  }

  @PluginMethod
  public void launchApp(PluginCall call) {
    final PackageManager packageManager = getActivity().getPackageManager();
    String packageName = call.getString("packageName");
    
    Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);
    if(launchIntent != null) {
      getContext().startActivity(launchIntent);
      call.success();
    } else {
      call.reject("Cannot open the app");
    }
  }

  static private Bitmap getBitmapFromDrawable( Drawable drawable) {
    final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    final Canvas canvas = new Canvas(bmp);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);
    return bmp;
  }
}
