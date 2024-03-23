fun Document.toText(): String {
    return buildString {
        append("<?xml version=\"${getVersion()}\" encoding=\"${getEncoding()}\"?>\n")
        append(getRoot().toText())
    }
}

fun Entity.toText(): String {
    var s = buildString {
        if (getText().isEmpty() && getChildren().isEmpty()) {
            append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}/>\n")
        } else if (getText().isNotEmpty() && getChildren().isEmpty()) {
            append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}>${getText()}</${getName()}>\n")
        } else {
            append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}>\n")
            getChildren().forEach {
                append(it.toText())
            }
            append("${"\t".repeat(depth - 1)}</${getName()}>\n")
        }
    }
    return s
}

fun Attribute.toText(): String {
    return " ${this.getName()}=\"${this.getValue()}\""
}

fun XMLDocument.toText(): String {
    return buildString {
        append("<?xml version=\"${getVersion()}\" encoding=\"${getEncoding()}\"?>\n")
        append(getRoot().toText())
    }
}

fun XMLEntity.toText(): String {
    var s = StringBuilder()
    if (this is ParentEntity) {
        if (getChildren().isNotEmpty()) {
            s.append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}>\n")
            getChildren().forEach {
                s.append(it.toText())
            }
            s.append("${"\t".repeat(depth - 1)}</${getName()}>\n")
        }  else {
            s.append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}/>\n")
        }
    } else if (this is SimpleEntity) {
        if (getText().isEmpty()) {
            s.append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}/>\n")
        } else {
            s.append("${"\t".repeat(depth - 1)}<${getName()}${getAttributes().joinToString("") { it.toText() }}>${getText()}</${getName()}>\n")
        }
    }
    return s.toString()
}

fun XMLAttribute.toText(): String {
    return " ${this.getName()}=\"${this.getValue()}\""
}