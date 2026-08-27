import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.XCFramework

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.multiplatform.library)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.devtools.ksp)
}

kotlin {
  android {
    namespace = "com.example.sharedlogic"
    compileSdk = 36
    minSdk = 24
    compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    androidResources { enable = true }
  }

  iosArm64()
  iosSimulatorArm64()

  // Configure XCFramework
  val xcframeworkName = "SharedLogic"
  val xcf = XCFramework(xcframeworkName)
  
  listOf(
    targets.getByName("iosArm64"),
    targets.getByName("iosSimulatorArm64")
  ).forEach { target ->
    target.binaries.framework {
      baseName = xcframeworkName
      xcf.add(this)
    }
  }

  sourceSets {
    commonMain.dependencies {
      implementation(libs.kotlinx.coroutines.core)
    }
    androidMain.dependencies {
      implementation(libs.androidx.room.runtime)
      implementation(libs.androidx.room.ktx)
      implementation(libs.kotlinx.coroutines.android)
      implementation(libs.okhttp)
      implementation(libs.moshi.kotlin)
      implementation(libs.retrofit)
    }
  }
}

dependencies {
  add("kspAndroid", libs.androidx.room.compiler)
}
