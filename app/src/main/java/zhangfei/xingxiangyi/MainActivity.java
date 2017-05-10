
package zhangfei.xingxiangyi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.data.ExFilePickerResult;
import zhangfei.xingxiangyi.activitys.XingXiangYiActivity;
import zhangfei.xingxiangyi.fragments.CalendarFragment;
import zhangfei.xingxiangyi.fragments.GuaFragmentEntrance;
import zhangfei.xingxiangyi.fragments.ImageAndTextFragment;
import zhangfei.xingxiangyi.activitys.SettingsActivity;
import zhangfei.xingxiangyi.fragments.XingXiangYiFragment;
import zhangfei.xingxiangyi.model.App;
import zhangfei.xingxiangyi.utils.Util;

public class MainActivity extends XingXiangYiActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CircleImageView navCircleImageView;
    private ArrayList<XingXiangYiFragment> fragments;

    private final int EX_FILE_PICKER_RESULT = 0xfa01;

    private AppCompatActivity mActivity;

    private String startDirectory = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        setContentView(R.layout.activity_main);

        fragments = new ArrayList<>();

        XingXiangYiFragment guaFragmentEntrance = new GuaFragmentEntrance();
        fragments.add(guaFragmentEntrance);
        XingXiangYiFragment contentsFragment = new ImageAndTextFragment();
        fragments.add(contentsFragment);
        XingXiangYiFragment calendarFragment = new CalendarFragment();
        fragments.add(calendarFragment);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setSmoothScrollingEnabled(true);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        navCircleImageView = (CircleImageView) headerLayout
                .findViewById(R.id.nav_circle_image_view);

        navCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExFilePicker exFilePicker = new ExFilePicker();
                exFilePicker.setCanChooseOnlyOneItem(true);
                exFilePicker.setQuitButtonEnabled(true);
                if (!TextUtils.isEmpty(startDirectory))
                    exFilePicker.setStartDirectory(startDirectory);
                exFilePicker.setChoiceType(ExFilePicker.ChoiceType.FILES);
                exFilePicker.start(mActivity, EX_FILE_PICKER_RESULT);
            }
        });

        if (TextUtils.isEmpty(Util.getUserAvatarPath(mActivity))) {
            Glide.with(this).load(R.mipmap.ic_launcher).crossFade(App.GLIDE_CROSS_FADE_TIME)
                    .into(navCircleImageView);
        } else {
            Uri uri = Uri.parse(Util.getUserAvatarPath(mActivity));
            try {
                Glide.with(this).load(uri).error(R.mipmap.ic_launcher)
                        .crossFade(App.GLIDE_CROSS_FADE_TIME)
                        .into(navCircleImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_version_name) {
            Toast.makeText(mActivity, Util.getVersionName(mActivity),
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_version_code) {
            Toast.makeText(mActivity,
                    String.valueOf(Util.getVersionCode(mActivity)),
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(mActivity, R.string.designed_info, Toast.LENGTH_SHORT)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EX_FILE_PICKER_RESULT) {
            ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
            if (result != null && result.getCount() > 0) {
                String path = result.getPath();

                List<String> names = result.getNames();
                for (int i = 0; i < names.size(); i++) {
                    File f = new File(path, names.get(i));
                    try {
                        Uri uri = Uri.fromFile(f);
                        Glide.with(this).load(uri).error(R.mipmap.ic_launcher)
                                .crossFade(App.GLIDE_CROSS_FADE_TIME)
                                .into(navCircleImageView);

                        Util.setUserAvatarPath(mActivity, uri.toString());

                        startDirectory = path;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
