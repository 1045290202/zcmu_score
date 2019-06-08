package com.sjk.zcmu.score.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.EditText;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.sjk.zcmu.score.R;

import java.io.File;

public class SOCR {
    private static Context context;
    public static String ocr = "";

    public static void initAccessToken(Context context) {
        SOCR.context = context;
        OCR.getInstance(context).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                String token = result.getAccessToken();
                System.out.println(token);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
            }
        }, context);
    }

    public static void recognizeGeneralBasic(File file, final Activity activity) {
        // 通用文字识别参数设置
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);
        param.setLanguageType(GeneralBasicParams.ENGLISH);
        param.setImageFile(file);

        // 调用通用文字识别服务
        OCR.getInstance(context).recognizeGeneralBasic(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                // 调用成功，返回GeneralResult对象
                StringBuilder string = new StringBuilder();
                for (WordSimple wordSimple : result.getWordList()) {
                    // wordSimple不包含位置信息
                    string.append(wordSimple.getWords());
                }
                ocr = string.toString().replaceAll(" ", "");
                EditText checkCode = activity.findViewById(R.id.check_code);
                checkCode.setText(ocr);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
            }
        });
    }
}
