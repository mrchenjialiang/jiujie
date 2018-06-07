//package com.jiujie.jiujie.ui.activity;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.jiujie.base.jk.OnListener;
//import com.jiujie.base.util.UIHelper;
//import com.jiujie.base.util.permission.PermissionsManager;
//import com.jiujie.jiujie.R;
//import com.xunrui.ad.AdManager;
//import com.xunrui.ad.jk.OnSplashAdListener;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//public class AdCsActivity extends MyBaseActivity {
//
//    @Bind(R.id.cs_ad_container)
//    ViewGroup cs_ad_container;
//    @Bind(R.id.ad_pass)
//    View ad_pass;
//
//    public static void launch(Activity activity) {
//        activity.startActivity(new Intent(activity,AdCsActivity.class));
//    }
//
//    @Override
//    public void initUI() {
//        ButterKnife.bind(this);
//    }
//
//    @Override
//    public void initData() {
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_cs;
//    }
//
//    public void onStartThread(View view) {
//        PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
//                                                   @Override
//                                                   public void onListen(Boolean isHas) {
//                                                       UIHelper.showLog("PermissionsManager isHas:"+isHas);
//                                                       showSplashAd();
//                                                   }
//                                               },
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.ACCESS_FINE_LOCATION,//这个定位权限，APP本身不需求，也不影响广告的显示，主要是第三方如友盟之类的用于用户位置统计
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        );
//    }
//
//    private void showSplashAd() {
//        AdManager.instance(mActivity).fetchSplashAD(this, cs_ad_container, ad_pass, new OnSplashAdListener() {
//            @Override
//            public void onADDismissed() {
//                if (mActivity == null || mActivity.isFinishing()) return;
//                //广告消息点击跳转链接也会消失，但这时候不应该跳转进入下一页，不然会遮挡广告跳转的链接
//                UIHelper.showLog(AdCsActivity.this, "onADDismissed");
//            }
//
//            @Override
//            public void onNoAD() {
//                UIHelper.showLog(AdCsActivity.this, "onNoAD");
//            }
//
//            @Override
//            public void onADPresent() {
//                UIHelper.showLog(AdCsActivity.this, "onADPresent");
//            }
//            @Override
//            public void onADTick(long time) {
//                UIHelper.showLog(AdCsActivity.this, "onADTick time:"+time);
//            }
//
//            @Override
//            public void onADClicked() {
//                UIHelper.showLog(AdCsActivity.this, "onADClicked");
//            }
//        });
//    }
//}
