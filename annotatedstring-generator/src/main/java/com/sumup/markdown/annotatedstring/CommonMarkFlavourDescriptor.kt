package com.sumup.markdown.annotatedstring

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.accept
import org.intellij.markdown.ast.acceptChildren
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.commonmark.CommonMarkMarkerProcessor
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.URI
import org.intellij.markdown.lexer.MarkdownLexer
import org.intellij.markdown.lexer._MarkdownLexer
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkerProcessorFactory
import org.intellij.markdown.parser.sequentialparsers.EmphasisLikeParser
import org.intellij.markdown.parser.sequentialparsers.SequentialParser
import org.intellij.markdown.parser.sequentialparsers.SequentialParserManager
import org.intellij.markdown.parser.sequentialparsers.impl.AutolinkParser
import org.intellij.markdown.parser.sequentialparsers.impl.BacktickParser
import org.intellij.markdown.parser.sequentialparsers.impl.EmphStrongDelimiterParser
import org.intellij.markdown.parser.sequentialparsers.impl.ImageParser
import org.intellij.markdown.parser.sequentialparsers.impl.InlineLinkParser
import org.intellij.markdown.parser.sequentialparsers.impl.ReferenceLinkParser

/**
 * CommonMark flavour descriptor that provides default rendering for standard markdown elements.
 *
 * This class implements [MarkdownFlavourAnnotatedStringDescriptor] to convert CommonMark
 * markdown syntax into Jetpack Compose [AnnotatedString] with appropriate styling for:
 * - Bold, italic, and code spans
 * - Headers (levels 1-6)
 * - Links and images
 * - Ordered and unordered lists
 * - Code blocks and fenced code
 * - Block quotes
 * - Horizontal rules
 */
public class CommonMarkFlavourDescriptor : MarkdownFlavourAnnotatedStringDescriptor {

    override fun createInlinesLexer(): MarkdownLexer = MarkdownLexer(_MarkdownLexer())

    override val markerProcessorFactory: MarkerProcessorFactory = CommonMarkMarkerProcessor.Factory

    override val sequentialParserManager: SequentialParserManager = object : SequentialParserManager() {
        override fun getParserSequence(): List<SequentialParser> {
            return listOf(
                AutolinkParser(listOf(MarkdownTokenTypes.AUTOLINK)),
                BacktickParser(),
                ImageParser(),
                InlineLinkParser(),
                ReferenceLinkParser(),
                EmphasisLikeParser(EmphStrongDelimiterParser())
            )
        }
    }

