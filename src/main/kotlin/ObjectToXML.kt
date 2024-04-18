import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType.Primitive

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class XmlId(val name: String)

@Target(AnnotationTarget.PROPERTY)
annotation class XmlType(val type: String)

@Target(AnnotationTarget.PROPERTY)
annotation class Exclude()

@Target(AnnotationTarget.PROPERTY)
annotation class XmlString(val stringTransformer : KClass<out Any>) // tornar numa interface StringTransformer

@Target(AnnotationTarget.CLASS)
annotation class XmlAdapter(val adapter : KClass<out Any>) // tornar numa interface adapter


fun objectToXMLInstance(obj: Any): XMLEntity {
    val clazz = obj::class

    val classXmlId = clazz.findAnnotation<XmlId>()?.name ?: (clazz.simpleName + "")

    val classEntity = ParentEntity(classXmlId)

    /*
    if (clazz.findAnnotation<XmlAdapter>() != null) {

    }
    */

    clazz.declaredMemberProperties.forEach { property ->
        if (!property.hasAnnotation<Exclude>()) {
            val propertyXmlId = property.findAnnotation<XmlId>()?.name ?: property.name

            val value = property.call(obj)

            /*
            if (clazz.findAnnotation<XmlString>() != null) {

            }
            */

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
                    s.setText(value.toString())
                    classEntity.addChild(s)

                } else {
                    classEntity.addChild(objectToXMLInstance(value!!))
                }


            } else if (type == "attribute") {
                val a = XMLAttribute(propertyXmlId, value.toString())
                classEntity.addAttribute(a)


            } else {
                println("xd")
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
