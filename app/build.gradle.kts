plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //added from lucy for firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.cs407.pieceitpc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cs407.pieceitpc"
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
    // Add this block to handle conflicting META-INF files
    packaging {
        resources {
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)

    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.camera.lifecycle)
    implementation("com.google.mlkit:image-labeling:17.0.7")
    //implementation(libs.firebase.ml.vision)
    implementation(libs.vision.common)
    implementation(libs.image.labeling.common)
    implementation(libs.image.labeling.default.common)
    implementation(libs.identity.jvm)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //added from lucy for firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.1")




    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    // Declare the dependency for the Cloud Firestore library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-firestore-ktx:24.5.0")

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // Main coroutines library

    implementation("com.github.bumptech.glide:glide:4.15.1")




}
