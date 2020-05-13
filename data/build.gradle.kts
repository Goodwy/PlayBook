import deps.Deps
import deps.Versions

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlinx-serialization")
  id("kotlin-kapt")
}

android {

  compileSdkVersion(Versions.compileSdk)

  defaultConfig {
    minSdkVersion(Versions.minSdk)
    targetSdkVersion(Versions.targetSdk)

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    javaCompileOptions {
      annotationProcessorOptions {
        arguments = mapOf("room.schemaLocation" to "$projectDir/schemas")
      }
    }
  }

  flavorDimensions("free")
  productFlavors {
    create("opensource") {
      setDimension("free")
    }
    create("proprietary") {
      setDimension("free")
    }
  }

  sourceSets {
    named("test") {
      assets.srcDir(project.file("schemas"))
    }
  }

  testOptions {
    unitTests.isReturnDefaultValues = true
    animationsDisabled = true
    unitTests.isIncludeAndroidResources = true
  }

  compileOptions {
    sourceCompatibility = Versions.sourceCompatibility
    targetCompatibility = Versions.targetCompatibility
  }
}

dependencies {
  implementation(project(":common"))
  implementation(project(":crashreporting"))
  implementation(Deps.AndroidX.appCompat)
  implementation(Deps.timber)
  implementation(Deps.Kotlin.coroutines)
  implementation(Deps.Kotlin.coroutinesAndroid)
  implementation(Deps.AndroidX.ktx)
  implementation(Deps.Kotlin.Serialization.runtime)

  api(Deps.AndroidX.Room.runtime)
  kapt(Deps.AndroidX.Room.compiler)

  implementation(Deps.Dagger.core)
  kapt(Deps.Dagger.compiler)

  testImplementation(Deps.AndroidX.Room.testing)
  testImplementation(Deps.AndroidX.Test.core)
  testImplementation(Deps.AndroidX.Test.junit)
  testImplementation(Deps.AndroidX.Test.runner)
  testImplementation(Deps.junit)
  testImplementation(Deps.robolectric)
  testImplementation(Deps.truth)

  api(Deps.ThreeTen.android)
  testImplementation(Deps.ThreeTen.jvm)
}
