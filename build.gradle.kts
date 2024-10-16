plugins {
    id("ru.squareland.lib") version "1.1.0"
}

group = "ru.squareland"
version = System.getenv("CI_COMMIT_TAG") ?: "1.0"