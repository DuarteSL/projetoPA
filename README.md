# Biblioteca de Manipulação de XML - Read Me
## Conteúdo
Este repositório contêm o código fonte e documentação para o projeto da unidade curricular *Programação Avançada*.  
Este documento apresenta as funcionalidades oferecidas pela biblioteca assim como exemplos e casos de uso.  
O objetivo desta biblioteca é permitir ao utilizador a criação e modificação de entidades XML e a geração do documento XML correspondente.

## Descrição das funcionalidades
Descrição das várias funcionalidades

## Tutorial
Nesta seccão disponibilizamos exemplos de diferentes maneiras de representar um documento XML.
### XML gerado pelos exemplos dos tutoriais
```xml
<?xml version="1.0" encoding="UTF-8"?>
<liga nome="Liga Portuguesa">
    <ano>2024</ano>
    <equipas>
        <equipa id="12">
            <nome>Benfica</nome>
            <nrjogadores>26</nrjogadores>
        </equipa>
        <equipa id="21">
            <nome>Sporting</nome>
            <nrjogadores>24</nrjogadores>
        </equipa>
    </equipas>
</liga>
```

### Tutorial simples
Representação do documento XML apresentado acima utilizando a instaciação das classes base:
```kotlin
val document = XMLDocument("liga")
document.getRoot().addAttribute(XMLAttribute("nome","Liga Portuguesa"))

val ano = SimpleEntity("ano")
ano.setText("2024")
document.getRoot().addChild(ano)

val equipas = ParentEntity("equipas")
document.getRoot().addChild(equipas)

val equipa1 = ParentEntity("equipa")
equipa1.addAttribute(XMLAttribute("id", "12"))
val nome1 = SimpleEntity("nome")
val nrjogadores1 = SimpleEntity("nrjogadores")
nome1.setText("Benfica")
nrjogadores1.setText("26")
equipa1.addChild(nome1)
equipa1.addChild(nrjogadores1)
equipas.addChild(equipa1)

val equipa2 = ParentEntity("equipa")
equipa2.addAttribute(XMLAttribute("id", "21"))
val nome2 = SimpleEntity("nome")
val nrjogadores2 = SimpleEntity("nrjogadores")
nome2.setText("Sporting")
nrjogadores2.setText("24")
equipa2.addChild(nome2)
equipa2.addChild(nrjogadores2)
equipas.addChild(equipa2)
```

### Tutorial anotações
Representação do documento XML apresentado acima utilizando anotações e a função *objectToXMLInstance*:

```kotlin
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
val liga = Liga("Liga Portuguesa", 2024, listOf(
    Equipa(12,"Benfica",26, "JJ"),
    Equipa(21,"Sporting", 24, "RA")
))
val ligaentity = objectToXMLInstance(liga)
val document = XMLDocument(ligaentity)
```

### Tutorial DSL
Representação do documento XML apresentado acima utilizando a DSL:

```kotlin
val doc = document("liga") {
    simpleentity("ano") text "2024"
    parententity("equipas") {
        parententity("equipa") {
            simpleentity("nome") text "Benfica"
            simpleentity("nrjogadores") text "26"
        } addAttr XMLAttribute("id","12")
        parententity("equipa") {
            simpleentity("nome") text "Sporting"
            simpleentity("nrjogadores") text "24"
        } addAttr XMLAttribute("id","21")
    }
} addAttr XMLAttribute("nome", "Liga Portuguesa")
```

## Autores
Diogo Peng - 99776 - MEI  
Duarte Laureano - 98824 - MEI  
