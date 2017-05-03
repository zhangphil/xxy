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
 * Created by Phil on 2017/4/24.
 */

public class GuaFragmentEntrance extends XingXiangYiFragment {

    private final int AUTO_FRAGMENT_INDEX = 0;
    private final int MANUAL_FRAGMENT_INDEX = 1;
    private final String[] FRAGMENT_TAG = {"自动", "手动"};

    private XingXiangYiFragment paiguaFragmentAuto;
    private XingXiangYiFragment paiguaFragmentManual;

    @Override
    public String getTitle() {
        return "排卦";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paiguaFragmentAuto = new PaiGuaFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(getString(R.string.qiguafangshi), getString(R.string.diannaozidong));
        paiguaFragmentAuto.setArguments(bundle1);

        paiguaFragmentManual = new PaiGuaFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(getString(R.string.qiguafangshi), getString(R.string.shougongzhiding));
        paiguaFragmentManual.setArguments(bundle2);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gua_fragment, null);
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
        ft.add(R.id.fragment_container, paiguaFragmentAuto, FRAGMENT_TAG[0]);
        ft.add(R.id.fragment_container, paiguaFragmentManual, FRAGMENT_TAG[1]);
        ft.hide(paiguaFragmentManual);
        ft.show(paiguaFragmentAuto);
        ft.commit();
    }

    private void switchFragment(int id) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (id == AUTO_FRAGMENT_INDEX) {
            Fragment f = fm.findFragmentByTag(FRAGMENT_TAG[id]);
            ft.show(f);
            ft.hide(paiguaFragmentManual);
            ft.commit();
        }

        if (id == MANUAL_FRAGMENT_INDEX) {
            Fragment f = fm.findFragmentByTag(FRAGMENT_TAG[id]);
            ft.show(f);
            ft.hide(paiguaFragmentAuto);
            ft.commit();
        }
    }
}
