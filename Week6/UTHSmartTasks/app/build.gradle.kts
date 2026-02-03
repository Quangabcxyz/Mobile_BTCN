plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.uthsmarttasks"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.uthsmarttasks"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // bộ khung firebase
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    // thư viện xác thực (đăng nhập/đăng ký)
    implementation("com.google.firebase:firebase-auth")
    // thư viện đăng nhập bằng google (quan trọng cho bài này)
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    // Thư viện để chuyển màn hình (Navigation)
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // Thư viện để load ảnh avatar từ URL (Coil)
    implementation("io.coil-kt:coil-compose:2.6.0")
    // Thư viện Firestore
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-messaging")

    // Thư viện gọi API (Retrofit)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Thư viện dịch JSON sang Class (Gson)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Thư viện load ảnh (Coil) - cái này ông có rồi thì thôi, chưa có thì thêm
    implementation("io.coil-kt:coil-compose:2.6.0")
    
    // Room - Local Database (MVVM)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    
    // ViewModel & Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
}
