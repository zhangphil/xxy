package zhangfei.xingxiangyi.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.model.XingXiangYiBean;
import zhangfei.xingxiangyi.utils.Util;

/**
 * Created by Phil on 2017/5/2.
 */

public class ImageActivity extends XingXiangYiActivity {
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton fabShare;
    private FloatingActionButton fabDelete;

    private XingXiangYiBean imageItemBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        Intent intent = getIntent();
        imageItemBean = (XingXiangYiBean) intent.getSerializableExtra(XingXiangYiBean.TAG);
        Uri uri = Uri.fromFile(imageItemBean.file);

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        if (uri != null)
            photoView.setImageURI(uri);

        initFloatingActionButton();
    }

    private void initFloatingActionButton() {
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_action_menu);
        floatingActionMenu.setClosedOnTouchOutside(true);
        fabShare = (FloatingActionButton) findViewById(R.id.fab_share);
        fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete);

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageItemBean != null)
                    Util.shareImage(getApplicationContext(), imageItemBean.file);

                floatingActionMenu.close(true);
            }
        });


        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f = imageItemBean.file;
                f.delete();
                Toast.makeText(getApplicationContext(), f.getName() + "已删除", Toast.LENGTH_SHORT).show();
                floatingActionMenu.close(true);

                closeThisActivity();
            }
        });
    }

    private void    closeThisActivity(){
        this.finish();
    }
}
