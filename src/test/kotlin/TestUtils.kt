import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestUtils {

    val doc = Document(Entity("root"))
    val entity1 = Entity("entity1")
    val entity2 = Entity("entity2")
    val entity2test = Entity("entity2")
    val entity3 = Entity("entity3")
    val attribute1 = Attribute("attribute1", "1")
    val attribute2 = Attribute("attribute2", "2")

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
}