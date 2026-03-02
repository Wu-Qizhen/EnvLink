import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    // alias(libs.plugins.android.application)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "aethex.matrix"
    compileSdk = 36

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    /*kotlinOptions {
        jvmTarget = "11"
    }*/
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    androidTestImplementation(libs.androidx.espresso.core)

    // 使用 BOM 统一管理 Compose 版本
    implementation(platform(libs.androidx.compose.bom))

    // 基础 UI 工具包
    implementation(libs.ui)
    implementation(libs.ui.graphics) // 图形工具
    implementation(libs.ui.tooling.preview) // 预览支持

    // Material Design 组件
    implementation(libs.material3)

    // 基础布局和交互组件
    implementation(libs.androidx.foundation)

    // Activity 与 Compose 集成
    // implementation(libs.androidx.activity.compose.v172)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}