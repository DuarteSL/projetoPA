import java.io.File

/**
 * Serializes the XML document to a file. Overwrites if a file with the specified name already exists.
 *
 * @param fileName The name of the file to which the XML document will be serialized.
 */
fun XMLDocument.serializeToFile(fileName: String) {
    val documentAsString = toText()
    File(fileName).writeText(documentAsString)
}

/**
 * Converts the XML document to its String representation.
 *
 * @return The representation of the XML document as a [String].
 */
fun XMLDocument.toText(): String {
    return buildString {
        append("<?xml version=\"${getVersion()}\" encoding=\"${getEncoding()}\"?>\n")
        append(getRoot().toText())
    }
}

/**
 * Converts the XML entity to its String representation.
 *
 * @throws IllegalStateException in case there is no textual visualization defined for that type of [XMLEntity]
 * @return The representation of the XML entity as a [String].
 */
fun XMLEntity.toText(): String {
    return when(this) {
        is ParentEntity -> this.toText()
        is SimpleEntity -> this.toText()
        else -> error("There is no textual visualization defined for that type of entity")
    }
}

/**
 * Converts the parent entity to its String representation.
 *
 * @return The representation of the parent entity as a [String].
 */
fun ParentEntity.toText(): String {
    return buildString {
        if (getChildren().isNotEmpty()) {
            append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}>\n")
            getChildren().forEach {
                append(it.toText())
            }
            append("${"\t".repeat(depth - 1)}</${getName()}>\n")
        }  else {
            append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}/>\n")
        }
    }
}

/**
 * Converts the simple entity to its String representation.
 *
 * @return The representation of the simple entity as a [String].
 */
fun SimpleEntity.toText(): String {
    return buildString {
        if (getText().isEmpty()) {
            append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}/>\n")
        } else {
            append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}>${getText()}</${getName()}>\n")
        }
    }
}

/**
 * Converts the XML attribute to its String representation.
 *
 * @return The representation of the XML attribute as a [String].
 */
fun XMLAttribute.toText(): String {
    return " ${this.getName()}=\"${this.getValue()}\""
}
