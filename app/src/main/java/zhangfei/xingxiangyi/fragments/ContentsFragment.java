package zhangfei.xingxiangyi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.utils.RecyclerViewUtil;

/**
 * Created by Phil on 2017/4/24.
 */

public class ContentsFragment extends XingXiangYiFragment {

    private RecyclerView recyclerView;

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


        RecyclerViewUtil recyclerViewUtil = new RecyclerViewUtil(getContext(), recyclerView);
        recyclerViewUtil.setOnItemClickListener(new RecyclerViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Toast.makeText(getContext(),position+" 单击",Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewUtil.setOnItemLongClickListener(new RecyclerViewUtil.OnItemLongClickListener(){
            @Override
            public void onItemLongClick(int position, View view) {
                Toast.makeText(getContext(),position+" 长按",Toast.LENGTH_SHORT).show();
            }
        });

        //FloatingActionButton fab= (FloatingActionButton) view.findViewById(R.id.fab);
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