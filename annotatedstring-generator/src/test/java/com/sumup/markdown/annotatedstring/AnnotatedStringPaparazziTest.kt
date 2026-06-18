package com.sumup.markdown.annotatedstring

import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

/**
 * Paparazzi snapshot tests for AnnotatedString rendering.
 * These tests capture visual snapshots of markdown rendered as AnnotatedString.
 *
 * Run `./gradlew :annotatedstring-generator:recordPaparazziDebug` to record new snapshots.
 * Run `./gradlew :annotatedstring-generator:verifyPaparazziDebug` to verify against existing snapshots.
 */
class AnnotatedStringPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi()

    // ===================
    // Basic Formatting Tests
    // ===================

    @Test
    fun plainText() {
        paparazzi.snapshot {
            MarkdownPreview("Hello, World!")
        }
    }

    @Test
    fun boldText() {
        paparazzi.snapshot {
            MarkdownPreview("This is **bold** text")
        }
    }

    @Test
    fun italicText() {
        paparazzi.snapshot {
            MarkdownPreview("This is *italic* text")
        }
    }

    @Test
    fun boldAndItalic() {
        paparazzi.snapshot {
            MarkdownPreview("This is ***bold and italic*** text")
        }
    }

    @Test
    fun inlineCode() {
        paparazzi.snapshot {
            MarkdownPreview("This is `inline code` text")
        }
    }

    @Test
    fun link() {
        paparazzi.snapshot {
            MarkdownPreview("Click [here](https://example.com) to visit")
        }
    }

    // ===================
    // Headers Tests
    // ===================

    @Test
    fun header1() {
        paparazzi.snapshot {
            MarkdownPreview("# Header Level 1")
        }
    }

    @Test
    fun header2() {
        paparazzi.snapshot {
            MarkdownPreview("## Header Level 2")
        }
    }

    @Test
    fun header3() {
        paparazzi.snapshot {
            MarkdownPreview("### Header Level 3")
        }
    }

    @Test
    fun header4() {
        paparazzi.snapshot {
            MarkdownPreview("#### Header Level 4")
        }
    }

    @Test
    fun header5() {
        paparazzi.snapshot {
            MarkdownPreview("##### Header Level 5")
        }
    }

    @Test
    fun header6() {
        paparazzi.snapshot {
            MarkdownPreview("###### Header Level 6")
        }
    }

    @Test
    fun allHeaders() {
        paparazzi.snapshot {
            MarkdownExamplesPreview(
                listOf(
                    "# Header 1",
                    "## Header 2",
                    "### Header 3",
                    "#### Header 4",
                    "##### Header 5",
                    "###### Header 6"
                )
            )
        }
    }

    // ===================
    // Lists Tests
    // ===================

    @Test
    fun unorderedList() {
        paparazzi.snapshot {
            MarkdownPreview(
                """
                - First item
                - Second item
                - Third item
                """.trimIndent()
            )
        }
    }

    @Test
    fun orderedList() {
        paparazzi.snapshot {
            MarkdownPreview(
                """
                1. First item
                2. Second item
                3. Third item
                """.trimIndent()
            )
        }
    }

    @Test
    fun mixedList() {
        paparazzi.snapshot {
            MarkdownExamplesPreview(
                listOf(
                    "- Bullet point",
                    "1. Numbered item"
                )
            )
        }
    }

    // ===================
    // Code Blocks Tests
    // ===================

    @Test
    fun codeBlock() {
        paparazzi.snapshot {
            MarkdownPreview(
                """
                ```
                fun hello() {
                    println("Hello")
                }
                ```
                """.trimIndent()
            )
        }
    }

    @Test
    fun codeBlockWithLanguage() {
        paparazzi.snapshot {
            MarkdownPreview(
                """
                ```kotlin
                val greeting = "Hello, World!"
                println(greeting)
                ```
                """.trimIndent()
            )
        }
    }

    // ===================
    // Block Quote Tests
    // ===================

    @Test
    fun blockQuote() {
        paparazzi.snapshot {
            MarkdownPreview("> This is a quoted text")
        }
    }

    @Test
    fun multiLineBlockQuote() {
        paparazzi.snapshot {
            MarkdownPreview(
                """
                > Line one of the quote
                > Line two of the quote
                > Line three of the quote
                """.trimIndent()
            )
        }
    }

    @Test
    fun blockQuoteWithLazyContinuation() {
        paparazzi.snapshot {
            MarkdownPreview(
                """
                > This is a block quote.
                > It can span multiple lines.
                not a quote
                """.trimIndent()
            )
        }
    }

    // ===================
    // Horizontal Rule Tests
    // ===================

    @Test
    fun horizontalRule() {
        paparazzi.snapshot {
            MarkdownPreview(
                """
                Text above
                
                ---
                
                Text below
                """.trimIndent()
            )
        }
    }

    // ===================
    // Nested Formatting Tests
    // ===================

    @Test
    fun boldWithinItalic() {
        paparazzi.snapshot {
            MarkdownPreview("*italic with **bold** inside*")
        }
    }

    @Test
    fun italicWithinBold() {
        paparazzi.snapshot {
            MarkdownPreview("**bold with *italic* inside**")
        }
    }

    @Test
    fun linkWithBoldText() {
        paparazzi.snapshot {
            MarkdownPreview("**[Bold Link](https://example.com)**")
        }
    }

    @Test
    fun complexNestedFormatting() {
        paparazzi.snapshot {
            MarkdownPreview("This has **bold**, *italic*, `code`, and a [link](https://example.com)")
        }
    }

    // ===================
    // Edge Cases - Unicode & Special Characters
    // ===================

    @Test
    fun unicodeEmoji() {
        paparazzi.snapshot {
            MarkdownPreview("Hello 👋 World 🌍! How are you? 😊")
        }
    }

    @Test
    fun unicodeJapanese() {
        paparazzi.snapshot {
            MarkdownPreview("日本語テキスト - Japanese text")
        }
    }

    @Test
    fun unicodeKorean() {
        paparazzi.snapshot {
            MarkdownPreview("한국어 텍스트 - Korean text")
        }
    }

    @Test
    fun unicodeChinese() {
        paparazzi.snapshot {
            MarkdownPreview("中文文本 - Chinese text")
        }
    }

    @Test
    fun rtlArabic() {
        paparazzi.snapshot {
            MarkdownPreview("مرحبا بالعالم - Hello World in Arabic")
        }
    }

    @Test
    fun rtlHebrew() {
        paparazzi.snapshot {
            MarkdownPreview("שלום עולם - Hello World in Hebrew")
        }
    }

    @Test
    fun mixedLtrRtl() {
        paparazzi.snapshot {
            MarkdownPreview("English مرحبا English עברית English")
        }
    }

    @Test
    fun specialCharacters() {
        paparazzi.snapshot {
            MarkdownPreview("Special: & < > \" ' © ® ™ € £ ¥")
        }
    }

    @Test
    fun mathSymbols() {
        paparazzi.snapshot {
            MarkdownPreview("Math: ± × ÷ √ ∞ ≠ ≤ ≥ ∑ ∏ π")
        }
    }

    // ===================
    // Edge Cases - Malformed Markdown
    // ===================

    @Test
    fun unclosedBold() {
        paparazzi.snapshot {
            MarkdownPreview("**unclosed bold text")
        }
    }

    @Test
    fun unclosedItalic() {
        paparazzi.snapshot {
            MarkdownPreview("*unclosed italic text")
        }
    }

    @Test
    fun unclosedCode() {
        paparazzi.snapshot {
            MarkdownPreview("`unclosed code")
        }
    }

    @Test
    fun brokenLink() {
        paparazzi.snapshot {
            MarkdownPreview("[broken link(https://example.com)")
        }
    }

    @Test
    fun emptyLink() {
        paparazzi.snapshot {
            MarkdownPreview("[](https://example.com)")
        }
    }

    // ===================
    // Complex Documents
    // ===================

    @Test
    fun complexDocument() {
        paparazzi.snapshot {
            MarkdownPreview(
                """
                # Welcome to the App
                
                This is a **feature-rich** markdown renderer with *multiple* formatting options.
                
                ## Features
                
                - **Bold text** support
                - *Italic text* support
                - `Inline code` support
                - [Links](https://example.com) support
                
                ## Code Example
                
                ```kotlin
                fun greet(name: String) {
                    println("Hello, ${'$'}name!")
                }
                ```
                
                > Note: This is a blockquote with important information.
                
                ---
                
                Visit our [website](https://example.com) for more information.
                """.trimIndent()
            )
        }
    }

    @Test
    fun multipleParagraphs() {
        paparazzi.snapshot {
            MarkdownPreview(
                """
                First paragraph with some text.
                
                Second paragraph with **bold** and *italic*.
                
                Third paragraph with a [link](https://example.com).
                """.trimIndent()
            )
        }
    }

    @Test
    fun longText() {
        paparazzi.snapshot {
            MarkdownPreview(
                """
                This is a very long paragraph that contains a lot of text to test how the 
                renderer handles text wrapping and line breaks. It includes **bold text**, 
                *italic text*, and `inline code` to ensure that formatting is preserved 
                correctly even when the text spans multiple lines. The quick brown fox jumps 
                over the lazy dog.
                """.trimIndent()
            )
        }
    }

    // ===================
    // Image Markdown
    // ===================

    @Test
    fun imageMarkdown() {
        paparazzi.snapshot {
            MarkdownPreview("![Alt text for image](https://example.com/image.png)")
        }
    }

    // ===================
    // Empty and Edge States
    // ===================

    @Test
    fun emptyString() {
        paparazzi.snapshot {
            MarkdownPreview("")
        }
    }

    @Test
    fun whitespaceOnly() {
        paparazzi.snapshot {
            MarkdownPreview("   ")
        }
    }

    @Test
    fun singleCharacter() {
        paparazzi.snapshot {
            MarkdownPreview("A")
        }
    }
}
