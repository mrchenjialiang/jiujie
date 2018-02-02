package com.jiujie.base.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.jiujie.base.activity.ChoosePhotoActivity;
import com.jiujie.base.activity.cropimage.JJCropImageActivity;
import com.jiujie.base.jk.ICallbackSimple;
import com.jiujie.base.jk.OnListener;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 *
 * @author QQ576507648 从手机相册中或者拍照来获取图片
 */
public class GetPictureUtil {


	private static File mCameraFile;
	private static final int REQUEST_CAMERA = 1012;
	private static boolean isShouldCrop;
	private static OnListener<List<String>> onGetPhotoResultListener;
	private static float cropScaleHeight;
	private static int cropFileMaxLength;

	private static void requestPermissions(Context context, final OnListener<Boolean> onListener){
		RxPermissions.getInstance(context).requestEach(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.subscribe(new Action1<Permission>() {
					boolean isCallback;
					@Override
					public void call(Permission permission) {
						if(isCallback){
							return;
						}
						if (permission.granted) {
							onListener.onListen(true);
						}else {
							onListener.onListen(false);
						}
						isCallback = true;
					}
				});
	}

	private static Uri getCameraOutPutUri(Context context,Intent intent,String dir,String name){
		File file = new File(dir,name);
		if(file.exists()){
			file.delete();
		}
		if(!file.getParentFile().exists()){
			boolean mkdirs = file.getParentFile().mkdirs();
			if(!mkdirs){
				UIHelper.showLog("拍照文件创建失败");
				return null;
			}
		}
		mCameraFile = file;
		return UriUtil.getUri(context, intent, file, true);
	}

	private static void clear(){
		GetPictureUtil.isShouldCrop = false;
		GetPictureUtil.cropScaleHeight = 1;
		GetPictureUtil.cropFileMaxLength = 0;
		GetPictureUtil.onGetPhotoResultListener = null;
	}

	/**
	 * 记得onActivityResult
     */
	public static void getPhotoFromCamera(final Activity mActivity,
										  final String saveDir,
										  final String saveName,
										  final boolean isShouldCrop,
										  final float cropScaleHeight,
										  final int cropFileMaxLength,
										  final OnListener<List<String>> onGetPhotoResultListener) {
		requestPermissions(mActivity,new OnListener<Boolean>() {
			@Override
			public void onListen(Boolean isHasPermissions) {
				if(!isHasPermissions){
					UIHelper.showToastShort(mActivity,"无拍照权限");
					return;
				}
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
					Uri getCameraUri = getCameraOutPutUri(mActivity,intent,saveDir,saveName);
					if (getCameraUri!=null) {
						GetPictureUtil.isShouldCrop = isShouldCrop;
						GetPictureUtil.cropScaleHeight = cropScaleHeight;
						GetPictureUtil.cropFileMaxLength = cropFileMaxLength;
						GetPictureUtil.onGetPhotoResultListener = onGetPhotoResultListener;

						intent.putExtra(MediaStore.EXTRA_OUTPUT, getCameraUri);
						mActivity.startActivityForResult(intent, REQUEST_CAMERA);
					} else {
						mCameraFile = null;
						UIHelper.showToastShort(mActivity,"空间不足，无法拍照");
					}
				} else {
					mCameraFile = null;
					UIHelper.showToastShort(mActivity,"系统不支持拍照");
				}
			}
		});
	}

	public static void getPhotoFromAlbum(final Activity mActivity,
										  final int photoMaxSize,
										  final List<String> checkedPhotoList,
										  final boolean isShouldCrop,
										  final float cropScaleHeight,
										  final int cropFileMaxLength,
										  final OnListener<List<String>> onGetPhotoResultListener) {
		if(photoMaxSize<=0)return;
		requestPermissions(mActivity,new OnListener<Boolean>() {
			@Override
			public void onListen(Boolean isHasPermissions) {
				if(!isHasPermissions){
					UIHelper.showToastShort(mActivity,"无查看相册权限");
					return;
				}
				GetPictureUtil.isShouldCrop = isShouldCrop;
				GetPictureUtil.cropScaleHeight = cropScaleHeight;
				GetPictureUtil.cropFileMaxLength = cropFileMaxLength;
				GetPictureUtil.onGetPhotoResultListener = onGetPhotoResultListener;
				ChoosePhotoActivity.launch(mActivity, photoMaxSize,checkedPhotoList, new ICallbackSimple<List<String>>() {
					@Override
					public void onSucceed(List<String> result) {
						if(result!=null&&result.size()>0){
							if(isShouldCrop&&photoMaxSize==1)toCrop(mActivity,result.get(0),cropScaleHeight,cropFileMaxLength);
							else{
								if(onGetPhotoResultListener!=null){
									onGetPhotoResultListener.onListen(result);
								}
								clear();
							}
						}
					}
				});
			}
		});
	}

	private static void toCrop(final Activity mActivity,String filePath,float cropScaleHeight,int cropFileMaxLength){
//		String saveDir = mActivity.getCacheDir().getAbsolutePath();//这个目录下的，裁剪后上传会失败貌似
		String saveDir = ImageUtil.instance().getCacheSDDic(mActivity)+ "res/image/crop";
		String saveName = UIHelper.getTimeFileName(".jpg");
		JJCropImageActivity.launch(mActivity, filePath, saveDir, saveName, cropScaleHeight,cropFileMaxLength, new OnListener<String>() {
			@Override
			public void onListen(String s) {
				if(!TextUtils.isEmpty(s)){
					File cropFile = new File(s);
					if(cropFile.exists()&&cropFile.length()>0){
						//应用缓存内，不做刷新
						UIHelper.refreshSystemImage(mActivity,cropFile);
						List<String> list = new ArrayList<>();
						list.add(cropFile.getAbsolutePath());
						if(onGetPhotoResultListener!=null){
							onGetPhotoResultListener.onListen(list);
						}
						clear();
					}
				}
			}
		});
	}

	/**
	 * 拍照返回需调用
     */
	public static void onActivityResult(Activity mActivity,int requestCode, int resultCode, Intent data) {
		//拍照 成功时resultCode==RESULT_OK，data==null，因为设置了outPut，不返回
		if (requestCode == REQUEST_CAMERA){
			if(resultCode==Activity.RESULT_OK){
				if(mCameraFile==null||!mCameraFile.exists()||mCameraFile.length()==0){
					UIHelper.showLog("GetPhoto mCameraFile 有问题 ");
					return;
				}
				// notify system the image has change
				UIHelper.refreshSystemImage(mActivity,mCameraFile);
				if(isShouldCrop)toCrop(mActivity,mCameraFile.getAbsolutePath(),cropScaleHeight,cropFileMaxLength);
				else{
					List<String> list = new ArrayList<>();
					list.add(mCameraFile.getAbsolutePath());
					if(onGetPhotoResultListener!=null){
						onGetPhotoResultListener.onListen(list);
					}
					clear();
				}
			}
		}
	}

}
