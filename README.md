# SpringBoot Annotations AOP

Este projeto demonstra 
como utilizar a Programação Orientada a Aspectos (AOP) com Anota no Spring Boot para modularizar funcionalidades transversais, como logging, segurança, transações, entre outras. No exemplo específico deste projeto, focamos em como utilizar AOP para interceptar a execução de métodos e realizar ações adicionais, como enviar mensagens para uma fila RabbitMQ.

## Configuracao Inicial

> Importante: Ter Docker instalado para rodar os container e subir o projeto

Rode o comando docker-compose na pasta docker do projeto

```bash
$ cd docker
$ docker-compose up -d 
$ docker ps

## output
CONTAINER ID   IMAGE                      COMMAND                  CREATED      STATUS          PORTS                                                                                                         NAMES
0a7615d61fd5   postgres:15.2              "docker-entrypoint.s…"   2 days ago   Up 2 days       0.0.0.0:5432->5432/tcp                                                                                        postgres-dev
76d0773444de   adminer:4.8.1              "entrypoint.sh php -…"   2 days ago   Up 2 days       0.0.0.0:8090->8080/tcp                                                                                        adminer
144f188e52dd   rabbitmq:3.11-management   "docker-entrypoint.s…"   2 days ago   Up 2 days       4369/tcp, 5671/tcp, 0.0.0.0:5672->5672/tcp, 15671/tcp, 15691-15692/tcp, 25672/tcp, 0.0.0.0:15672->15672/tcp   rabbitmq-dev
```


## O Que é AOP?

AOP (Aspect-Oriented Programming) é um paradigma de programação que permite a separação de preocupações transversais (como logging, segurança, etc.) do código de negócio principal. Com AOP, é possível aplicar comportamentos comuns a múltiplas partes do sistema de forma centralizada e modular.

## Como Usar Aspectos no Spring Boot
### Dependências
Certifique-se de que o spring-boot-starter-aop está incluído em seu pom.xml:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>

