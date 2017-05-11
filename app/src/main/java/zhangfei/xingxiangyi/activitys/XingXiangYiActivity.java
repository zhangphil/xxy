
package zhangfei.xingxiangyi.activitys;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.io.File;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.core.BaseData;
import zhangfei.xingxiangyi.utils.FileUtil;
import zhangfei.xingxiangyi.utils.Util;

/**
 * Created by Phil on 2017/4/25.
 */

public class XingXiangYiActivity extends AppCompatActivity {
    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private AppCompatActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // getWindow().setStatusBarColor(Color.TRANSPARENT);

        boolean has = Util.hasPermissions(this, permissions[0], permissions[1]);
        if (!has) {
            Toast.makeText(this, "星象仪未能获取读写权限，不能正常使用", Toast.LENGTH_LONG).show();
        }

        /** 全局初始化BaseData一次 */
        BaseData.init();

        /**
         * 判断SD卡是否插入
         * 0Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
         * 获得sd卡根目录： File skRoot = Environment.getExternalStorageDirectory();
         */
        boolean sdcard = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);

        if (sdcard) {
            File dir = new File(FileUtil.getHistoryFilePath(mActivity));
            if (!dir.exists())
                dir.mkdirs();

            File dir_snapshot = new File(FileUtil.getSnapshotFilePath(mActivity));
            if (!dir_snapshot.exists())
                dir_snapshot.mkdirs();

            File dir_avatar = FileUtil.getAvatarFilePath(mActivity);
            if (!dir_avatar.exists())
                dir_avatar.mkdirs();
        } else {
            new AlertDialog.Builder(this).setTitle("存储设备异常")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("SDCard异常，请检查SDCard是否正确加装")
                    .setPositiveButton("确定", null).show();
        }
    }

    public void setOnBack(final Activity activity, View view) {
        View v = view.findViewById(R.id.back);
        v.setVisibility(View.VISIBLE);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);// 友盟统计
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);// 友盟统计
    }
}
