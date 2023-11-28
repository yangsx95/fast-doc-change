plugins {
    id("java")
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.28")
    implementation("com.github.oxo42:stateless4j:2.5.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
