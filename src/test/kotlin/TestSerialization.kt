import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class TestSerialization {

    @Test
    fun testDocumentToFile() {
        val document = XMLDocument("plano")
        val curso = SimpleEntity("curso")
        curso.setText("Mestrado em Engenharia Informática")
        document.getRoot().addChild(curso)
        val fuc1 = ParentEntity("fuc")
        fuc1.addAttribute(XMLAttribute("codigo", "M4310"))
        document.getRoot().addChild(fuc1)
        val nome1 = SimpleEntity("nome")
        nome1.setText("Programação Avançada")
        val ects1 = SimpleEntity("ects")
        ects1.setText("6.0")
        val avaliacao1 = ParentEntity("avaliacao")
        val componente1 = SimpleEntity("componente")
        val componente2 = SimpleEntity("componente")
        componente1.addAttribute(XMLAttribute("nome", "Quizzes"))
        componente1.addAttribute(XMLAttribute("peso", "20%"))
        componente2.addAttribute(XMLAttribute("nome", "Projeto"))
        componente2.addAttribute(XMLAttribute("peso", "80%"))
        fuc1.addChild(nome1)
        fuc1.addChild(ects1)
        avaliacao1.addChild(componente1)
        avaliacao1.addChild(componente2)
        fuc1.addChild(avaliacao1)

        val fuc2 = ParentEntity("fuc")
        fuc2.addAttribute(XMLAttribute("codigo", "03782"))
        document.getRoot().addChild(fuc2)
        val nome2 = SimpleEntity("nome")
        nome2.setText("Dissertação")
        val ects2 = SimpleEntity("ects")
        ects2.setText("42.0")
        val avaliacao2 = ParentEntity("avaliacao")
        val componente3 = SimpleEntity("componente")
        val componente4 = SimpleEntity("componente")
        val componente5 = SimpleEntity("componente")
        componente3.addAttribute(XMLAttribute("nome", "Dissertação"))
        componente3.addAttribute(XMLAttribute("peso", "60%"))
        componente4.addAttribute(XMLAttribute("nome", "Apresentação"))
        componente4.addAttribute(XMLAttribute("peso", "20%"))
        componente5.addAttribute(XMLAttribute("nome", "Discussão"))
        componente5.addAttribute(XMLAttribute("peso", "20%"))
        fuc2.addChild(nome2)
        fuc2.addChild(ects2)
        avaliacao2.addChild(componente3)
        avaliacao2.addChild(componente4)
        avaliacao2.addChild(componente5)
        fuc2.addChild(avaliacao2)

        val expectedContent = document.toText()

        val fileName = "example.xml"
        document.serializeToFile(fileName)

        val actualContent = File(fileName).readText()

        assertEquals(expectedContent, actualContent)
    }
}