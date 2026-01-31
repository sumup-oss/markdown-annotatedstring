package com.sumup.markdown.annotatedstring

import org.intellij.markdown.ast.ASTNode

/**
 * Provider interface for custom node processing.
 *
 * Implement this interface to provide custom rendering logic for specific
 * markdown element types. Each provider handles the conversion of a specific
 * AST node type to [AnnotatedString] content.
 */
public interface AnnotatedStringGeneratingProvider {
    /**
     * Processes an AST node and appends the result to the visitor's builder.
     *
     * @param visitor The recursive visitor that manages the AnnotatedString builder
     * @param markdown The original markdown text
     * @param node The AST node to process
     */
    public fun processNode(
        visitor: AnnotatedStringRecursiveVisitor,
        markdown: String,
        node: ASTNode
    )
}