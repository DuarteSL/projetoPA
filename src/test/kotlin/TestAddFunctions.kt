import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestAddFunctions {

    val doc = Document(Entity("root"))

    @Test
    fun testAddChild() {
        val a = Entity("Teste")
        doc.getRoot().addChild(a)
        assertTrue(doc.getRoot().getChildren().contains(a))
    }

    @Test
    fun testAddAttribute() {
        val a = Attribute("testeName", "testeValue")
        doc.getRoot().addAttribute(a)
        assertTrue(doc.getRoot().getAttributes().contains(a))
    }

    @Test
    fun testAddAttributeWithAnExistingName() {
        val a = Attribute("testeName", "testeValue1")
        val b = Attribute("testeName", "testeValue2")
        doc.getRoot().addAttribute(a)
        assertThrows<IllegalStateException> {
            doc.getRoot().addAttribute(b)
        }
        assertEquals(1, doc.getRoot().getAttributes().size)
        assertTrue(doc.getRoot().getAttributes().contains(a))
    }

    @Test
    fun test() {
        val document = Document(Entity("plano"))
        val curso = Entity("curso")
        curso.setText("Mestrado em Engenharia Informática")
        document.getRoot().addChild(curso)
        val fuc1 = Entity("fuc")
        fuc1.addAttribute(Attribute("codigo", "M4310"))
        document.getRoot().addChild(fuc1)
        val nome1 = Entity("nome")
        nome1.setText("Programação Avançada")
        val ects1 = Entity("ects")
        ects1.setText("6.0")
        val avaliacao1 = Entity("avaliacao")
        val componente1 = Entity("componente")
        val componente2 = Entity("componente")
        componente1.addAttribute(Attribute("nome", "Quizzes"))
        componente1.addAttribute(Attribute("peso", "20%"))
        componente2.addAttribute(Attribute("nome", "Projeto"))
        componente2.addAttribute(Attribute("peso", "80%"))
        fuc1.addChild(nome1)
        fuc1.addChild(ects1)
        avaliacao1.addChild(componente1)
        avaliacao1.addChild(componente2)
        fuc1.addChild(avaliacao1)

        val fuc2 = Entity("fuc")
        fuc2.addAttribute(Attribute("codigo", "03782"))
        document.getRoot().addChild(fuc2)
        val nome2 = Entity("nome")
        nome2.setText("Dissertação")
        val ects2 = Entity("ects")
        ects2.setText("42.0")
        val avaliacao2 = Entity("avaliacao")
        val componente3 = Entity("componente")
        val componente4 = Entity("componente")
        val componente5 = Entity("componente")
        componente3.addAttribute(Attribute("nome", "Dissertação"))
        componente3.addAttribute(Attribute("peso", "60%"))
        componente4.addAttribute(Attribute("nome", "Apresentação"))
        componente4.addAttribute(Attribute("peso", "20%"))
        componente5.addAttribute(Attribute("nome", "Discussão"))
        componente5.addAttribute(Attribute("peso", "20%"))
        fuc2.addChild(nome2)
        fuc2.addChild(ects2)
        avaliacao2.addChild(componente3)
        avaliacao2.addChild(componente4)
        avaliacao2.addChild(componente5)
        fuc2.addChild(avaliacao2)

        assertEquals("<plano>\n" +
                "\t<curso>Mestrado em Engenharia Informática</curso>\n" +
                "\t<fuc codigo=\"M4310\">\n" +
                "\t\t<nome>Programação Avançada</nome>\n" +
                "\t\t<ects>6.0</ects>\n" +
                "\t\t<avaliacao>\n" +
                "\t\t\t<componente nome=\"Quizzes\" peso=\"20%\"/>\n" +
                "\t\t\t<componente nome=\"Projeto\" peso=\"80%\"/>\n" +
                "\t\t</avaliacao>\n" +
                "\t</fuc>\n" +
                "\t<fuc codigo=\"03782\">\n" +
                "\t\t<nome>Dissertação</nome>\n" +
                "\t\t<ects>42.0</ects>\n" +
                "\t\t<avaliacao>\n" +
                "\t\t\t<componente nome=\"Dissertação\" peso=\"60%\"/>\n" +
                "\t\t\t<componente nome=\"Apresentação\" peso=\"20%\"/>\n" +
                "\t\t\t<componente nome=\"Discussão\" peso=\"20%\"/>\n" +
                "\t\t</avaliacao>\n" +
                "\t</fuc>\n" +
                "</plano>\n", document.toText())
    }
}