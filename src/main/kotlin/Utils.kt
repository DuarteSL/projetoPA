fun XMLDocument.addAttributes(entityName: String, attributeName: String, attributeValue: String) {
    accept {
        if (it.getName() == entityName) it.addAttribute(XMLAttribute(attributeName, attributeValue))
        true
    }
}

fun XMLDocument.changeEntityName(entityName: String, newName: String) {
    accept {
        if (it.getName() == entityName) it.setName(newName)
        true
    }
}

fun XMLDocument.changeAttributeName(entityName: String, attributeName: String, newName: String) {
    accept {
        if (it.getName() == entityName) {
            it.getAttributes().forEach {a ->
                if(a.getName() == attributeName) a.setName(newName)
            }
        }
        true
    }
}

fun XMLDocument.removeEntity(entityName: String) {
    val list = mutableListOf<Pair<XMLEntity,XMLEntity>>()
    accept {
        if (it.getName() == entityName) list.add(Pair(it.getParent()!!, it))
        true
    }
    list.forEach {
        (it.first as ParentEntity).removeChild(it.second)
    }
}

fun XMLDocument.removeAttributeFromEntity(entityName: String, attributeName: String) {
    accept {
        if (it.getName() == entityName) {
            var attr: XMLAttribute? = null
            it.getAttributes().forEach {a ->
                if (a.getName() == attributeName) attr = a
            }
            if(attr != null) it.removeAttribute(attr!!)
        }
        true
    }
}

fun XMLDocument.findEntities(entityName: String): List<XMLEntity> {
    val list = mutableListOf<XMLEntity>()
    accept {
        if (it.getName() == entityName) list.add(it)
        true
    }
    return list
}

fun XMLDocument.filterByXPath(xpath: String): List<XMLEntity> {
    val xpathsplit = xpath.split("/").filter { it.isNotBlank() }
    var current = mutableListOf<XMLEntity>()
    current.addAll(findEntities(xpathsplit.first()))

    xpathsplit.drop(1).forEach { i ->
        var next = mutableListOf<XMLEntity>()
        current.forEach { entity ->
            if (entity is ParentEntity) next.addAll(entity.getChildren().filter { it.getName() == i })
        }
        current = next
    }

    return current
}

