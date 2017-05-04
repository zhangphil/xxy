package zhangfei.xingxiangyi.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.kareluo.ui.OptionMenu;
import me.kareluo.ui.OptionMenuView;
import me.kareluo.ui.PopupMenuView;
import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.activitys.ImageActivity;
import zhangfei.xingxiangyi.model.XingXiangYiBean;
import zhangfei.xingxiangyi.model.XingXiangYiRecyclerView;
import zhangfei.xingxiangyi.utils.Util;

/**
 * Created by Phil on 2017/5/2.
 */

public class ImagesFragment extends XingXiangYiFragment {
    private XingXiangYiRecyclerView recyclerView;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private ItemAdapter mItemAdapter;
    private ArrayList<XingXiangYiBean> mItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItems = new ArrayList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (XingXiangYiRecyclerView) view.findViewById(R.id.recyclerview);

        mItemAdapter = new ItemAdapter(getContext());
        recyclerView.setAdapter(mItemAdapter);

        addItems(null);

        recyclerView.setXingXiangYiRecyclerViewClickListener(new XingXiangYiRecyclerView.XingXiangYiRecyclerViewClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                openFile(mItems.get(position));
            }

            @Override
            public void onItemLongClick(int position, View view) {
                showMenu(position, view);
                //showPopupMenu(view, position);
            }
        });
    }

    private void openFile(XingXiangYiBean bean) {
        //打开指定的一张照片
        Intent intent = new Intent(getContext(), ImageActivity.class);
        intent.putExtra(XingXiangYiBean.TAG, bean);
        startActivity(intent);
    }

    private Observable<ArrayList> getFilesObservable(final String string) {
        return Observable.defer(new Callable<ObservableSource<ArrayList>>() {
            @Override
            public ObservableSource<ArrayList> call() throws Exception {
                return Observable.just(readFiles());
            }
        });
    }

    private void addItems(final String s) {
        DisposableObserver disposableObserver = new DisposableObserver<ArrayList>() {

            @Override
            public void onNext(@NonNull ArrayList lists) {
                for (int i = 0; i < lists.size(); i++) {
                    XingXiangYiBean bean = (XingXiangYiBean) lists.get(i);
                    mItems.add(bean);
                }
            }

            @Override
            public void onComplete() {
                mItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }
        };

        mCompositeDisposable.add(
                getFilesObservable(s)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(disposableObserver)
        );
    }

    private ArrayList readFiles() {
        ArrayList lists = new ArrayList();

        File f = new File(getResources().getString(R.string.file_dir_snapshot));
        File[] fs = f.listFiles();
        int cnt = fs.length;

        Util.SelectSortSort(fs);

        String sfn;
        for (int i = 0; i < cnt; i++) {
            File file = fs[i];
            sfn = file.getName().toLowerCase();
            if (!sfn.endsWith(".png") && !sfn.endsWith(".jpeg") && !sfn.endsWith(".jpg"))
                continue;

            XingXiangYiBean imageItemBean = new XingXiangYiBean();
            imageItemBean.file = file;
            lists.add(imageItemBean);
        }

        return lists;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView image;
        public TextView text;

        public ItemViewHolder(View itemView) {
            super(itemView);
            image = (CircleImageView) itemView.findViewById(R.id.circle_image_view);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        private LayoutInflater inflater;

        public ItemAdapter(Context context) {
            super();
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.image_and_text_item, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            XingXiangYiBean bean = mItems.get(position);
            Glide.with(getActivity()).load(android.R.drawable.ic_menu_gallery).crossFade(1000).into(holder.image);
            holder.text.setText(String.valueOf(bean.file.getName()));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //如果退出程序，就清除后台任务
        mCompositeDisposable.clear();
    }

    private void showMenu(final int pos, View view) {
        PopupMenuView menuView = new PopupMenuView(getContext(), R.menu.menu_list, new MenuBuilder(getContext()));
        menuView.setOnMenuClickListener(new OptionMenuView.OnOptionMenuClickListener() {
            @Override
            public boolean onOptionMenuClick(int position, OptionMenu menu) {
                if (menu.getId() == R.id.action_open) {
                    openFile(mItems.get(pos));
                }

                if (menu.getId() == R.id.action_del) {
                    File f = mItems.get(pos).file;
                    f.delete();
                    Toast.makeText(getActivity(), f.getName() + "已删除", Toast.LENGTH_SHORT).show();

                    mItems.remove(pos);
                    mItemAdapter.notifyDataSetChanged();
                }

                return true;
            }
        });

        // 显示在控件的周围
        menuView.show(view);
    }
}