
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import zhangfei.xingxiangyi.activitys.XingXiangYiActivity;
import zhangfei.xingxiangyi.fragments.CalendarFragment;
import zhangfei.xingxiangyi.fragments.GuaFragmentEntrance;
import zhangfei.xingxiangyi.fragments.ImageAndTextFragment;
import zhangfei.xingxiangyi.activitys.SettingsActivity;
import zhangfei.xingxiangyi.fragments.XingXiangYiFragment;
import zhangfei.xingxiangyi.model.App;
import zhangfei.xingxiangyi.utils.FileUtil;
import zhangfei.xingxiangyi.utils.Util;

public class MainActivity extends XingXiangYiActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CircleImageView navCircleImageView;
    private ArrayList<XingXiangYiFragment> fragments;

    private AppCompatActivity mActivity;

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
                Crop.pickImage(mActivity);
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

        TextView info = (TextView) headerLayout.findViewById(R.id.info);
        info.setText(String.valueOf(Util.getVersionName(mActivity)));
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
        } else if (id == R.id.nav_version) {
            String verName = Util.getVersionName(getApplicationContext());
            String verCode = String.valueOf(Util.getVersionCode(getApplicationContext()));
            String author = getString(R.string.designed_info);

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setMessage("版本名: " + verName + "\n版本号: " + verCode + "\n" + author)
                    // .setNegativeButton("取消", null)
                    // .setPositiveButton("确定", null)
                    .setNeutralButton("确定", null)
                    .setTitle("星象仪")
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void beginCrop(Uri source) {
        File f = new File(FileUtil.getAvatarFilePath(mActivity), UUID.randomUUID() + ".png");
        Uri destination = Uri.fromFile(f);
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            try {

                Uri uri = Crop.getOutput(result);
                Glide.with(this).load(Crop.getOutput(result)).error(R.mipmap.ic_launcher)
                        .crossFade(App.GLIDE_CROSS_FADE_TIME)
                        .into(navCircleImageView);

                Util.setUserAvatarPath(mActivity, uri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }
}
