import java.io.File

fun XMLDocument.serializeToFile(fileName: String) {
    val documentAsString = toText()
    File(fileName).writeText(documentAsString)
}