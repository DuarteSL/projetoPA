/**
 * Add an attribute to all the XML entities with the specified name.
 *
 * @receiver [XMLDocument]
 * @param entityName The name of the XML entity to add attributes to.
 * @param attributeName The name of the attribute to add.
 * @param attributeValue The value of the attribute to add.
 * @throws IllegalStateException in case the given [attributeName] is an empty String or in case the given [attributeName] already exists in one of the XML entities.
 */
fun XMLDocument.addAttributes(entityName: String, attributeName: String, attributeValue: String) {
    accept {
        if (it.getName() == entityName) it.addAttribute(XMLAttribute(attributeName, attributeValue))
        true
    }
}

/**
 * Changes the name of all the XML entities with a specified name.
 *
 * @receiver [XMLDocument]
 * @param entityName The current name of the XML entity.
 * @param newName The new name for the XML entity.
 * @throws IllegalStateException in case the given [newName] is an empty String.
 */
fun XMLDocument.changeEntityName(entityName: String, newName: String) {
    accept {
        if (it.getName() == entityName) it.setName(newName)
        true
    }
}

/**
 * Change the name of an attribute of all the XML entities with a specified name.
 *
 * @receiver [XMLDocument]
 * @param entityName The name of the XML entity containing the attribute.
 * @param attributeName The current name of the attribute.
 * @param newName The new name for the attribute.
 * @throws IllegalStateException in case the given [newName] already exists in one of the XML entities.
 */
fun XMLDocument.changeAttributeName(entityName: String, attributeName: String, newName: String) {
    accept {
        if (it.getName() == entityName) {
            it.getAttributes().forEach { a ->
                if (a.getName() == attributeName) it.changeAttribute(a,newName)
            }
        }
        true
    }
}

/**
 * Removes all the XML entities with a specified name.
 *
 * @receiver [XMLDocument]
 * @param entityName The name of the XML entities to remove.
 */
fun XMLDocument.removeEntity(entityName: String) {
    val list = mutableListOf<Pair<XMLEntity, XMLEntity>>()
    accept {
        if (it.getName() == entityName) list.add(Pair(it.getParent()!!, it))
        true
    }
    list.forEach {
        (it.first as ParentEntity).removeChild(it.second)
    }
}

/**
 * Removes an attribute from all the XML entities with a specified name.
 *
 * @receiver [XMLDocument]
 * @param entityName The name of the XML entities containing the attribute to remove.
 * @param attributeName The name of the attribute to remove.
 */
fun XMLDocument.removeAttributeFromEntity(entityName: String, attributeName: String) {
    accept {
        if (it.getName() == entityName) {
            var attr: XMLAttribute? = null
            it.getAttributes().forEach { a ->
                if (a.getName() == attributeName) attr = a
            }
            if (attr != null) it.removeAttribute(attr!!)
        }
        true
    }
}

/**
 * Finds XML entities with a specified name.
 *
 * @receiver [XMLDocument]
 * @param entityName The name of the XML entities to find.
 * @return A list of XML entities with the specified name.
 */
fun XMLDocument.findEntities(entityName: String): List<XMLEntity> {
    val list = mutableListOf<XMLEntity>()
    accept {
        if (it.getName() == entityName) list.add(it)
        true
    }
    return list
}

/**
 * Filters XML entities by an XPath expression.
 *
 * @receiver [XMLDocument]
 * @param xpath The XPath expression to filter by.
 * @return A list of XML entities filtered by the given XPath expression.
 */
fun XMLDocument.filterByXPath(xpath: String): List<XMLEntity> {
    val xpathsplit = xpath.split("/").filter { it.isNotBlank() }
    var current = mutableListOf<XMLEntity>()
    current.addAll(findEntities(xpathsplit.first()))

    xpathsplit.drop(1).forEach { i ->
        val next = mutableListOf<XMLEntity>()
        current.forEach { entity ->
            if (entity is ParentEntity) next.addAll(entity.getChildren().filter { it.getName() == i })
        }
        current = next
    }

    return current
}
