package com.jiujie.base.util.photo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.jiujie.base.activity.ChoosePhotoActivity;
import com.jiujie.base.activity.cropimage.JJCropImageActivity;
import com.jiujie.base.jk.ICallbackSimple;
import com.jiujie.base.jk.OnListener;
import com.jiujie.base.util.FileUtil;
import com.jiujie.base.util.PermissionsManager;
import com.jiujie.base.util.UIHelper;
import com.jiujie.base.util.UriUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author QQ576507648 从手机相册中或者拍照来获取图片
 */
abstract class BaseGetPhotoUtil implements GetPhotoRequest{

	protected Activity mActivity;
	private File mCameraFile;
	private final int REQUEST_CAMERA = 1012;

	BaseGetPhotoUtil(Activity mActivity) {
		this.mActivity = mActivity;
	}

	private void requestPermissions(final OnListener<Boolean> onListener){
		PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
			@Override
			public void onListen(Boolean isHasPermission) {
				onListener.onListen(isHasPermission);
			}
		}, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA);
	}

	private Uri getCameraOutPutUri(Context context,Intent intent){
		File file = new File(getCameraOutPutDir(), getCameraOutPutFileName());
		if(file.exists()){
			file.delete();
		}
		File dirFile = file.getParentFile();
		if(!dirFile.exists()){
			boolean mkdirs = dirFile.mkdirs();
			if(!mkdirs){
				UIHelper.showLog("拍照文件创建失败");
				return null;
			}
		}else if(!dirFile.isDirectory()){
			boolean delete = dirFile.delete();
			if(delete){
				return getCameraOutPutUri(context, intent);
			}else{
				UIHelper.showLog("getCameraOutPutUri dirFile !isDirectory and delete fail");
				return null;
			}
		}
		mCameraFile = file;
		return UriUtil.getUri(context, intent, file, true);
	}

	public int getCameraMethodType(){
		return 1;
	}

	/**
	 * 记得onActivityResult
     */
	public void getPhotoFromCamera() {
		PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
			@Override
			public void onListen(Boolean isHasPermissions) {
				if(!isHasPermissions){
					UIHelper.showToastShort("权限不足，无法进行拍照");
					return;
				}

				if(getCameraMethodType()==1){
					doOpenCameraMethod1();
				}else{
					doOpenCameraMethod2();
				}
			}
		}, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA);
	}

	private void doOpenCameraMethod2(){
		mCameraFile = FileUtil.createFile(getCameraOutPutDir(), getCameraOutPutFileName());
		if(mCameraFile==null){
			UIHelper.showToastShort("创建文件失败");
			return;
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (mActivity.getApplicationInfo().targetSdkVersion <= 23 || Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile));
		}else {
			ContentValues contentValues = new ContentValues(1);
			contentValues.put(MediaStore.Images.Media.DATA, mCameraFile.getAbsolutePath());
			Uri uri = mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}
		mActivity.startActivityForResult(intent, REQUEST_CAMERA);
	}

	private void doOpenCameraMethod1() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            Uri getCameraUri = getCameraOutPutUri(mActivity,intent);
            if (getCameraUri!=null) {
                UIHelper.showLog("BaseGetPhotoUtil","getCameraUri:"+getCameraUri);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, getCameraUri);
                mActivity.startActivityForResult(intent, REQUEST_CAMERA);
            } else {
                mCameraFile = null;
                UIHelper.showToastShort("创建文件失败");
            }
        } else {
            mCameraFile = null;
            UIHelper.showToastShort("系统不支持拍照");
        }
	}

	public void getPhotoFromAlbum() {
		if(getPhotoMaxSize()<=0)return;
		PermissionsManager.getPermissionSimple(new OnListener<Boolean>() {
			@Override
			public void onListen(Boolean isHasPermissions) {
				if(!isHasPermissions){
					UIHelper.showToastShort("无查看相册权限");
					return;
				}
				ChoosePhotoActivity.launch(mActivity, getPhotoMaxSize(),getCheckedPhotoList(), new ICallbackSimple<List<String>>() {
					@Override
					public void onSucceed(List<String> result) {
						if(result!=null&&result.size()>0){
							if(isShouldCrop()&&getPhotoMaxSize()==1)toCrop(result.get(0),false);
							else{
								onGetPhotoEnd(false,result);
							}
						}
					}
				});
			}
		}, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
	}

	public void toCrop(String filePath, final boolean isFromCamera){
//		String saveDir = mActivity.getCacheDir().getAbsolutePath();//这个目录下的，裁剪后上传会失败貌似
		JJCropImageActivity.launch(mActivity, filePath, getCropSaveDir(), getCropSaveName(), getCropScaleHeight(),getCropFileMaxLength(), new OnListener<String>() {
			@Override
			public void onListen(String s) {
				if(!TextUtils.isEmpty(s)){
					File cropFile = new File(s);
					if(cropFile.exists()&&cropFile.length()>0){
						//应用缓存内，不做刷新
						UIHelper.refreshSystemImage(mActivity,cropFile);
						List<String> list = new ArrayList<>();
						list.add(cropFile.getAbsolutePath());
						onGetPhotoEnd(isFromCamera,list);
					}
				}
			}
		});
	}

	/**
	 * 拍照返回需调用
     */
	public void onActivityResult(Activity mActivity,int requestCode, int resultCode, Intent data) {
		UIHelper.showLog("BaseGetPhotoUtil","onActivityResult requestCode:"+requestCode);
		UIHelper.showLog("BaseGetPhotoUtil","onActivityResult resultCode:"+resultCode);
		UIHelper.showLog("BaseGetPhotoUtil","onActivityResult mCameraFile:"+(mCameraFile==null?"null":mCameraFile.length()));
		UIHelper.showLog("BaseGetPhotoUtil","onActivityResult data:"+data);
		//拍照 成功时resultCode==RESULT_OK，data==null，因为设置了outPut，不返回
		if (requestCode == REQUEST_CAMERA){
			if(resultCode==Activity.RESULT_OK){
				if(mCameraFile==null||!mCameraFile.exists()||mCameraFile.length()==0){
					UIHelper.showLog("GetPhoto mCameraFile 有问题 ");
					return;
				}
				// notify system the image has change
				UIHelper.refreshSystemImage(mActivity,mCameraFile);
				if(isShouldCrop())toCrop(mCameraFile.getAbsolutePath(),true);
				else{
					List<String> list = new ArrayList<>();
					list.add(mCameraFile.getAbsolutePath());
					onGetPhotoEnd(true,list);
				}
			}else{
				if(mCameraFile!=null&&mCameraFile.exists()){
					mCameraFile.delete();
				}
			}
		}
	}

}
