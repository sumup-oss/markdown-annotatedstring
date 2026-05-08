package com.sumup.markdown.annotatedstring

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import org.intellij.markdown.parser.MarkdownParser

/**
 * Generates an AnnotatedString from markdown text.
 */
fun markdownToAnnotatedString(markdown: String): AnnotatedString {
    val flavour = CommonMarkFlavourDescriptor()
    val parser = MarkdownParser(flavour)
    val root = parser.buildMarkdownTreeFromString(markdown)
    return AnnotatedStringGenerator(markdown, root, flavour).generateAnnotatedString()
}

/**
 * A composable that renders markdown text as an AnnotatedString for snapshot testing.
 */
@Composable
fun MarkdownPreview(
    markdown: String,
    modifier: Modifier = Modifier
) {
    val annotatedString = markdownToAnnotatedString(markdown)

    BasicText(
        text = annotatedString,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

/**
 * A composable that renders multiple markdown examples in a column for comparison.
 */
@Composable
fun MarkdownExamplesPreview(
    markdownExamples: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
        markdownExamples.forEach { markdown ->
            MarkdownPreview(
                markdown = markdown,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
