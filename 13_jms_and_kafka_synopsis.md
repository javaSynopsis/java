Источники:
* общее о том как сервисы взаимодействуют и некоторые формулы расчета нагрузки [тут](https://dzone.com/articles/tens-of-thousands-of-socket-connections-in-java)
* общий состав элементов jms [тут](https://www.javacodegeeks.com/jms-tutorials)

# JMS
Источники: [тут](https://www.javacodegeeks.com/jms-tutorials)

Java Message Service (JMS) - это одно из MOM (message-oriented middleware API), для пересылки сообщений между двумя и более клиентами. Это реализация (implementation) для работы с producer–consumer problem. JMS часть Java EE. JMS это стандарт обмена сообщениями, который дает Java EE приложениям create, send, receive, and read messages. Позволяет обмен сообщениями между разными компонентами слабосвязно (loosely coupled), надежно (reliable), асинхронно.

Начиная с Java EE 1.4 JMS provider должен быть реализован всеми Java EE application servers (серверами приложений), реализованы через Java EE Connector Architecture.

**Elements**
* JMS provider - реализация JMS **или** реализация в виде адаптера (прослойки) если реализация не на Java (т.е. это ActiveMQ, RabbitMQ, Kafka etc)
* JMS client - приложение или процесс, которые делают publisher и/или subscriber
* JMS producer/publisher - клиент который отсылает сообщения (тут видимо имеется ввиду в том числе и просто функции-отправители в рамках одного приложения)
* JMS consumer/subscriber - клиент который получает сообщения (тут видимо имеется ввиду в том числе и просто функции-получатели в рамках одного приложения)
* JMS message - объект обертка вокруг отправляемых или получаемых данных, т.е. это сообщения с содержимым
* JMS queue - структура (коллекция) содержащая полученные сообщения и ожидающие обработки, очередь не гарантирует порядок сообщений в том же порядке в котором они были отправлены, она гарантирует только то что сообщение будет обработано только один раз
* JMS topic - аналогично queue, но сообщения может получить множество разных получателей

**Models**
* Point-to-point (one to one) - представлен через queue, сообщения обрабатывает только один получатель
  * сообщение может быть восстановлено (вернуто) в очередь
  * только один получатель может получить сообщение
  * сильная coupling между отправителем и получателем
  * сложность работы с ней ниже
* Publish-and-subscribe (one to many) - представлен через topic, сообщения обрабатывает множество получателей
  * слабая связанность (Decoupling)
  * сообщение не может быть восстановлено
  * сложность работы с ней выше

# Spring JMS
# kafka
# jms vs kafka
https://stackoverflow.com/questions/42664894/jms-vs-kafka-in-specific-conditions