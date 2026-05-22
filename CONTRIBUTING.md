# Contributing

## Welcome!

We encourage everyone to use, contribute to, and improve this library. Whether you're fixing a bug, proposing a new feature, or improving the documentation, your contributions are welcome and appreciated!

## Code of Conduct (CoC)

We want to foster an inclusive and friendly community around our Open Source efforts. Like all SumUp Open Source projects, this project follows the Contributor Covenant Code of Conduct. Please, [read it and follow it](CODE_OF_CONDUCT.md).

If you feel another member of the community violated our CoC or you are experiencing problems participating in our community because of another individual's behavior, please get in touch with our maintainers. We will enforce the CoC.

## Submitting an Issue

In most cases, submitting an issue is the first step to contributing. Check the existing issues and verify that your issue is not already submitted. Then use one of the templates to [report a bug](https://github.com/sumup-oss/markdown-annotatedstring/issues/new?template=bug_report.md) or [request a feature](https://github.com/sumup-oss/markdown-annotatedstring/issues/new?template=feature_request.md).

## Submitting a Pull Request

We appreciate pull requests (PRs) for smaller changes and bug fixes. For larger changes, we encourage you to [submit an issue](https://github.com/sumup-oss/markdown-annotatedstring/issues/new) to collect feedback first. The normal workflow looks as follows:

1. Let others know that you're working on an issue by leaving a comment or assigning it to yourself.
2. Fork the repository and branch out from the latest `main` branch.
3. Code, add, commit, and push your changes in your feature branch.
4. Submit a pull request and make sure that the CI checks pass.
5. Collaborate with the codeowners/reviewers to merge your changes to `main`.

## Quick Start

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable version recommended)
- JDK 17 or higher
- Android SDK with API level 34 or higher

### Installation

Clone the repository:

```bash
git clone https://github.com/sumup-oss/markdown-annotatedstring.git
cd markdown-annotatedstring
```

### Building

Build the library:

```bash
./gradlew :annotatedstring-generator:assembleRelease
```

### Testing

Run unit tests:

```bash
./gradlew :annotatedstring-generator:testDebugUnitTest
```

Run Paparazzi snapshot tests:

```bash
./gradlew :annotatedstring-generator:verifyPaparazziDebug
```

To record new Paparazzi snapshots after making visual changes:

```bash
./gradlew :annotatedstring-generator:recordPaparazziDebug
```

### Running the Sample App

To try out the library in a sample app:

```bash
./gradlew :sample:installDebug
```

## Code Conventions

- Follow standard [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html).
- Write unit tests for new functionality.
- Update Paparazzi snapshots if your changes affect visual output.
- Keep pull requests focused — one feature or fix per PR.

## Troubleshooting

**Changes aren't being picked up after a rebuild**

Try cleaning the build first:

```bash
./gradlew clean
```

Then rebuild:

```bash
./gradlew :annotatedstring-generator:assembleRelease
```

If the issue persists, try invalidating caches in Android Studio via _File > Invalidate Caches / Restart_.

