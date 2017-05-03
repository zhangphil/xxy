package zhangfei.xingxiangyi.activitys;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.model.XingXiangYiBean;

/**
 * Created by Phil on 2017/5/2.
 */

public class BaseDispalyActivity extends XingXiangYiActivity {

    private EditText editText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_displaygua, null);
        setContentView(view);
        view.setKeepScreenOn(true);

        editText = (EditText) findViewById(R.id.editTextDisplayGua);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        editText.setMinWidth(displayMetrics.widthPixels);
        editText.setMinHeight(displayMetrics.heightPixels);

        try {
            Intent intent = getIntent();
            XingXiangYiBean bean = (XingXiangYiBean) intent.getSerializableExtra(XingXiangYiBean.TAG);

            FileInputStream fis = new FileInputStream(bean.file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "utf-8"));

            String txt = null;
            while ((txt = br.readLine()) != null) {
                if (txt != null) {
                    editText.append(txt + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveToFile(String fp) {
        String str = new String(editText.getText() + "");

        File file = new File(fp);
        FileOutputStream fos = null;
        try {
            file.createNewFile();

            fos = new FileOutputStream(file);
            fos.write(str.getBytes("utf-8"));
            fos.flush();
            fos.close();

            Toast msg = Toast.makeText(BaseDispalyActivity.this, file.getName() + " 已经保存", Toast.LENGTH_LONG);
            msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
            msg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void removeFile(String fp) {
        File f = null;
        try {
            f = new File(fp);
            f.delete();

            Toast msg = Toast.makeText(BaseDispalyActivity.this, f.getName() + " 已删除", Toast.LENGTH_LONG);
            msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
            msg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class SaveToImage {
        private Calendar cal = null;
        private int year, month, day, hour, min, sec;

        private String fp = null, fn = null;
        private File file = null;
        private FileOutputStream fos = null;

        public SaveToImage() {
            fp = getString(R.string.file_dir_snapshot);
            cal = Calendar.getInstance();
        }

        public void save() {
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH) + 1;
            day = cal.get(Calendar.DAY_OF_MONTH);
            hour = cal.get(Calendar.HOUR_OF_DAY);
            min = cal.get(Calendar.MINUTE);
            sec = cal.get(Calendar.SECOND);

            fn = year + "年" + month + "月" + day + "日" + hour + "时" + min + "分" + sec + "秒";
            //fn=year+""+month+""+day+""+hour+""+min+""+sec;

            editText.setDrawingCacheEnabled(true);
            editText.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            editText.setDrawingCacheBackgroundColor(Color.WHITE);

            Bitmap cachebmp = loadBitmapFromView(editText);
            Bitmap bitmap = Bitmap.createBitmap(createWatermarkBitmap(cachebmp, getString(R.string.app_name)));

            try {
                file = new File(fp, fn + ".png");
                //file.createNewFile();
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);

                fos.flush();
                fos.close();

                Toast msg = Toast.makeText(BaseDispalyActivity.this, "已完成截图,现在去图库查看吧!", Toast.LENGTH_LONG);
                msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                msg.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            editText.destroyDrawingCache();
            //editText.setDrawingCacheEnabled(false);
        }

        private Bitmap loadBitmapFromView(View v) {
            int w = editText.getWidth();
            int h = editText.getHeight();

            Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            c.drawColor(Color.WHITE);/**如果不设置canvas画布为白色，则生成透明*/
            v.layout(0, 0, w, h);
            v.draw(c);

            return b;
        }

        // 图片添加水印
        private Bitmap createWatermarkBitmap(Bitmap src, String str) {
            int w = src.getWidth();
            int h = src.getHeight();

            Bitmap bmpTemp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmpTemp);

            Paint p = new Paint();
            p.setColor(Color.LTGRAY);
            //p.setTextSize(RECORD_EDIT_TEXT_FONT_SIZE);
            p.setAntiAlias(true);//去锯齿

            canvas.drawBitmap(src, 0, 0, p);

            canvas.drawText(str, w - 150, h - 30, p);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();

            return bmpTemp;
        }
    }
}
