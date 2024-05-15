import kotlin.reflect.*
import kotlin.reflect.full.*

/**
 * Annotation used to mark a property or class as representing an XML identifier.
 * @property name The name of the XML identifier.
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class XmlId(val name: String)

/**
 * Annotation used to specify the XML type of property.
 * @property type The XML type of the property.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class XmlType(val type: String)

/**
 * Annotation used to exclude a property from being converted to XML.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class Exclude

/**
 * Annotation used to specify a custom string transformation for a property when converting to XML.
 * @property stringTransformer The string transformer class that implements [StringTransformer].
 */
@Target(AnnotationTarget.PROPERTY)
annotation class XmlString(val stringTransformer: KClass<out StringTransformer>)

/**
 * Annotation used to specify a custom XML adapter for the entire class.
 * @property xmlAdapter The XML adapter class that implements [Adapter].
 */
@Target(AnnotationTarget.CLASS)
annotation class XmlAdapter(val xmlAdapter: KClass<out Adapter>)

/**
 * Interface for defining a string transformation.
 */
interface StringTransformer {
    /**
     * Transforms the input string.
     * @param value The input string.
     * @return The transformed string.
     */
    fun transform(value: String): String
}

/**
 * Interface for defining an XML adapter.
 */
interface Adapter {
    /**
     * Adapts the XML entity.
     * @param entity The XML entity to be adapted.
     * @return The adapted XML entity.
     */
    fun adapt(entity: ParentEntity): ParentEntity
}

/**
 * Converts an object to its XML representation.
 * @param obj The object to convert.
 * @return The XML [ParentEntity] representing the object.
 * @throws IllegalStateException in case a property isn't either marked with the [Exclude] annotation or the type of [XmlType] is not "entity" or "attribute".
 */
fun objectToXMLInstance(obj: Any): ParentEntity {
    val clazz = obj::class

    val classXmlId = clazz.findAnnotation<XmlId>()?.name ?: (clazz.simpleName + "")

    var classEntity = ParentEntity(classXmlId)

    clazz.declaredMemberProperties.forEach { property ->
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

    if (clazz.hasAnnotation<XmlAdapter>()) {
        val xmlAdapter = clazz.findAnnotation<XmlAdapter>()!!.xmlAdapter
        val adapterInstance = xmlAdapter.objectInstance ?: xmlAdapter.createInstance()
        classEntity = adapterInstance.adapt(classEntity)
    }

    return classEntity
}

private fun isPrimitive(value: Any?): Boolean {
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