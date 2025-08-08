# ===== 기본 유지 설정 =====
# 애플리케이션의 모든 클래스 및 메서드 이름은 난독화되더라도 앱의 진입점은 유지해야 함
-keep class com.bottari.** { *; }

# ===== Android 필수 =====
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# ViewBinding 유지 (예: XxxBinding)
-keep class **_ViewBinding { *; }
-keep class **Binding { *; }

# ===== Retrofit / OkHttp =====
# Retrofit 인터페이스와 모델 클래스 보존
-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keep class kotlinx.serialization.** { *; }

# ===== Firebase Crashlytics =====
# stack trace 정확히 보고하기 위한 유지 설정
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# ===== Kotlin =====
-keepclassmembers class kotlin.Metadata { *; }
-dontwarn kotlin.**
-dontnote kotlin.**

# ===== 기타 =====
-dontwarn org.jetbrains.annotations.**
