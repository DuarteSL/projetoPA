import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class TestAnnotations {

    @XmlId("test")
    class Teste(
        @XmlId("name")
        val nome : String,
        @XmlType("entity")
        @XmlString(AddPercentage::class)
        val numero : Int,
        @Exclude
        val aptidao : String
    )

    @Test
    fun testXmlIdAndXmlType() {
        val teste = Teste::class
        assertEquals("test",teste.findAnnotation<XmlId>()!!.name)

        teste.declaredMemberProperties.forEach {
            val xmlid = it.findAnnotation<XmlId>()?.name
            val xmltype = it.findAnnotation<XmlType>()?.type

            if (xmlid != null)
                assertEquals("name",xmlid)

            if (xmltype != null)
                assertEquals("entity",xmltype)
        }
    }

    @Test
    fun testXmlExclude() {
        val teste = Teste::class
        val totalproperties = teste.declaredMemberProperties.size

        var propertie = 0
        var excluded = 0
        teste.declaredMemberProperties.forEach {
            if (!it.hasAnnotation<Exclude>())
                propertie++
            else
                excluded++
        }
        assertEquals(1, excluded)
        assertEquals(propertie+excluded, totalproperties)

    }

    @Test
    fun testXmlString() {
        val teste = Teste::class
        val obj = Teste("teste",10,"lol")

        teste.declaredMemberProperties.forEach {
            if (it.hasAnnotation<XmlString>()) {
                val stringTransformer = it.findAnnotation<XmlString>()!!.stringTransformer
                val transformerInstance = stringTransformer.objectInstance ?: stringTransformer.createInstance()
                val xmlString = transformerInstance.transform(it.call(obj).toString())
                assertEquals("10%",xmlString)
            }
        }
    }
}