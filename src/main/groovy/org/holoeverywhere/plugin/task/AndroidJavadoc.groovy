package org.holoeverywhere.plugin.task

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.sdklib.IAndroidTarget
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.javadoc.Javadoc

public class AndroidJavadoc extends Javadoc {
    AndroidJavadoc() {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
    }

    def Configuration configuration
    def String sourceSet

    public void setSourceSet(String sourceSet) {
        this.sourceSet = sourceSet
        if (sourceSet != null) {
            setSource(project.extensions.getByType(LibraryExtension).sourceSets.findByName(sourceSet)?.allJava)
        } else {
            setSource(null)
        }
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration
        if (configuration != null) {
            setClasspath(project.files(
                    // Compiled source code
                    "${project.buildDir}/classes/release",

                    // JAR dependencies
                    configuration,

                    // AAR dependencies
                    project.fileTree("${project.buildDir}/exploded-bundles") { include '*/classes.jar' },

                    // Android framework
                    project.plugins.getPlugin(LibraryPlugin).loadedSdkParser.target.getPath(IAndroidTarget.ANDROID_JAR)
            ))
        } else {
            setClasspath(null)
        }
    }
}
