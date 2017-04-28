package zhangfei.xingxiangyi.activitys;

/**
 * Created by Phil on 2017/4/28.
 */

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MenuItem;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.widget.EditText;

import java.io.File;
import java.util.ArrayList;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.data.ImageItemBean;
import zhangfei.xingxiangyi.fragments.ImageFragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class ImageViewPagerActivity extends XingXiangYiActivity {

    //private String[] items = new String[]{"请选择", "分享", "重命名", "删除"};

    private ViewPager viewPager;

    private ArrayList<ImageFragment> mItems=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mActivity = this;
        mItems=new ArrayList();

        setContentView(R.layout.image_view_pager_activity);

        File f = new File(getResources().getString(R.string.file_dir_snapshot));
        File[] fs = f.listFiles();
        int cnt = fs.length;

        SelectSortSort(fs);

        String sfn;
        for (int i = 0; i < cnt; i++) {
            File file=fs[i];
            sfn = file.getName().toLowerCase();
            if (!sfn.endsWith(".png") && !sfn.endsWith(".jpeg") && !sfn.endsWith(".jpg"))
                continue;

            ImageItemBean imageItemBean=new ImageItemBean();
            imageItemBean.imagefile=file;

            Bundle bundle=new Bundle();
            bundle.putSerializable(ImageItemBean.TAG,imageItemBean);

            ImageFragment imageFragment=new ImageFragment();
            imageFragment.setArguments(bundle);

            mItems.add(imageFragment);
        }

        ImageFragmentAdapter adapter=new ImageFragmentAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
    }

    private class ImageFragmentAdapter extends FragmentPagerAdapter{
        public ImageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }
    }


    private class RenameFile {
        private String oldfn = null, newfn = null;
        private EditText inputFileNameEditText = null;
        private AlertDialog dialogInputFileName = null;

        private PagerAdapter mPagerAdapter;
        private int pos;

        private String file_suffix;

        public RenameFile(String old, int ps) {
            oldfn = old;
            pos = ps;

            file_suffix = oldfn.substring(oldfn.lastIndexOf("."));

            inputFileNameEditText = new EditText(ImageViewPagerActivity.this);
            inputFileNameEditText.setText(oldfn.substring(0, oldfn.lastIndexOf(".")));
            inputFileNameEditText.setSelectAllOnFocus(true);

            dialogInputFileName = new AlertDialog.Builder(ImageViewPagerActivity.this).setTitle("输入新文件名").setIcon(android.R.drawable.ic_dialog_info).setView(inputFileNameEditText).setPositiveButton("确定", new DialogEditTextListener()).setNegativeButton("取消", null).show();
        }

        private class DialogEditTextListener implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (whichButton == DialogInterface.BUTTON_POSITIVE) {
                    String s = inputFileNameEditText.getText().toString().trim();

                    if (s.equals("")) {
                        new AlertDialog.Builder(ImageViewPagerActivity.this).setTitle("文件名错误").setIcon(android.R.drawable.ic_dialog_alert).setMessage("文件名不能为空").setPositiveButton("确定", null).show();

                        return;
                    }

                    try {
                        String[] fs = new File(getString(R.string.file_dir_snapshot)).list();
                        for (int i = 0; i < fs.length; i++) {
                            if (fs[i].equals(s + file_suffix)) {
                                new AlertDialog.Builder(ImageViewPagerActivity.this).setTitle("文件名错误").setIcon(android.R.drawable.ic_dialog_alert).setMessage("文件名已存在").setPositiveButton("确定", null).show();

                                return;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String fp = getString(R.string.file_dir_snapshot) + s + file_suffix;
                    try {
                        File file = new File(getString(R.string.file_dir_snapshot), oldfn);
                        File nf = new File(fp);
                        file.renameTo(nf);

                        //pagerTitle.set(pos, nf.getName());

                        mPagerAdapter = (PagerAdapter) viewPager.getAdapter();
                        mPagerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void SelectSortSort(File[] fs) {

        /**选择排序
         * 排序结果根据“最后修改时间”大到小逆
         * */
        int LEN = fs.length;
        int index, j, min;
        long lmin, lj;
        File tmpFile = null;
        for (index = 0; index < LEN - 1; index++) {
            min = index;
            /**查找最大值*/
            for (j = index + 1; j < LEN; j++) {
                lmin = Long.valueOf(fs[min].lastModified());
                lj = Long.valueOf(fs[j].lastModified());

                if (lmin < lj)
                    min = j;
            }

            /**交换*/
            tmpFile = fs[min];
            fs[min] = fs[index];
            fs[index] = tmpFile;
        }
    }


    /*
    private void actionChoose(int position) {
        String fp = getString(R.string.file_dir_snapshot);

        //防止数组越界
        if (views.size() <= 0)
            return;

        switch (position) {
            //分享
            case 1:

                mActivity.openOptionsMenu();

                //String path=fp+pagerTitle.get(viewPager.getCurrentItem());
                //Bitmap bitmap = BitmapFactory.decodeFile(path);
                //byte[] bytes=Bitmap2Bytes(bitmap);
                //UMServiceFactory.shareTo(ImageViewPagerActivity.this, "我使用星象仪,分享了", bytes);

                return;

            //重命名
            case 2:

                int pos = viewPager.getCurrentItem();
                RenameFile renameFile = new RenameFile(pagerTitle.get(pos), pos);

                return;

            //删除
            case 3:

                File f = new File(fp, pagerTitle.get(viewPager.getCurrentItem()));
                f.delete();

                views.remove(viewPager.getCurrentItem());
                pagerTitle.remove(viewPager.getCurrentItem());

                PagerAdapter pa = (PagerAdapter) (viewPager.getAdapter());
                pa.notifyDataSetChanged();

                return;

            default:
                return;
        }
    }

*/





}