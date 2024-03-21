fun Document.addAttributes(entityName: String, attributeName: String, attributeValue: String) {
    accept {
        if (it.getName() == entityName) it.addAttribute(Attribute(attributeName, attributeValue))
        true
    }
}

fun Document.changeEntityName(entityName: String, newName: String) {
    accept {
        if (it.getName() == entityName) it.setName(newName)
        true
    }
}

fun Document.changeAttributeName(entityName: String, attributeName: String, newName: String) {
    accept {
        if (it.getName() == entityName) {
            it.getAttributes().forEach {a ->
                if(a.getName() == attributeName) a.setName(newName)
            }
        }
        true
    }
}

fun Document.removeEntity(entityName: String) {
    val list = mutableListOf<Pair<Entity,Entity>>()
    accept {
        if (it.getName() == entityName) list.add(Pair(it.getParent()!!,it))
        true
    }
    list.forEach {
        it.first.removeChild(it.second)
    }
}

fun Document.removeAttributeFromEntity(entityName: String, attributeName: String) {
    accept {
        if (it.getName() == entityName) {
            var attr: Attribute? = null
            it.getAttributes().forEach {a ->
                if (a.getName() == attributeName) attr = a
            }
            if(attr != null) it.removeAttribute(attr!!)
        }
        true
    }

}

