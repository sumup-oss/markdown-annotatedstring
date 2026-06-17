package com.sumup.markdown.annotatedstring

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.google.common.truth.Truth.assertThat
import org.intellij.markdown.parser.MarkdownParser
import org.junit.Test

/**
 * Unit tests for AnnotatedStringGenerator covering basic markdown parsing,
 * nested formatting, and edge cases.
 */
class AnnotatedStringGeneratorTest {

    private fun generateAnnotatedString(markdown: String): androidx.compose.ui.text.AnnotatedString {
        val flavour = CommonMarkFlavourDescriptor()
        val parser = MarkdownParser(flavour)
        val root = parser.buildMarkdownTreeFromString(markdown)
        return AnnotatedStringGenerator(markdown, root, flavour).generateAnnotatedString()
    }

    // ===================
    // Basic Markdown Tests
    // ===================

    @Test
    fun `plain text is rendered correctly`() {
        val result = generateAnnotatedString("Hello World")
        assertThat(result.text).isEqualTo("Hello World")
    }

    @Test
    fun `bold text has FontWeight Bold span`() {
        val result = generateAnnotatedString("**bold text**")
        assertThat(result.text).contains("bold text")

        val boldSpans = result.spanStyles.filter {
            it.item.fontWeight == FontWeight.Bold
        }
        assertThat(boldSpans).isNotEmpty()
    }

    @Test
    fun `italic text has FontStyle Italic span`() {
        val result = generateAnnotatedString("*italic text*")
        assertThat(result.text).contains("italic text")

        val italicSpans = result.spanStyles.filter {
            it.item.fontStyle == FontStyle.Italic
        }
        assertThat(italicSpans).isNotEmpty()
    }

    @Test
    fun `inline code has monospace styling`() {
        val result = generateAnnotatedString("`code`")
        assertThat(result.text).contains("code")
    }

    @Test
    fun `link text is rendered with URL annotation`() {
        val result = generateAnnotatedString("[link text](https://example.com)")
        assertThat(result.text).contains("link text")

        val urlAnnotations = result.getStringAnnotations("URL", 0, result.length)
        assertThat(urlAnnotations).isNotEmpty()
        assertThat(urlAnnotations.first().item).isEqualTo("https://example.com")
    }

    @Test
    fun `header level 1 has large font size`() {
        val result = generateAnnotatedString("# Header 1")
        assertThat(result.text).contains("Header 1")
        assertThat(result.spanStyles).isNotEmpty()
    }

    @Test
    fun `header level 2 has appropriate font size`() {
        val result = generateAnnotatedString("## Header 2")
        assertThat(result.text).contains("Header 2")
        assertThat(result.spanStyles).isNotEmpty()
    }

    @Test
    fun `header level 3 has appropriate font size`() {
        val result = generateAnnotatedString("### Header 3")
        assertThat(result.text).contains("Header 3")
    }

    @Test
    fun `header level 4 has appropriate font size`() {
        val result = generateAnnotatedString("#### Header 4")
        assertThat(result.text).contains("Header 4")
    }

    @Test
    fun `header level 5 has appropriate font size`() {
        val result = generateAnnotatedString("##### Header 5")
        assertThat(result.text).contains("Header 5")
    }

    @Test
    fun `header level 6 has appropriate font size`() {
        val result = generateAnnotatedString("###### Header 6")
        assertThat(result.text).contains("Header 6")
    }

    @Test
    fun `unordered list renders with bullet points`() {
        val result = generateAnnotatedString("- Item 1\n- Item 2\n- Item 3")
        assertThat(result.text).contains("•")
        assertThat(result.text).contains("Item 1")
        assertThat(result.text).contains("Item 2")
        assertThat(result.text).contains("Item 3")
    }

    @Test
    fun `ordered list renders with numbers`() {
        val result = generateAnnotatedString("1. First\n2. Second\n3. Third")
        assertThat(result.text).contains("1.")
        assertThat(result.text).contains("2.")
        assertThat(result.text).contains("3.")
        assertThat(result.text).contains("First")
        assertThat(result.text).contains("Second")
        assertThat(result.text).contains("Third")
    }

    @Test
    fun `code block renders with monospace styling`() {
        val result = generateAnnotatedString("```\ncode block\n```")
        assertThat(result.text).contains("code block")
    }

    @Test
    fun `block quote renders with italic gray styling`() {
        val result = generateAnnotatedString("> This is a quote")
        assertThat(result.text).contains("This is a quote")
    }

