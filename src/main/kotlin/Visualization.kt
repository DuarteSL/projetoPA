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