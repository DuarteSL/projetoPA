fun Document.toText(): String {
    return getRoot().toText()
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