package com.sjk.zcmu.score;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.orhanobut.hawk.Hawk;
import com.sjk.zcmu.score.model.Info;
import com.sjk.zcmu.score.model.ScoreTableBean;
import com.sjk.zcmu.score.utils.SBitmap;
import com.sjk.zcmu.score.utils.SOCR;
import com.sjk.zcmu.score.utils.SPermissions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.sjk.zcmu.score.utils.SBitmap.ZCMU_SCORE;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String ZJTCM_URL = "http://zfxk.zjtcm.net/";
    //private static final String USER_AGENT = "Mozilla/5.0( X11;Ubuntu;Linux x86_64 ;rv:61.0) Gecko20100101Firefox/61.0";
    private static final String USER_AGENT = "zcmu_score(by SJK)";
    private static final String NOMEDIA = ".nomedia";
    private String checkCodeUrl;
    private String submitUrl;
    private String __VIEWSTATE;
    private String __VIEWSTATEGENERATOR;

    private static Document doc;
    private static Map<String, String> cookies;
    private boolean haveStoragePermissions = false;
    private boolean useHistory = true;
    private boolean useOCR = false;

    //用户信息
    private String name;
    private String studentId;

    private ImageView checkCodeImage;
    private MaterialDialog waitDialog;

    private List<Integer> clickViews = Arrays.asList(
            R.id.submit,
            R.id.check_code_image,
            R.id.check_code_layout,
            R.id.use_web
    );

    /*
        回调
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0: {
                    Toast.makeText(MainActivity.this, "网络好像出了一点小差", Toast.LENGTH_SHORT).show();
                    waitDialog.dismiss();
                    break;
                }
                case 1: {
                    Document doc = (Document) msg.obj;
                    initCheckCode(doc);
                    initLogin(doc);
                    break;
                }
                case 2: {
                    getScore();
//                    Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
//                    startActivity(intent);
                    break;
                }
                case 3: {
                    Element tbody = ((Elements) msg.obj).get(0);

                    List<String> nameList = new ArrayList<>();
                    List<List<String>> scoreListList = new ArrayList<>();

                    Elements trs = tbody.select("tr");
                    int i = 0;
                    for (Element tr : trs) {
                        Elements tds = tr.select("td");
                        List<String> list = new ArrayList<>();
                        for (Element td : tds) {
                            if (i < 1) {
                                nameList.add(td.text());
                            } else {
                                list.add(td.text());
                            }
                        }
                        if (i >= 1) {
                            scoreListList.add(list);
                        }
                        i++;
                    }

                    List<ScoreTableBean> scoreTableBeanList = new ArrayList<>();
                    List<String> schoolYearList = new ArrayList<>();
                    for (List<String> scoreList : scoreListList) {
                        ScoreTableBean scoreTableBean = new ScoreTableBean(
                                scoreList.get(0),
                                scoreList.get(1),
                                scoreList.get(2),
                                scoreList.get(3),
                                scoreList.get(4),
                                scoreList.get(5),
                                scoreList.get(6),
                                scoreList.get(7),
                                scoreList.get(8),
                                scoreList.get(9),
                                scoreList.get(10),
                                scoreList.get(11),
                                scoreList.get(12),
                                scoreList.get(13),
                                scoreList.get(14),
                                scoreList.get(15));
                        scoreTableBeanList.add(scoreTableBean);
                        schoolYearList.add(scoreList.get(0));
                    }
                    Info.setScoreTableBeanList(scoreTableBeanList);
                    HashSet<String> hashSet = new HashSet<>(schoolYearList);
                    schoolYearList.clear();
                    schoolYearList.add("全部学年");
                    schoolYearList.addAll(hashSet);
                    Info.setSchoolYearList(schoolYearList);

                    waitDialog.dismiss();

                    Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                    startActivity(intent);

                    EditText checkCodeText = findViewById(R.id.check_code);
                    checkCodeText.setText("");
                    initZJTCM();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hawk.init(this).build();

        checkCodeImage = findViewById(R.id.check_code_image);
        TextView vpnText = findViewById(R.id.vpn_text);
        vpnText.setText(Html.fromHtml("无法查询？试试下载<a href=\"https://vpn.zcmu.edu.cn\">浙中医大VPN</a>"));
        vpnText.setMovementMethod(LinkMovementMethod.getInstance());

        if (SPermissions.checkStoragePermissions(this)) {
            haveStoragePermissions = true;
            initOCR();
            initZJTCM();
            initHistory();
        } else {
            SPermissions.requestPermissions(this);
        }

        initNoMedia();
        setViewClick();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                haveStoragePermissions = true;
                initOCR();
                initZJTCM();
                initHistory();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void setViewClick() {
        for (int clickView : clickViews) {
            findViewById(clickView).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit: {
                login();
                break;
            }
            case R.id.check_code_layout:
            case R.id.check_code_image: {
                initZJTCM();
                break;
            }
            case R.id.use_web: {
                Intent intent = new Intent(this, WebActivity.class);
                startActivity(intent);
            }
            default: {
                break;
            }
        }
    }

    public void initOCR() {
        useOCR = Hawk.get("useOCR", false);
        if (useOCR) {
            SOCR.initAccessToken(this);
        }
    }

    private void initHistory() {
        useHistory = Hawk.get("useHistory", true);

        if (useHistory) {
            EditText usernameText = findViewById(R.id.username);
            EditText passwordText = findViewById(R.id.password);
            usernameText.setText(Hawk.get("username", ""));
            passwordText.setText(Hawk.get("password", ""));
        }
    }

    private void initZJTCM() {
        waitDialog = new MaterialDialog.Builder(this)
                .title("正在加载正方教务管理系统")
                .content("加载中...")
                .progress(true, 100, true)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .build();
        waitDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc;
                try {
                    Connection con = Jsoup.connect(ZJTCM_URL);
                    Connection.Response resp = con.execute();
                    doc = con.get();
                    cookies = resp.cookies();

                    Message message = new Message();
                    message.what = 1;
                    message.obj = doc;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private void initCheckCode(Document doc) {
        checkCodeUrl = ZJTCM_URL + doc.select("img#icode").attr("src");
        downloadCheckCodeImage(checkCodeUrl);
    }

    private void downloadCheckCodeImage(String checkCodeUrl) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不缓存
                .skipMemoryCache(true);

        LazyHeaders.Builder builder = new LazyHeaders.Builder();
        StringBuilder cookie = new StringBuilder();

        int l = cookies.size();
        int i = 0;
        for (String key : cookies.keySet()) {
            String value = cookies.get(key);
            cookie.append(key).append("=").append(value);
            if (i < l - 1) {
                cookie.append(";");
                i++;
            }
        }

        SDocument.setCookies(cookies);
        builder.addHeader("Cookie", cookie.toString());
        GlideUrl glideUrl = new GlideUrl(checkCodeUrl, builder.build());

        Glide.with(this)
                .asBitmap()
                .load(glideUrl)
                .apply(requestOptions)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        waitDialog.dismiss();
                        Toast.makeText(MainActivity.this, "获取验证码失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(final Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        waitDialog.dismiss();

                        if (useOCR) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] bitmapByte = stream.toByteArray();
                            SBitmap.savaBitmap(SBitmap.CHECK_CODE, bitmapByte);
                            try {
                                File file = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + SBitmap.ZCMU_SCORE + "/" + SBitmap.CHECK_CODE);
                                SOCR.recognizeGeneralBasic(file, MainActivity.this);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return false;
                    }
                })
                .into(checkCodeImage);

    }


    private void initLogin(Document doc) {
        submitUrl = ZJTCM_URL + doc.select("form#form1").attr("action");
        __VIEWSTATE = doc.select("input[name=__VIEWSTATE]").val();
        __VIEWSTATEGENERATOR = doc.select("input[name=__VIEWSTATEGENERATOR]").val();
    }

    private void login() {
        EditText usernameText = findViewById(R.id.username);
        EditText passwordText = findViewById(R.id.password);
        EditText checkCodeText = findViewById(R.id.check_code);

        final String username = usernameText.getText().toString();
        final String password = passwordText.getText().toString();
        final String checkCode = checkCodeText.getText().toString();

        if (username.equals("")) {
            Toast.makeText(this, "用户名未填写", Toast.LENGTH_SHORT).show();
            usernameText.setFocusable(true);
            usernameText.setFocusableInTouchMode(true);
            usernameText.requestFocus();
            return;
        } else if (password.equals("")) {
            Toast.makeText(this, "密码未填写", Toast.LENGTH_SHORT).show();
            passwordText.setFocusable(true);
            passwordText.setFocusableInTouchMode(true);
            passwordText.requestFocus();
            return;
        } else if (checkCode.equals("")) {
            Toast.makeText(this, "验证码未填写", Toast.LENGTH_SHORT).show();
            checkCodeText.setFocusable(true);
            checkCodeText.setFocusableInTouchMode(true);
            checkCodeText.requestFocus();
            return;
        }

        Hawk.put("username", username);
        Hawk.put("password", password);

        waitDialog = new MaterialDialog.Builder(this)
                .title("正在努力获取数据")
                .content("加载中...")
                .progress(true, 100, true)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .build();
        waitDialog.show();

        studentId = username;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection con = Jsoup.connect(submitUrl);
                    con.cookies(cookies)
                            .data("__VIEWSTATE", __VIEWSTATE)
                            .data("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR)
                            .data("txtUserName", username)
                            .data("Textbox1", "")
                            .data("TextBox2", password)
                            .data("txtSecretCode", checkCode)
                            .data("RadioButtonList1", "%D1%A7%C9%FA")
                            .data("Button1", "")
                            .data("hidPdrs", "")
                            .data("hidsc", "")
                            .userAgent(USER_AGENT);
                    Connection.Response resp = con.execute();
                    doc = con.get();
                    cookies = resp.cookies();

                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    waitDialog.dismiss();
                }
            }
        }).start();
    }

    private void getScore() {
        name = doc.select("span#xhxm").text().replace("同学", "");
        if (name.equals("")) {
            //失败
            Toast.makeText(MainActivity.this, "登录失败，请检查输入信息是否正确", Toast.LENGTH_SHORT).show();
            EditText checkCodeText = findViewById(R.id.check_code);
            checkCodeText.setText("");
            waitDialog.dismiss();
            initZJTCM();
            return;
        }
        System.out.println("姓名：" + name);
        System.out.println("学号：" + studentId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = ZJTCM_URL + "xscj_gc.aspx?xh=" + studentId + "&xm=" + name + "&gnmkdm=N121605";
                    Connection con = Jsoup.connect(url);
                    con.followRedirects(false);
                    con.cookies(SDocument.getCookies())
                            .header("Referer", ZJTCM_URL + "xs_main.aspx?xh=" + studentId)
                            .userAgent(USER_AGENT);
                    doc = con.post();

                    ///
                    String __VIEWSTATE = doc.select("input[name=__VIEWSTATE]").val();
                    String __VIEWSTATEGENERATOR = doc.select("input[name=__VIEWSTATEGENERATOR]").val();
                    System.out.println(__VIEWSTATE);
                    ///

                    //成绩查询
                    con = Jsoup.connect(ZJTCM_URL + "xscj_gc.aspx?xh=" + studentId + "&xm=" + name + "&gnmkdm=N121605");
                    con.followRedirects(false);
                    con.cookies(SDocument.getCookies())
                            .data("__VIEWSTATE", __VIEWSTATE)
                            .data("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR)
                            .data("Button2", "在校学习成绩查询")
                            .userAgent(USER_AGENT)
                            .header("Referer", ZJTCM_URL + "xs_main.aspx?xh=" + studentId);
                    doc = con.post();

                    //开始解析文档，获取成绩
                    Elements tbody = doc.select("table#Datagrid1").select("tbody");

                    Message message = new Message();
                    message.what = 3;
                    message.obj = tbody;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    waitDialog.dismiss();
                }
            }
        }).start();
    }

    private void initNoMedia() {
        try {
            File no = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + ZCMU_SCORE + "/" + NOMEDIA);
            if (!no.exists()) {
                no.createNewFile();
            }
        } catch (Exception e) {
            System.err.println("创建.nomedia文件失败");
        }
    }
}
