# Consumer ProGuard rules for the annotatedstring-generator library.
# These rules are automatically included when another project depends on this library.

# Keep the public API of the library.
# This ensures that any public classes and methods are not removed or renamed,
# preserving the API for consumers of the library.
-keep public class com.sumup.markdown.annotatedstring.** { public *; }
-keep public interface com.sumup.markdown.annotatedstring.** { *; }

# Keep any classes that implement the library's interfaces, as these might be
# defined in the consuming app and instantiated dynamically.
-keep class * implements com.sumup.markdown.annotatedstring.AnnotatedStringGeneratingProvider
-keep class * implements com.sumup.markdown.annotatedstring.MarkdownFlavourAnnotatedStringDescriptor

# Keep rules for the 'org.jetbrains:markdown' dependency.
# This library is a transitive dependency, and its classes are part of this
# library's public API. These rules prevent them from being stripped by R8.
-keep class org.intellij.markdown.** { *; }
-keepclassmembers class org.intellij.markdown.** { *; }
-dontwarn org.intellij.markdown.**

# Keep Kotlin metadata to support reflection.
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
