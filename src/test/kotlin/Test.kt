import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Test {

    val doc = XMLDocument("root")
    val entity1 = ParentEntity("entity1")
    val entity2 = SimpleEntity("entity2")
    val entity3 = SimpleEntity("entity3")
    val attribute1 = XMLAttribute("attribute1", "1")
    val attribute2 = XMLAttribute("attribute2", "2")

    init {
        doc.getRoot().addChild(entity1)
        entity1.addChild(entity2)
        doc.getRoot().addChild(entity3)
        entity1.addAttribute(attribute1)
        entity2.addAttribute(attribute2)
    }
    @Test
    fun testDepth() {
        assertEquals(1, doc.getRoot().depth)
        assertEquals(2, entity1.depth)
        assertEquals(3, entity2.depth)
        assertEquals(2, entity3.depth)
    }

    @Test
    fun testGetEntityCount() {
        assertEquals(4, doc.getEntityCount())
    }

    @Test
    fun testRemoveChild() {
        entity1.removeChild(entity2)
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<entity1 attribute1=\"1\"/>\n" +
                "\t<entity3/>\n" +
                "</root>\n", doc.toText())
    }

    @Test
    fun testRemoveAttribute() {
        entity1.removeAttribute(attribute2)
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<entity1 attribute1=\"1\">\n" +
                "\t\t<entity2 attribute2=\"2\"/>\n" +
                "\t</entity1>\n" +
                "\t<entity3/>\n" +
                "</root>\n",doc.toText())
    }

    @Test
    fun testChangeAttribute() {
        entity1.changeAttribute(attribute1, "testeName")
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<entity1 testeName=\"1\">\n" +
                "\t\t<entity2 attribute2=\"2\"/>\n" +
                "\t</entity1>\n" +
                "\t<entity3/>\n" +
                "</root>\n",doc.toText())

        entity1.changeAttribute(attribute1, value="testeValue")
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<entity1 testeName=\"testeValue\">\n" +
                "\t\t<entity2 attribute2=\"2\"/>\n" +
                "\t</entity1>\n" +
                "\t<entity3/>\n" +
                "</root>\n",doc.toText())

        entity1.changeAttribute(attribute1, "testChangeName", "testChangeValue")
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<entity1 testChangeName=\"testChangeValue\">\n" +
                "\t\t<entity2 attribute2=\"2\"/>\n" +
                "\t</entity1>\n" +
                "\t<entity3/>\n" +
                "</root>\n",doc.toText())

        val testRepeat = XMLAttribute("testRepeat","val")
        entity1.addAttribute(testRepeat)
        assertThrows<IllegalStateException> {
            entity1.changeAttribute(testRepeat, "testChangeName")
        }
    }

    @Test
    fun testGetParent() {
        assertEquals("entity1", entity2.getParent()!!.getName())
        assertEquals("root", entity1.getParent()!!.getName())
    }

    @Test
    fun testGetChildren() {
        val a = entity1.getChildren()
        val b = doc.getRoot().getChildren()

        val case1 = a.joinToString(",") { it.getName() }
        val case2 = b.joinToString(",") { it.getName() }

        assertEquals("entity2",case1)
        assertEquals("entity1,entity3",case2)
    }
}