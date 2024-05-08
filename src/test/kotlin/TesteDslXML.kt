import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TesteDslXML {

    @Test
    fun testEntityCreation() {
        val doc = document("root") {
            parententity("artists") {
                parententity("beatles") {
                    parententity("help") {
                        simpleentity("ineedyou")
                    }
                }
            }
        }
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<artists>\n" +
                "\t\t<beatles>\n" +
                "\t\t\t<help>\n" +
                "\t\t\t\t<ineedyou/>\n" +
                "\t\t\t</help>\n" +
                "\t\t</beatles>\n" +
                "\t</artists>\n" +
                "</root>\n", doc.toText())
    }

    @Test
    fun testAttributeCreation() {
        val doc = document("root") {
            parententity("artists") {
                simpleentity("test") addAttr XMLAttribute("name", "simple")
            } addAttr XMLAttribute("name", "parent")
        }
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<artists name=\"parent\">\n" +
                "\t\t<test name=\"simple\"/>\n" +
                "\t</artists>\n" +
                "</root>\n", doc.toText())
    }

    @Test
    fun testOperators() {
        val doc = document("root") {
            parententity("artists") {
                parententity("beatles") {
                    parententity("help") {
                        simpleentity("ineedyou")
                    }
                }
            }
        }
        val help = doc.getRoot() / "artists" / "beatles" / "help"
        assertEquals("help", help.getName())

        val ineedyou = (doc.getRoot() / "artists" / "beatles" / "help")["ineedyou"]
        assertEquals("ineedyou", ineedyou.getName())
    }
}