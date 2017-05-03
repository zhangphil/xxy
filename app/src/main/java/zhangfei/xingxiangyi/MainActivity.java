package zhangfei.xingxiangyi;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import zhangfei.xingxiangyi.activitys.XingXiangYiActivity;
import zhangfei.xingxiangyi.fragments.CalendarFragment;
import zhangfei.xingxiangyi.fragments.GuaFragmentEntrance;
import zhangfei.xingxiangyi.fragments.ImageAndTextFragment;
import zhangfei.xingxiangyi.fragments.XingXiangYiFragment;

public class MainActivity extends XingXiangYiActivity {

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
}
