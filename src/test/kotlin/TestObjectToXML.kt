import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestObjectToXML {

    @XmlId("componente")
    class ComponenteAvaliacao(
        @XmlType("attribute")
        @XmlId("nome")
        val name: String,

        @XmlType("attribute")
        @XmlId("peso")
        @XmlString(AddPercentage::class)
        val weight: Int
    )

    @XmlId("fuc")
    class FUC(
        @XmlType("attribute")
        val codigo: String,
        @XmlType("entity")
        val nome: String,
        @XmlType("entity")
        val ects: Double,
        @Exclude
        val observacoes: String,
        @XmlType("entity")
        val avaliacao: List<ComponenteAvaliacao>
    )

    @XmlId("fuc")
    @XmlAdapter(FUCAdapter::class)
    class FUCAdapted(
        @XmlType("attribute")
        val codigo: String,
        @XmlType("entity")
        val nome: String,
        @XmlType("entity")
        val ects: Double,
        @Exclude
        val observacoes: String,
        @XmlType("entity")
        val avaliacao: List<ComponenteAvaliacao>
    )

    @Test
    fun testObjectToXML() {
        val f = FUC("M4310", "Programação Avançada", 6.0, "la la...",
            listOf(
                ComponenteAvaliacao("Quizzes", 20),
                ComponenteAvaliacao("Projeto", 80)
            )
        )

        val teste = objectToXMLInstance(f)
        assertEquals("<fuc codigo=\"M4310\">\n" +
                "\t<avaliacao>\n" +
                "\t\t<componente nome=\"Quizzes\" peso=\"20%\"/>\n" +
                "\t\t<componente nome=\"Projeto\" peso=\"80%\"/>\n" +
                "\t</avaliacao>\n" +
                "\t<ects>6.0</ects>\n" +
                "\t<nome>Programação Avançada</nome>\n" +
                "</fuc>\n", teste.toText())
    }

    @Test
    fun testObjectToXMLWithAdapter() {
        val f = FUCAdapted("M4310", "Programação Avançada", 6.0, "la la...",
            listOf(
                ComponenteAvaliacao("Quizzes", 20),
                ComponenteAvaliacao("Projeto", 80)
            )
        )

        val teste = objectToXMLInstance(f)
        assertEquals("<fuc codigo=\"M4310\">\n" +
                "\t<nome>Programação Avançada</nome>\n" +
                "\t<ects>6.0</ects>\n" +
                "\t<avaliacao>\n" +
                "\t\t<componente nome=\"Quizzes\" peso=\"20%\"/>\n" +
                "\t\t<componente nome=\"Projeto\" peso=\"80%\"/>\n" +
                "\t</avaliacao>\n" +
                "</fuc>\n", teste.toText())
    }
}

class AddPercentage : StringTransformer {
    override fun transform(value: String): String {
        return "$value%"
    }
}

class FUCAdapter : Adapter {
    override fun adapt(entity: ParentEntity): ParentEntity {
        val children = entity.getChildren()
        val sorted = children.sortedBy { getOrder(it) }
        children.removeAll { true }
        children.addAll(sorted)

        return entity
    }

    private fun getOrder(childEntity: XMLEntity): Int {
        val map = mapOf(
            "nome" to 1,
            "ects" to 2,
            "avaliacao" to 3,
        )
        return map[childEntity.getName()] ?: Int.MAX_VALUE
    }
}