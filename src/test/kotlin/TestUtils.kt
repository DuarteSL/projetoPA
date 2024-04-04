import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.swing.text.html.parser.Entity

class TestUtils {

    val doc = XMLDocument("root")
    val entity1 = ParentEntity("entity1")
    val entity2 = SimpleEntity("entity2")
    val entity2test = SimpleEntity("entity2")
    val entity3 = SimpleEntity("entity3")
    val attribute1 = XMLAttribute("attribute1", "1")
    val attribute2 = XMLAttribute("attribute2", "2")

    init {
        doc.getRoot().addChild(entity1)
        entity1.addChild(entity2)
        doc.getRoot().addChild(entity3)
        entity1.addAttribute(attribute1)
        entity2.addAttribute(attribute2)
        doc.getRoot().addChild(entity2test)
        entity3.setText("teste")
    }

    @Test
    fun testAddAttributes() {
        doc.addAttributes("entity2", "newAttribute", "testeValue")
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<entity1 attribute1=\"1\">\n" +
                "\t\t<entity2 attribute2=\"2\" newAttribute=\"testeValue\"/>\n" +
                "\t</entity1>\n" +
                "\t<entity3>teste</entity3>\n" +
                "\t<entity2 newAttribute=\"testeValue\"/>\n" +
                "</root>\n",doc.toText())
    }

    @Test
    fun testChangeEntityName() {
        doc.changeEntityName("entity2", "entityteste")
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<entity1 attribute1=\"1\">\n" +
                "\t\t<entityteste attribute2=\"2\"/>\n" +
                "\t</entity1>\n" +
                "\t<entity3>teste</entity3>\n" +
                "\t<entityteste/>\n" +
                "</root>\n",doc.toText())
    }

    @Test
    fun testChangeAttributeName() {
        doc.changeAttributeName("entity2", "attribute2", "newAttribute")
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<entity1 attribute1=\"1\">\n" +
                "\t\t<entity2 newAttribute=\"2\"/>\n" +
                "\t</entity1>\n" +
                "\t<entity3>teste</entity3>\n" +
                "\t<entity2/>\n" +
                "</root>\n",doc.toText())
    }

    @Test
    fun testRemoveEntity() {
        doc.removeEntity("entity2")
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<entity1 attribute1=\"1\"/>\n" +
                "\t<entity3>teste</entity3>\n" +
                "</root>\n",doc.toText())
    }

    @Test
    fun testRemoveAttribute() {
        doc.removeAttributeFromEntity("entity2", "attribute2")
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<entity1 attribute1=\"1\">\n" +
                "\t\t<entity2/>\n" +
                "\t</entity1>\n" +
                "\t<entity3>teste</entity3>\n" +
                "\t<entity2/>\n" +
                "</root>\n",doc.toText())
    }

    @Test
    fun testFindEntities() {
        val a = doc.findEntities("entity2")
        val b = doc.findEntities("entity3")
        val c = doc.findEntities("root")

        assertEquals(listOf(entity2,entity2test),a)
        assertEquals(listOf(entity3),b)
        assertEquals(listOf(doc.getRoot()),c)
    }

    @Test
    fun testFilterByXPath() {
        val pa = ParentEntity("entity1")
        val si = SimpleEntity("entity2")

        doc.getRoot().addChild(pa)
        pa.addChild(si)

        val xpath1 = "root/entity1/entity2"
        val xpath2 = "root/entity3"
        val xpath3 = "entity2"
        val xpath4 = "entity1/entity2"
        val xpath5 = "testempty"

        val a = doc.filterByXPath(xpath1)
        val b = doc.filterByXPath(xpath2)
        val c = doc.filterByXPath(xpath3)
        val d = doc.filterByXPath(xpath4)
        val e = doc.filterByXPath(xpath5)

        assertEquals(listOf(entity2,si), a)
        assertEquals(listOf(entity3), b)
        assertEquals(listOf(entity2, entity2test, si), c)
        assertEquals(listOf(entity2, si), d)
        assertEquals(emptyList<XMLEntity>(), e)
    }
}