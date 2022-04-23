plugins {
    id(Config.Plugins.androidApplication)
    id(Config.Plugins.kotlinAndroid)
    id(Config.Plugins.kotlinKapt)
    id(Config.Plugins.kotlinParcelize)
    id(Config.Plugins.navigationSafeArgs)
    id(Config.Plugins.daggerHilt)
}

android {
    compileSdk = Config.Android.androidCompileSdkVersion

    defaultConfig {
        applicationId = Environment.Release.appId
        minSdk = Config.Android.androidMinSdkVersion
        targetSdk = Config.Android.androidTargetSdkVersion
        versionCode = Environment.Release.appVersionCode
        versionName = Environment.Release.appVersionName

        testInstrumentationRunner = Config.testRunner
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = Config.Kotlin.jvmTargetVersion
    }
    buildFeatures {
        viewBinding = true
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(Modules.resources))
    implementation(project(Modules.common))

    implementation(Dependencies.AndroidXDependencies.coreKtx)
    implementation(Dependencies.AndroidXDependencies.appCompat)
    implementation(Dependencies.AndroidXDependencies.navigationFragment)
    implementation(Dependencies.AndroidXDependencies.navigationUi)
    implementation(Dependencies.AndroidXDependencies.pagingRuntime)

    implementation(Dependencies.GoogleDependencies.materialDesign)
    implementation(Dependencies.GoogleDependencies.gson)

    implementation(Dependencies.DaggerHiltDependencies.hiltCore)
    kapt(Dependencies.DaggerHiltDependencies.hiltCompiler)

    implementation(Dependencies.CoroutinesDependencies.coroutines)
    implementation(Dependencies.CoroutinesDependencies.coroutinesAndroid)

    implementation(Dependencies.RetrofitDependencies.core)
    implementation(Dependencies.RetrofitDependencies.gson)
    implementation(Dependencies.OkHttpDependencies.core)
    implementation(Dependencies.OkHttpDependencies.loggingInterceptor)

    releaseImplementation(Dependencies.ChuckerDependencies.chuckerRelease)
    debugImplementation(Dependencies.ChuckerDependencies.chuckerDebug)

    implementation(Dependencies.OtherDependencies.timber)
}