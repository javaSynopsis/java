Источники:
* общее о том как сервисы взаимодействуют и некоторые формулы расчета нагрузки [тут](https://dzone.com/articles/tens-of-thousands-of-socket-connections-in-java)
* общий состав элементов jms [тут](https://www.javacodegeeks.com/jms-tutorials)

# JMS
Источники: [тут](https://www.javacodegeeks.com/jms-tutorials)

Java Message Service (JMS) - это одно из MOM (message-oriented middleware API), для пересылки сообщений между двумя и более клиентами. Это реализация (implementation) для работы с producer–consumer problem. JMS часть Java EE. JMS это стандарт обмена сообщениями, который дает Java EE приложениям create, send, receive, and read messages. Позволяет обмен сообщениями между разными компонентами слабосвязно (loosely coupled), надежно (reliable), асинхронно.

Начиная с Java EE 1.4 JMS provider должен быть реализован всеми Java EE application servers (серверами приложений), реализованы через Java EE Connector Architecture.

**Elements**
* **JMS provider** - реализация JMS **или** реализация в виде адаптера (прослойки) если реализация не на Java (т.е. это ActiveMQ, RabbitMQ, Kafka etc)
* **JMS client** - приложение или процесс, которые делают publisher и/или subscriber
* **JMS producer/publisher** - клиент который отсылает сообщения (тут видимо имеется ввиду в том числе и просто функции-отправители в рамках одного приложения)
* **JMS consumer/subscriber** - клиент который получает сообщения (тут видимо имеется ввиду в том числе и просто функции-получатели в рамках одного приложения)
* **JMS message** - объект обертка вокруг отправляемых или получаемых данных, т.е. это сообщения с содержимым
* **JMS queue** - структура (коллекция) содержащая полученные сообщения и ожидающие обработки, очередь не гарантирует порядок сообщений в том же порядке в котором они были отправлены, она гарантирует только то что сообщение будет обработано только один раз
* **JMS topic** - аналогично queue, но сообщения может получить множество разных получателей

**topic** еще могут называть **log**

**JMS broker** - это по сути JMS provider, точнее по сути приложения для управления queue или topic сообщения, можно сказать это процесс содержащий коллекции (topic, queue) сообщений. Если используется конвертер сообщений, то брокер может выполнять и функции конвертации при получении / отправке причем конвертацию можно описывать на разных спец. языках (как SQL например).

**Models**
* **Point-to-point** (queue, one to one) - представлен через queue, сообщения обрабатывает только один получатель
  * сообщение может быть восстановлено (вернуто) в очередь
  * только один получатель может получить сообщение
  * сильная coupling между отправителем и получателем
  * сложность работы с ней ниже
* **Publish-and-subscribe** (topic, one to many) - представлен через topic, сообщения обрабатывает множество получателей
  * слабая связанность (Decoupling)
  * сообщение не может быть восстановлено
  * сложность работы с ней выше

**Note.** Даже если listener для queue запущен в разных копиях одной и той же приложенки, то сообщение обработает только одна копия.

# Использование JMS
Сервера приложений Java EE используют одну из реализация JMS (JMS Provider). Чтобы использовать JMS в приложениях развернутых на этих серверах нужно создать через UI этого сервера или объявить в конфигурации этого сервера **Queue Factory** или **Topic Factory** с определенным именем. В самом приложении из JNDI (из контекста) по имени получить эту Factory, создать connection из нее, создать **session** из **connection**, создать объект **queue** или **topic** и связать с session. После этого можно отправлять или получать сообщения.
```java
InitialContext ctx=new InitialContext();
QueueConnectionFactory f=(QueueConnectionFactory)ctx.lookup("myQueueConnectionFactory");
QueueConnection con=f.createQueueConnection();
con.start();
QueueSession ses=con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
Queue t=(Queue)ctx.lookup("myQueue");

// в классе receiver
QueueReceiver receiver=ses.createReceiver(t); 
receiver.setMessageListener(new MessageListener {
     public void onMessage(Message m) {
     }
});

// в классе sender
QueueSender sender=ses.createSender(t);
TextMessage msg=ses.createTextMessage();
msg.setText("bla");
```

# Spring JMS
# kafka

**Плюсы kafka:**
* Decoupling of Data Streams.
* Real time instead of batch processing. (работа с сообщениями сразу вместо ожидания пока они накопятся, а потом проведение операций сразу с группой)


**Note.** kafka также как и JMS называют middle layer.

* **Broker** - сервер или несколько серверов (тогда между ними разделяется нагрузка). Если вы соеденены с одним Broker, то вы соеденены со всеми (со всем кластером, т.е. можно не думать о ручном соединении с каждым сервером и просто работать с ними как с одним). Если 1ин Broker падает, то данные **replicated** (будут получены) из другого.
* **Record**
* **Topic**
* **Partition** - каждый topic разбит на Partitions. Копии Partitions разбросаны по разным копиям Brokers (если Brokers несколько). В каждом Broker есть свой **leader** и **ISRs** (in sync replicas), т.е. главный и второстепенные Partitions. Записать в topic идет через **leader**.
* **Offset** - сообщение в Partition расположено со смещением (offset) относительно чего-то
* **Producer**
* **Consumer**
* **Consumer Group**
* **Zookeeper**

**Note.** Если данные записаны в Partition они не могут быть изменены (Immutability)

**Note.** Данные в Partition хранятся ограниченный срок (в настройках установлен limit по времени).

**Note.** **leader** это то (тот Broker) где лежит файл log (с сообщениями), остальные куда log копируется для надежности. log файл распределенный потому что лежит на нескольких носителях (копии log файла).

# kafka tool
# jms vs kafka
Источник: [тут](https://stackoverflow.com/questions/42664894/jms-vs-kafka-in-specific-conditions), [тут](https://stackoverflow.com/questions/30453882/is-apache-kafka-another-api-for-jms)

kafka отличается от jms (т.е. это не jms provider), kafka имеет меньше features (функционала), использует не jms протокол и она ориентирована на performances. Т.е. код jms и kafka отличается. kafka не использует pont-to-point, только Publish-and-subscribe. kafka лучше для scalability (разнесения по разным узлам) потому что она разработана как partitioned topic log (копии файлов с данными topic разнесены на разные сервера). kafka может разделять поток сообщений на группы. Поэтому kafka имеет лучший ACLs (access control). consumer решает какое сообщений потребить на основе offset (смещения в topic относительно чего-то), это снижает сложность написания producer.

