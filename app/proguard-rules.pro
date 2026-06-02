-keep class com.neotestdev.fiauct.MainActivity { *; }

# Keep model classes in case Firestore mapping changes to toObject().
-keep class com.neotestdev.fiauct.data.model.** { *; }
-keepclassmembers class com.neotestdev.fiauct.data.model.** { *; }

# Firebase/Auth/Play Services safety rules.
-keep class com.google.firebase.firestore.** { *; }
-keep class com.google.firebase.auth.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
