
package zhangfei.xingxiangyi.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;

import java.io.File;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.model.App;

/**
 * Created by Phil on 2017/5/3.
 */
public class Util {

    public static void setUserAvatarPath(Context context, String path) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.user_store_info), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(App.USER_AVATAR_PATH, path);
        edit.commit();
    }

    public static String getUserAvatarPath(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.user_store_info), Context.MODE_PRIVATE);
        String path = prefs.getString(App.USER_AVATAR_PATH, null);
        return path;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    // 图片添加水印
    public static Bitmap createWatermarkBitmap(Context context, Bitmap srcBmp, String str,
            float textSize) {
        int w = srcBmp.getWidth();
        int h = srcBmp.getHeight();

        Bitmap bmpTemp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpTemp);

        Paint p = new Paint();
        p.setColor(Color.LTGRAY);
        p.setTextSize(convertDpToPixel(textSize, context));
        p.setAntiAlias(true);// 去锯齿

        canvas.drawBitmap(srcBmp, 0, 0, p);
        canvas.drawText(str, w - 160, h - 40, p);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmpTemp;
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static void SelectSortSort(File[] fs) {

        /**
         * 选择排序 排序结果根据“最后修改时间”大到小逆
         */
        int LEN = fs.length;
        int index, j, min;
        long lmin, lj;
        File tmpFile = null;
        for (index = 0; index < LEN - 1; index++) {
            min = index;
            /** 查找最大值 */
            for (j = index + 1; j < LEN; j++) {
                lmin = Long.valueOf(fs[min].lastModified());
                lj = Long.valueOf(fs[j].lastModified());

                if (lmin < lj)
                    min = j;
            }

            /** 交换 */
            tmpFile = fs[min];
            fs[min] = fs[index];
            fs[index] = tmpFile;
        }
    }

    public static void shareImage(@NonNull Context context, @NonNull File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "zhangfei.xingxiangyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    // 判断权限集合
    public static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (hasPermission(context, permission)) {
                return true;
            }
        }

        return false;
    }

    // 判断是否有该权限
    private static boolean hasPermission(Context mContext, String permission) {
        return ContextCompat.checkSelfPermission(mContext,
                permission) == PackageManager.PERMISSION_GRANTED;
    }
}
