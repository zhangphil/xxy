package zhangfei.xingxiangyi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import zhangfei.xingxiangyi.R;

/**
 * Created by Phil on 2017/5/4.
 */

public class FileUtil {

    private static final String TAG = "FileUtil";

    public static Bitmap convertViewToBitmap(View view, int width, int height) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(Color.WHITE);/**如果不设置canvas画布为白色，则生成透明*/
        view.layout(0, 0, width, height);
        view.draw(c);

        return b;
    }

    public static void save(Context context, View view, int width, int height, OnFileListener listener) {
        save(context, convertViewToBitmap(view, width, height), listener);
    }

    public static void save(Context context, final Bitmap bitmap, final OnFileListener listener) {
        CompositeDisposable mCompositeDisposable = new CompositeDisposable();
        DisposableObserver disposableObserver = new DisposableObserver<File>() {

            @Override
            public void onNext(@NonNull File file) {
                if (listener != null) {
                    listener.onDone(file);
                }
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString(), e);
                if (listener != null) {
                    listener.onError(e);
                }
            }
        };

        /**
         * 注意此处的写法！
         */
        mCompositeDisposable.add(
                getFileObservable(context, bitmap)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(disposableObserver)
        );
    }


    private static Observable<File> getFileObservable(@NonNull final Context context, @NonNull final Bitmap bitmap) {
        return Observable.defer(new Callable<ObservableSource<File>>() {
            @Override
            public ObservableSource<File> call() throws Exception {
                File file = saveBitmapAsFile(context, bitmap);
                return Observable.just(file);
            }
        });
    }


    public static File saveBitmapAsFile(Context context, Bitmap bitmap) {
        String fp = getFilePath(context);
        String fn = getFileName();

        File file = null;
        try {
            file = new File(fp, fn + ".png");
            //file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);

            fos.flush();
            fos.close();

            Log.d("文件保存", file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public static String getFilePath(Context context) {
        String fp = context.getString(R.string.file_dir_snapshot);
        return fp;
    }

    public static String getFileName() {
        Calendar cal = Calendar.getInstance();
        int year, month, day, hour, min, sec;

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);
        sec = cal.get(Calendar.SECOND);
        String fn = year + "年" + month + "月" + day + "日" + hour + "时" + min + "分" + sec + "秒";

        return fn;
    }

    // 图片添加水印
    private static Bitmap createWatermarkBitmap(Bitmap src, String str) {
        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bmpTemp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpTemp);

        Paint p = new Paint();
        p.setColor(Color.LTGRAY);
        p.setTextSize(15);
        p.setAntiAlias(true);//去锯齿

        canvas.drawBitmap(src, 0, 0, p);

        canvas.drawText(str, w - 160, h - 30, p);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmpTemp;
    }

    public interface OnFileListener {
        void onDone(File file);
        void onError(Throwable e);
    }




    private void saveFile(final File file) {
        Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {



                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<File>() {

                    @Override
                    public void onNext(File file) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString(), e);
                    }
                });
    }
}