```

## Principais Anotações Utilizadas no Projeto

### `@Aspect`

Esta anotação indica que uma classe é um Aspecto, ou seja, um módulo que encapsula uma preocupação transversal.

### `@Component`

Registra o Aspecto como um bean Spring, permitindo que ele seja gerenciado pelo container de inversão de controle (IoC) do Spring.

### `@AfterReturning`

Esta anotação é utilizada para definir um advice que será executado após a execução bem-sucedida de um método. No contexto deste projeto, ele é usado para enviar uma mensagem para o RabbitMQ após a execução de métodos anotados com `@NotifyMessage`.

### `@annotation`

Este é um Pointcut que indica que o advice deve ser aplicado a métodos anotados com uma anotação específica, como `@NotifyMessage` neste projeto.

## Como Criar Diferentes Tipos de Pointcuts

### `execution`

O Pointcut `execution` é usado para interceptar a execução de métodos específicos. Ele permite que você defina quais métodos, em quais classes e pacotes, devem ser interceptados com base em seu tipo de retorno, nome, parâmetros, entre outros.

### `within`

O Pointcut `within` é usado para restringir a aplicação do advice a métodos de classes dentro de um determinado pacote. Isso é útil quando você quer aplicar uma lógica transversal apenas em um conjunto específico de classes dentro de um pacote.

### `@annotation`

Este Pointcut é utilizado para interceptar métodos anotados com uma anotação específica. Isso permite que você aplique o advice apenas em métodos que possuem uma determinada anotação, como `@NotifyMessage` neste projeto.

## Benefícios de Usar AOP

- **Reutilização de Código**: Facilita a centralização de lógica comum, como logging e segurança, evitando duplicação de código.
- **Manutenção Simplificada**: Permite modificar comportamentos transversais em um único local, sem necessidade de alterar o código de negócio em múltiplos lugares.
- **Modularidade**: Separa preocupações transversais da lógica de negócio, resultando em um código mais limpo e fácil de entender.

## Expressões
A expressão execution(* com.example.service.*.*(..)) é um Pointcut no Spring AOP que define quais métodos serão interceptados. Vamos detalhar o que significa cada parte dessa expressão:

**execution:**

É o designator que indica que estamos definindo um Pointcut para a execução de métodos.
* (antes de com.example.service):

Esse asterisco representa o tipo de retorno dos métodos. O * significa "qualquer tipo de retorno". Pode ser substituído por tipos específicos como void, String, int, etc., caso queira limitar o Pointcut apenas a métodos que retornam um tipo específico.
com.example.service:

Especifica o pacote onde os métodos a serem interceptados estão localizados. Neste caso, refere-se ao pacote com.example.service.
* (depois de com.example.service):

Esse asterisco representa o nome da classe. O * indica "qualquer classe" dentro do pacote com.example.service. Por exemplo, pode ser UserService, OrderService, etc.
* (antes dos parênteses):

Esse asterisco representa o nome do método. Aqui, * significa "qualquer método" nas classes correspondentes. Pode ser getUser, findAll, save, etc.
(..):

Os dois pontos dentro dos parênteses indicam os parâmetros do método. Os dois pontos seguidos indicam "qualquer tipo e qualquer número de parâmetros". Se quisesse especificar métodos sem parâmetros, usaria (), ou poderia especificar tipos de parâmetros específicos, como (String, int) para métodos que aceitam um String e um int.
Resumindo a Expressão:
execution(* com.example.service.*.*(..)) captura a execução de qualquer método (*) em qualquer classe (*) dentro do pacote com.example.service, que pode ter qualquer tipo de retorno (*) e aceitar qualquer tipo e número de parâmetros ((..)).
Exemplo de Aplicação:
Se houver uma classe UserService no pacote com.example.service com métodos como getUser(Long id), findAll(), ou save(User user), a expressão execution(* com.example.service.*.*(..)) será aplicada a todos esses métodos, pois todos eles estão em uma classe dentro do pacote com.example.service, com qualquer nome de método e qualquer conjunto de parâmetros.


## Aspecto de Notificação de Mensagens (`NotifyMessageAspect`)

O aspecto `NotifyMessageAspect` é uma implementação de AOP (Aspect-Oriented Programming) no Spring Boot que intercepta a execução de métodos anotados com a anotação personalizada `@NotifyMessage`. Ele permite que a aplicação envie mensagens automaticamente para uma fila RabbitMQ após a conclusão bem-sucedida desses métodos.

### Visão Geral do Funcionamento

- **Objetivo**: Modularizar a lógica de envio de mensagens para uma fila de mensagens (RabbitMQ) sempre que determinados métodos são executados com sucesso, sem a necessidade de duplicar essa lógica em várias partes do código.

- **Tecnologias Utilizadas**:
    - **Spring AOP** para interceptar métodos.
    - **RabbitTemplate** para enviar mensagens para RabbitMQ.
    - **ObjectMapper** para serializar o objeto retornado pelo método interceptado em JSON.

### Estrutura e Explicação do Código

```java
@Log4j2
@Aspect
@Component
public class NotifyMessageAspect {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    public NotifyMessageAspect(RabbitTemplate rabbitTemplate, ObjectMapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    @AfterReturning(value = "@annotation(notifyMessage)", returning = "result")
    public void notifyMessaging (
            JoinPoint joinPoint,
            NotifyMessage notifyMessage,
            Object result) throws Throwable {

        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            log.info("===> Running: m=({}) q=({})", method.getName(), notifyMessage.queue());

            if(result instanceof Optional) {
                if(((Optional<?>) result).isPresent()) {
                    result = ((Optional<?>) result).get();
                }
            }

            if (notifyMessage.enabled()) {
                rabbitTemplate.convertAndSend(
                        notifyMessage.queue(),
                        mapper.writeValueAsString(result));
            }
        } catch (Exception e) {
            log.error("===> Error under notify event message: {}", e.getMessage());
        }
        log.info("===> Finished Process");
    }
}
```
#### Anotações Principais

- **`@Aspect`**: Indica que esta classe é um Aspecto no contexto da Programação Orientada a Aspectos (AOP).
- **`@Component`**: Registra o Aspecto como um bean Spring, permitindo que ele seja gerenciado pelo Spring IoC container.
- **`@Log4j2`**: Habilita o uso de logging com Log4j2.

#### Dependências

- **`RabbitTemplate`**: Utilizado para enviar mensagens para RabbitMQ.
- **`ObjectMapper`**: Usado para serializar objetos Java em JSON.

#### Método `notifyMessaging`

- **`@AfterReturning`**: Este advice é executado após a execução bem-sucedida de métodos anotados com `@NotifyMessage`. Ele captura o valor de retorno do método (`result`).
- **`@annotation(notifyMessage)`**: Define um Pointcut que intercepta métodos anotados com `@NotifyMessage`.
- **`JoinPoint`**: Representa o ponto de execução do método interceptado, permitindo acesso a informações sobre o método, como o nome.
- **`NotifyMessage notifyMessage`**: A anotação `@NotifyMessage` do método interceptado é passada para o advice, permitindo acessar seus atributos.
- **`Object result`**: Captura o valor de retorno do método interceptado.

#### Lógica de Envio de Mensagem

- Verifica se o resultado (`result`) do método é uma instância de `Optional` e, se for, extrai o valor presente.
- Verifica se a anotação `@NotifyMessage` tem o atributo `enabled` definido como `true` antes de enviar a mensagem.
- Serializa o resultado em JSON usando `ObjectMapper` e envia a mensagem para a fila RabbitMQ definida na anotação `@NotifyMessage`.

#### Logging

- Usa Log4j2 para registrar a execução do aspecto e quaisquer erros que ocorram durante o processo.

### Exemplo de Uso

Para usar este aspecto, basta anotar os métodos que devem acionar o envio de mensagens para o RabbitMQ com `@NotifyMessage`.

```java
@NotifyMessage(queue = "myQueue", enabled = true)
public MyResponseType myMethod() {
// Lógica do método
return new MyResponseType();
}
```
- **`queue`**: Define a fila para a qual a mensagem deve ser enviada.
- **`enabled`**: Se definido como `true`, ativa o envio da mensagem; se `false`, o envio é ignorado.

### Benefícios

- **Reutilização de Código**: Centraliza a lógica de envio de mensagens em um único local.
- **Facilidade de Manutenção**: Reduz a duplicação de código e facilita ajustes futuros na lógica de notificação.
- **Modularização**: Separa a lógica de negócio principal da lógica de infraestrutura, como o envio de mensagens, usando AOP.

### Conclusão

O `NotifyMessageAspect` demonstra o poder da Programação Orientada a Aspectos no Spring, permitindo que você gerencie de forma eficaz preocupações transversais, como a notificação de eventos, de maneira modular e reutilizável. Isso facilita a manutenção e evolução do código, ao mesmo tempo que mantém a lógica de negócio limpa e focada em seu propósito principal.
