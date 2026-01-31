package com.sumup.markdown.annotatedstring

import androidx.compose.ui.text.AnnotatedString
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.acceptChildren
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.ast.visitors.RecursiveVisitor

/**
 * Recursive visitor for processing AST nodes and building an AnnotatedString.
 *
 * This visitor traverses the markdown AST and uses providers to convert each
 * node type to the appropriate [AnnotatedString] content. Extend this class
 * and override [visitNode] or [visitLeaf] for custom behavior.
 *
 * @param markdown The original markdown text
 * @param builder The [AnnotatedString.Builder] to append content to
 * @param providers Map of element types to their corresponding providers
 */
public open class AnnotatedStringRecursiveVisitor(
    protected val markdown: String,
    public val builder: AnnotatedString.Builder,
    protected val providers: Map<IElementType, AnnotatedStringGeneratingProvider> = emptyMap()
) : RecursiveVisitor() {

    override fun visitNode(node: ASTNode) {
        providers[node.type]?.processNode(this, markdown, node)
            ?: if (node.children.isEmpty()) {
                visitLeaf(node)
            } else {
                node.acceptChildren(this)
            }
    }

    /**
     * Visits a leaf node (a node with no children).
     *
     * @param node The leaf AST node to process
     */
    public open fun visitLeaf(node: ASTNode) {
        providers[node.type]?.processNode(this, markdown, node)
            ?: builder.append(node.getTextInNode(markdown).toString())
    }
}