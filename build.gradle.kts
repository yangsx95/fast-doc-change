import org.apache.tools.ant.taskdefs.Java
import org.gradle.internal.SystemProperties

description = "fast-doc-change 快速进行文档变更"

subprojects {
    group = "github.yangsx95"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://repo.spring.io/milestone")
    }

    apply<JavaPlugin>()
    apply<MavenPublishPlugin>()

    dependencies {
        "testImplementation"(platform("org.junit:junit-bom:5.9.1"))
        "testImplementation"("org.junit.jupiter:junit-jupiter")
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                url = uri(property("maven.publish.release.url")?.toString() ?: "")
                credentials {
                    username = property("maven.publish.release.username")?.toString() ?: ""
                    password = property("maven.publish.release.password")?.toString() ?: ""
                }
            }
            maven {
                url = uri(property("maven.publish.snapshot.url")?.toString() ?: "")
                credentials {
                    username = property("maven.publish.snapshot.username")?.toString() ?: ""
                    password = property("maven.publish.snapshot.password")?.toString() ?: ""
                }
            }
        }
    }

    // 设置核心任务 test，使用 JUnit 5
    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType(JavaCompile::class) {
        options.encoding = "UTF-8"
        options.javaModuleVersion = JavaVersion.VERSION_17.toString()
    }

}
