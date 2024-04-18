import kotlin.reflect.*
import kotlin.reflect.full.*

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class XmlId(val name: String)

@Target(AnnotationTarget.PROPERTY)
annotation class XmlType(val type: String)

@Target(AnnotationTarget.PROPERTY)
annotation class Exclude

@Target(AnnotationTarget.PROPERTY)
annotation class XmlString(val stringTransformer : KClass<out StringTransformer>) // tornar numa interface StringTransformer

@Target(AnnotationTarget.CLASS)
annotation class XmlAdapter(val xmlAdapter : KClass<out Adapter>) // tornar numa interface adapter

fun objectToXMLInstance(obj: Any): XMLEntity {
    val clazz = obj::class

    val classXmlId = clazz.findAnnotation<XmlId>()?.name ?: (clazz.simpleName + "")
    var properties = clazz.declaredMemberProperties

    val classEntity = ParentEntity(classXmlId)

    /*
    var result: Any? = null

    if (clazz.hasAnnotation<XmlAdapter>()) {
        val xmlAdapter = clazz.findAnnotation<XmlAdapter>()!!.xmlAdapter
        val adapterInstance = xmlAdapter.objectInstance ?: xmlAdapter.createInstance()
        result = adapterInstance.adapt(clazz)
    }

    if (result is Collection<*>) {
        properties = result as Collection<KProperty1<out Any, *>>
    }
    */

    properties.forEach { property ->
        if (!property.hasAnnotation<Exclude>()) {
            val propertyXmlId = property.findAnnotation<XmlId>()?.name ?: property.name

            val value = property.call(obj)

            var transformedValue = value.toString()


            if (property.hasAnnotation<XmlString>()) {
                val stringTransformer = property.findAnnotation<XmlString>()!!.stringTransformer
                val transformerInstance = stringTransformer.objectInstance ?: stringTransformer.createInstance()
                transformedValue = transformerInstance.transform(value.toString())
            }


            val type = property.findAnnotation<XmlType>()?.type

            if (type == "entity") {
                if (value is Collection<*>) {
                    val p = ParentEntity(propertyXmlId)
                    value.forEach {
                        p.addChild(objectToXMLInstance(it!!))
                    }
                    classEntity.addChild(p)

                } else if (isPrimitive(value)) {
                    val s = SimpleEntity(propertyXmlId)
                    s.setText(transformedValue)
                    classEntity.addChild(s)

                } else {
                    classEntity.addChild(objectToXMLInstance(value!!))
                }


            } else if (type == "attribute") {
                val a = XMLAttribute(propertyXmlId, transformedValue)
                classEntity.addAttribute(a)


            } else {
                error("Use the annotation XmlType on all the class attributes with either \"attribute\" or \"entity\".")
            }

        }
    }


    return classEntity
}

fun isPrimitive(value: Any?): Boolean {
    return value is String ||
            value is Int ||
            value is Double ||
            value is Float ||
            value is Boolean ||
            value is Long ||
            value is Short ||
            value is Byte ||
            value is Char
}

interface StringTransformer {
    fun transform(value: String): String
}

class AddPercentage : StringTransformer {
    override fun transform(value: String): String {
        return "$value%"
    }
}

interface Adapter {
    fun adapt(clazz: KClass<*>): Any
}

class FUCAdapter : Adapter {
    override fun adapt(clazz: KClass<*>): Any {
        return clazz.declaredMemberProperties.sortedBy { getOrder(it) }
    }

    private fun getOrder(property: KProperty<*>): Int {
        val map = mapOf(
            "nome" to 1,
            "ects" to 2,
            "avaliacao" to 3,
        )
        return map[property.name] ?: Int.MAX_VALUE
    }
}
