# Changelog
All notable changes to this project will be documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
## [Unreleased]
### Added
- Initial implementation of `AnnotatedStringGenerator` to convert Markdown AST into Compose `AnnotatedString`.
- Support for bold, italic, headers, block quotes, ordered/unordered lists, links, code blocks, inline code, horizontal rules, and image placeholders.
- `CommonMarkFlavourDescriptor` with default styling providers.
- `MarkdownFlavourAnnotatedStringDescriptor` interface for custom flavour support.
- Paparazzi snapshot tests for visual regression testing.
- Unit tests for all supported Markdown elements.
- Maven Central publishing configuration via `com.vanniktech.maven.publish`.
