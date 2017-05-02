package zhangfei.xingxiangyi.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.chrisbanes.photoview.PhotoView;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.model.XingXiangYiBean;

/**
 * Created by Phil on 2017/5/2.
 */

public class ImageActivity extends XingXiangYiActivity {
    private Uri uri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        Intent intent = getIntent();
        XingXiangYiBean imageItemBean = (XingXiangYiBean) intent.getSerializableExtra(XingXiangYiBean.TAG);
        uri = Uri.fromFile(imageItemBean.file);

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        if (uri != null)
            photoView.setImageURI(uri);
    }
}
