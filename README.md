# Markdown to AnnotatedString Generator

[![Build Status](https://github.com/sumup-oss/markdown-annotatedstring/actions/workflows/build-verification.yml/badge.svg?branch=main)](https://github.com/sumup-oss/markdown-annotatedstring/actions/workflows/build-verification.yml)
[![Release](https://img.shields.io/github/v/release/sumup-oss/markdown-annotatedstring)](https://github.com/sumup-oss/markdown-annotatedstring/releases)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](./LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.sumup.markdown/annotatedstring-generator)](https://central.sonatype.com/artifact/com.sumup.markdown/annotatedstring-generator)


A lightweight Android library to convert Markdown text into a Jetpack Compose `AnnotatedString`. Use `com.sumup.markdown:annotatedstring-generator` to produce one `AnnotatedString` for a Compose `Text` on Android (`minSdk` 24).

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

## Requirements

*   Android `minSdk` 24
*   Jetpack Compose. Compose Foundation is brought in by this library.

## When to use this

Use this library when Markdown should become one styled string inside a Compose `Text`. The parser is CommonMark through [JetBrains intellij-markdown](https://github.com/JetBrains/markdown). Links are a `"URL"` string annotation. Images are text placeholders. A `TextView`/`Spannable` library, or a renderer that emits a column of Compose nodes, fits a different layout.

## Installation

Add the dependency to your module's `build.gradle.kts`. The Maven Central badge above shows the latest published version. This snippet uses `0.1.1`.

```kotlin
dependencies {
    implementation("com.sumup.markdown:annotatedstring-generator:0.1.1")
}
```

<details>
<summary>Groovy (build.gradle)</summary>

```groovy
dependencies {
    implementation 'com.sumup.markdown:annotatedstring-generator:0.1.1'
}
```
</details>

<details>
<summary>Snapshot versions</summary>

Stable releases are published to Maven Central. To try an unpublished snapshot (for example `0.1.2-SNAPSHOT`), add the Maven Central snapshots repository in your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}
```

Then use the snapshot version in your dependency:

```kotlin
dependencies {
    implementation("com.sumup.markdown:annotatedstring-generator:0.1.2-SNAPSHOT")
}
```
</details>

## Usage

Imports:

```kotlin
import com.sumup.markdown.annotatedstring.AnnotatedStringGenerator
import com.sumup.markdown.annotatedstring.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
```

### Basic Usage

Parse your markdown and generate an `AnnotatedString`:

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

The [`sample`](sample) module is a small Compose playground for the same API.

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

## Project

*   [Changelog](CHANGELOG.md)
*   [Contributing](CONTRIBUTING.md)
*   [Security](SECURITY.md)

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
