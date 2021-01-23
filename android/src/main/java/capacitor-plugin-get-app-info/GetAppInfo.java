package capacitor.plugin.get.app.info;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.nfc.Tag;
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

  
  @PluginMethod
  public void canLaunchApp(PluginCall call) {
    final PackageManager packageManager = getActivity().getPackageManager();
    String packageName = call.getString("packageName");
    
    Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);
    if(launchIntent != null) {
      call.success();
    } else {
      call.reject("Cannot open the app");
    }
  }

  @PluginMethod
  public void getAvailableApps(PluginCall call) {
    final PackageManager packageManager = getActivity().getPackageManager();

    try {
      List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

      ArrayList<JSObject> apps = new ArrayList<JSObject>();

      for (ApplicationInfo packageInfo : packages) {
        if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null
                && !packageManager.getLaunchIntentForPackage(packageInfo.packageName).equals("")) {

          JSObject ret = new JSObject();

          final String pkgName = packageInfo.packageName;
          //Intent launchIntent = packageManager.getLaunchIntentForPackage(packageInfo.packageName);
          final String lbl = packageManager.getApplicationLabel(packageInfo).toString();
          Drawable icon = packageManager.getApplicationIcon(packageInfo.packageName);
          Bitmap iconBitmap = getBitmapFromDrawable(icon);
          ByteArrayOutputStream stream = new ByteArrayOutputStream();
          iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
          final String iconBase64 = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);

          ret.put("image", "data:image/png;base64," + iconBase64);
          ret.put("package", pkgName);
          ret.put("name", lbl);
          //ret.put("launchable", launchIntent != null);
          apps.add(ret);
        }
      }

      JSObject r = new JSObject();
      r.put("applications", apps);
      call.success(r);
    } catch (final NameNotFoundException e) {
      //Log.d(TAG, e.getMessage());
      call.reject("Cannot get app icon");
    } catch ( Exception ex) {
      call.reject(ex.getLocalizedMessage());
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
