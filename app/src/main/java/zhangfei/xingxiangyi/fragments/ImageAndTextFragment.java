package zhangfei.xingxiangyi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huibin.androidsegmentcontrol.SegmentControl;

import zhangfei.xingxiangyi.R;

/**
 * Created by Phil on 2017/5/2.
 */

public class ImageAndTextFragment extends XingXiangYiFragment {
    private final int TEXT_LIST_FRAGMENT_INDEX = 0;
    private final int IMAGE_LIST_FRAGMENT_INDEX = 1;
    private final String[] FRAGMENT_TAG = {"text", "image"};

    private XingXiangYiFragment contentsFragment;
    private XingXiangYiFragment imagesFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentsFragment = new ContentsFragment();
        imagesFragment = new ImagesFragment();
    }

    @Override
    public String getTitle() {
        return "记录";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_and_text_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SegmentControl segmentControl = (SegmentControl) view.findViewById(R.id.segment_control);
        segmentControl.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                switchFragment(index);
            }
        });

        addFragments();
    }


    private void addFragments() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, contentsFragment, FRAGMENT_TAG[0]);
        ft.add(R.id.fragment_container, imagesFragment, FRAGMENT_TAG[1]);
        ft.hide(imagesFragment);
        ft.show(contentsFragment);
        ft.commit();
    }

    private void switchFragment(int id) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (id == TEXT_LIST_FRAGMENT_INDEX) {
            Fragment f = fm.findFragmentByTag(FRAGMENT_TAG[id]);
            ft.show(f);
            ft.hide(imagesFragment);
            ft.commit();
        }

        if (id == IMAGE_LIST_FRAGMENT_INDEX) {
            Fragment f = fm.findFragmentByTag(FRAGMENT_TAG[id]);
            ft.show(f);
            ft.hide(contentsFragment);
            ft.commit();
        }
    }
}
