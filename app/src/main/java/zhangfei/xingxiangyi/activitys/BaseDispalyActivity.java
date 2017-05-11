package zhangfei.xingxiangyi.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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

    private EditText editText = null;
    private XingXiangYiBean xingXiangYiBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_displaygua, null);
        setContentView(view);
        view.setKeepScreenOn(true);

        setOnBack(this, view);

        editText = (EditText) findViewById(R.id.editTextDisplayGua);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        editText.setMinWidth(displayMetrics.widthPixels);
        editText.setMinHeight(displayMetrics.heightPixels);

        try {
            Intent intent = getIntent();
            xingXiangYiBean = (XingXiangYiBean) intent.getSerializableExtra(XingXiangYiBean.TAG);

            FileInputStream fis = new FileInputStream(xingXiangYiBean.file);
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

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtil.saveTextToFile(editText.getText() + "", xingXiangYiBean.file, new FileUtil.OnFileListener() {
                    @Override
                    public void onDone(File file) {
                        Toast.makeText(getApplicationContext(), "已保存" + file.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

                floatingActionMenu.close(true);
            }
        });

        fabShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtil.saveView(getApplicationContext(), editText, editText.getWidth(), editText.getHeight(), new FileUtil.OnFileListener() {
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

                FileUtil.saveView(getApplicationContext(), editText, editText.getWidth(), editText.getHeight(), new FileUtil.OnFileListener() {
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
}
