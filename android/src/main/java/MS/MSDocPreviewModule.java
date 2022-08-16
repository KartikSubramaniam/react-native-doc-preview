package MS;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;
import androidx.core.app.ShareCompat;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
// import com.facebook.react.views.webview.ReactWebViewManager;

import org.apache.commons.io.IOUtils;

//Third Libraries
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MSDocPreviewModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private static final String TAG = MSDocPreviewModule.class.getSimpleName();

  public MSDocPreviewModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "MSDocPreview";
  }

  private InputStream openAssetInputStream(Uri uri) throws IOException {
    // AssetManager expects no leading slash.
    String asset = uri.getPath().substring(1);
    return reactContext.getAssets().open(asset);
  }

  private InputStream openResourceInputStream(String resourceName) throws IOException {
    int resourceId = reactContext.getResources().getIdentifier(resourceName, "raw", reactContext.getPackageName());
    if (resourceId == 0) {
      // this resource doesn't exist in the raw folder, so try drawable
      resourceId = reactContext.getResources().getIdentifier(resourceName, "drawable", reactContext.getPackageName());
      if (resourceId == 0) {
        throw new FileNotFoundException("No resource found with the name " + resourceName);
      }
    }
    return reactContext.getResources().openRawResource(resourceId);
  }

  private File uriToFile(Uri uri) {
    return new File(uri.getPath());
  }

  private static boolean isSAFUri(Uri uri) {
    return ("content".equals(uri.getScheme()) && uri.getHost().startsWith("com.android.externalstorage"));
  }

  @ReactMethod
  public void readAsStringAsync(String uriStr, Promise promise) {
    try {
      Uri uri = Uri.parse(uriStr);

      // TODO:Bacon: Add more encoding types to match iOS
      String contents;
        if ("file".equals(uri.getScheme())) {
          contents = IOUtils.toString(new FileInputStream(uriToFile(uri)));
        } else if ("asset".equals(uri.getScheme())) {
          contents = IOUtils.toString(openAssetInputStream(uri));
        } else if (uri.getScheme() == null) {
          // this is probably an asset embedded by the packager in resources
          contents = IOUtils.toString(openResourceInputStream(uriStr));
        } else if (isSAFUri(uri)) {
          contents = IOUtils.toString(reactContext.getContentResolver().openInputStream(uri));
        } else {
          throw new IOException("Unsupported scheme for location '" + uri + "'.");
        }
      promise.resolve(contents);
    } catch (Exception e) {
      Log.e(TAG, e.getMessage());
      promise.reject(e);
    }
  }

  @ReactMethod
  public void shareDoc(ReadableArray args, Callback callback) {
    final ReadableMap arg_object = args.getMap(0);
    try {
      if (arg_object.getString("url") != null && arg_object.getString("fileName") != null) {
        // parameter parsing

        final String url = arg_object.getString("url");
        final String fileName = arg_object.getString("fileName");
        final String extension = url.substring(url.lastIndexOf('.'), url.length());

        // String yourFilePath =
        // this.getReactApplicationContext().getFilesDir().getAbsolutePath() + "/" +
        // url;
        File file = new File(url);

        Uri uri = FileProvider.getUriForFile(this.getReactApplicationContext(),
            this.getReactApplicationContext().getPackageName(), file);

        Intent intent = null;

        if (url.contains(".doc") || url.contains(".docx")) {
          // Word document

          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("application/msword").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "application/msword").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else if (url.contains(".pdf")) {
          // PDF file

          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("application/pdf").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "application/pdf").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else if (url.contains(".ppt") || url.contains(".pptx")) {
          // Powerpoint file

          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("application/vnd.ms-powerpoint").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "application/vnd.ms-powerpoint").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else if (url.contains(".html")) {
          // HTML File

          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("text/html").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "text/html").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else if (url.contains(".xls") || url.contains(".xlsx")) {
          // Excel file

          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("application/vnd.ms-excel").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "application/vnd.ms-excel").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else if (url.contains(".csv")) {
          // Excel file

          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("application/vnd.ms-excel").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "application/vnd.ms-excel").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else if (url.contains(".rtf")) {
          // RTF file

          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("application/rtf").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "application/rtf").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else if (url.contains(".wav")) {
          // WAV audio file

          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("audio/x-wav").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "audio/x-wav").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else if (url.contains(".gif")) {
          // GIF file

          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("image/gif").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "image/gif").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else if (url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png")) {
          // JPG file

          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("image/jpeg").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "image/jpeg").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else if (url.contains(".txt") || url.contains(".xml")) {
          // Text file
          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("text/plain").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "text/plain").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else if (url.contains(".mpg") || url.contains(".mpeg") || url.contains(".mpe") || url.contains(".mp4")
            || url.contains(".avi")) {
          // Video files

          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("video/*").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "video/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else {
          intent = ShareCompat.IntentBuilder.from(this.getCurrentActivity()).setStream(uri) // uri from FileProvider
              .setType("text/plain").getIntent().setAction(Intent.ACTION_VIEW) // Change if needed
              .setDataAndType(uri, "text/plain").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        List<ResolveInfo> list = reactContext.getPackageManager().queryIntentActivities(intent,
            PackageManager.MATCH_DEFAULT_ONLY);

        if (list.size() == 0) {
          callback.invoke(true);
        } else {
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

          reactContext.startActivity(intent);
        }
      } else {
        callback.invoke(false);
      }
    } catch (Exception e) {
      callback.invoke(e.getMessage());
    }
  }
}
