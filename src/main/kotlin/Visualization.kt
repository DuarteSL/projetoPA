fun XMLDocument.toText(): String {
    return buildString {
        append("<?xml version=\"${getVersion()}\" encoding=\"${getEncoding()}\"?>\n")
        append(getRoot().toText())
    }
}

fun XMLEntity.toText(): String {
    return when(this) {
        is ParentEntity -> this.toText()
        is SimpleEntity -> this.toText()
        else -> error("There is no visualization defined for that type of entity")
    }
}

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

fun SimpleEntity.toText(): String {
    return buildString {
        if (getText().isEmpty()) {
            append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}/>\n")
        } else {
            append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}>${getText()}</${getName()}>\n")
        }
    }
}

fun XMLAttribute.toText(): String {
    return " ${this.getName()}=\"${this.getValue()}\""
}