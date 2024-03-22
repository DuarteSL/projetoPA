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
}