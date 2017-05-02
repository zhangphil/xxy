package zhangfei.xingxiangyi.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.data.ImageItemBean;

/**
 * Created by Phil on 2017/4/28.
 */

public class ImageFragment extends XingXiangYiFragment {

    private Uri uri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        ImageItemBean imageItemBean = (ImageItemBean) b.getSerializable(ImageItemBean.TAG);
        uri = Uri.fromFile(imageItemBean.imagefile);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        if (uri != null)
            photoView.setImageURI(uri);
    }
}
