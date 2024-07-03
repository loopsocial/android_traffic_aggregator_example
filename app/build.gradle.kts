plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.firework.example.trafficaggregator"
    compileSdk = 31

    defaultConfig {
        applicationId = "com.firework.example.trafficaggregator"

        minSdk = 19
        targetSdk = 31

        versionCode = 1
        versionName = "1.0.0"

        buildConfigField("String", "FW_CLIENT_ID", "\"b9ca62b643050300d2990c57e8d76dbf65ddf3ed64b8f4c64c9faf21d5d37d4c\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Firework Android Traffic Aggregator SDK
    val fireworkSdkVersion = "1.0.0-beta.6"
    implementation("com.firework.embed:traffic-aggregator:$fireworkSdkVersion")
}
