class XMLDocument(
    private val root: ParentEntity,
    private val version: Double = 1.0,
    private val encoding: String = "UTF-8"
) {
    fun getRoot(): ParentEntity {
        return root
    }

    fun getVersion(): Double {
        return version
    }

    fun getEncoding(): String {
        return encoding
    }

    fun getEntityCount(): Int {
        var i = 0
        accept {
            i++
            true
        }
        return i
    }

    fun accept(visitor: (XMLEntity) -> Boolean) {
        root.accept(visitor)
    }
}

abstract class XMLEntity (
    private var name: String
)
{
    private val attributes: MutableList<XMLAttribute> = mutableListOf()
    private var parent: ParentEntity? = null

    val depth: Int
        get() {
            return if (parent == null)
                1
            else
                1 + parent!!.depth
        }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    internal fun getParent() : ParentEntity? {
        return parent
    }

    internal fun setParent(parent: ParentEntity) {
        this.parent = parent
    }

    fun getParentCopy(): ParentEntity? {
        return parent?.copy()
    }

    internal fun getAttributes() : List<XMLAttribute> {
        return attributes
    }

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

    fun removeAttribute(attributeToRemove: XMLAttribute) {
        attributes.remove(attributeToRemove)
    }

    fun changeAttribute(attributeToChange: XMLAttribute, name: String? = null, value: String? = null) {
        attributes.forEach {
            if (it == attributeToChange) {
                if (name != null) it.setName(name)
                if (value != null) it.setValue(value)
            }
        }
    }

    open fun accept(visitor: (XMLEntity) -> Boolean) {
        visitor(this)
    }
}

data class ParentEntity(
    private var name: String
): XMLEntity(name) {
    private val children: MutableList<XMLEntity> = mutableListOf()

    internal fun getChildren(): List<XMLEntity> {
        return children
    }

    fun getChildrenCopy(): List<XMLEntity> {
        val list = mutableListOf<XMLEntity>()
        children.forEach {
            if (it is ParentEntity) list.add(it.copy())
            if (it is SimpleEntity) list.add(it.copy())
        }
        return list
    }

    fun addChild(entityToAdd: XMLEntity) {
        children.add(entityToAdd)
        entityToAdd.setParent(this)
    }

    fun removeChild(entityToRemove: XMLEntity) {
        children.remove(entityToRemove)
    }

    override fun accept(visitor: (XMLEntity) -> Boolean) {
        if(visitor(this)) {
            children.forEach {
                it.accept(visitor)
            }
        }
    }
}

data class SimpleEntity(
    private var name: String
): XMLEntity(name) {
    private var text: String = ""

    fun getText(): String {
        return text
    }

    fun setText(name: String) {
        this.text = name
    }
}

class XMLAttribute(
    private var name: String,
    private var value: String,
) {
    fun getName(): String {
        return name
    }

    fun setName(newName: String) {
        name = newName
    }

    fun getValue(): String {
        return value
    }

    fun setValue(newValue: String) {
        value = newValue
    }

}



















