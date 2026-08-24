import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.multiplatform.library)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  android {
    namespace = "com.example"
    compileSdk = 36
    minSdk = 24
    compilerOptions { jvmTarget = JvmTarget.JVM_11 }
    androidResources { enable = true }
  }

  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    androidMain.dependencies {
      implementation(project(":sharedLogic"))
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.compose.bom)
      implementation(libs.androidx.compose.material3)
      implementation(libs.androidx.compose.material.icons.core)
      implementation(libs.androidx.compose.material.icons.extended)
      implementation(libs.androidx.compose.ui)
      implementation(libs.androidx.compose.ui.graphics)
      implementation(libs.androidx.compose.ui.tooling.preview)
      implementation(libs.androidx.lifecycle.runtime.compose)
      implementation(libs.androidx.lifecycle.runtime.ktx)
      implementation(libs.androidx.lifecycle.viewmodel.compose)
      implementation(libs.androidx.navigation.compose)
      implementation(libs.coil.compose)
      implementation(libs.kotlinx.coroutines.android)
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.androidx.core.ktx)
      implementation(libs.retrofit)
    }
    commonTest.dependencies { implementation(libs.kotlin.test) }
  }
}

dependencies {
  androidRuntimeClasspath(libs.androidx.compose.ui.tooling)
}