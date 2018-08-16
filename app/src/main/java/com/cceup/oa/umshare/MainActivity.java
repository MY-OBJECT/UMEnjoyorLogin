package com.cceup.oa.umshare;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    private ShareAction mShareA;
    private CustomShareListener mShareListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        mShareListener = new CustomShareListener(this);
        mShareA = new ShareAction(this).setDisplayList(
                SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ,
                SHARE_MEDIA.QZONE,
                SHARE_MEDIA.ALIPAY, SHARE_MEDIA.DINGTALK, SHARE_MEDIA.DOUBAN,
                SHARE_MEDIA.DROPBOX, SHARE_MEDIA.EMAIL, SHARE_MEDIA.EVERNOTE,
                SHARE_MEDIA.LINE, SHARE_MEDIA.INSTAGRAM, SHARE_MEDIA.YIXIN)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        UMWeb web = new UMWeb(Constant.url);
                        web.setTitle("来自分享面板标题");
                        web.setDescription("来自分享面板内容");
                        web.setThumb(new UMImage(MainActivity.this, R.drawable.ic_launcher_background));
                        new ShareAction(MainActivity.this).withMedia(web)
                                .setPlatform(share_media)
                                .setCallback(mShareListener)
                                .share();
                    }
                });
    }

    /**
     * 微信
     *
     * @param view
     */
    public void wxshare(View view) {
        ShareUtil.shareWeb(this, Constant.url, Constant.title, Constant.text, Constant.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN);
    }

    /**
     * 朋友圈
     *
     * @param view
     */
    public void wxcircleshare(View view) {
        ShareUtil.shareWeb(this, Constant.url, Constant.title, Constant.text, Constant.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    /**
     * qq分享
     *
     * @param view
     */
    public void qqshare(View view) {
        ShareUtil.shareWeb(this, Constant.url, Constant.title, Constant.text, Constant.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.QQ);
    }

    /**
     * qq空间分享
     *
     * @param view
     */
    public void qqcircleshare(View view) {
        ShareUtil.shareWeb(this, Constant.url, Constant.title, Constant.text, Constant.imageurl, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE);
    }

    /**
     *
     */
    public void loginQQ(View view) {
        authorization(SHARE_MEDIA.QQ);
    }

    /**
     * 微信登录
     *
     * @param view
     */
    public void loginWX(View view) {
        authorization(SHARE_MEDIA.WEIXIN);
    }

    /**
     * @param view
     */
    public void loginSine(View view) {
        authorization(SHARE_MEDIA.SINA);
    }

    /**
     * 更多分享
     *
     * @param view
     */
    public void moreEnjoy(View view) {
        mShareA.open();
    }


    /**
     * 登录授权
     */
    private void authorization(SHARE_MEDIA share_media) {

        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.e("debug", "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.e("debug", "授权完成");

                String uid = map.get("uid");
                String openid = map.get("openid");
                String unionid = map.get("unionid");
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");
                String expires_in = map.get("expires_in");
                String name = map.get("name");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");
                Log.e("debug", "uid=" + uid + ",openid=" + openid + ",unionid=" + unionid + ",access_token=" + access_token + ",refresh_token=" + refresh_token + ",expires_in=" + expires_in +
                        ",name=" + name + ",gender=" + gender + ",iconurl=" + iconurl
                );
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.e("debug", "授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.e("debug", "授权取消");
            }
        });
    }


    /**
     * 分享监听
     */
    private static class CustomShareListener implements UMShareListener {
        private WeakReference<MainActivity> mActivity;

        private CustomShareListener(MainActivity activity) {
            mActivity = new WeakReference(activity);
        }
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

            if (share_media.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), share_media + "收藏成功！", Toast.LENGTH_SHORT).show();
            } else {
                if (share_media != SHARE_MEDIA.MORE && share_media != SHARE_MEDIA.SMS
                        && share_media != SHARE_MEDIA.EMAIL
                        && share_media != SHARE_MEDIA.FLICKR
                        && share_media != SHARE_MEDIA.FOURSQUARE
                        && share_media != SHARE_MEDIA.TUMBLR
                        && share_media != SHARE_MEDIA.POCKET
                        && share_media != SHARE_MEDIA.PINTEREST

                        && share_media != SHARE_MEDIA.INSTAGRAM
                        && share_media != SHARE_MEDIA.GOOGLEPLUS
                        && share_media != SHARE_MEDIA.YNOTE
                        && share_media != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(),"分享成功",Toast.LENGTH_SHORT).show();

                }
            }

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            if (share_media != SHARE_MEDIA.MORE && share_media != SHARE_MEDIA.SMS
                    && share_media != SHARE_MEDIA.EMAIL
                    && share_media != SHARE_MEDIA.FLICKR
                    && share_media != SHARE_MEDIA.FOURSQUARE
                    && share_media != SHARE_MEDIA.TUMBLR
                    && share_media != SHARE_MEDIA.POCKET
                    && share_media != SHARE_MEDIA.PINTEREST

                    && share_media != SHARE_MEDIA.INSTAGRAM
                    && share_media != SHARE_MEDIA.GOOGLEPLUS
                    && share_media != SHARE_MEDIA.YNOTE
                    && share_media != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), share_media + " 分享失败啦", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
