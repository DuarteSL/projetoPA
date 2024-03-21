class Document(
    private val root: Entity
) {
    fun getRoot() : Entity {
        return root
    }

    fun accept(visitor: (Entity) -> Boolean) {
        root.accept(visitor)
    }
}

data class Entity(
    private var name: String,
) {
    private var text: String = ""
    private val children: MutableList<Entity> = mutableListOf()
    private val attributes: MutableList<Attribute> = mutableListOf()
    private var parent: Entity? = null

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

    fun getText(): String {
        return text
    }

    fun setText(name: String) {
        this.text = name
    }

    internal fun getParent() : Entity? {
        return parent
    }

    fun getParentCopy(): Entity? {
        return parent?.copy()
    }

    internal fun getChildren(): List<Entity> {
        return children
    }

    fun getChildrenCopy(): List<Entity> {
        val list = mutableListOf<Entity>()
        children.forEach {
            list.add(it.copy())
        }
        return list
    }

    fun addChild(entityToAdd: Entity) {
        children.add(entityToAdd)
        entityToAdd.parent = this
    }

    fun removeChild(entityToRemove: Entity) {
        children.remove(entityToRemove)
    }

    internal fun getAttributes() : List<Attribute> {
        return attributes
    }

    fun addAttribute(attributeToAdd: Attribute) {
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

    fun removeAttribute(attributeToRemove: Attribute) {
        attributes.remove(attributeToRemove)
    }

    fun changeAttribute(attributeToChange: Attribute, name: String? = null, value: String? = null) {
        attributes.forEach {
            if (it == attributeToChange) {
                if (name != null) it.setName(name)
                if (value != null) it.setValue(value)
            }
        }
    }

    fun accept(visitor: (Entity) -> Boolean) {
        if(visitor(this))
            children.forEach {
                it.accept(visitor)
            }
    }

}

class Attribute(
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

