plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp) // Добавлено KSP
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.cccc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cccc"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.databinding.adapters)
    ksp(libs.androidx.room.compiler) // Используйте ksp вместо kapt для Room
    //ksp("androidx.room:room-compiler:2.5.0") // Используйте ksp вместо kapt для Room

    // Room KTX для работы с корутинами
    implementation(libs.androidx.room.ktx) // Добавьте эту строку

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1") // Убедитесь, что эта строка добавлена

    // Koin core
    // implementation(libs.koin.android)

    // Koin для ViewModel (если используете ViewModel с Koin)
    //implementation(libs.koin.androidx.viewmodel)

    // Dagger 2
    implementation("com.google.dagger:dagger:2.52")
    ksp("com.google.dagger:dagger-compiler:2.52")
    implementation("javax.inject:javax.inject:1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(kotlin("script-runtime"))
}