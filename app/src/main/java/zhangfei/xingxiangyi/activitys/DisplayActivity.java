package zhangfei.xingxiangyi.activitys;

import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.view.Gravity;

import java.util.Calendar;
import java.io.File;
import java.io.FileOutputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.view.View;
import android.widget.Toast;
import android.widget.ZoomControls;

import android.content.SharedPreferences;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.xw.repo.XEditText;

import org.json.*;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.core.BaseData;
import zhangfei.xingxiangyi.core.GanZhiCalendar;
import zhangfei.xingxiangyi.core.GuaFormat;
import zhangfei.xingxiangyi.core.LiuYaoPaiPan;
import zhangfei.xingxiangyi.core.Lunar;
import zhangfei.xingxiangyi.utils.FileUtil;
import zhangfei.xingxiangyi.utils.Util;


public class DisplayActivity extends XingXiangYiActivity {
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton fabSave;
    private FloatingActionButton fabShare;
    private FloatingActionButton fabShot;

    private String[] items = new String[]{"请选择", "保存", "编辑[关]", "删除", "设置"};

    private String QiGuaFangShi = null;

    private EditText editText = null;
    private boolean editable = false;

    private XEditText inputFileNameEditText = null;
    private AlertDialog dialogInputFileName = null;

    private int ZOOM_SIZE = 16;

    private String FILE_PATH = null;
    private String DEFAULT_FILE_NAME = "";

    private float DISPLAY_EDIT_TEXT_FONT_SIZE = 18.0f;
    private final static String DISPLAY_EDIT_TEXT_FONT_SIZE_TAG = "display_edit_text_font_size";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_displaygua, null);
        setContentView(view);
        view.setKeepScreenOn(true);

        items[2] = editable ? "编辑[开]" : "编辑[关]";

        editText = (EditText) findViewById(R.id.editTextDisplayGua);

        inputFileNameEditText = new XEditText(this);
        dialogInputFileName = new AlertDialog.Builder(this)
                .setTitle("输入文件名")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(inputFileNameEditText)
                .setPositiveButton("确定", new DialogEditTextListener())
                .setNegativeButton("取消", null)
                .create();


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

        Bundle bundle = getIntent().getBundleExtra("UserData");

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
        fabShare = (FloatingActionButton) findViewById(R.id.fab_share);
        fabShot = (FloatingActionButton) findViewById(R.id.fab_shot);

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
                FileUtil.save(getApplicationContext(), editText, editText.getWidth(), editText.getHeight(), new FileUtil.OnFileListener() {
                    @Override
                    public void onDone(File file) {
                        Toast.makeText(getApplicationContext(), "截图已保存" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

                floatingActionMenu.close(true);
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FileUtil.save(getApplicationContext(), editText, editText.getWidth(), editText.getHeight(), new FileUtil.OnFileListener() {
                    @Override
                    public void onDone(File file) {
                        Util.shareImage(getApplicationContext(), file);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

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

            //Toast msg = Toast.makeText(DisplayActivity.this, f.getName() + " 已删除", Toast.LENGTH_LONG);
            //msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
            //msg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}