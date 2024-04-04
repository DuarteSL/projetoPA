import java.io.File

/**
 * Serializes the XML document to a file. Overwrites if a file with the specified name already exists.
 *
 * @receiver [XMLDocument]
 * @param fileName The name of the file to which the XML document will be serialized.
 */
fun XMLDocument.serializeToFile(fileName: String) {
    val documentAsString = toText()
    File(fileName).writeText(documentAsString)
}