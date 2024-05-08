/**
 * Defines a sample XML document structure using a DSL (Domain-Specific Language).
 * This DSL allows for easy creation of XML documents with nested entities.
 *
 * Example Usage:
 *
 * val doc = document("root") {
 *     parententity("parent") {
 *         simpleentity("child1") addAttr XMLAttribute("attribute2", "value2")
 *         parententity("child2") {
 *             simpleentity("grandchild")
 *         }
 *     } addAttr XMLAttribute("attribute1", "value1")
 * }
 *
 *
 * @param name The name of the root element of the XML document.
 * @param build DSL lambda for building the structure of the XML document.
 * @return An instance of [XMLDocument] representing the created XML document.
 */
fun document(name: String, build: XMLDocument.() -> Unit): XMLDocument {
    return XMLDocument(name).apply { build(this) }
}

/**
 * Extends [XMLDocument] to add a parent entity within the document.
 *
 * @param name The name of the parent entity.
 * @param build DSL lambda for building the structure of the parent entity.
 * @return An instance of [ParentEntity] representing the created parent entity.
 */
fun XMLDocument.parententity(name: String, build: ParentEntity.() -> Unit): ParentEntity {
    val childParent = ParentEntity(name)
    this.getRoot().addChild(childParent)
    return childParent.apply { build(this) }
}

/**
 * Extends [ParentEntity] to add a child parent entity within another parent entity.
 *
 * @param name The name of the child parent entity.
 * @param build DSL lambda for building the structure of the child parent entity.
 * @return An instance of [ParentEntity] representing the created child parent entity.
 */
fun ParentEntity.parententity(name: String, build: ParentEntity.() -> Unit): ParentEntity {
    val childParent = ParentEntity(name)
    this.addChild(childParent)
    return childParent.apply { build(this) }
}

/**
 * Adds a simple entity as a child of the current parent entity.
 *
 * @param name The name of the simple entity.
 * @return An instance of [SimpleEntity] representing the created simple entity.
 */
fun ParentEntity.simpleentity(name: String): SimpleEntity {
    val childSimple = SimpleEntity(name)
    this.addChild(childSimple)
    return childSimple
}

/**
 * Adds an attribute to an XML entity.
 *
 * @param attr The XML attribute to add.
 * @return The modified XML entity.
 */
infix fun XMLEntity.addAttr(attr: XMLAttribute): XMLEntity {
    this.addAttribute(attr)
    return this
}