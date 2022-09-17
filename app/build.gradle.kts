plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

ext {
    set("NEW_VERSION_CODE", 1234)
}

androidComponents {
    val isServer = project.ext.properties["IS_GRADLE_PROPERTIES"].toString().toBoolean()

    finalizeDsl {
        println("===Called finalizeDsl===")
        // Apply, new version code
        println("[Old] version code = ${it.defaultConfig.versionCode}")
        it.defaultConfig.versionCode = 3
        println("[New] version code = ${it.defaultConfig.versionCode}")
    }

    onVariants { variant ->
        println("===Called onVariants [${variant.name}]===")
        // Check properties
        println("[Check] contains flag = ${project.ext.properties.contains("IS_GRADLE_PROPERTIES")}")
        println("[Apply] isServer = $isServer")

        val applyVersionCode = if (isServer) {
            project.ext.properties["NEW_VERSION_CODE"] as Int
        } else {
            2_000
        }
        variant.outputs.forEach { output ->
            println("[Old] version code : " + output.versionCode.get())
            output.versionCode.set(applyVersionCode)
            println("[New] version code : " + output.versionCode.get())
        }
    }
}

android {
    namespace = "com.pluu.sample.versioncodeupdater"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.pluu.sample.versioncodeupdater"
        minSdk = 21
        targetSdk = 33
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

    flavorDimensions += "version"
    productFlavors {
        create("demo") {
            dimension = "version"
            versionCode = 2
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.6.1")
}