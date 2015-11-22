## Dagger2-ButterKnife-Retrofit-OkHttp-Picasso-Gson_Annotations

## Introduction

Third party librabries make developers' life way easy. They can significantly reduce the amount of code in your project. You can build robust apps by using these libraries. In this tutorial I am going to introduce some very powerful libraries for android app development.  Lets do some reading practice first. 

**Dagger**
_"The best classes in any application are the ones that do stuff: the BarcodeDecoder, the KoopaPhysicsEngine, and the AudioStreamer. These classes have dependencies; perhaps a BarcodeCameraFinder, DefaultPhysicsEngine, and an HttpStreamer._

_To contrast, the worst classes in any application are the ones that take up space without doing much at all: the BarcodeDecoderFactory, the CameraServiceLoader, and the MutableContextWrapper. These classes are the clumsy duct tape that wires the interesting stuff together._

_Dagger is a replacement for these FactoryFactory classes. It allows you to focus on the interesting classes. Declare dependencies, specify how to satisfy them, and ship your app"._
Official Site: http://square.github.io/dagger/

**ButterKnife**
_"Field and method binding for Android views which uses annotation processing to generate boilerplate code for you."_

Eliminate findViewById calls by using @Bind on fields.
Group multiple views in a list or array. Operate on all of them at once with actions, setters, or properties.
Eliminate anonymous inner-classes for listeners by annotating methods with @OnClick and others.
Eliminate resource lookups by using resource annotations on fields
Official Site: http://jakewharton.github.io/butterknife/

**Retrofit**
_"etrofit turns your HTTP API into a Java interface."_
Use annotations to describe the HTTP request:

    URL parameter replacement and query parameter support
    Object conversion to request body (e.g., JSON, protocol buffers)
    Multipart request body and file upload
Official Site: http://square.github.io/retrofit/

**OkHttp**
_"HTTP is the way modern applications network. It’s how we exchange data & media. Doing HTTP efficiently makes your stuff load faster and saves bandwidth."_ Official Site: http://square.github.io/okhttp/

**Picasso**
_"Images add much-needed context and visual flair to Android applications. Picasso allows for hassle-free image loading in your application—often in one line of code!"_
_"Many common pitfalls of image loading on Android are handled automatically by Picasso:"_

    Handling ImageView recycling and download cancelation in an adapter.
    Complex image transformations with minimal memory use.
    Automatic memory and disk caching.
Official Site : http://square.github.io/picasso/

**GSON Annotations: com.google.gson.annotations**
_"Gson provides a set of annotations to simplify the serialisation and deserialisation processes. In this article we will see how we can use these annotations and how these can simplify the use of Gson to convert between Java objects and JSON objects."_

OK thats enough theories ;) Lets getting our hand dirty...
## Project Description

**App's Gradle Setup**

Please add the following gradle setup in your project or simply clone my project. Note that these libraries are always keep updating. 

```Java
apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'

android {
    compileSdkVersion 23
    buildToolsVersion "23.0.1"
    useLibrary 'org.apache.http.legacy'

    defaultConfig {
        applicationId "com.shohrab_carmudi"
        minSdkVersion 16
        targetSdkVersion 23
        versionCode 1
        versionName "1.0"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    compile 'com.android.support:appcompat-v7:23.0.1'
    //Needed for Injecting Views
    compile 'com.jakewharton:butterknife:6.1.0'

    //Needed for managing image loading and for easy caching
    compile 'com.squareup.picasso:picasso:2.5.2'

    // Needed for managing DI
    compile 'com.google.dagger:dagger:2.0.1'
    apt "com.google.dagger:dagger-compiler:2.0.1"


    // Needed to annotated field variables of POJO/Model class for easy mapping with JSON fields
    provided 'javax.annotation:jsr250-api:1.0'
    compile 'javax.annotation:jsr250-api:1.0'

    // Needed to make network based API call
    compile 'com.squareup.retrofit:retrofit:2.0.0-beta2'
    compile 'com.squareup.retrofit:converter-gson:2.0.0-beta2'

    //"Response caching avoids the network completely for repeat requests" - http://square.github.io/okhttp/
    compile 'com.squareup.okhttp:okhttp:2.5.0'
}

```

**Project's Gradle Setup**

```Java
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.3.0'
        // Assists in working with annotation processors for Android Studio.
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

```
