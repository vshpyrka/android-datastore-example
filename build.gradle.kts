import com.google.protobuf.gradle.id

plugins {
    alias(pluginLibs.plugins.android.library)
    alias(pluginLibs.plugins.kotlin.android)

    // find latest version number here:
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-gradle-plugin
    alias(pluginLibs.plugins.protobuf)
    alias(pluginLibs.plugins.serialization)

    alias(pluginLibs.plugins.hilt)
    alias(pluginLibs.plugins.kotlin.kapt)
}

android {
    namespace = "com.example.datastore"
    compileSdk = sdk.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = sdk.versions.minSdk.get().toInt()
        targetSdk = sdk.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "com.example.datastore.runner.CustomTestRunner"
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
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true

            all { test ->
                with(test) {
                    testLogging {
                        events = setOf(
                            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR,
                        )
                    }
                }
            }
        }
    }
}

protobuf {
    // Configures the Protobuf compilation and the protoc executable
    protoc {
        // Downloads from the repositories
        // find latest version number here:
        // https://mvnrepository.com/artifact/com.google.protobuf/protoc
        artifact = libs.protobuf.compiler.get().toString()
    }
//    plugins {
//        // Optional: an artifact spec for a protoc plugin, with "grpc" as
//        // the identifier, which can be referred to in the "plugins"
//        // container of the "generateProtoTasks" closure.
//        id("grpc") {
//            artifact = "io.grpc:protoc-gen-grpc-java:1.15.1"
//        }
//    }
    // Generates the java Protobuf-lite code for the Protobufs in this project
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                // https://developers.googleblog.com/2021/11/announcing-kotlin-support-for-protocol.html
                id("kotlin")
                // Configures the task output type
                create("java") {
                    // Java Lite has smaller code size and is recommended for Android
                    option("lite")
                }
            }
            // Apply the "grpc" plugin whose spec is defined above, without
            // options. Note the braces cannot be omitted, otherwise the
            // plugin will not be added. This is because of the implicit way
            // NamedDomainObjectContainer binds the methods.
//            task.plugins {
//                create("grpc") {
//                    option("lite")
//                }
//            }
        }
    }
}

configurations.configureEach {
    resolutionStrategy.force("androidx.datastore:datastore-preferences:1.1.4")
}

dependencies {

    implementation(libs.hilt.android.core)
    kapt(libs.hilt.compiler)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)

    // Looks like this library is used also in tests, not sure which exact
    // library is missing that pulls that tracing lib
    implementation(libs.tracing)

    implementation(libs.datastore)
    // find latest version number here:
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-javalite
    implementation(libs.protobuf.lite)
    implementation(libs.protobuf.kotlin)

    implementation(libs.kotlin.serialization.json)

    androidTestImplementation(testLibs.hilt.android.testing)
    kaptAndroidTest(testLibs.hilt.compiler)

    androidTestImplementation(testLibs.core.ktx)
    androidTestImplementation(testLibs.espresso)
    androidTestImplementation(testLibs.espresso.device)
    androidTestImplementation(testLibs.android.junit.ktx)
    androidTestImplementation(testLibs.espresso.contrib) {
        exclude(group = "org.checkerframework", module = "checker")
        exclude(module = "protobuf-lite")
    }
    androidTestImplementation(testLibs.espresso.intents)
    androidTestImplementation(testLibs.test.runner)
    androidTestImplementation(testLibs.test.rules)
    androidTestImplementation(testLibs.uiautomator)
    // find latest version number here:
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-javalite
    androidTestImplementation(libs.protobuf.lite)
    androidTestImplementation(libs.protobuf.kotlin)

    implementation("com.google.guava:guava:32.0.1-jre")
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")
}
