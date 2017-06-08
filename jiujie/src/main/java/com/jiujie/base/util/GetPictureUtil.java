package com.jiujie.base.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.widget.Toast;

import com.jiujie.base.activity.cropimage.CropImageActivity;
import com.jiujie.base.jk.ICallBackNoParam;

import java.io.File;

/**
 * 
 * @author QQ576507648 从手机相册中或者拍照来获取图片
 */
public class GetPictureUtil {
	public static int GetPictureFromAlbum = 1234;
	public static int GetPictureFromCamera = 1235;
	public static int GetPictureToCrop = 1236;

	/**
	 * 拍照获取清晰图片,用此方法,返回的intent data 是空的!
	 * @param saveDirectory 存放目录
	 * @param saveName  存储文件名
	 * @param CAMERA 请求码
	 */
	public static String getPicFromCamera(Activity activity,String saveDirectory,String saveName,int CAMERA){
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(activity, "请检查您的SD卡", Toast.LENGTH_LONG).show();
			return null;
		}
		File fileDic = new File(saveDirectory);
		if(!fileDic.exists()){
			fileDic.mkdirs();
		}
		File file = new File(fileDic, saveName);
		Intent intent = new Intent();
		Uri uri = Uri.fromFile(file);
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		activity.startActivityForResult(intent, CAMERA);
		return file.getAbsolutePath();
	}

	/**
	 * 从相册中选取图片
	 * OnActivityResult方法，调用getAlbumPicFromResult
	 * @param ALBUM 请求码
	 */
	public static void getPicFromAlbum(final Activity activity, final int ALBUM){
		PermissionsManager.doReadSdCard(activity, null, "读取选取图片，特申请读取SD卡权限", new ICallBackNoParam() {
			@Override
			public void onSucceed() {
				getPicFromAlbumReal(activity, ALBUM);
			}

			@Override
			public void onFail() {
				com.jiujie.base.util.UIHelper.showToastShort(activity, "用户已拒绝读取图片");
			}
		});

	}

	private static  void getPicFromAlbumReal(Activity activity,int ALBUM){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		activity.startActivityForResult(intent, ALBUM);
	}
	/**
	 * 从相册中选择图片   返回图片路径
	 * @param data 结果中返回的intent
	 * @return 图片路径
	 */
	public static String getAlbumPicFromResult(Intent data,Activity activity){
		Uri uri = data.getData();
		if (!TextUtils.isEmpty(uri.getAuthority())) {
			Cursor cursor =activity.getContentResolver().query(uri,
					new String[] { MediaColumns.DATA },
					null, null, null);
			if (null == cursor) {
				Toast.makeText(activity, "图片没找到", Toast.LENGTH_SHORT).show();
				return null;
			}
			cursor.moveToFirst();
			String path = cursor.getString(cursor.getColumnIndex(MediaColumns.DATA));
			cursor.close();
			return path;
		} else {
			return uri.getPath();
		}
	}
	/**
	 * 截取图片,截取后的图片大小被我默认为150*150
	 * @param CROPIMAGE  请求码
	 * @param savePath  存储截图目录
	 * @param saveName  存储截图文件名,不带格式
	 */
	public static void cropImage(Activity activity,String filePath,int CROPIMAGE,int width,int height,String savePath,String saveName){
		Intent intent = new Intent(activity, CropImageActivity.class);
		intent.putExtra("path", filePath);
		intent.putExtra("savePath", savePath);
		intent.putExtra("saveName", saveName);
		intent.putExtra("width", width);
		intent.putExtra("height", height);
		activity.startActivityForResult(intent, CROPIMAGE);
	}

}
