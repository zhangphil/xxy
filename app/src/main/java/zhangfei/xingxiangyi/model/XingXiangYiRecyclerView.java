package zhangfei.xingxiangyi.model;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

//import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import zhangfei.xingxiangyi.utils.PopupList;
import zhangfei.xingxiangyi.utils.RecyclerViewUtil;


/**
 * Created by Phil on 2017/5/2.
 */

public class XingXiangYiRecyclerView extends RecyclerView {
    private List<String> popupMenuItemList = new ArrayList<>();
    private Context context;
    private XingXiangYiRecyclerViewClickListener mXingXiangYiRecyclerViewClickListener = null;

    public XingXiangYiRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);

        popupMenuItemList.add("打开");
        popupMenuItemList.add("删除");

        final RecyclerViewUtil recyclerViewUtil = new RecyclerViewUtil(getContext(), this);
        recyclerViewUtil.setOnItemClickListener(new RecyclerViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (mXingXiangYiRecyclerViewClickListener != null) {
                    mXingXiangYiRecyclerViewClickListener.onItemClick(position, view);
                }
            }
        });

        recyclerViewUtil.setOnItemLongClickListener(new RecyclerViewUtil.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, View view, MotionEvent e) {
                PopupList popupList = new PopupList(context);
                popupList.showPopupListWindow(view, position, e.getRawX(), e.getRawY(), popupMenuItemList, new PopupList.PopupListListener() {
                    @Override
                    public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                        return true;
                    }

                    @Override
                    public void onPopupListClick(View contextView, int contextPosition, int position) {
                        if (mXingXiangYiRecyclerViewClickListener != null) {
                            mXingXiangYiRecyclerViewClickListener.onPopupListClick(contextView, contextPosition, position);
                        }
                    }
                });
            }

            @Override
            public void onItemLongClick(int position, View view) {

            }
        });

//        Paint paint = new Paint();
//        paint.setStrokeWidth(1);
//        paint.setColor(Color.LTGRAY);
//        paint.setAntiAlias(true);
//        this.addItemDecoration(
//                new HorizontalDividerItemDecoration.Builder(getContext())
//                        .paint(paint)
//                        .build());
    }

    public void setXingXiangYiRecyclerViewClickListener(XingXiangYiRecyclerViewClickListener l) {
        this.mXingXiangYiRecyclerViewClickListener = l;
    }

    public interface XingXiangYiRecyclerViewClickListener {
        void onItemClick(int position, View view);

        void onPopupListClick(View contextView, int contextPosition, int position);
    }
}