    override fun createAnnotatedStringGeneratingProviders(
        linkMap: LinkMap,
        baseURI: URI?
    ): Map<IElementType, AnnotatedStringGeneratingProvider> {
        return mapOf(
            // Basic text tokens
            MarkdownTokenTypes.TEXT to createTextTokenProvider(),
            MarkdownTokenTypes.WHITE_SPACE to createTextTokenProvider(),
            MarkdownTokenTypes.EOL to createTextTokenProvider(),

            // Structure elements
            MarkdownElementTypes.MARKDOWN_FILE to createMarkdownFileProvider(),
            MarkdownElementTypes.PARAGRAPH to createParagraphProvider(),

            // Inline formatting elements
            MarkdownElementTypes.STRONG to createStrongProvider(),
            MarkdownElementTypes.EMPH to createEmphProvider(),
            MarkdownElementTypes.CODE_SPAN to createCodeSpanProvider(),

            // Header elements
            MarkdownElementTypes.ATX_1 to createHeaderProvider(1),
            MarkdownElementTypes.ATX_2 to createHeaderProvider(2),
            MarkdownElementTypes.ATX_3 to createHeaderProvider(3),
            MarkdownElementTypes.ATX_4 to createHeaderProvider(4),
            MarkdownElementTypes.ATX_5 to createHeaderProvider(5),
            MarkdownElementTypes.ATX_6 to createHeaderProvider(6),

            // Link and image elements
            MarkdownElementTypes.INLINE_LINK to createInlineLinkProvider(),
            MarkdownElementTypes.IMAGE to createImageProvider(),

            // List elements
            MarkdownElementTypes.ORDERED_LIST to createOrderedListProvider(),
            MarkdownElementTypes.UNORDERED_LIST to createUnorderedListProvider(),
            MarkdownElementTypes.LIST_ITEM to createListItemProvider(),

            // Code block elements
            MarkdownElementTypes.CODE_BLOCK to createCodeBlockProvider(),
            MarkdownElementTypes.CODE_FENCE to createCodeFenceProvider(),

            // Quote and rule elements
            MarkdownElementTypes.BLOCK_QUOTE to createBlockQuoteProvider(),
            MarkdownTokenTypes.HORIZONTAL_RULE to createHorizontalRuleProvider(),

            // Punctuation and special tokens
            MarkdownTokenTypes.COLON to createTextTokenProvider(),
            MarkdownTokenTypes.DOUBLE_QUOTE to createTextTokenProvider(),
            MarkdownTokenTypes.SINGLE_QUOTE to createTextTokenProvider(),
            MarkdownTokenTypes.LPAREN to createTextTokenProvider(),
            MarkdownTokenTypes.RPAREN to createTextTokenProvider(),
            MarkdownTokenTypes.LBRACKET to createTextTokenProvider(),
            MarkdownTokenTypes.RBRACKET to createTextTokenProvider(),
            MarkdownTokenTypes.EXCLAMATION_MARK to createTextTokenProvider(),
            MarkdownTokenTypes.LT to createTextTokenProvider(),
            MarkdownTokenTypes.GT to createTextTokenProvider(),

            // Code and formatting tokens (usually skipped by elements)
            MarkdownTokenTypes.BACKTICK to createTextTokenProvider(),
            MarkdownTokenTypes.ESCAPED_BACKTICKS to createTextTokenProvider(),
            MarkdownTokenTypes.EMPH to createTextTokenProvider(),

            // List tokens (usually skipped by LIST_ITEM element)
            MarkdownTokenTypes.LIST_BULLET to createTextTokenProvider(),
            MarkdownTokenTypes.LIST_NUMBER to createTextTokenProvider(),

            // Header tokens (usually skipped by ATX_* elements)
            MarkdownTokenTypes.ATX_HEADER to createTextTokenProvider(),
            MarkdownTokenTypes.ATX_CONTENT to createTextTokenProvider(),
            MarkdownTokenTypes.SETEXT_1 to createTextTokenProvider(),
            MarkdownTokenTypes.SETEXT_2 to createTextTokenProvider(),
            MarkdownTokenTypes.SETEXT_CONTENT to createTextTokenProvider(),

            // Code fence tokens (usually skipped by CODE_FENCE element)
            MarkdownTokenTypes.CODE_FENCE_START to createTextTokenProvider(),
            MarkdownTokenTypes.CODE_FENCE_CONTENT to createTextTokenProvider(),
            MarkdownTokenTypes.CODE_FENCE_END to createTextTokenProvider(),
            MarkdownTokenTypes.CODE_LINE to createTextTokenProvider(),

            // BLOCK_QUOTE tokens are the ">" marker characters themselves. They are
            // structural markers — never rendered as text, regardless of where they
            // appear in the AST (top-level block quote children OR nested inside a
            // paragraph for continuation lines).
            MarkdownTokenTypes.BLOCK_QUOTE to NoOpProvider,

            // Link tokens (usually processed by INLINE_LINK element)
            MarkdownTokenTypes.LINK_TITLE to createTextTokenProvider(),
            MarkdownTokenTypes.LINK_ID to createTextTokenProvider(),

            // HTML and autolink tokens
            MarkdownTokenTypes.EMAIL_AUTOLINK to createTextTokenProvider(),
            MarkdownTokenTypes.HTML_TAG to createTextTokenProvider(),
            MarkdownTokenTypes.HTML_BLOCK_CONTENT to createTextTokenProvider(),

            // Special line break
            MarkdownTokenTypes.HARD_LINE_BREAK to object : AnnotatedStringGeneratingProvider {
                override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
                    visitor.builder.append("\n")
                }
            }
        )
    }

    private fun createTextTokenProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            visitor.builder.append(node.getTextInNode(markdown).toString())
        }
    }

    /** Provider that produces no output. Use for structural tokens that should never be rendered. */
    private object NoOpProvider : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            // Intentionally empty
        }
    }

    // Extract provider creation methods for better organization
    private fun createStrongProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            visitor.builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                // Skip the opening and closing markers (first and last children are usually the ** markers)
                node.children.forEach { child ->
                    if (child.type != MarkdownTokenTypes.EMPH) {
                        child.accept(visitor)
                    }
                }
            }
        }
    }

    private fun createEmphProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            visitor.builder.withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                // Skip the opening and closing markers (first and last children are usually the * markers)
                node.children.forEach { child ->
                    if (child.type != MarkdownTokenTypes.EMPH) {
                        child.accept(visitor)
                    }
                }
            }
        }
    }

    private fun createMarkdownFileProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            var seenFirstBlock = false
            node.children.forEach { child ->
                if (isBlockElement(child.type)) {
                    if (seenFirstBlock) {
                        // Normalize the spacing before each block (except the first) to
                        // exactly one blank line. Any EOL tokens from the previous block
                        // have already been written, so we just top up whatever's missing.
                        val current = visitor.builder.toAnnotatedString().text
                        when {
                            current.isEmpty() || current.endsWith("\n\n") -> { /* fine */
                            }

                            current.endsWith("\n") -> visitor.builder.append("\n")
                            else -> visitor.builder.append("\n\n")
                        }
                    }
                    child.accept(visitor)
                    seenFirstBlock = true
                } else {
                    child.accept(visitor)
                }
            }
        }
    }

    private fun createParagraphProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            node.acceptChildren(visitor)
        }
    }

    private fun createCodeSpanProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            visitor.builder.withStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    background = Color.LightGray
                )
            ) {
                // Skip backtick markers and only get the content
                node.children.forEach { child ->
                    if (child.type != MarkdownTokenTypes.BACKTICK) {
                        child.accept(visitor)
                    }
                }
            }
        }
    }

    private fun createInlineLinkProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            val linkTextNode = node.children.find { it.type == MarkdownElementTypes.LINK_TEXT }
            val linkDestinationNode = node.children.find { it.type == MarkdownElementTypes.LINK_DESTINATION }

            val linkText = linkTextNode?.children
                ?.filter { it.type != MarkdownTokenTypes.LBRACKET && it.type != MarkdownTokenTypes.RBRACKET }
                ?.joinToString("") { it.getTextInNode(markdown).toString() } ?: ""

            val url = linkDestinationNode?.getTextInNode(markdown)?.toString() ?: ""

            if (url.isNotEmpty()) {
                visitor.builder.pushStringAnnotation(tag = "URL", annotation = url)
                visitor.builder.withStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                    visitor.builder.append(linkText.ifEmpty { url })
                }
                visitor.builder.pop()
            } else {
                // Process children but skip link syntax markers
                node.children.forEach { child ->
                    if (child.type != MarkdownTokenTypes.LBRACKET &&
                        child.type != MarkdownTokenTypes.RBRACKET &&
                        child.type != MarkdownTokenTypes.LPAREN &&
                        child.type != MarkdownTokenTypes.RPAREN
                    ) {
                        child.accept(visitor)
                    }
                }
            }
        }
    }

    private fun createHeaderProvider(level: Int): AnnotatedStringGeneratingProvider {
        return object : AnnotatedStringGeneratingProvider {
            override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
                val textStyle = when (level) {
                    1 -> SpanStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    2 -> SpanStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    3 -> SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    4 -> SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    5 -> SpanStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    6 -> SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    else -> SpanStyle()
                }
                visitor.builder.withStyle(textStyle) {
                    // Skip ATX markers (# symbols) and only process content
                    node.children.forEach { child ->
                        if (child.type != MarkdownTokenTypes.ATX_HEADER) {
                            child.accept(visitor)
                        }
                    }
                }
            }
        }
    }

    private fun isBlockElement(type: Any?): Boolean {
        return type == MarkdownElementTypes.PARAGRAPH ||
                type == MarkdownElementTypes.ATX_1 || type == MarkdownElementTypes.ATX_2 ||
                type == MarkdownElementTypes.ATX_3 || type == MarkdownElementTypes.ATX_4 ||
                type == MarkdownElementTypes.ATX_5 || type == MarkdownElementTypes.ATX_6 ||
                type == MarkdownElementTypes.BLOCK_QUOTE ||
                type == MarkdownElementTypes.CODE_BLOCK || type == MarkdownElementTypes.CODE_FENCE ||
                type == MarkdownElementTypes.ORDERED_LIST || type == MarkdownElementTypes.UNORDERED_LIST ||
                type == MarkdownTokenTypes.HORIZONTAL_RULE
    }

    // not needed
    override fun createHtmlGeneratingProviders(
        linkMap: LinkMap,
        baseURI: URI?
    ): Map<IElementType, GeneratingProvider> = emptyMap()

    // Add missing provider creation methods
    private fun createImageProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            val altTextNode = node.children.find { it.type == MarkdownElementTypes.LINK_TEXT }
            val imageUrlNode = node.children.find { it.type == MarkdownElementTypes.LINK_DESTINATION }

            val altText = altTextNode?.children
                ?.filter { it.type != MarkdownTokenTypes.LBRACKET && it.type != MarkdownTokenTypes.RBRACKET }
                ?.joinToString("") { it.getTextInNode(markdown).toString() } ?: "Image"

            val imageUrl = imageUrlNode?.getTextInNode(markdown)?.toString() ?: ""

            visitor.builder.append("[$altText]($imageUrl)")
        }
    }

    private fun createOrderedListProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            appendListItems(visitor, node)
        }
    }

    private fun createUnorderedListProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            appendListItems(visitor, node)
        }
    }

    /**
     * Renders list items so they sit on consecutive lines (no blank line between
     * them). The parser emits `EOL` / `WHITE_SPACE` tokens between sibling list
     * items at the list level – we skip those structural tokens and instead
     * top up to exactly one trailing newline between items ourselves, with no
     * separator after the final item.
     */
    private fun appendListItems(visitor: AnnotatedStringRecursiveVisitor, node: org.intellij.markdown.ast.ASTNode) {
        val items = node.children.filter { it.type == MarkdownElementTypes.LIST_ITEM }
        node.children.forEach { child ->
            when (child.type) {
                MarkdownTokenTypes.EOL,
                MarkdownTokenTypes.WHITE_SPACE -> {
                    // Skip – we control inter-item spacing manually.
                }

                MarkdownElementTypes.LIST_ITEM -> {
                    child.accept(visitor)
                    if (child != items.last()) {
                        val current = visitor.builder.toAnnotatedString().text
                        if (!current.endsWith("\n")) {
                            visitor.builder.append("\n")
                        }
                    }
                }

                else -> child.accept(visitor)
            }
        }
    }

    private fun createListItemProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            val parentType = node.parent?.type
            if (parentType == MarkdownElementTypes.UNORDERED_LIST) {
                visitor.builder.append("• ")
            } else if (parentType == MarkdownElementTypes.ORDERED_LIST) {
                val listParent = node.parent
                val itemIndex = listParent?.children?.filter { it.type == MarkdownElementTypes.LIST_ITEM }?.indexOf(node) ?: 0
                visitor.builder.append("${itemIndex + 1}. ")
            }
            // Skip list markers and only process content
            node.children.forEach { child ->
                if (child.type != MarkdownTokenTypes.LIST_BULLET &&
                    child.type != MarkdownTokenTypes.LIST_NUMBER
                ) {
                    child.accept(visitor)
                }
            }
        }
    }

    private fun createCodeBlockProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            visitor.builder.withStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    background = Color.LightGray
                )
            ) {
                // Only process code content, skip indentation markers
                node.children.forEach { child ->
                    if (child.type == MarkdownTokenTypes.CODE_LINE) {
                        child.accept(visitor)
                    }
                }
            }
        }
    }

    private fun createCodeFenceProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            visitor.builder.withStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    background = Color.LightGray
                )
            ) {
                // Skip fence markers (```) and only process content
                node.children.forEach { child ->
                    if (child.type == MarkdownTokenTypes.CODE_FENCE_CONTENT) {
                        child.accept(visitor)
                    }
                }
            }
        }
    }

    private fun createBlockQuoteProvider() = object : AnnotatedStringGeneratingProvider {
        private val quoteStyle = SpanStyle(color = Color.Gray, fontStyle = FontStyle.Italic)

        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            // Pre-compute which source line indices are real quoted lines (start with ">").
            // Lazy-continuation lines (no ">") are included in the BLOCK_QUOTE AST node by
            // CommonMark but should NOT receive quote styling.
            val quotedLines = computeQuotedLines(markdown, node)

            node.children.forEach { child ->
                if (child.type == MarkdownTokenTypes.BLOCK_QUOTE) return@forEach
                renderWithLineAwareStyle(visitor, markdown, child, quotedLines)
                if (isBlockElement(child.type)) {
                    visitor.builder.append("\n")
                }
            }
        }

        private fun computeQuotedLines(markdown: String, node: org.intellij.markdown.ast.ASTNode): Set<Int> {
            val startLine = lineNumberAt(markdown, node.startOffset)
            val endLine = lineNumberAt(markdown, node.endOffset)
            val sourceLines = markdown.lines()
            return (startLine..endLine.coerceAtMost(sourceLines.lastIndex))
                .filter { sourceLines[it].trimStart().startsWith(">") }
                .toSet()
        }

        private fun renderWithLineAwareStyle(
            visitor: AnnotatedStringRecursiveVisitor,
            markdown: String,
            node: org.intellij.markdown.ast.ASTNode,
            quotedLines: Set<Int>
        ) {
            val startLine = lineNumberAt(markdown, node.startOffset)
            val endLine = lineNumberAt(markdown, node.endOffset)
            val lineRange = startLine..endLine
            val allQuoted = lineRange.all { it in quotedLines }
            val noneQuoted = lineRange.none { it in quotedLines }

            when {
                allQuoted -> visitor.builder.withStyle(quoteStyle) { node.accept(visitor) }
                noneQuoted -> node.accept(visitor)
                node.children.isEmpty() -> splitLeafByLine(visitor, markdown, node, quotedLines)
                else -> node.children.forEach {
                    if (it.type == MarkdownTokenTypes.BLOCK_QUOTE) return@forEach
                    renderWithLineAwareStyle(visitor, markdown, it, quotedLines)
                }
            }
        }

        /** Splits a leaf node's source text by line, applying quote style only to quoted lines. */
        private fun splitLeafByLine(
            visitor: AnnotatedStringRecursiveVisitor,
            markdown: String,
            node: org.intellij.markdown.ast.ASTNode,
            quotedLines: Set<Int>
        ) {
            val startLine = lineNumberAt(markdown, node.startOffset)
            val lines = markdown.substring(node.startOffset, node.endOffset).split("\n")
            lines.forEachIndexed { index, line ->
                val lineNum = startLine + index
                if (lineNum in quotedLines) {
                    // Strip leading ">" marker (and optional space) before applying style
                    val content = line.replace(Regex("^\\s*>\\s?"), "")
                    visitor.builder.withStyle(quoteStyle) { visitor.builder.append(content) }
                } else {
                    visitor.builder.append(line)
                }
                if (index < lines.lastIndex) visitor.builder.append("\n")
            }
        }

        private fun lineNumberAt(markdown: String, offset: Int): Int =
            markdown.substring(0, offset).count { it == '\n' }
    }

    private fun createHorizontalRuleProvider() = object : AnnotatedStringGeneratingProvider {
        override fun processNode(visitor: AnnotatedStringRecursiveVisitor, markdown: String, node: org.intellij.markdown.ast.ASTNode) {
            visitor.builder.append("\n--- --- ---\n")
        }
    }
}