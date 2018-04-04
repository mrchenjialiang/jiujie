# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidStudio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 7
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontoptimize
-ignorewarnings
-verbose
-dontwarn
-dontskipnonpubliclibraryclassmembers
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

#-keep public class * extends android.app.Activity
#-keep public class * extends android.support.v4.app.Fragment
#-keep public class * extends android.support.v4.app.FragmentActivity
#-keep public class * extends android.support.v7.app.AppCompatActivity
#-keep public class * extends android.app.View
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep class android.support.v4.*.**{*;}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers class * {
    public void onEventMainThread(...);
}
-keepclassmembers class * {
    public void onGet(...);
}
-keepclassmembers class * {
    public void onEventAsync(...);
}
-keepclassmembers class * {
    public void onEventBackgroundThread(...);
}
-keepclassmembers class * {
    public void onEvent(...);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**

-dontwarn com.igexin.**
-keep class com.igexin.**{*;}

-dontwarn com.mob.tools.network.**
-keep class com.mob.**{*;}
-keep class com.mob.tools.network.**{*;}
-keep class com.mob.tools.network.HttpConnectionImpl

-dontwarn com.umeng.analytics.**
-dontwarn com.umeng.common.net.**
-dontwarn com.umeng.fb.**
-dontwarn com.umeng.fb.a.**
-dontwarn com.umeng.fb.util.**
-keep class com.umeng.**{*;}

-dontwarn okio.**
-keep class okio.**{*;}

-dontwarn retrofit2.**
-keep class retrofit2.**{*;}

-dontwarn rx.internal.util.unsafe.**
-keep class rx.internal.util.unsafe.**{*;}

-dontwarn org.apache.**
-keep class org.apache.*.**{*;}


-keep class com.edmodo.** {*; }
-keep class com.nostra13.** {*; }
-keep class opensource.** {*; }
-keep class com.nineoldandroids.** {*; }
-keep class uk.** {*; }
-keep class com.emilsjolander.** {*; }

-keep class com.turbomanage.** {*; }
-keep class org.apache.** {*; }
-keep class android.** { *; }
-keep class com.baidu.** { *; }
-keep class com.amap.** { *; }
-keep class com.autonavi.** { *; }
-keep class com.mapabc.** { *; }
-keep class com.google.** { *; }
-keep class com.j256.** { *; }
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class m.framework.**{*;}
-keep class com.android.pc.ioc.**{*;}

-keep class **.R$* {*;}

-keep public class com.jiujie.base.R$*{
    public static final int *;
}
-keep class com.jiujie.base.*.** {*; }
-keep class com.jiujie.base.APP {*;}
-keep class com.jiujie.base.util.glide.GlideModuleMine {*;}
-keep class com.jiujie.base.Title {*;}
-keep class com.jiujie.base.util.MyHandler {*;}

#-keep class com.jiujie.base.activity.BaseActivity {*; }
#-keep class com.jiujie.base.activity.BaseListActivity {*; }
#-keep class com.jiujie.base.activity.BaseListSimpleActivity {*; }
#-keep class com.jiujie.base.activity.ChoosePhotoActivity {*; }
#-keep class com.jiujie.base.activity.ImageViewPagerActivity {*; }
#-keep class com.jiujie.base.adapter.BaseAdvertAdapter {*; }
#-keep class com.jiujie.base.adapter.BaseExpandAdapter {*; }
#-keep class com.jiujie.base.adapter.BaseRecyclerViewAdapter {*; }
#-keep class com.jiujie.base.adapter.BaseRecyclerViewHAdapter {*; }
#-keep class com.jiujie.base.adapter.BaseRecyclerViewSimpleAdapter {*; }
#-keep class com.jiujie.base.adapter.ViewPagerFragmentTabAdapter {*; }
#-keep class com.jiujie.base.adapter.ViewPagerViewTabAdapter {*; }


-keep  class * implements com.badlogic.gdx.utils.Json*
-keep  class com.umeng.*.** {*; }
-keep  class cn.sharesdk.system.text.*
-keep  class com.commonsware.cwac.merge.*
-keep  class com.devsmart.android.*
-keep  class org.apache.*.** {*; }
-keep  class okhttp3.** {*; }
-keep  class okhttp3.Request$Builder



-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable {*;}

-assumenosideeffects class android.util.Log{
 public static *** v(...);
 public static *** e(...);
 public static *** d(...);
 public static *** i(...);
 public static *** w(...);
 }

 -keep public class * extends android.view.View {
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
     public void set*(...);
 }

##Butterknife混淆配置 start
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$Finder { *; }
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
##Butterknife混淆配置 end


#Glide  start
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
#Glide  end
