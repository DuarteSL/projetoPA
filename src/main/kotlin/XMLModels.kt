/**
 * Represents an XML document.
 *
 * @property rootName The name of the root entity of the XML document.
 * @property version The version of the XML document. Default is 1.0.
 * @property encoding The encoding of the XML document. Default is "UTF-8".
 * @property root The root entity of the XML document.
 * @constructor Creates an XML document with the specified [version] and [encoding]. Defines the [root] of the XML document based of the specified [rootName].
 * @throws IllegalStateException in case the given [rootName] doesn't meet the requirementes for an XML entity name.
 */
class XMLDocument(
    private val rootName: String,
    private val version: Double = 1.0,
    private val encoding: String = "UTF-8"
) {

    private val root = ParentEntity(rootName)
    /**
     * Returns the root entity of the XML document.
     *
     * @return The root entity of the XML document.
     */
    fun getRoot(): ParentEntity {
        return root
    }

    /**
     * Returns the version of the XML document.
     *
     * @return The version of the XML document.
     */
    fun getVersion(): Double {
        return version
    }

    /**
     * Returns the encoding of the XML document.
     *
     * @return The encoding of the XML document.
     */
    fun getEncoding(): String {
        return encoding
    }

    /**
     * Returns the total number of entities in the XML document.
     *
     * @return The total number of entities in the XML document.
     */
    fun getEntityCount(): Int {
        var i = 0
        accept {
            i++
            true
        }
        return i
    }

    /**
     * Accepts a visitor function to traverse the entities of the XML document.
     *
     * @param visitor A function that takes an [XMLEntity] and returns a boolean indicating whether to continue visiting its children.
     */
    fun accept(visitor: (XMLEntity) -> Boolean) {
        root.accept(visitor)
    }
}

/**
 * Represents an XML entity.
 *
 * @property name The name of the XML entity.
 * @property attributes The list of attributes of the XML entity
 * @property parent The [ParentEntity] of this entity
 * @constructor Creates an XML entity with the specified [name].
 * @throws IllegalStateException in case the XML entity [name] doesn't meet the requirementes for an XML entity name.
 *
 */
abstract class XMLEntity(
    private var name: String
) {
    private val attributes: MutableList<XMLAttribute> = mutableListOf()
    private var parent: ParentEntity? = null

    init {
        validateName()
    }

    /**
     * Returns the depth of the entity in the XML hierarchy.
     *
     * @return The depth of the entity in the XML hierarchy.
     */
    val depth: Int
        get() {
            return if (parent == null)
                1
            else
                1 + parent!!.depth
        }

    /**
     * Returns the name of the entity.
     *
     * @return The name of the entity.
     */
    fun getName(): String {
        return name
    }

    /**
     * Sets the name of the entity.
     *
     * @param name The new name for the entity.
     * @throws IllegalStateException in case the given [name] is an empty String.
     */
    fun setName(name: String) {
        if (name.isEmpty()) {
            error("The entity name cannot be changed to empty")
        }
        this.name = name
    }

    private fun validateName() {
        val regex = Regex("^[A-Za-z_][A-Za-z0-9_.-]*$")
        if (!name.matches(regex))
            error("The provided entity name does meet the requirements for a XML Entity name. Expected regex pattern: ${regex.pattern}")
    }

    /**
     * Returns the parent entity of the current entity.
     *
     * @return The parent entity of the current entity.
     */
    fun getParent(): ParentEntity? {
        return parent
    }

    internal fun setParent(parent: ParentEntity?) {
        this.parent = parent
    }

    /**
     * Returns the attributes of the entity.
     *
     * @return The attributes of the entity.
     */
    fun getAttributes(): MutableList<XMLAttribute> {
        return attributes
    }

    /**
     * Adds an attribute to the entity.
     *
     * @throws IllegalStateException in case an [XMLAttribute] with the same name of the attribute to add already exists
     * @param attributeToAdd The attribute to add.
     */
    fun addAttribute(attributeToAdd: XMLAttribute) {
        if (attributes.isEmpty()) {
            attributes.add(attributeToAdd)
        } else {
            attributes.forEach {
                if (it.getName() == attributeToAdd.getName())
                    error("Attribute already exists, if you want to change it use the changeAttribute function")
            }
            attributes.add(attributeToAdd)
        }
    }

    /**
     * Removes an attribute from the entity.
     *
     * @param attributeToRemove The attribute to remove.
     * @return `true` if the attribute was successfully removed; `false` if it was not present in the [attributes] list
     */
    fun removeAttribute(attributeToRemove: XMLAttribute): Boolean {
        return attributes.remove(attributeToRemove)
    }

    /**
     * Changes the name or value of an attribute.
     *
     * @param attributeToChange The attribute to change.
     * @param name The new name for the attribute, if any.
     * @param value The new value for the attribute, if any.
     * @throws IllegalStateException in case it already exists an attribute with the provided [name]
     */
    fun changeAttribute(attributeToChange: XMLAttribute, name: String? = null, value: String? = null) {
        if (name != null) {
            if (attributes.joinToString { it.getName() }.contains(name)) error("It already exists an attriute with the name provided")
        }
        attributes.forEach {
            if (it == attributeToChange) {
                if (name != null) it.setName(name)
                if (value != null) it.setValue(value)
            }
        }
    }

    /**
     * Accepts a visitor function for traversing the XML hierarchy.
     *
     * @param visitor A function that takes an [XMLEntity] and returns a boolean indicating whether to continue visiting its children.
     */
    open fun accept(visitor: (XMLEntity) -> Boolean) {
        visitor(this)
    }
}

