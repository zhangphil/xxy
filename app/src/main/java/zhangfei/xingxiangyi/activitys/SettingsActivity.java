
package zhangfei.xingxiangyi.activitys;

import android.Manifest;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import zhangfei.xingxiangyi.utils.Util;

/**
 * Created by Phil on 2017/5/8.
 */

public class SettingsActivity extends PreferenceActivity {
    private PreferenceScreen root;

    private final String AUTHORITY = "authority";
    private final String CLEAR_SHARED_PREFERENCES = "clear SharedPreferences";

    private PreferenceActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setPreferenceScreen(createPreferenceHierarchy());
    }

    private PreferenceScreen createPreferenceHierarchy() {
        // 根
        root = getPreferenceManager().createPreferenceScreen(this);

        // 一类
        PreferenceCategory settingsPrefCat = new PreferenceCategory(this);
        settingsPrefCat.setKey("settings");
        settingsPrefCat.setTitle("设置");
        root.addPreference(settingsPrefCat);

        Preference authority = new Preference(this);
        authority.setKey(AUTHORITY);
        authority.setTitle("权限");
        authority.setSummary("获取应用运行所需的权限");
        settingsPrefCat.addPreference(authority);

        Preference clearSharedPreferences = new Preference(this);
        clearSharedPreferences.setKey(CLEAR_SHARED_PREFERENCES);
        clearSharedPreferences.setTitle("清理偏好");
        clearSharedPreferences.setSummary("恢复初装基础设置，重启星象仪生效");
        settingsPrefCat.addPreference(clearSharedPreferences);

        return root;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();

        if (key.equals(AUTHORITY)) {
            getPermissions();
        }

        if (key.equals(CLEAR_SHARED_PREFERENCES)) {
            Util.clearSharedPreferences(mActivity);
            Toast.makeText(mActivity, "已清理", Toast.LENGTH_SHORT).show();
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void getPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET)
                .subscribe(granted -> { // will emit 2 Permission objects
                    if (granted) {
                        Toast.makeText(mActivity, "已经获取所需权限", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(mActivity, "未能获取所需权限", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}
