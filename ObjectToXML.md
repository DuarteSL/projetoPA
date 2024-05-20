# Funcionalidade objeto para XML
## Função objectToXMLInstance

A função `objectToXMLInstance` converte um objeto Kotlin na sua representação XML. Este processo envolve várias etapas e depende do uso correto das anotações e interfaces disponibilizadas:

1.  **Obter informações da classe**:
    - A função começa por obter a classe do objeto dado.
    - Verifica se há a anotação `XmlId` para determinar o nome em XML. Se não existir, usa o nome simples da classe.
2.  **Inicializar a entidade central**:
    - Uma entidade XML (`ParentEntity`) central é criada com o nome XML determinado.
3.  **Processar as propriedades da classe**:
    - A função itera sobre cada propriedade da classe.
    - As propriedades marcadas com a anotação `Exclude` são ignoradas.
4.  **Determinar a representação XML de cada propriedade**:
    - Para cada propriedade, a função verifica se há a anotação `XmlId` para determinar o nome em XML.
    - Obtêm o valor da propriedade e converte-o para string.
5.  **Aplicar a transformação sobre o valor**:
    - Se a propriedade tiver a anotação `XmlString`, o transformador da string especificado é usado para transformar o valor.
6.  **Verificar o Tipo XML de cada propriedade**:
    - A função verifica a anotação `XmlType` para determinar se a propriedade deve ser tratada como uma entidade ou como um atributo XML.
    - **Entidade**:  
    Em todos os casos, a entidade criada é adicionada como filha da entidade central.
        - Se a propriedade for uma coleção, é criada uma `ParentEntity`com o nome determinado no ponto 4 e, através de recursividade, cada elemento da coleção é convertido para XML e adicionado como filho da entidade criada. 
        - Se a propriedade for um tipo primitivo, é criada uma `SimpleEntity` sendo o seu texto o valor da propriedade (caso se aplique o ponto 5, este valor foi transformado).
        - Caso contrário, a própria propriedade é convertida numa instância XML através de recursividade.
    - **Atributo**:
        - A propriedade é adicionada como um `XMLAttribute`.
7.  **Aplicar o adaptador XML à entidade central**:
    - Se a classe tiver uma anotação `XmlAdapter`, o adaptador especificado é usado para adaptar a entidade XML central.
8.  **Devolver a representação da classe XML**:
    - A função devolve a entidade `ParentEntity` central que representa o objeto no formato XML.