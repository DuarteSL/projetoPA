import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestVisualization {

    @Test
    fun testAttributeToText() {
        val a = XMLAttribute("testattribute", "visualization")
        assertEquals(" testattribute=\"visualization\"", a.toText())
    }

    @Test
    fun testEntityToText() {
        val a = ParentEntity("entitytest")
        assertEquals("<entitytest/>\n", a.toText())

        val b = SimpleEntity("entitytest")
        assertEquals("<entitytest/>\n", b.toText())

        a.addAttribute(XMLAttribute("testattribute", "visualization"))
        assertEquals("<entitytest testattribute=\"visualization\"/>\n",a.toText())

        b.setText("testtext")
        b.addAttribute(XMLAttribute("testattribute", "visualization"))
        assertEquals("<entitytest testattribute=\"visualization\">testtext</entitytest>\n",b.toText())

        a.addChild(ParentEntity("childentitytest"))
        assertEquals("<entitytest testattribute=\"visualization\">\n" +
                "\t<childentitytest/>\n" +
                "</entitytest>\n", a.toText())
    }

    @Test
    fun testDocumentToText() {
        val a = XMLDocument(ParentEntity("root"))
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root/>\n",a.toText())

        val b = XMLDocument(ParentEntity("root"), 2.0, "UTF-16")
        assertEquals("<?xml version=\"2.0\" encoding=\"UTF-16\"?>\n" +
                "<root/>\n",b.toText())
    }


    @Test
    fun testFullExample() {
        val document = XMLDocument(ParentEntity("plano"))
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

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<plano>\n" +
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