package com.sumup.markdown.annotatedstring

import org.intellij.markdown.IElementType
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.html.URI
import org.intellij.markdown.parser.LinkMap

/**
 * Extension of [MarkdownFlavourDescriptor] that adds support for generating [AnnotatedString]
 * instead of HTML. This allows for rich text formatting in Compose UI.
 *
 * Implement this interface to create custom markdown flavours that can be rendered
 * as Jetpack Compose [AnnotatedString].
 */
public interface MarkdownFlavourAnnotatedStringDescriptor : MarkdownFlavourDescriptor {
    /**
     * Creates providers for generating AnnotatedString content from markdown AST nodes.
     * Similar to [createHtmlGeneratingProviders] but for AnnotatedString generation.
     *
     * @param linkMap Map of reference links found in the document
     * @param baseURI Base URI for resolving relative links (can be null)
     * @return Map of element types to their corresponding providers
     */
    public fun createAnnotatedStringGeneratingProviders(
        linkMap: LinkMap,
        baseURI: URI?
    ): Map<IElementType, AnnotatedStringGeneratingProvider>
}