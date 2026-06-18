package com.sumup.markdown.annotatedstring.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sumup.markdown.annotatedstring.AnnotatedStringGenerator
import com.sumup.markdown.annotatedstring.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MarkdownPlayground()
                }
            }
        }
    }
}

private val SAMPLE_MARKDOWN = """
# Markdown Demo

This is a **bold** and *italic* text example.

You can also combine ***bold and italic*** together.

## Links

Check out [SumUp](https://sumup.com) for more info.

## Code

Inline code: `val x = 42`

```kotlin
fun greet(name: String): String {
    return "Hello, ${'$'}name!"
}
```

## Lists

### Unordered
- First item
- Second item
- Third item

### Ordered
1. Step one
2. Step two
3. Step three

## Block Quote

> This is a block quote.
> It can span multiple lines.
not a quote
---
x
___
That's it! Edit the markdown above to try it out.
""".trimIndent()

@Composable
fun MarkdownPlayground() {
    var markdownInput by remember { mutableStateOf(SAMPLE_MARKDOWN) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Markdown Editor",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = markdownInput,
            onValueChange = { markdownInput = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            label = { Text("Enter Markdown") },
            textStyle = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Preview",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        val annotatedString = remember(markdownInput) {
            renderMarkdown(markdownInput)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            BasicText(
                text = annotatedString,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

private fun renderMarkdown(markdown: String): AnnotatedString {
    return try {
        val flavour = CommonMarkFlavourDescriptor()
        val parser = MarkdownParser(flavour)
        val root = parser.buildMarkdownTreeFromString(markdown)
        AnnotatedStringGenerator(markdown, root, flavour).generateAnnotatedString()
    } catch (e: Exception) {
        AnnotatedString("Error parsing markdown: ${e.message}")
    }
}

@Preview(showBackground = true)
@Composable
private fun MarkdownPlaygroundPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MarkdownPlayground()
        }
    }
}


