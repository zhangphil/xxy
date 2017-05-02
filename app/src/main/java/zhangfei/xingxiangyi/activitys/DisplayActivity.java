package zhangfei.xingxiangyi.activitys;

import android.os.Bundle;
import android.os.Handler;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.Gravity;

import java.util.Calendar;
import java.io.File;
import java.io.FileOutputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ZoomControls;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.*;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.core.BaseData;
import zhangfei.xingxiangyi.core.GanZhiCalendar;
import zhangfei.xingxiangyi.core.GuaFormat;
import zhangfei.xingxiangyi.core.LiuYaoPaiPan;
import zhangfei.xingxiangyi.core.Lunar;


public class DisplayActivity extends XingXiangYiActivity {
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton fabSave;
    private FloatingActionButton fabShot;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabDelete;

    private String[] items = new String[]{"请选择", "保存", "编辑[关]", "删除", "设置"};

    private String QiGuaFangShi = null;

    private EditText editText = null;
    private boolean editable = false;

    private EditText inputFileNameEditText = null;
    private AlertDialog dialogInputFileName = null;

    private int ZOOM_SIZE = 16;

    private String FILE_PATH = null;
    private String DEFAULT_FILE_NAME = "";

    private float DISPLAY_EDIT_TEXT_FONT_SIZE = 18.0f;
    private final static String DISPLAY_EDIT_TEXT_FONT_SIZE_TAG = "display_edit_text_font_size";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaygua);

        items[2] = editable ? "编辑[开]" : "编辑[关]";

        editText = (EditText) findViewById(R.id.editTextDisplayGua);

        inputFileNameEditText = new EditText(this);
        dialogInputFileName = new AlertDialog.Builder(this)
                .setTitle("输入文件名")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(inputFileNameEditText)
                .setPositiveButton("确定", new DialogEditTextListener())
                .setNegativeButton("取消", null)
                .create();


        //如果打算接收用户编辑EditText则设置下面两句,如果不允许编辑EditText，则全部设置false
        editText.setFocusable(editable);
        editText.setFocusableInTouchMode(editable);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        editText.setMinWidth(displayMetrics.widthPixels);
        editText.setMinHeight(displayMetrics.heightPixels);

        dialogInputFileName = new AlertDialog.Builder(this).setTitle("输入文件名").setIcon(android.R.drawable.ic_dialog_info).setView(inputFileNameEditText).setPositiveButton("确定", new DialogEditTextListener()).setNegativeButton("取消", null).create();

        ZoomControls zoomcontrols = (ZoomControls) findViewById(R.id.zoomcontrols);
        zoomcontrols.setOnZoomInClickListener(new ZoomInClickListenerImpl());
        zoomcontrols.setOnZoomOutClickListener(new ZoomOutClickListenerImpl());

        FILE_PATH = null;/*必须置null*/

        DISPLAY_EDIT_TEXT_FONT_SIZE = editText.getTextSize();

        SharedPreferences sp = this.getSharedPreferences(getString(R.string.user_store_info), Context.MODE_PRIVATE);
        float etff = sp.getFloat(DISPLAY_EDIT_TEXT_FONT_SIZE_TAG, -1.0f);
        if (etff > 0) {
            DISPLAY_EDIT_TEXT_FONT_SIZE = etff;
            editText.setTextSize(DISPLAY_EDIT_TEXT_FONT_SIZE);
        }


        Bundle bundle = this.getIntent().getBundleExtra("UserData");

        QiGuaFangShi = bundle.getString(getString(R.string.qiguafangshi));

        String name = bundle.getString("姓名");
        String gender = bundle.getString("性别");
        String birthdayyear = bundle.getString("生年");
        String things = bundle.getString("占卜事项");

        int[] dt = bundle.getIntArray("起卦时间");
        int year = dt[0], month = dt[1], day = dt[2], hour = dt[3], minute = dt[4];


        /**缺省的文件名*/
        DEFAULT_FILE_NAME = "[" + year + "年" + (month + 1) + "月" + day + "日" + hour + "时" + minute + "分]";

        if (!things.equals(getResources().getString(R.string.unknown)))
            DEFAULT_FILE_NAME = DEFAULT_FILE_NAME + things + ".";
        if (!name.equals(getResources().getString(R.string.unknown)))
            DEFAULT_FILE_NAME = DEFAULT_FILE_NAME + name + ",";
        if (!gender.equals(getResources().getString(R.string.unknown)))
            DEFAULT_FILE_NAME = DEFAULT_FILE_NAME + gender + ",";
        if (!birthdayyear.equals(getResources().getString(R.string.unknown)))
            DEFAULT_FILE_NAME = DEFAULT_FILE_NAME + birthdayyear;

        inputFileNameEditText.setText(DEFAULT_FILE_NAME);
        inputFileNameEditText.selectAll();
        inputFileNameEditText.setSelectAllOnFocus(true);


        if (!name.equals(getResources().getString(R.string.unknown)))
            editText.append(name + ",");

        if (!gender.equals(getResources().getString(R.string.unknown)))
            editText.append(gender + ",");

        if (!birthdayyear.equals(getResources().getString(R.string.unknown)))
            editText.append(birthdayyear + "生");

        if (!things.equals(getResources().getString(R.string.unknown)))
            editText.append("\n占卜事项：" + things);
        editText.append("\n起卦方式：" + QiGuaFangShi + "\n");
        String nongli = "";
        try {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            Lunar lunar = new Lunar(c);
            nongli = lunar.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        editText.append("农历：" + nongli + "\n");

        String guayao = bundle.getString("卦爻");

        GanZhiCalendar gzcal = new GanZhiCalendar();
        JSONArray jatime = gzcal.get_JSONArray_GanZhi_NianYueRiShi(year, month, day, hour, true);

        String nian_ganzhi = null, yue_ganzhi = null, ri_ganzhi = null, shi_ganzhi = null;
        try {
            nian_ganzhi = jatime.getString(0);
            yue_ganzhi = jatime.getString(1);
            ri_ganzhi = jatime.getString(2);
            shi_ganzhi = jatime.getString(3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LiuYaoPaiPan liuyaopaipan = new LiuYaoPaiPan();
        JSONObject all = liuyaopaipan.paiPan(guayao, yue_ganzhi, ri_ganzhi);

        StringBuffer sb = null;
        try {
            all.put("公历", year + "年" + (month + 1) + "月" + day + "日" + hour + "时" + minute + "分");

            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            int day_of_week = c.get(Calendar.DAY_OF_WEEK);
            all.put("星期", BaseData.DaysOfWeek[day_of_week - 1]);

            all.put("干支", jatime);
            all.put("空亡", gzcal.getKongWang(ri_ganzhi));

            sb = new GuaFormat().format(all);
        } catch (Exception e) {
            e.printStackTrace();
        }

        editText.append(sb.toString());
        editText.append("\n\n备注: ");

        initFloatingActionButton();
    }

    private void initFloatingActionButton() {
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_action_menu);
        floatingActionMenu.setClosedOnTouchOutside(true);

        fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        fabShot = (FloatingActionButton) findViewById(R.id.fab_shot);
        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FILE_PATH == null) {
                    dialogInputFileName.show();
                } else
                    saveToFile(FILE_PATH);

                floatingActionMenu.close(true);
            }
        });

        fabShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                new SaveToImageProcess().process();
                            }
                        }, 10
                );

                floatingActionMenu.close(true);
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "删除", Toast.LENGTH_SHORT).show();
                floatingActionMenu.close(true);
            }
        });
    }

    private class DialogEditTextListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int whichButton) {
            if (whichButton == DialogInterface.BUTTON_POSITIVE) {
                String s = inputFileNameEditText.getText().toString().trim();

                if (s.equals("")) {
                    new AlertDialog.Builder(DisplayActivity.this)
                            .setTitle("文件名错误")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("文件名不能为空")
                            .setPositiveButton("确定", null)
                            .show();

                    return;
                }

                try {
                    String[] fs = new File(getString(R.string.file_dir_history)).list();
                    for (int i = 0; i < fs.length; i++) {
                        if (fs[i].equals(s + ".txt")) {
                            new AlertDialog.Builder(DisplayActivity.this).setTitle("文件名错误").setIcon(android.R.drawable.ic_dialog_alert).setMessage("文件名已存在").setPositiveButton("确定", null).show();

                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String fp = getString(R.string.file_dir_history) + s + ".txt";
                FILE_PATH = fp;
                saveToFile(FILE_PATH);
            }
        }
    }


    private class ZoomInClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ZOOM_SIZE = ZOOM_SIZE + 1;
            editText.setTextSize(ZOOM_SIZE);
        }

    }

    private class ZoomOutClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ZOOM_SIZE = ZOOM_SIZE - 1;
            if (ZOOM_SIZE < 0 || ZOOM_SIZE == 0) {
                //zoomcontrols.setIsZoomOutEnabled(false);
                return;
            }

            editText.setTextSize(ZOOM_SIZE);
        }
    }


    private class SaveToImageProcess {
        public void process() {
            new SaveToImage().save();
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

                Toast msg = Toast.makeText(getApplicationContext(), "已完成截图,现在去图库查看吧!", Toast.LENGTH_LONG);
                msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                msg.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            editText.destroyDrawingCache();
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
            p.setTextSize(DISPLAY_EDIT_TEXT_FONT_SIZE);
            p.setAntiAlias(true);//去锯齿

            canvas.drawBitmap(src, 0, 0, p);

            canvas.drawText(str, w - 150, h - 30, p);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();

            return bmpTemp;
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

            Toast msg = Toast.makeText(getApplicationContext(), file.getName() + " 已经保存至 历史记录", Toast.LENGTH_LONG);
            msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
            msg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeFile(String fp) {
        try {
            File f = new File(fp);
            f.delete();

            Toast msg = Toast.makeText(DisplayActivity.this, f.getName() + " 已删除", Toast.LENGTH_LONG);
            msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
            msg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void actionChoose(int position) {
        switch (position) {
            //保存
            case 1:
                if (FILE_PATH == null) {
                    dialogInputFileName.show();
                } else
                    saveToFile(FILE_PATH);

                return;

            //编辑
            case 2:
                editable = !editable;
                editText.setFocusable(editable);
                editText.setFocusableInTouchMode(editable);
                if (editable)
                    editText.requestFocus();

                String s = editable ? "编辑[开]" : "编辑[关]";
                items[2] = s;

                return;

            //删除
            case 3:
                if (FILE_PATH != null) {
                    removeFile(FILE_PATH);

                    Intent it = new Intent(this, PaiGua.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    it.putExtra(getString(R.string.qiguafangshi), QiGuaFangShi);
                    startActivity(it);
                    this.finish();
                } else {
                    //Toast msg=Toast.makeText(DisplayActivity.this,"此卦还没有保存", Toast.LENGTH_LONG);
                    //	msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                    //	msg.show();

                    this.finish();
                }

                return;

            //设置
            case 4:
                new SetDisplayActivity();

                return;

            default:
                return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ID = item.getItemId();

        switch (ID) {
            case android.R.id.home:
                Intent intent = new Intent(this, PaiGua.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(getString(R.string.qiguafangshi), QiGuaFangShi);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private class SetDisplayActivity {
        private CheckBox checkBoxSet1 = null;
        private EditText editTextSet1 = null, editTextSet2 = null, editTextSet3 = null;
        private AlertDialog dialogInputSet = null;
        private SharedPreferences sp = null;
        private Editor editor = null;

        public SetDisplayActivity() {
            sp = DisplayActivity.this.getSharedPreferences(getString(R.string.user_store_info), Context.MODE_PRIVATE);
            editor = sp.edit();

            LayoutInflater inflater = (LayoutInflater) (DisplayActivity.this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.set, null);

            checkBoxSet1 = (CheckBox) view.findViewById(R.id.checkBoxSet1);
            //checkBoxSet1.setEnabled(false);
            checkBoxSet1.setVisibility(View.GONE);
            //checkBoxSet1.setHeight(0);

            CheckBox checkBoxSet2 = (CheckBox) view.findViewById(R.id.checkBoxSet2);
            //checkBoxSet2.setEnabled(false);
            checkBoxSet2.setVisibility(View.GONE);
            //checkBoxSet2.setHeight(0);

            editTextSet1 = (EditText) (view.findViewById(R.id.editTextSet1));
            editTextSet1.setHint("字体大小：" + DISPLAY_EDIT_TEXT_FONT_SIZE);

            editTextSet2 = (EditText) (view.findViewById(R.id.editTextSet2));
            //editTextSet2.setEnabled(false);
            editTextSet2.setVisibility(View.GONE);
            //editTextSet2.setHeight(0);

            editTextSet3 = (EditText) (view.findViewById(R.id.editTextSet3));
            //editTextSet3.setEnabled(false);
            editTextSet3.setVisibility(View.GONE);
            //editTextSet3.setHeight(0);

            dialogInputSet = new AlertDialog.Builder(DisplayActivity.this).setTitle("设置本页显示内容").setIcon(android.R.drawable.ic_menu_set_as).setView(view).setPositiveButton("确定", new DialogEditTextListener()).setNegativeButton("取消", null).show();
        }


        private class DialogEditTextListener implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (whichButton == DialogInterface.BUTTON_POSITIVE) {
                    String s1 = editTextSet1.getText() + "".trim();

                    try {
                        if (!s1.equals("")) {
                            DISPLAY_EDIT_TEXT_FONT_SIZE = Float.valueOf(s1);
                            editor.putFloat(DISPLAY_EDIT_TEXT_FONT_SIZE_TAG, DISPLAY_EDIT_TEXT_FONT_SIZE);
                        }

                        editor.commit();

                        editText.setTextSize(DISPLAY_EDIT_TEXT_FONT_SIZE);

                        Toast msg = Toast.makeText(DisplayActivity.this, "设置已完成", Toast.LENGTH_LONG);
                        msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                        msg.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}