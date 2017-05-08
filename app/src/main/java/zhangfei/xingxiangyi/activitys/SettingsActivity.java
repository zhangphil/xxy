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


/**
 * Created by Phil on 2017/5/8.
 */

public class SettingsActivity extends PreferenceActivity {
    private PreferenceScreen root;

    private final String AUTHORITY = "authority";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPreferenceScreen(createPreferenceHierarchy());
    }

    private PreferenceScreen createPreferenceHierarchy() {
        //根
        root = getPreferenceManager().createPreferenceScreen(this);

        //一类
        PreferenceCategory settingsPrefCat = new PreferenceCategory(this);
        settingsPrefCat.setKey("settings");
        settingsPrefCat.setTitle("设置");
        root.addPreference(settingsPrefCat);

        Preference p = new Preference(this);
        p.setKey(AUTHORITY);
        p.setTitle("权限");
        p.setSummary("获取应用运行所需的权限");
        settingsPrefCat.addPreference(p);

        return root;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();

        if (key.equals(AUTHORITY)) {
            getPermissions();
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
                        Toast.makeText(getApplicationContext(), "已经获取所需权限", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "未能获取所需权限", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}