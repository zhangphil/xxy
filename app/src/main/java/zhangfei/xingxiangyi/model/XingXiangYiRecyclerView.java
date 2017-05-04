package zhangfei.xingxiangyi.model;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

//import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import me.kareluo.ui.OptionMenuView;
import zhangfei.xingxiangyi.utils.RecyclerViewUtil;

/**
 * Created by Phil on 2017/5/2.
 */

public class XingXiangYiRecyclerView extends RecyclerView {
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
            public void onItemLongClick(int position, View view) {
                if (mXingXiangYiRecyclerViewClickListener != null) {
                    mXingXiangYiRecyclerViewClickListener.onItemLongClick(position, view);
                }
            }

            @Override
            public void onItemLongClick(int position, View view, MotionEvent e) {

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

        void onItemLongClick(int position, View view);
    }
}
