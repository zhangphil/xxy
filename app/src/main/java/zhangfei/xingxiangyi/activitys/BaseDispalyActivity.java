package zhangfei.xingxiangyi.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.model.XingXiangYiBean;
import zhangfei.xingxiangyi.utils.FileUtil;
import zhangfei.xingxiangyi.utils.Util;

/**
 * Created by Phil on 2017/5/2.
 */

public class BaseDispalyActivity extends XingXiangYiActivity {
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton fabSave;
    private FloatingActionButton fabShare;
    private FloatingActionButton fabShot;
    private FloatingActionButton fabDelete;

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

        initFloatingActionButton();
    }


    private void initFloatingActionButton() {
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floating_action_menu);
        floatingActionMenu.setClosedOnTouchOutside(true);

        fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        fabShare = (FloatingActionButton) findViewById(R.id.fab_share);
        fabShot = (FloatingActionButton) findViewById(R.id.fab_shot);
        fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "删除", Toast.LENGTH_SHORT).show();
                floatingActionMenu.close(true);
            }
        });
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
}
