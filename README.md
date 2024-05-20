# Biblioteca de Manipulação de XML - Read Me
## Conteúdo
Este repositório contêm o código fonte e documentação para o projeto da unidade curricular *Programação Avançada*.  
Este documento apresenta as funcionalidades oferecidas pela biblioteca assim como exemplos e casos de uso.  
O objetivo desta biblioteca é permitir ao utilizador a criação e modificação de entidades XML e a geração do documento XML correspondente.

## Descrição das funcionalidades
### XMLDocument

-   **Construtor `XMLDocument`** - Inicializa um documento XML com o `rootName (String)` especificado como o nome da entidade `root`, `version` e `encoding`.
-   **Construtor segundário `XMLDocument`** - Inicializa um documento XML com a `rootEntity (ParentEntity)` especificada como a entidade `root`, `version` e `encoding`.
-   **`getRoot`** - Devolve a entidade raiz do documento XML.
-   **`getVersion`** - Devolve a versão do documento XML.
-   **`getEncoding`** - Devolve a codificação do documento XML.
-   **`getEntityCount`** - Devolve o número total de entidades no documento XML.
-   **`accept`** - Aceita uma função visitante para percorrer as entidades do documento XML.
-   **`serializeToFile`** - Serializa o documento XML para um ficheiro.
-   **`toText`** - Converte o documento XML para a sua representação em String.
-   **`addAttributes`** - Adiciona um atributo a todas as entidades XML com o nome especificado.
-   **`changeEntityName`** - Altera o nome de todas as entidades XML com o nome especificado para o outro nome especificado.
-   **`changeAttributeName`** - Altera o nome de um atributo de todas as entidades XML com o nome especificado para outro nome especificado.
-   **`removeEntity`** - Remove todas as entidades XML com o nome especificado.
-   **`removeAttributeFromEntity`** - Remove um atributo com o nome especificado de todas as entidades XML com o nome especificado.
-   **`findEntities`** - Encontra e devolve todas as entidades XML com o nome especificado.
-   **`filterByXPath`** - Filtra o documento por uma expressão XPath e devolve as entidades que correspondem. 

### XMLEntity

-   **`depth`** - Devolve a profundidade da entidade na hierarquia XML. 
-   **`getName`** - Devolve o nome da entidade.
-   **`setName`** - Define o nome da entidade.
-   **`getParent`** - Devolve a entidade pai da entidade.
-   **`getAttributes`** - Devolve a lista dos atributos da entidade.
-   **`addAttribute`** - Adiciona um atributo à entidade.
-   **`removeAttribute`** - Remove um atributo da entidade.
-   **`changeAttribute`** - Altera o nome ou valor de um atributo.
-   **`accept`** - Aceita uma função visitante para percorrer a hierarquia XML.
-   **`toText`** - Converte a entidade XML para a sua representação em String.

### ParentEntity (estende XMLEntity)

-   **Construtor `ParentEntity`** - Inicializa uma entidade pai XML com o `nome` especificado. Inicializa uma lista mutável vazia de entidades filhas.
-   **`getChildren`** - Devolve a lista das entidades filhas da entidade.
-   **`addChild`** - Adiciona uma entidade filha à lista de filhos da entidade.
-   **`removeChild`** - Remove uma entidade filha da lista de filhos da entidade.
-   **Sobreposição do operador `div`** - Devolve uma entidade `ParentEntity` filha pelo seu nome.
-   **Sobreposição do operador `get`** - Devolve uma entidade `SimpleEntity` filha pelo seu nome.
-   **`accept`** - Aceita uma função visitante para percorrer a hierarquia XML.

### SimpleEntity (estende XMLEntity)

-   **Construtor `SimpleEntity`** - Inicializa uma entidade XML simples com o `nome` especificado.
-   **`getText`** - Devolve o conteúdo de texto da entidade simples.
-   **`setText`** - Define o conteúdo de texto da entidade simples.

### XMLAttribute

-   **Construtor `XMLAttribute`** - Inicializa um atributo XML com o `nome` e o `valor` especificados.
-   **`getName`** - Devolve o nome do atributo.
-   **`setName`** - Define o nome do atributo.
-   **`getValue`** - Devolve o valor do atributo.
-   **`setValue`** - Define o valor do atributo.
-   **`toText`** - Converte o atributo XML para a sua representação em String.

### Objeto para XML

#### Anotações

-   **`XmlId`** - Anotação usada para anotar uma propriedade ou classe para definir o nome em XML.
-   **`XmlType`** - Anotação usada para anotar uma propriedade e assim identificar o tipo XML que esta deve ser convertida (entidade ou atributo).
-   **`Exclude`** - Anotação usada para anotar uma propriedade de maneira a excluir a mesma da conversão para XML.
-   **`XmlString`** - Anotação usada para anotar uma propriedade para especificar uma transformação de String personalizada ao converter a mesma para XML.
-   **`XmlAdapter`** - Anotação usada para anotar uma classe de maneira a aplicar um adaptador à `ParentEntity` que resulta da conversão do objeto, antes de esta ser devolvida.

#### Interfaces

-   **`StringTransformer`** - Contém uma função `transform` que transforma a string de entrada.
-   **`Adapter`** - Contém uma função `adapt` que adapta a `ParentEntity`.

#### Função

-   **`objectToXMLInstance`** - Converte um objeto para sua representação XML. ([mais informação](ObjectToXML.md))

### DSL

-   **`document`** - Cria uma estrutura de documento XML usando uma DSL.

#### XMLDocument

-   **`parententity`** - Adiciona uma `ParentEntity` filha à raiz do documento.
-   **`simpleentity`** - Adiciona uma `SimpleEntity` filha à raiz do documento.
-   **`addAttr`** - Adiciona um atributo XML à raiz do documento.

#### XMLEntity

-   **`addAttr`** - Adiciona um atributo XML a uma entidade XML.

#### ParentEntity

-   **`parententity`** - Adiciona uma `ParentEntity` filha dentro de outra entidade pai.
-   **`simpleentity`** - Adiciona uma `SimpleEntity` filha dentro de outra entidade simples.

#### SimpleEntity

-   **`text`** - Define o conteúdo de texto da entidade simples.

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
