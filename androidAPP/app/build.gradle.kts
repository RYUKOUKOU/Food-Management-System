plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.foodapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.foodapp"
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
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.google.code.gson:gson:2.8.9'")
    implementation ("io.socket:socket.io-client:2.0.1")
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("io.socket:socket.io-client:2.0.0")
    implementation ("org.json:json:20230227")
    //implementation("org.springframework.boot:spring-boot-starter-web")
}