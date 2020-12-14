# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\SDKForAndroidStudio/tools/proguard/proguard-android.txt
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

-keep class com.ultrafast.battery.charger.** {
 *;
}
-dontwarn com.squareup.okhttp.**
-dontwarn org.apache.commons.**
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**


-keepattributes EnclosingMethod

-dontnote com.google.android.gms.internal.zzry

-keep class android.support.** { *; }
-keep public class com.facebook.ads.** {
   public *;
}
