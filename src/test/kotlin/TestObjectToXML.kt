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
    @XmlAdapter(FUCAdapter::class)
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

    @Test
    fun testObjectToXML() {
        val f = FUC("M4310", "Programação Avançada", 6.0, "la la...",
            listOf(
                ComponenteAvaliacao("Quizzes", 20),
                ComponenteAvaliacao("Projeto", 80)
            )
        )

        val a = objectToXMLInstance(f)
        assertEquals("<fuc codigo=\"M4310\">\n" +
                "\t<nome>Programação Avançada</nome>\n" +
                "\t<ects>6.0</ects>\n" +
                "\t<avaliacao>\n" +
                "\t\t<componente nome=\"Quizzes\" peso=\"20%\"/>\n" +
                "\t\t<componente nome=\"Projeto\" peso=\"80%\"/>\n" +
                "\t</avaliacao>\n" +
                "</fuc>\n", a.toText())
    }
}