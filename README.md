# Markdown to AnnotatedString Generator

[![Build Status](https://img.shields.io/github/actions/workflow/status/sumup-oss/markdown-annotatedstring/test.yml?branch=main)](https://github.com/sumup-oss/markdown-annotatedstring/actions/workflows/test.yml)
[![Release](https://img.shields.io/github/v/release/sumup-oss/markdown-annotatedstring)](https://github.com/sumup-oss/markdown-annotatedstring/releases)
[![License](https://img.shields.io/github/license/sumup-oss/markdown-annotatedstring)](LICENSE)

A lightweight Android library to convert Markdown text into a Jetpack Compose `AnnotatedString`. This allows you to easily render formatted text from Markdown in your Compose UI.

This library is built on top of [JetBrains intellij-markdown](https://github.com/JetBrains/markdown), a multiplatform Markdown processor written in Kotlin.

This library supports common Markdown syntax, including:
*   Bold and Italic text
*   Headers
*   Block quotes
*   Ordered and unordered lists
*   Links
*   Code blocks and inline code
*   Horizontal rules
*   Images (rendered as text placeholder)

## Installation

Add the dependency to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.sumup.markdown:annotatedstring-generator:0.1.0-SNAPSHOT")
}
```

<details>
<summary>Groovy (build.gradle)</summary>

```groovy
dependencies {
    implementation 'com.sumup.markdown:annotatedstring-generator:0.1.0-SNAPSHOT'
}
```
</details>

For **snapshot** versions, make sure Maven Central snapshots repository is added in your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/") // for SNAPSHOT versions
    }
}
```

> **Note:** Once a stable release is published, you won't need the snapshot repository — just `mavenCentral()`.

## Usage

### Basic Usage

Parse your markdown and generate an `AnnotatedString` in three lines:

```kotlin
val markdown = "Hello **world**! This is *italic* and `code`."
val flavour = CommonMarkFlavourDescriptor()
val parser = MarkdownParser(flavour)
val root = parser.buildMarkdownTreeFromString(markdown)
val annotatedString = AnnotatedStringGenerator(markdown, root, flavour).generateAnnotatedString()
```

### In Jetpack Compose

Use the generated `AnnotatedString` directly in a Compose `Text` or `ClickableText`:

```kotlin
@Composable
fun MarkdownText(markdown: String, modifier: Modifier = Modifier) {
    val flavour = remember { CommonMarkFlavourDescriptor() }
    val annotatedString = remember(markdown) {
        val parser = MarkdownParser(flavour)
        val root = parser.buildMarkdownTreeFromString(markdown)
        AnnotatedStringGenerator(markdown, root, flavour).generateAnnotatedString()
    }

    Text(
        text = annotatedString,
        modifier = modifier,
    )
}
```

### Handling Link Clicks

Links are annotated with a `"URL"` string annotation. Use `ClickableText` to handle taps:

```kotlin
@Composable
fun ClickableMarkdownText(markdown: String, modifier: Modifier = Modifier) {
    val flavour = remember { CommonMarkFlavourDescriptor() }
    val annotatedString = remember(markdown) {
        val parser = MarkdownParser(flavour)
        val root = parser.buildMarkdownTreeFromString(markdown)
        AnnotatedStringGenerator(markdown, root, flavour).generateAnnotatedString()
    }

    val uriHandler = LocalUriHandler.current
    ClickableText(
        text = annotatedString,
        modifier = modifier,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        }
    )
}
```

## Building from Source

To build the library from source, clone the repository and run the following command:

```bash
./gradlew :annotatedstring-generator:assembleRelease
```

## Testing

This project uses JUnit and Paparazzi for testing.

To run the unit tests:
```bash
./gradlew :annotatedstring-generator:testDebugUnitTest
```

To run the Paparazzi snapshot tests:
```bash
./gradlew :annotatedstring-generator:verifyPaparazziDebug
```

## License

```
Copyright 2026 SumUp

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