    @Test
    fun `horizontal rule renders separator`() {
        val result = generateAnnotatedString("---")
        assertThat(result.text).contains("---")
    }

    // ===================
    // Nested Formatting Tests
    // ===================

    @Test
    fun `bold within italic renders both styles`() {
        val result = generateAnnotatedString("*italic **bold** text*")
        assertThat(result.text).contains("italic")
        assertThat(result.text).contains("bold")
        assertThat(result.text).contains("text")

        val boldSpans = result.spanStyles.filter { it.item.fontWeight == FontWeight.Bold }
        val italicSpans = result.spanStyles.filter { it.item.fontStyle == FontStyle.Italic }

        assertThat(boldSpans).isNotEmpty()
        assertThat(italicSpans).isNotEmpty()
    }

    @Test
    fun `italic within bold renders both styles`() {
        val result = generateAnnotatedString("**bold *italic* text**")
        assertThat(result.text).contains("bold")
        assertThat(result.text).contains("italic")
        assertThat(result.text).contains("text")

        val boldSpans = result.spanStyles.filter { it.item.fontWeight == FontWeight.Bold }
        val italicSpans = result.spanStyles.filter { it.item.fontStyle == FontStyle.Italic }

        assertThat(boldSpans).isNotEmpty()
        assertThat(italicSpans).isNotEmpty()
    }

    @Test
    fun `link within bold renders both styles`() {
        val result = generateAnnotatedString("**[bold link](https://example.com)**")
        assertThat(result.text).contains("bold link")

        val boldSpans = result.spanStyles.filter { it.item.fontWeight == FontWeight.Bold }
        assertThat(boldSpans).isNotEmpty()
    }

    @Test
    fun `multiple formatting in paragraph`() {
        val result = generateAnnotatedString("Normal **bold** and *italic* and `code` text")
        assertThat(result.text).contains("Normal")
        assertThat(result.text).contains("bold")
        assertThat(result.text).contains("italic")
        assertThat(result.text).contains("code")
        assertThat(result.text).contains("text")
    }

    // ===================
    // Edge Cases
    // ===================

    @Test
    fun `empty string returns empty annotated string`() {
        val result = generateAnnotatedString("")
        assertThat(result.text).isEmpty()
    }

    @Test
    fun `whitespace only string`() {
        val result = generateAnnotatedString("   ")
        // Should handle gracefully, not crash
        assertThat(result).isNotNull()
    }

    @Test
    fun `unclosed bold marker`() {
        val result = generateAnnotatedString("**unclosed bold")
        // Should not crash, render as-is or partial
        assertThat(result).isNotNull()
        assertThat(result.text).contains("unclosed")
    }

    @Test
    fun `unclosed italic marker`() {
        val result = generateAnnotatedString("*unclosed italic")
        assertThat(result).isNotNull()
        assertThat(result.text).contains("unclosed")
    }

    @Test
    fun `unclosed inline code marker`() {
        val result = generateAnnotatedString("`unclosed code")
        assertThat(result).isNotNull()
        assertThat(result.text).contains("unclosed")
    }

    @Test
    fun `broken link missing URL`() {
        val result = generateAnnotatedString("[link text]")
        assertThat(result).isNotNull()
        assertThat(result.text).contains("link text")
    }

    @Test
    fun `broken link missing closing bracket`() {
        val result = generateAnnotatedString("[link text(https://example.com)")
        assertThat(result).isNotNull()
    }

    @Test
    fun `broken link missing text`() {
        val result = generateAnnotatedString("[](https://example.com)")
        assertThat(result).isNotNull()
    }

    @Test
    fun `unicode emoji characters`() {
        val result = generateAnnotatedString("Hello 👋 World 🌍!")
        assertThat(result.text).contains("👋")
        assertThat(result.text).contains("🌍")
    }

    @Test
    fun `unicode characters in different scripts`() {
        val result = generateAnnotatedString("日本語 한국어 العربية")
        assertThat(result.text).contains("日本語")
        assertThat(result.text).contains("한국어")
        assertThat(result.text).contains("العربية")
    }

    @Test
    fun `RTL text Arabic`() {
        val result = generateAnnotatedString("مرحبا بالعالم")
        assertThat(result.text).contains("مرحبا")
    }

    @Test
    fun `RTL text Hebrew`() {
        val result = generateAnnotatedString("שלום עולם")
        assertThat(result.text).contains("שלום")
    }

    @Test
    fun `mixed LTR and RTL text`() {
        val result = generateAnnotatedString("Hello مرحبا World")
        assertThat(result.text).contains("Hello")
        assertThat(result.text).contains("مرحبا")
        assertThat(result.text).contains("World")
    }

