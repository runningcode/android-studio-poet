package com.google.androidstudiopoet.generators.project

import com.google.androidstudiopoet.models.ConfigPOJO
import com.google.androidstudiopoet.utils.joinPath
import com.google.androidstudiopoet.writers.FileWriter

internal const val ASSETS_PATH = "src/main/assets"

class ProjectBuildGradleGenerator(val fileWriter: FileWriter) {

    fun generate(root: String, config: ConfigPOJO) {

        val (kotlinBuildScript, kotlinClasspath) = if (config.useKotlin) {
            "ext.kotlin_version = '${config.kotlinVersion}'" to """classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:${'$'}kotlin_version""""
        } else {
            Pair("", "")
        }

        val gradleText = """
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    $kotlinBuildScript
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:${config.androidGradlePluginVersion}'
        $kotlinClasspath

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
plugins {
    id 'com.gradle.build-scan' version '1.8'

}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

buildScan {
 licenseAgreementUrl = 'https://gradle.com/terms-of-service'
 licenseAgree = 'yes'
 tag 'SAMPLE'
 link 'GitHub', 'https://github.com/gradle/gradle-build-scan-quickstart'
}

""".trim()

        fileWriter.writeToFile(gradleText, root.joinPath("build.gradle"))
    }
}