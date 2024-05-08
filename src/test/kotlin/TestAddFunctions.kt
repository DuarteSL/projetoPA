import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestAddFunctions {

    val doc = XMLDocument("root")

    @Test
    fun testValidName() {
        ParentEntity("testvalid1")
        ParentEntity("_testvalid2")
        XMLDocument("root")
        assertThrows<IllegalStateException> {
            XMLDocument("1")
            ParentEntity("")
        }
    }

    @Test
    fun testAddChild() {
        val a = ParentEntity("Teste")
        doc.getRoot().addChild(a)
        assertTrue(doc.getRoot().getChildren().contains(a))
    }

    @Test
    fun testAddAttribute() {
        val a = XMLAttribute("testeName", "testeValue")
        doc.getRoot().addAttribute(a)
        assertTrue(doc.getRoot().getAttributes().contains(a))
    }

    @Test
    fun testAddAttributeWithAnExistingName() {
        val a = XMLAttribute("testeName", "testeValue1")
        val b = XMLAttribute("testeName", "testeValue2")
        doc.getRoot().addAttribute(a)
        assertThrows<IllegalStateException> {
            doc.getRoot().addAttribute(b)
        }
        assertEquals(1, doc.getRoot().getAttributes().size)
        assertTrue(doc.getRoot().getAttributes().contains(a))
    }
}