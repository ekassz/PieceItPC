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
    packaging {
        resources {
            excludes.add("META-INF/INDEX.LIST")
            excludes.add("META-INF/DEPENDENCIES")
        }
    }
}

configurations.all {
    resolutionStrategy {
        // Let Firebase BOM manage all versions
        eachDependency {
            if (requested.group == "io.grpc") {
                useVersion("1.57.2")
            }
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
<<<<<<< HEAD
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
<<<<<<< HEAD
    //added from lucy for firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
}
=======
}

>>>>>>> refs/remotes/origin/main
=======
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)

    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.camera.lifecycle)
    implementation("com.google.mlkit:image-labeling:17.0.7")
    //implementation(libs.firebase.ml.vision)
    implementation(libs.vision.common)
    implementation(libs.image.labeling.common)
    implementation(libs.image.labeling.default.common)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //added from lucy for firebase
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-database:21.0.0")
<<<<<<< HEAD
    implementation("com.google.firebase:firebase-auth:23.1.0")


    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // Declare the dependency for the Cloud Firestore library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-firestore-ktx:24.5.0")

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // Main coroutines library

    implementation("com.github.bumptech.glide:glide:4.15.1")

    //implementation("com.google.api-client:google-api-client-android:2.2.0")
    //implementation("com.google.apis:google-api-services-youtube:v3-rev222-1.25.0")
    implementation("com.google.http-client:google-http-client-gson:1.45.0")
    //implementation("com.google.android.youtube:youtube-android-player-api:1.2.2")
    implementation("com.google.apis:google-api-services-youtube:v3-rev20241117-2.0.0")
}
=======
}
>>>>>>> lucyUpdates
>>>>>>> main
