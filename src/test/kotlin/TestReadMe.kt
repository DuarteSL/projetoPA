import org.junit.jupiter.api.Test

class TestReadMe {

    @XmlId("liga")
    @XmlAdapter(PropertiesOrder::class)
    class Liga(
        @XmlType("attribute")
        @XmlId("nome")
        val name: String,

        @XmlType("entity")
        @XmlId("ano")
        @XmlString(AnoParaEpoca::class)
        val year: Int,

        @XmlType("entity")
        val equipas: List<Equipa>
    )

    @XmlId("equipa")
    class Equipa(
        @XmlId("id")
        @XmlType("attribute")
        val teamid: Int,

        @XmlType("entity")
        val nome: String,

        @XmlType("entity")
        val nrjogadores: Int,

        @Exclude
        val treinador: String
    )

    @Test
    fun testReadMe() {
        val liga = Liga("Liga Portuguesa", 2024, listOf(
            Equipa(12,"Benfica",26, "JJ"),
            Equipa(21,"Sporting", 24, "RA")
        ))

        val ligaentity = objectToXMLInstance(liga)
        println(ligaentity.toText())
    }
}

class AnoParaEpoca : StringTransformer {
    override fun transform(value: String): String {
        val ano = value.toInt()
        val anoanterior = ano - 1
        return "$anoanterior-$ano"
    }
}

class PropertiesOrder : Adapter {
    override fun adapt(entity: ParentEntity): ParentEntity {
        val children = entity.getChildren()
        val sorted = children.sortedBy { getOrder(it) }
        children.removeAll { true }
        children.addAll(sorted)

        return entity
    }

    private fun getOrder(childEntity: XMLEntity): Int {
        val map = mapOf(
            "nome" to 1,
            "ano" to 2,
            "equipas" to 3,
        )
        return map[childEntity.getName()] ?: Int.MAX_VALUE
    }
}
