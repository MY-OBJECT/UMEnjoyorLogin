package com.cceup.oa.umshare;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * 姓名：孟向阳
 * 公司：中能联合工程技术有限公司
 * 邮箱：1171134249@qq.com
 * 时间：2018/8/15
 * 描述：
 */

public class ShareUtil {

    public static void shareWeb(final Activity activity, String webUrl, String title, String description, String imagUrl, int imagid, SHARE_MEDIA platform){

        UMWeb web = new UMWeb(webUrl);//连接地址
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        if (TextUtils.isEmpty(webUrl)) {
            web.setThumb(new UMImage(activity, imagid));  //本地缩略图
        } else {
            web.setThumb(new UMImage(activity, imagUrl));  //网络缩略图
        }

        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (share_media.name().equals("WEIXIN_FAVORITE")) {
                                    Toast.makeText(activity, share_media + " 收藏成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, share_media + " 分享成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (throwable != null) {
                            Log.d("throw", "throw:" + throwable.getMessage());
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, share_media + " 分享失败", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, share_media + " 分享取消", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .share();
    }
}
