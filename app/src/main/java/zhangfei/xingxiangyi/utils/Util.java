package zhangfei.xingxiangyi.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by Phil on 2017/5/3.
 */
public class Util {
    public static void SelectSortSort(File[] fs) {

        /**选择排序
         * 排序结果根据“最后修改时间”大到小逆
         * */
        int LEN = fs.length;
        int index, j, min;
        long lmin, lj;
        File tmpFile = null;
        for (index = 0; index < LEN - 1; index++) {
            min = index;
            /**查找最大值*/
            for (j = index + 1; j < LEN; j++) {
                lmin = Long.valueOf(fs[min].lastModified());
                lj = Long.valueOf(fs[j].lastModified());

                if (lmin < lj)
                    min = j;
            }

            /**交换*/
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
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
