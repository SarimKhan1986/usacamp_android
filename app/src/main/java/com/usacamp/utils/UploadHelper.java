package com.usacamp.utils;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.usacamp.activities.MyApplication;
import com.usacamp.constants.Constants;
import com.usacamp.services.OssService;

import java.io.File;
import java.util.Date;

import kotlin.Metadata;

public class UploadHelper {
//    //与个人的存储区域有关
//    private static final String ENDPOINT = "你的END_POINT ";
//    //上传仓库名
//    private static final String BUCKET_NAME = "你的BUCKET_NAME ";

    private static OSS getOSSClient() {
        OSSCredentialProvider credentialProvider =
                new OSSPlainTextAKSKCredentialProvider(Constants.OSS_ACCESS_KEY_ID,
                        Constants.OSS_ACCESS_KEY_SECURITY);
        return new OSSClient( MyApplication.getInstance().getCurrentActivity(), Constants.OSS_END_POINT, credentialProvider);
    }

    /**
     * 上传方法
     *
     * @param objectKey 标识
     * @param path      需上传文件的路径
     * @return 外网访问的路径
     */
    private static String upload(String objectKey, String path) {
        // 构造上传请求
        PutObjectRequest request =
                new PutObjectRequest(Constants.OSS_BUCKET_NAME,
                        objectKey, path);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader("Authorization", OSSUtils.sign(Constants.OSS_ACCESS_KEY_ID,Constants.OSS_ACCESS_KEY_SECURITY,objectKey));
        request.setMetadata(metadata);
        request.setCRC64(OSSRequest.CRC64Config.YES);

        try {
            //得到client
            OSS client = getOSSClient();
            //上传获取结果
            PutObjectResult result = client.putObject(request);
            //获取可访问的url
            String url = client.presignConstrainedObjectURL(Constants.OSS_BUCKET_NAME, objectKey, 3000);
            //格式打印输出
            String returnUrl = url.substring(0, url.indexOf("?"));
            return returnUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // path 除根路径以外的其它部分

    /**
     * 上传普通图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadImage(String path) {
        String key = getObjectImageKey(path);
        return upload(key, path);
    }

    /**
     * 上传头像
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadPortrait(String path) {
        String key = getObjectPortraitKey(path);
        return upload(key, path);
    }

    /**
     * 上传audio
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadAudio(String path, int level, int lesson, int part) {
        String key = getObjectAudioKey(path, level, lesson,part);
        return upload(key, path);
    }


    /**
     * 获取时间
     *
     * @return 时间戳 例如:201805
     */
    private static String getDateString() {
//        return DateFormat.format("yyyyMM", new Date()).toString();
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 返回key
     *
     * @param path 本地路径
     * @return key
     */
    //格式: image/201805/sfdsgfsdvsdfdsfs.jpg
    private static String getObjectImageKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    //格式: portrait/201805/sfdsgfsdvsdfdsfs.jpg
    private static String getObjectPortraitKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    //格式: audio/201805/sfdsgfsdvsdfdsfs.mp3
    private static String getObjectAudioKey(String path,int lv, int ls, int pr) {
        //String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("audio/camp%s/%s/%s/a_%s.wav", lv, ls, pr,dateString);
    }

}
