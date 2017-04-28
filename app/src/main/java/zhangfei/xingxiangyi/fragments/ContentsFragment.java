package zhangfei.xingxiangyi.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.activitys.ImageViewPagerActivity;
import zhangfei.xingxiangyi.utils.PopupList;
import zhangfei.xingxiangyi.utils.RecyclerViewUtil;

/**
 * Created by Phil on 2017/4/24.
 */

public class ContentsFragment extends XingXiangYiFragment {

    private RecyclerView recyclerView;

    private List<String> popupMenuItemList = new ArrayList<>();

    @Override
    public String getTitle() {
        return "历史记录";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contents_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.getActivity());
        recyclerView.setAdapter(adapter);


        popupMenuItemList.add("打开");
        popupMenuItemList.add("删除");

        final RecyclerViewUtil recyclerViewUtil = new RecyclerViewUtil(getContext(), recyclerView);
        recyclerViewUtil.setOnItemClickListener(new RecyclerViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Toast.makeText(getContext(), position + " 单击", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewUtil.setOnItemLongClickListener(new RecyclerViewUtil.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, View view, MotionEvent e) {
                PopupList popupList = new PopupList(getActivity());
                popupList.showPopupListWindow(view, position, e.getRawX(), e.getRawY(), popupMenuItemList, new PopupList.PopupListListener() {
                    @Override
                    public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                        return true;
                    }

                    @Override
                    public void onPopupListClick(View contextView, int contextPosition, int position) {
                        Toast.makeText(getActivity(), "列表位置:" + contextPosition + " , 弹出菜单位置:" + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onItemLongClick(int position, View view) {

            }
        });

        view.findViewById(R.id.fab_gallery).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),ImageViewPagerActivity.class);
                startActivity(intent);
            }
        });
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        private LayoutInflater inflater;

        public RecyclerViewAdapter(Context context) {
            super();
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = inflater.inflate(R.layout.recycler_view_item, viewGroup, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int pos) {

        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }
}