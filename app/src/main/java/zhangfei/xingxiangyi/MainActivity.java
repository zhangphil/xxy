package zhangfei.xingxiangyi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import zhangfei.xingxiangyi.activitys.XingXiangYiActivity;
import zhangfei.xingxiangyi.fragments.CalendarFragment;
import zhangfei.xingxiangyi.fragments.GuaFragmentEntrance;
import zhangfei.xingxiangyi.fragments.ImageAndTextFragment;
import zhangfei.xingxiangyi.activitys.SettingsActivity;
import zhangfei.xingxiangyi.fragments.XingXiangYiFragment;
import zhangfei.xingxiangyi.utils.Util;

public class MainActivity extends XingXiangYiActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<XingXiangYiFragment> fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Toast.makeText(getApplicationContext(), Util.getVersionName(getApplicationContext()), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_version_code) {
            Toast.makeText(getApplicationContext(), String.valueOf(Util.getVersionCode(getApplicationContext())), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(getApplicationContext(), R.string.designed_info, Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
