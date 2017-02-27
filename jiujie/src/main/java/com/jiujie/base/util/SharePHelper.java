package com.jiujie.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@SuppressLint("SdCardPath")
public class SharePHelper {

	private final Context context;
	public SharedPreferences sp;
	String fileName;
	private static SharePHelper sharePHelper;

	protected SharePHelper(Context context){
		this(context, "jiujie_key");
	}

	protected SharePHelper(Context context,String fileName) {
		if(TextUtils.isEmpty(fileName))
			fileName = "jiujie_key";
		this.fileName = fileName;
		this.context = context;
		sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}

	public long getCacheSize(){
		return UIHelper.getFileSize(new File(getDataCachePath()));
	}

	public String getDataCachePath() {
//		context.getFilesDir().getPath()
//		/data/data/com.jiujie.cs/files
		return "/data/data/" + context.getPackageName() + "/shared_prefs/"+getFileName()+".xml";
	}

	public static SharePHelper instance(Context context){
		if(sharePHelper==null){
			sharePHelper = new SharePHelper(context);
		}
		return sharePHelper;
	}

	public String getFileName() {
		return fileName;
	}

	public static SharePHelper instance(Context context, String fileName){
		if(sharePHelper==null||!fileName.equals(sharePHelper.getFileName())){
			sharePHelper = new SharePHelper(context,fileName);
		}
		return sharePHelper;
	}
	
	public SharedPreferences getSp(){
		return sp;
	}
	
	public void saveObject(String key, Object object) {
		if(object==null) return;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(object);
			String oAuth_Base64 = new String(Base64.encode(os.toByteArray(),
					Base64.DEFAULT));
			Editor editor = sp.edit();
			editor.putString(key, oAuth_Base64);
			editor.apply();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T readObject(String key) {
		if(!sp.contains(key)){
			return null;
		}
		String string = sp.getString(key, "");
		if (TextUtils.isEmpty(string)) {
			return null;
		}
		Object object;
		byte[] base64 = Base64.decode(string.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream is = new ByteArrayInputStream(base64);
		T object1 = null;
		try {
			ObjectInputStream bis = new ObjectInputStream(is);
			object = bis.readObject();
			object1 = (T) object;
		} catch (Exception e) {
			UIHelper.showLog("Exception SharePHelper readObject:"+e.getMessage());
			e.printStackTrace();
		}
		return object1;
	}

	public void clear() {
		sp.edit().clear().apply();
	}

	public boolean isContainsKey(String key){
		return sp.contains(key);
	}

	public void remove(String key){
		sp.edit().remove(key).apply();
	}
}
