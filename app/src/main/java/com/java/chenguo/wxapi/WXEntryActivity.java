package com.java.chenguo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.*;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.util.ManifestUtil;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ManifestUtil.getWeixinKey(this), false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Intent intent = new Intent();
        intent.setAction(ShareConstant.ACTION_WEIXIN_CALLBACK);
        intent.putExtra(ShareConstant.EXTRA_WEIXIN_RESULT, resp.errCode);
        sendBroadcast(intent);
        finish();
    }
}