/**
 * Represents a parent entity in XML, which can have child entities.
 *
 * @property name The name of the parent entity.
 * @property children The list of children entities of the [ParentEntity]
 * @constructor Creates a parent entity with the specified [name].
 */
data class ParentEntity(
    private var name: String
) : XMLEntity(name) {
    private val children: MutableList<XMLEntity> = mutableListOf()

    /**
     * Returns the children entities of this parent entity.
     *
     * @return The children entities of this parent entity.
     */
    fun getChildren(): MutableList<XMLEntity> {
        return children
    }

    /**
     * Adds a child entity to this parent entity.
     *
     * @param entityToAdd The entity to add as a child.
     */
    fun addChild(entityToAdd: XMLEntity) {
        children.add(entityToAdd)
        entityToAdd.setParent(this)
    }

    /**
     * Removes a child entity from the parent entity, and sets it parent as `null`.
     *
     * @param entityToRemove The entity to remove from children.
     * @return `true` if the entity was successfully removed; `false` if it was not present in the [children] list
     */
    fun removeChild(entityToRemove: XMLEntity): Boolean {
        if (children.remove(entityToRemove)) {
            entityToRemove.setParent(null)
            return true
        }
        return false

    }

    /**
     * Retrieves a child entity by its name and returns it as a [ParentEntity].
     *
     * @param childName The name of the child entity to retrieve.
     * @return The child entity with the specified name, cast as a [ParentEntity]. If no child with the specified name is found, null is returned.
     */
    operator fun div(childName: String): ParentEntity =
        children.find { it.getName() == childName } as ParentEntity

    /**
     * Retrieves a child entity by its name and returns it as a [SimpleEntity].
     *
     * @param childName The name of the child entity to retrieve.
     * @return The child entity with the specified name, cast as a [SimpleEntity]. If no child with the specified name is found, null is returned.
     */
    operator fun get(childName: String): SimpleEntity =
        children.find { it.getName() == childName } as SimpleEntity


    /**
     * Accepts a visitor function for traversing the XML hierarchy.
     *
     * @param visitor A function that takes an [XMLEntity] and returns a boolean indicating whether to continue visiting its children.
     */
    override fun accept(visitor: (XMLEntity) -> Boolean) {
        if (visitor(this)) {
            children.forEach {
                it.accept(visitor)
            }
        }
    }
}

/**
 * Represents a simple XML entity that contains only text.
 *
 * @property name The name of the simple entity.
 * @property text The text content of the simple entity
 * @constructor Creates a simple entity with the specified [name].
 */
data class SimpleEntity(
    private var name: String
) : XMLEntity(name) {
    private var text: String = ""

    /**
     * Returns the text content of the simple entity.
     *
     * @return The text content of the simple entity.
     */
    fun getText(): String {
        return text
    }

    /**
     * Sets the text content of the simple entity.
     *
     * @param name The text to set.
     */
    fun setText(name: String) {
        this.text = name
    }
}

/**
 * Represents an attribute of an XML entity.
 *
 * @property name The name of the attribute.
 * @property value The value of the attribute.
 * @constructor Creates an XML attribute with the specified [name] and [value].
 * @throws IllegalStateException in case the given [name] is an empty String.
 */
class XMLAttribute(
    private var name: String,
    private var value: String,
) {
    init {
        if (name.isEmpty()) {
            error("The attribute name cannot be empty")
        }
    }
    /**
     * Returns the name of the attribute.
     *
     * @return The name of the attribute.
     */
    fun getName(): String {
        return name
    }

    /**
     * Sets the name of the attribute.
     *
     * @param name The new name for the attribute.
     * @throws IllegalStateException in case the given [name] is an empty String.
     */
    fun setName(name: String) {
        if (name.isEmpty()) {
            error("The attribute name cannot be changed to empty")
        }
        this.name = name
    }

    /**
     * Returns the value of the attribute.
     *
     * @return The value of the attribute.
     */
    fun getValue(): String {
        return value
    }

    /**
     * Sets the value of the attribute.
     *
     * @param value The new value for the attribute.
     */
    fun setValue(value: String) {
        this.value = value
    }

}
