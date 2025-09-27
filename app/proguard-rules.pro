# ProGuard rules for AAVV Isla Plana Android application

# Keep the main activity
-keep public class com.aavv.islaplana.MainActivity {
    public <init>();
}

# Keep SyncManager and SyncService classes
-keep public class com.aavv.islaplana.sync.SyncManager {
    public <init>();
}

-keep public class com.aavv.islaplana.sync.SyncService {
    public <init>();
}

# Keep ApiClient and ApiService classes
-keep public class com.aavv.islaplana.api.ApiClient {
    public <init>();
}

-keep interface com.aavv.islaplana.api.ApiService {
    public <methods>;
}

# Keep DatabaseHelper class
-keep public class com.aavv.islaplana.data.local.DatabaseHelper {
    public <init>();
}

# Keep RemoteDataSource class
-keep public class com.aavv.islaplana.data.remote.RemoteDataSource {
    public <init>();
}

# Keep NetworkUtils class
-keep public class com.aavv.islaplana.utils.NetworkUtils {
    public <init>();
}

# Keep all model classes (if any)
-keep class com.aavv.islaplana.model.** { *; }

# Keep all annotations
-keepattributes *Annotation*

# Add any additional rules as needed for libraries or specific classes