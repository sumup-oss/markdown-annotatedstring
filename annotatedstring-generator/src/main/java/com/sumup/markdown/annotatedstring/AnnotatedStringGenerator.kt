package com.sumup.markdown.annotatedstring

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.accept
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkdownParser

/**
 * Generates an [AnnotatedString] from a markdown AST tree.
 *
 * This class processes markdown content and converts it to Jetpack Compose's
 * [AnnotatedString] format, preserving formatting like bold, italic, links, etc.
 *
 * @param markdown The original markdown text
 * @param root The root node of the parsed markdown AST
 * @param providers Map of element types to their corresponding providers for custom rendering
 */
public class AnnotatedStringGenerator(
    private val markdown: String,
    private val root: ASTNode,
    private val providers: Map<IElementType, AnnotatedStringGeneratingProvider>,
) {
    /**
     * Creates an AnnotatedStringGenerator using a markdown flavour descriptor.
     *
     * @param markdown The original markdown text
     * @param root The root node of the parsed markdown AST
     * @param flavour The flavour descriptor that provides rendering providers
     */
    public constructor(
        markdown: String,
        root: ASTNode,
        flavour: MarkdownFlavourAnnotatedStringDescriptor
    ) : this(
        markdown,
        root,
        flavour.createAnnotatedStringGeneratingProviders(
            LinkMap.buildLinkMap(
                MarkdownParser(flavour).buildMarkdownTreeFromString(markdown),
                markdown
            ),
            null
        ),
    )

    /**
     * Generates an [AnnotatedString] from the markdown AST.
     *
     * @return The generated [AnnotatedString] with all formatting applied
     */
    public fun generateAnnotatedString(): AnnotatedString {
        return buildAnnotatedString {
            val visitor = AnnotatedStringRecursiveVisitor(markdown, this, providers)
            root.accept(visitor)
        }
    }
}