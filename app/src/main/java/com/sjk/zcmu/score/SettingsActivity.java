package com.sjk.zcmu.score;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.hawk.Hawk;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Integer> clickViews = Arrays.asList(
            R.id.url
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Hawk.init(this).build();
        init();
        setViewClick();
    }

    private void setViewClick() {
        for (int clickView : clickViews) {
            findViewById(clickView).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.url: {
                MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                        .title("请输入")
                        .content("请输入正方教务管理系统链接")
                        .negativeText("取消")
                        .input("请输入链接",
                                Hawk.get("zf", MainActivity.ZJTCM_URL),
                                true,
                                new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                        if (input.length() != 0) {
                                            if (input.charAt(input.length() - 1) != '/') {
                                                input = input + "/";
                                            }
                                            Hawk.put("zf", input.toString());
                                        } else {
                                            Hawk.put("zf", MainActivity.ZJTCM_URL);
                                        }
                                        Toast.makeText(SettingsActivity.this, "重启APP生效", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .build();
                materialDialog.show();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void init() {
        //记录用户名密码
        Switch historySwitch = findViewById(R.id.history_switch);
        historySwitch.setChecked(Hawk.get("useHistory", true));
        historySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Hawk.put("useHistory", b);
                Hawk.put("username", "");
                Hawk.put("password", "");
                Toast.makeText(SettingsActivity.this, "重启APP生效", Toast.LENGTH_SHORT).show();
            }
        });

        //验证码识别
        Switch ocrSwitch = findViewById(R.id.ocr_switch);
        ocrSwitch.setChecked(Hawk.get("useOCR", false));
        ocrSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Hawk.put("useOCR", b);
                Toast.makeText(SettingsActivity.this, "重启APP生效", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
