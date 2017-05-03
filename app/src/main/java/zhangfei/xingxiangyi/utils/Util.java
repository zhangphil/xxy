package zhangfei.xingxiangyi.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Phil on 2017/5/3.
 */

public class Util {
    // 判断权限集合
    public static boolean hasPermissions(Context context,String... permissions) {
        for (String permission : permissions) {
            if (hasPermission(context,permission)) {
                return true;
            }
        }

        return false;
    }

    // 判断是否有该权限
    private static boolean hasPermission(Context mContext,String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
