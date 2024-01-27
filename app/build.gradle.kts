plugins {
    id("com.android.application")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.example.blog"
    compileSdk = 34

    packagingOptions {
        exclude ("META-INF/AL2.0")
        exclude ("META-INF/LGPL2.1")
    }

    defaultConfig {
        applicationId = "com.example.blog"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        viewBinding=true
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
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Koral Library for GIF Splash screen
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")

    // Circular ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // Lottie Animated Icons
    implementation ("com.airbnb.android:lottie:6.2.0")


    implementation ("com.google.firebase:firebase-bom:32.7.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")

    // Firebase Database
    implementation("com.google.firebase:firebase-database:20.3.0")

    // Firebase Core (Remove this line or use the version from BOM)
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.gms:google-services:4.4.0")

    // Firebase Storage
    implementation("com.google.firebase:firebase-storage:20.3.0")

    // Picasso
    implementation ("com.squareup.picasso:picasso:2.8")

    // Shimmer effect
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("com.todkars:shimmer-recyclerview:0.4.1")
    //timeago
    implementation("com.github.marlonlom:timeago:4.0.3")
}
