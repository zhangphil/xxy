package zhangfei.xingxiangyi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zhangfei.xingxiangyi.MainActivity;
import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.activitys.PaiGua;


/**
 * Created by Phil on 2017/4/24.
 */

public class GuaFragmentEntrance extends XingXiangYiFragment{

    @Override
    public String getTitle() {
        return "排卦";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gua_fragment,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.auto).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),PaiGua.class);
                intent.putExtra(getString(R.string.qiguafangshi), getString(R.string.diannaozidong));
                startActivity(intent);
            }
        });


        view.findViewById(R.id.manual).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),PaiGua.class);
                intent.putExtra(getString(R.string.qiguafangshi), getString(R.string.shougongzhiding));
                startActivity(intent);
            }
        });
    }
}
