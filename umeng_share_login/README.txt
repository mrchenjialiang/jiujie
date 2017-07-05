1,Application 须调用UMAPP.init(context,isDebug);
2,UMAPP中写明APPKEY 及 分享平台的key
3,各平台分享需求回调，如微信需包名.wxapi.WXEntryActivity及清单里注册之类的自行完善
4,,授权及登录、分享，部分需求在使用的Activity中重写，fragment中不行
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

5，WBShareActivity 调用新浪需在包名下配置