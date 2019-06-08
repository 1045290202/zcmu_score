package com.sjk.zcmu.score;

import android.os.Bundle;

import com.orhanobut.hawk.Hawk;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Hawk.init(this).build();
        init();
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
