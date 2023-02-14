import org.gradle.api.Plugin
import org.gradle.api.Project

// declared in the build.gradle.file
@Suppress("unused")
class LibraryPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.pluginManager.run {
      apply("com.android.library")
      apply("kotlin-android")
      withPlugin("com.android.library") {
        target.baseSetup()
      }
    }
  }
}