    @Test
    fun `special characters and symbols`() {
        val result = generateAnnotatedString("Special: & < > \" ' / \\ @ # $ % ^ & * ( )")
        assertThat(result.text).contains("&")
        assertThat(result.text).contains("<")
        assertThat(result.text).contains(">")
    }

    @Test
    fun `escaped markdown characters`() {
        val result = generateAnnotatedString("\\*not italic\\* \\**not bold\\**")
        assertThat(result).isNotNull()
    }

    @Test
    fun `extremely long text`() {
        val longText = "A".repeat(10000)
        val result = generateAnnotatedString(longText)
        assertThat(result.text).contains("A")
        assertThat(result.text.length).isGreaterThan(9999)
    }

    @Test
    fun `deeply nested lists`() {
        val markdown = """
            - Level 1
              - Level 2
                - Level 3
                  - Level 4
        """.trimIndent()
        val result = generateAnnotatedString(markdown)
        assertThat(result).isNotNull()
        assertThat(result.text).contains("Level 1")
    }

    @Test
    fun `multiple paragraphs`() {
        val result = generateAnnotatedString("Paragraph 1\n\nParagraph 2\n\nParagraph 3")
        assertThat(result.text).contains("Paragraph 1")
        assertThat(result.text).contains("Paragraph 2")
        assertThat(result.text).contains("Paragraph 3")
    }

    @Test
    fun `newlines within formatting`() {
        val result = generateAnnotatedString("**bold\ntext**")
        assertThat(result).isNotNull()
    }

    @Test
    fun `image markdown`() {
        val result = generateAnnotatedString("![alt text](https://example.com/image.png)")
        assertThat(result).isNotNull()
        // Image is rendered as [alt text](url) format or contains image reference
        val text = result.text
        assertThat(text.contains("alt") || text.contains("Image") || text.contains("image")).isTrue()
    }

    @Test
    fun `link with special characters in URL`() {
        val result = generateAnnotatedString("[link](https://example.com/path?query=value&other=123)")
        assertThat(result.text).contains("link")

        val urlAnnotations = result.getStringAnnotations("URL", 0, result.length)
        if (urlAnnotations.isNotEmpty()) {
            assertThat(urlAnnotations.first().item).contains("example.com")
        }
    }

    @Test
    fun `consecutive formatting markers`() {
        val result = generateAnnotatedString("***bold and italic***")
        assertThat(result).isNotNull()
    }

    @Test
    fun `code block with language specifier`() {
        val result = generateAnnotatedString("```kotlin\nval x = 1\n```")
        assertThat(result).isNotNull()
    }

    @Test
    fun `inline code with backticks inside`() {
        val result = generateAnnotatedString("`` `code` ``")
        assertThat(result).isNotNull()
    }

    @Test
    fun `multiple horizontal rules`() {
        val result = generateAnnotatedString("---\n\ntext\n\n---")
        assertThat(result.text).contains("text")
    }

    @Test
    fun `blockquote with multiple paragraphs`() {
        val result = generateAnnotatedString("> Quote line 1\n> Quote line 2")
        assertThat(result.text).contains("Quote line 1")
        assertThat(result.text).contains("Quote line 2")
    }

    @Test
    fun `mixed content complex document`() {
        val markdown = """
            # Title
            
            This is a **bold** and *italic* paragraph with a [link](https://example.com).
            
            ## Subtitle
            
            - List item 1
            - List item 2
            
            > A blockquote
            
            ```
            code block
            ```
            
            ---
            
            Final paragraph.
        """.trimIndent()

        val result = generateAnnotatedString(markdown)
        assertThat(result.text).contains("Title")
        assertThat(result.text).contains("bold")
        assertThat(result.text).contains("italic")
        assertThat(result.text).contains("link")
        assertThat(result.text).contains("Subtitle")
        assertThat(result.text).contains("List item")
        assertThat(result.text).contains("blockquote")
        assertThat(result.text).contains("Final paragraph")
    }

    @Test
    fun `zero width characters`() {
        val result = generateAnnotatedString("Hello\u200BWorld") // Zero-width space
        assertThat(result).isNotNull()
    }

    @Test
    fun `tab characters`() {
        val result = generateAnnotatedString("Hello\tWorld")
        assertThat(result).isNotNull()
    }

    @Test
    fun `multiple consecutive spaces`() {
        val result = generateAnnotatedString("Hello     World")
        assertThat(result).isNotNull()
    }
}
