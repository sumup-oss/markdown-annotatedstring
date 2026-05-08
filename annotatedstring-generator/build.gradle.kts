import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.mavenPublish)
}

val publishedGroup = providers.gradleProperty("GROUP").orElse("com.sumup.markdown").get()
val publishedArtifact = providers.gradleProperty("POM_ARTIFACT_ID").orElse("annotatedstring-generator").get()
val publishedVersion = providers.gradleProperty("VERSION_NAME").orElse("0.1.0-SNAPSHOT").get()

android {
    namespace = "com.sumup.markdown.annotatedstring"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        aarMetadata {
            minCompileSdk = 24
        }
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
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        explicitApi()
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.intellij.markdown)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(publishedGroup, publishedArtifact, publishedVersion)

    pom {
        name.set(providers.gradleProperty("POM_NAME").orElse("Markdown to AnnotatedString Generator"))
        description.set(providers.gradleProperty("POM_DESCRIPTION").orElse("Android library to convert Markdown into Compose AnnotatedString"))
        inceptionYear.set("2026")
        url.set(providers.gradleProperty("POM_URL").orElse("https://github.com/sumup-oss/markdown-annotatedstring"))

        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("sumup")
                name.set("SumUp")
                url.set("https://github.com/sumup-oss")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/sumup-oss/markdown-annotatedstring.git")
            developerConnection.set("scm:git:ssh://github.com:sumup-oss/markdown-annotatedstring.git")
            url.set("https://github.com/sumup-oss/markdown-annotatedstring/")
        }
    }
}

