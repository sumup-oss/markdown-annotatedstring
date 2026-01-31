# Markdown to AnnotatedString Generator

[![Build Status](https://img.shields.io/github/actions/workflow/status/sumup/markdown-annotatedstring/test.yml?branch=main)](https://github.com/sumup/markdown-annotatedstring/actions/workflows/test.yml)
[![Release](https://img.shields.io/github/v/release/sumup/markdown-annotatedstring)](https://github.com/sumup/markdown-annotatedstring/releases)
[![License](https://img.shields.io/github/license/sumup/markdown-annotatedstring)](LICENSE)

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

## Usage

Using the generator is straightforward. Parse the markdown using the IntelliJ Markdown parser and then generate the `AnnotatedString`:

```kotlin
val flavour = CommonMarkFlavourDescriptor()
val parser = MarkdownParser(flavour)
val root = parser.buildMarkdownTreeFromString(markdown)
val annotatedString = AnnotatedStringGenerator(markdown, root, flavour).generateAnnotatedString()
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
