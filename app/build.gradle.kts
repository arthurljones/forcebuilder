plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.compose.compiler)
}

android {
  namespace = "tech.ajones.forcebuilder"
  compileSdk = 35

  defaultConfig {
    applicationId = "tech.ajones.forcebuilder"
    minSdk = 28
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildFeatures {
    compose = true
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

  val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
  implementation(composeBom)
  androidTestImplementation(composeBom)

  // Choose one of the following:
  // Material Design 3
  implementation(libs.androidx.material3)

  // Android Studio Preview support
  implementation(libs.androidx.ui.tooling.preview)
  debugImplementation(libs.androidx.ui.tooling)

  // Optional - Included automatically by material, only add when you need
  // the icons but not the material library (e.g. when using Material3 or a
  // custom design system based on Foundation)
  implementation(libs.androidx.material.icons.core)
  // Optional - Add full set of material icons
  implementation(libs.androidx.material.icons.extended)
  // Optional - Add window size utils
  implementation(libs.androidx.adaptive)

  // Optional - Integration with activities
  implementation(libs.androidx.activity.compose)
  // Optional - Integration with ViewModels
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  // Optional - Integration with LiveData
  implementation(libs.androidx.runtime.livedata)

  implementation(libs.kotlin.csv.jvm)

}