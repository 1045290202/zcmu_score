package com.sjk.zcmu.score;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bin.david.form.utils.DensityUtils;
import com.sjk.zcmu.score.adapter.AboutAdapter;
import com.sjk.zcmu.score.model.About;
import com.sjk.zcmu.score.utils.SCheckApkExist;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    private final static String coolapkPackageName = "com.coolapk.market";

    private List<About> developerInfoList;
    private List<About> appInfoList;

    private int totalHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        init();
    }

    private void init() {
        developerInfoList = new ArrayList<>();
        appInfoList = new ArrayList<>();

        initDeveloperInfo();
        initAppInfo();
    }

    private void initDeveloperInfo() {
        About developer = new About("开发者", "酷安ID @大神sjk ，某个浙中医大滨江学院学生");
        developerInfoList.add(developer);
        About communicate = new About("QQ交流（1045290202）", "点击与开发者交流或反馈BUG");
        developerInfoList.add(communicate);

        AboutAdapter aboutAdapter = new AboutAdapter(this, R.layout.about, developerInfoList);
        ListView listView = findViewById(R.id.developer_info);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = DensityUtils.dp2px(this, 60 * 2) + listView.getDividerHeight() * 1;
        listView.setLayoutParams(params);

        listView.setAdapter(aboutAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                About about = developerInfoList.get(position);
                Uri uri;
                Intent intent;
                switch (position) {
                    case 0: {
                        if (SCheckApkExist.checkApkExist(AboutActivity.this, coolapkPackageName)) {
                            uri = Uri.parse("coolmarket://u/458995");
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.setPackage(coolapkPackageName);
                            startActivity(intent);
                        } else {
                            uri = Uri.parse("https://www.coolapk.com/u/458995");
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                        break;
                    }
                    case 1: {
                        if (SCheckApkExist.checkApkExist(AboutActivity.this, "com.tencent.mobileqq") ||
                                SCheckApkExist.checkApkExist(AboutActivity.this, "com.tencent.tim")) {
                            final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=1045290202";
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                        } else {
                            Toast.makeText(AboutActivity.this, "请安装QQ或TIM客户端", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                About about = developerInfoList.get(position);
                Uri uri;
                Intent intent;
                switch (position) {
                    case 0: {
                        String developer = "大神sjk";
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText(getAppName(AboutActivity.this), developer);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(AboutActivity.this, "已复制“" + developer + "”", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                return true;
            }
        });
    }

    private void initAppInfo() {
        PackageManager packageManager = getApplicationContext().getPackageManager();
        String version = "";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        About appVersion = new About("版本", version);
        appInfoList.add(appVersion);
        About app = new About("应用详情", "在酷安中查看");
        appInfoList.add(app);
        About openSource = new About("开源链接（GitHub）", "https://github.com/1045290202/zcmu_score");
        appInfoList.add(openSource);

        AboutAdapter aboutAdapter = new AboutAdapter(this, R.layout.about, appInfoList);
        ListView listView = findViewById(R.id.app_info);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = DensityUtils.dp2px(this, 60 * 3) + listView.getDividerHeight() * 2;
        listView.setLayoutParams(params);

        listView.setAdapter(aboutAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                About about = appInfoList.get(position);
                Uri uri;
                Intent intent;
                switch (position) {
                    case 1: {
                        if (SCheckApkExist.checkApkExist(AboutActivity.this, coolapkPackageName)) {
                            uri = Uri.parse("market://details?id=" + getPackageName());
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.setPackage(coolapkPackageName);
                            startActivity(intent);
                        } else {
                            uri = Uri.parse("https://www.coolapk.com/apk/com.sjk.zcmu.score");
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                        break;
                    }
                    case 2: {
                        uri = Uri.parse("https://github.com/1045290202/zcmu_score");
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                About about = appInfoList.get(position);
                Uri uri;
                Intent intent;
                switch (position) {
                    case 1: {
                        uri = Uri.parse("market://details?id=" + getPackageName());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (intent.getPackage() != null) {
                            startActivity(intent);
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
                return true;
            }
        });
    }

    public String getAppName(Context context) {
        try {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
