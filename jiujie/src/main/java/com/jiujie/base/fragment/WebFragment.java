package com.jiujie.base.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jiujie.base.R;

/**
 * WebView页面，需要传参 String url,String title
 * @author ChenJiaLiang
 */
public class WebFragment extends BaseFragment {
	
	private WebView webView;
	private String title;
	private String url;

	@Override
	protected void initUI() {
		initTitle();
		initWebView();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(webView!=null){
			webView.onResume();
			webView.resumeTimers();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		webView.loadUrl("about:blank");
		webView.destroy();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		webView.onPause();
		webView.pauseTimers();
		
		((AudioManager)mActivity.getSystemService(
				Context.AUDIO_SERVICE)).requestAudioFocus(
						new OnAudioFocusChangeListener() {
							@Override
							public void onAudioFocusChange(int focusChange) {}
						}, AudioManager.STREAM_MUSIC, 
						AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
	}
	
	public void setInfo(String title,String url) {
		this.title = title;
		this.url = url;
	}
	private void initTitle() {
		if(!TextUtils.isEmpty(title)){
			mTitle.setTitleText(title);
		}
//		mTitle.setLeftButtonBack();
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		webView = (WebView) mView.findViewById(R.id.webView1);
		webView.setClickable(true);
		webView.setInitialScale((int) 0.1);
		webView.setWebViewClient(new webViewClient()); 
		
		WebSettings  ws  = webView.getSettings();
		ws .setJavaScriptEnabled(true);
		ws .setDefaultTextEncodingName("UTF-8");
		ws.setUseWideViewPort(true);
		ws.setSupportZoom(true);
		ws.setBuiltInZoomControls(true);
		ws.setLoadWithOverviewMode(true);
//		ws.setDefaultZoom(ZoomDensity.FAR);
		
		DisplayMetrics metrics = new DisplayMetrics();  
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);  
		int mDensity = metrics.densityDpi;  
		if (mDensity == 240) {
			ws.setDefaultZoom(ZoomDensity.FAR);  
		} else if (mDensity == 160) {  
			ws.setDefaultZoom(ZoomDensity.MEDIUM);  
		} else if(mDensity == 120) {  
			ws.setDefaultZoom(ZoomDensity.CLOSE);  
		}else if(mDensity == DisplayMetrics.DENSITY_XHIGH){  
			ws.setDefaultZoom(ZoomDensity.FAR);   
		}else if (mDensity == DisplayMetrics.DENSITY_TV){  
			ws.setDefaultZoom(ZoomDensity.FAR);   
		}else{  
			ws.setDefaultZoom(ZoomDensity.MEDIUM);  
		} 
//		synCookies(mActivity, url);
		webView.loadUrl(url);
	}
//	/**
//	 * 同步一下cookie
//	 */
//	public void synCookies(Context activity, String url) {
//		if(TextUtils.isEmpty(url)) return;
//		PersistentCookieStore pcs = new PersistentCookieStore(activity);
//		List<Cookie> cookies2 = pcs.getCookies();
//		if(cookies2!=null){
//			String cookies = cookies2.toString();
//			CookieSyncManager.createInstance(activity);
//			CookieManager cookieManager = CookieManager.getInstance();
//			cookieManager.setAcceptCookie(true);
//			cookieManager.removeSessionCookie();//移除
//			cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie
//			CookieSyncManager.getInstance().sync();
//		}
//	}

	class webViewClient extends WebViewClient{
		//重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。 
		@Override 
		public boolean shouldOverrideUrlLoading(WebView view, String url) { 
//			synCookies(mActivity, url);
			view.loadUrl(url); 
			//如果不需要其他对点击链接事件的处理返回true，否则返回false 
			return true; 
		}
		@Override
		public void onPageFinished(WebView view, String url) {
//            CookieManager cookieManager = CookieManager.getInstance();
//            String CookieStr = cookieManager.getCookie(url);
//            UIHelper.showLog("Cookies = " + CookieStr);
            super.onPageFinished(view, url);
        }
	} ;
	
	public void goBack() {
		webView.goBack();
	}
	
	public boolean canGoBack(){
		return webView.canGoBack();
	}
	
	@Override
	public int getLayoutId() {
		return R.layout.activity_web;
	}
	@Override
	public void initData() {
		
	}
}
