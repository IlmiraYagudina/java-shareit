# Проект `ShareIt`
### Проект, созданный в рамках изучения Spring Data JPA, Docker, Hibernate, Mock и Spring Boot тестирования
## Идея проекта
Идея заключается в создании приложения для регистрации пользователей и возможности создания заявок на временное пользование вещами, которые могут добавлять пользователи. 
В проекте также предусмотрена возможность оставлять заявки на использование тех или иных вещей. Я использую две базы данных: PostgreSQL (основная) и H2 (для тестирования работоспособности приложения).

## Развёртывание проекта

## Стек проекта
Spring, PostgreSQL, Hibernate, H2, SLF4J, Lombok, Spring Data JPA, Docker и Mock.

## Документация проекта
<details>
 <summary> Часть 1 </summary>

В этом модуле я создаю сервис для шеринга (от англ. share — «делиться») вещей. Шеринг как экономика совместного использования набирает всё большую популярность. Если в 2014 году глобальный рынок шеринга оценивался всего в $15 млрд, то к 2025 может достигнуть $335 млрд.

Почему шеринг так популярен? Представьте, что на воскресной ярмарке я купил несколько картин и хочу повесить их дома. Но вот незадача — для этого нужна дрель, а её у меня нет. Я могу пойти в магазин и купить, но в такой покупке мало смысла — после того, как я повешу картины, дрель будет просто пылиться в шкафу. Я могу пригласить мастера — но за его услуги придётся заплатить. И тут я вспоминаю, что видел дрель у друга. Сама собой напрашивается идея — одолжить её.

Большая удача, что у меня оказался друг с дрелью, и я сразу вспомнил про него! А не то в поисках инструмента пришлось бы писать всем друзьям и знакомым. Или вернуться к первым двум вариантам — покупке дрели или найму мастера. Насколько было бы удобнее, если бы под рукой был сервис, где пользователи делятся вещами! Созданием такого проекта я и занимаюсь.

## Что умеет сервис
Мой проект называется `ShareIt`. Он должен обеспечить пользователям возможность рассказывать, какими вещами они готовы поделиться, а также находить нужную вещь и брать её в аренду на какое-то время.

Сервис не только позволяет бронировать вещь на определённые даты, но и закрывать к ней доступ на время бронирования от других желающих. Если нужной вещи на сервисе нет, пользователи могут возможность оставлять запросы. Вдруг древний граммофон, который странно даже предлагать к аренде, неожиданно понадобится для театральной постановки. По запросу можно будет добавлять новые вещи для шеринга.

## Каркас приложения
В этом спринте от меня требуется создать каркас приложения, а также разработать часть его веб-слоя. Основная сущность сервиса, вокруг которой будет строиться вся дальнейшая работа, — вещь. В коде она будет фигурировать как `Item`. Пользователь, который добавляет в приложение новую вещь, будет считаться её владельцем. При добавлении вещи есть возможность указать её краткое название и добавить небольшое описание. Например, название может быть — «Дрель “Салют”», а описание — «Мощность 600 вт, работает ударный режим, так что бетон возьмёт». Также у вещи обязательно должен быть статус — доступна ли она для аренды. Статус должен проставлять владелец.

Для поиска вещей должен быть организован поиск. Чтобы воспользоваться нужной вещью, её требуется забронировать. Бронирование, или Booking — ещё одна важная сущность приложения. Бронируется вещь всегда на определённые даты. Владелец вещи обязательно должен подтвердить бронирование.

После того как вещь возвращена, у пользователя, который её арендовал, должна быть возможность оставить отзыв. В отзыве можно поблагодарить владельца вещи и подтвердить, что задача выполнена — дрель успешно справилась с бетоном, и картины повешены.

Ещё одна сущность, которая мне понадобится, — запрос вещи ItemRequest. Пользователь создаёт запрос, если нужная ему вещь не найдена при поиске. В запросе указывается, что именно он ищет. В ответ на запрос другие пользователи могут добавить нужную вещь.

Также готов шаблон проекта с использованием `Spring Boot`. Я создала ветку `add-controllers` и переключился на неё — в этой ветке будет вестись вся разработка для 1го спринта.


## Реализация модели данных
В этом модуле я использую структуру не по типам классов, а по фичам (англ. Feature layout) — весь код для работы с определённой сущностью в одном пакете. Поэтому я сразу создала четыре пакета — `item`, `booking`, `request` и `user`. В каждом из этих пакетов будут свои контроллеры, сервисы, репозитории и другие классы, которые мне понадобятся в ходе разработки. В пакете `item` я создала класс `Item` .

## Создание DTO-объектов и мапперов
Созданные объекты Item и User я буду использовать для работы с базой данных (это ждёт меня в следующем спринте). Сейчас, помимо них, мне также понадобятся объекты, которые я буду возвращать пользователям через REST-интерфейс в ответ на их запросы.

Разделять объекты, которые хранятся в базе данных и которые возвращаются пользователям, — хорошая практика. Например, я могу не захотеть показывать пользователям владельца вещи (поле owner), а вместо этого возвращать только информацию о том, сколько раз вещь была в аренде. Чтобы это реализовать, нужно создать отдельную версию каждого класса, с которой будут работать пользователи — DTO (Data Transfer Object).

Кроме DTO-классов, понадобятся Mapper-классы — они помогут преобразовывать объекты модели в DTO-объекты и обратно. Для базовых сущностей Item и User я создам Mapper-класс и метод преобразования объекта модели в DTO-объект.

## Разработка контроллеров
Когда классы для хранения данных будут готовы, я могу перейти к реализации логики. В моем приложении будет три классических слоя — контроллеры, сервисы и репозитории. В этом спринте я буду работать преимущественно с контроллерами.

Для начала мне нужно научить приложение работать с пользователями. Ранее я уже создавал контроллеры для управления пользователями — создания, редактирования и просмотра. Теперь я сделаю то же самое: создам класс UserController и реализую методы для основных CRUD-операций. Также я реализую сохранение данных о пользователях в памяти.

Далее я перейду к основной функциональности этого спринта — работе с вещами. Мне нужно реализовать добавление новых вещей, их редактирование, просмотр списка вещей и поиск. Я создам класс ItemController, в котором будет сосредоточен весь REST-интерфейс для работы с вещами.

Вот основные сценарии, которые я должен поддерживать в приложении:

*Добавление новой вещи. Это будет происходить по эндпойнту POST /items. На вход будет поступать объект ItemDto. userId в заголовке X-Sharer-User-Id — это идентификатор пользователя, который добавляет вещь. Именно этот пользователь будет владельцем вещи. Идентификатор владельца будет поступать на вход в каждом из запросов.

*Редактирование вещи. Эндпойнт PATCH /items/{itemId}. Я смогу изменить название, описание и статус доступа к аренде, и редактировать вещь сможет только её владелец.

*Просмотр информации о конкретной вещи по её идентификатору. Эндпойнт GET /items/{itemId}. Информацию о вещи сможет просмотреть любой пользователь.

*Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой. Эндпойнт GET /items.

Поиск вещи потенциальным арендатором. Пользователь будет передавать в строке запроса текст, и система будет искать вещи, содержащие этот текст в названии или описании. Это будет происходить по эндпойнту /items/search?text={text}. В text я передам текст для поиска и проверю, что поиск возвращает только доступные для аренды вещи.

Для каждого из этих сценариев я создам соответствующий метод в контроллере. Также я создам интерфейс ItemService и реализующий его класс ItemServiceImpl, к которому будет обращаться мой контроллер. В качестве DAO я создам реализации, которые будут хранить данные в памяти приложения. Работу с базой данных я реализую в следующем спринте.


## Тестирование
Для проверки кода я подготовил [Postman-коллекцию](https://github.com/yandex-praktikum/java-shareit/blob/add-controllers/postman/sprint.json).. С её помощью я смогу протестировать API и убедиться, что все запросы успешно выполняются.


## Дополнительное
Если задание покажется недостаточно подробным, я могу обратиться к дополнительным материалам, которые помогут мне выполнить задание спринта. Но я помню, что реальные технические задания часто скупы на подробности, поэтому разработчику приходится самостоятельно принимать некоторые архитектурные решения. Чем раньше я научусь определять минимальные требования, необходимые для начала разработки проекта, тем проще мне будет работать в команде над реальным проектом:
[Дополнительные советы ментора](https://code.s3.yandex.net/Java/4mod1sprProject/mentors_advice_1.2.pdf).

</details>

<details>
 <summary> Часть 2 </summary>

В прошлом спринте вы приступили к проекту `ShareIt` и уже сделали немало — например,
реализовали слой контроллеров для работы с вещами. В этот раз вы продолжите совершенствовать сервис, так что он станет
по-настоящему полезным для пользователей.

Перед вами две большие задачи: добавить работу с базой данных в уже реализованную часть проекта,
а также дать пользователям возможность бронировать вещи.

## Немного подготовки
В этом спринте разработка будет вестись в ветке `add-bookings`. Создайте ветку с таким названием и переключитесь на неё.
Далее переходите к настройке базы данных. Пришло время использовать Hibernate и JPA самостоятельно. Для начала добавьте
зависимость `spring-boot-starter-data-jpa` и драйвер `postgresql` в файл `pom.xml`.

## Создание базы данных
Теперь поработайте над структурой базы данных. В ней будет по одной таблице для каждой из основных сущностей, а также
таблица, где будут храниться отзывы.

Подумайте, какой тип данных PostgreSQL лучше подойдёт для каждого поля. В качестве подсказки проанализируйте таблицы,
которые были использованы в приложении `Later`.

Напишите SQL-код для создания всех таблиц и сохраните его в файле `resources/schema.sql` — Spring Boot выполнит
содержащийся в нём скрипт на старте проекта. На данный момент вам достаточно создать таблицы для двух сущностей,
которые вы уже разработали — `Item` и `User`.

Важный момент: приложение будет запускаться много раз, и каждый раз Spring будет выполнять `schema.sql`. Чтобы ничего не
сломать и не вызвать ошибок, все конструкции в этом файле должны поддерживать множественное выполнение. Это значит, что
для создания таблиц следует использовать не просто конструкцию `CREATE TABLE`, но `CREATE TABLE IF NOT EXIST` — тогда
таблица будет создана, только если её ещё не существует в базе данных.

### Подсказка: пример кода для создания таблицы users
```
CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
); 
```
## Настройка JPA
Подготовка сущности к работе с базой данных. Для этого использую аннотации JPA:
`@Entity`, `@Table`, `@Column`, `@Id`. Для поля `status` в классе `Booking` - `@Enumerated`. Добавьте
соответствующие аннотации для сущностей.

Создайте репозитории для `User` и `Item` и доработайте сервисы, чтобы они работали с новыми репозиториями.

### Маппинг между столбцами БД и моделью данных
Если название поля в модели отличается от имени поля в базе, ууказаваю маппинг между ними с
помощью аннотации `@Column`.

## Реализация функции бронирования
Чтобы сделать приложение ещё более полезным и интересным, добалена возможность брать вещи в аренду на определённые даты.

Вот основные сценарии и эндпоинты:

* Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем, а затем подтверждён владельцем вещи.
Эндпоинт — `POST /bookings`. После создания запрос находится в статусе `WAITING` — «ожидает подтверждения».
* Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи. Затем статус
бронирования становится либо `APPROVED`, либо `REJECTED`. Эндпоинт — `PATCH /bookings/{bookingId}?approved={approved}`,
параметр `approved` может принимать значения `true` или `false`.
* Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором бронирования, либо
владельцем вещи, к которой относится бронирование. Эндпоинт — `GET /bookings/{bookingId}`.
* Получение списка всех бронирований текущего пользователя. Эндпоинт — `GET /bookings?state={state}`. Параметр state
необязательный и по умолчанию равен `ALL` (англ. «все»). Также он может принимать значения `CURRENT` (англ. «текущие»),
`**PAST**` (англ. «завершённые»), `FUTURE` (англ. «будущие»), `WAITING` (англ. «ожидающие подтверждения»),
`REJECTED` (англ. «отклонённые»). Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
* Получение списка бронирований для всех вещей текущего пользователя. Эндпоинт — `GET /bookings/owner?state={state}`.
Этот запрос имеет смысл для владельца хотя бы одной вещи. Работа параметра `state` аналогична его работе в предыдущем сценарии.

Для начала добавлена в модель данных сущность `Booking` и код для создания соответствующей таблицы в файл `resources/schema.sql`.

Создан контроллер `BookingController` и методы для каждого из описанных сценариев. 

Кроме контроллеров,  реализовано хранение данных — то есть сервисы и репозитории.

### Изменения в DTO
Например, полезно создать отдельное перечисление для возможных методов параметра `state`, ведь задачи этого
перечисления могут отличаться в слое представления (параметр для поиска) и в модели данных (состояние бронирования).

## Добавление дат бронирования при просмотре вещей
Добавила, чтобы владелец видел даты
последнего и ближайшего следующего бронирования для каждой вещи, когда просматривает список (`GET /items`).

## Добавление отзывов
Пользователи смогут оставлять отзывы на вещь после того, как взяли её в аренду. 

В базе данных уже есть таблица `comments`. Создан соответствующий класс модели данных `Comment` и добавлены
необходимые аннотации JPA. Поскольку отзыв — вспомогательная сущность и по сути часть вещи, отдельный пакет для отзывов
не нужен. Помещен класс в пакет `item`.

Комментарий можно добавить по эндпоинту `POST /items/{itemId}/comment`, в контроллере создан метод для него.

Реализовала логику по добавлению нового комментария к вещи в сервисе `ItemServiceImpl`. Для этого создала
интерфейс `CommentRepository`. Добавила проверку, что пользователь, который пишет комментарий, действительно
брал вещь в аренду.

Разрешила пользователям просматривать комментарии других пользователей. Отзывы можно будет увидеть по двум
эндпоинтам — по `GET /items/{itemId}` для одной конкретной вещи и по `GET /items` для всех вещей данного пользователя.

## Тестирование
Для проверки всей функциональности в этом спринте использовала
[Postman-коллекцию](https://github.com/yandex-praktikum/java-shareit/blob/add-bookings/postman/sprint.json) —
 для тестирования приложения.

## Дополнительно

[Подсказки](https://code.s3.yandex.net/Java/14sprint/MentorsAdvice_05_04_23v4.pdf).
</details>

<details>
 <summary> Часть 3 </summary>

*Добавила возможность
создавать запрос вещи и добавлять вещи в ответ на запросы других пользователей. 
*Реализовать тесты для всего приложения.

## Добавлен запрос вещи

В этом спринте разработка велась в ветке `add-item-requests`. Функциональность:
запроса на добавление вещи. Её суть в следующем.

Пользователь можен создать такой запрос, когда не может найти нужную вещь, воспользовавшись поиском, но при этом надеется,
что у кого-то она всё же имеется. Другие пользователи могут просматривать подобные запросы и, если у них есть
описанная вещь и они готовы предоставить её в аренду, добавлять нужную вещь в ответ на запрос.

Поэтому добавлено четыре новых эндпоинта:
* `POST /requests` — добавить новый запрос вещи. Основная часть запроса — текст запроса, где пользователь
описывает, какая именно вещь ему нужна.
* `GET /requests` — получить список своих запросов вместе с данными об ответах на них. Для каждого запроса должны
указываться описание, дата и время создания и список ответов в формате: `id` вещи, название, её описание
`description`, а также `requestId` запроса и признак доступности вещи `available`. Так в дальнейшем, используя указанные
`id` вещей, можно будет получить подробную информацию о каждой вещи. Запросы должны возвращаться в отсортированном
порядке от более новых к более старым.
* `GET /requests/all?from={from}&size={size}` — получить список запросов, созданных другими пользователями.
С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
Запросы сортируются по дате создания: от более новых к более старым. Результаты должны возвращаться постранично.
Для этого нужно передать два параметра: `from` — индекс первого элемента, начиная с 0, и `size` — количество
элементов для отображения.
* `GET /requests/{requestId}` — получить данные об одном конкретном запросе вместе с данными об ответах на него в том же
формате, что и в эндпоинте `GET /requests`. Посмотреть данные об отдельном запросе может любой пользователь.

## Добавляю опцию ответа на запрос
Добавлена ещё одна  полезная опция в приложение, чтобы пользователи могли отвечать на запросы друг друга. Для этого при
создании вещи есть возможность указать `id` запроса, в ответ на который создаётся нужная вещь.

Так же добавлено поле `requestId` в тело запроса `POST /items`. Обратите внимание, там сохраниться возможность добавить
вещь и без указания `requestId`.

## Добавлена пагинацию к существующим эндпоинтам
Использована в запросе `GET /requests/all` пагинация,
поскольку запросов может быть очень много.

Если пользователи жалуются, что запросы возвращают слишком много данных и с ними невозможно работать. Эта проблема
возникает при просмотре бронирований и особенно при просмотре вещей. Поэтому, чтобы приложение было комфортным
для пользователей, а также быстро работало, добавлена пагинация в эндпоинты `GET /items`,
`GET /items/search`, `GET /bookings` и `GET /bookings/owner`.

Параметры такие же, как и для эндпоинта на получение запросов вещей: номер первой записи и желаемое количество
элементов для отображения.

## Добавляем тесты

И наконец, ещё одна очень важная задача этого спринта — написать тесты для приложения `ShareIt`. Не оставляйте эту задачу
на конец работы. Делайте всё постепенно: перед тем как реализовать какую-либо часть задания, сформулируйте функциональные
и нефункциональные требования к ней. В соответствии с этими требованиями напишите реализацию, после этого напишите
юнит-тесты, проверяющие реализацию на соответствие требованиям.

После того как будут написаны тесты для новой функциональности, описанной в этом техзадании, перейдите к написанию
тестов к тому, что было реализовано в предыдущих спринтах. В реальной практике программисты пишут тесты параллельно с
новым кодом. Так каждая функция, которую они разрабатывают, изначально покрывается тестами.

При написании тестов вам предстоит решить несколько задач:
* Реализовать юнит-тесты для всего кода, содержащего логику. Выберите те классы, которые содержат в себе нетривиальные
методы, условия и ветвления. В основном это будут классы сервисов. Напишите юнит-тесты на все такие методы, используя
моки при необходимости.
* Реализовать интеграционные тесты, проверяющие взаимодействие с базой данных. Как вы помните, интеграционные тесты
представляют собой более высокий уровень тестирования: их обычно требуется меньше, но покрытие каждого — больше.
Мы предлагаем вам создать по одному интеграционному тесту для каждого крупного метода в ваших сервисах.
Например, для метода `getUserItems` в классе `ItemServiceImpl`.
* Реализовать тесты для REST-эндпоинтов вашего приложения с использованием `MockMVC`. Вам нужно покрыть тестами все
существующие эндпоинты. При этом для слоя сервисов используйте моки.
* Реализовать тесты для слоя репозиториев вашего приложения с использованием аннотации `@DataJpaTest`. Есть смысл
написать тесты для тех репозиториев, которые содержат кастомные запросы. Работа с аннотацией `@DataJpaTest` не
рассматривалась подробно в уроке, поэтому вам предстоит изучить пример самостоятельно,
перейдя по [ссылке](https://howtodoinjava.com/spring-boot2/testing/datajpatest-annotation/). Ещё больше деталей вы
сможете найти в приложенном [файле с советами ментора](https://code.s3.yandex.net/Java/4mod1sprProject/mentors_advice_3.0.pdf).
* Реализовать тесты для работы с JSON для DTO в вашем приложении с помощью аннотации `@JsonTest`. Такие тесты имеют смысл
в тех случаях, когда ваши DTO содержат в себе некоторую логику. Например, описание формата дат или валидацию. Выберите
DTO, где есть подобные условия, и напишите тесты.

Для проверки реализованной вами функциональности мы подготовили для вас
[postman-тесты](https://github.com/yandex-praktikum/java-shareit/blob/add-item-requests/postman/sprint.json).

</details>

<details>
 <summary> Техническое задание. Часть 4 </summary>

Ваше приложение для шеринга вещей почти готово! В нём уже реализована вся нужная функциональность — осталось добавить
несколько технических усовершенствований.

## Ставим проблему
Пользователей приложения `ShareIt` становится больше. Вы рады этому, но замечаете, что не всё идёт гладко: приложение
работает медленнее, пользователи чаще жалуются, что их запросы подолгу остаются без ответа.

После небольшого самостоятельного исследования вы начинаете понимать, в чём дело. Пользователи учатся программировать —
совсем так же, как и вы! Некоторые из них теперь используют ваше приложение через другие программы: собственноручно
написанные интерфейсы, боты… Чего они только не придумали!

Не все эти программы работают правильно. В `ShareIt` поступает много некорректных запросов — например, с невалидными
входными данными, в неверном формате или просто дублей. Ваше приложение тратит ресурсы на обработку каждого из запросов,
и в результате его работа замедляется. Пришло время разобраться с этим!

## Ищем решение
В реальной разработке для решения подобных проблем часто применяется микросервисная архитектура — с ней вы познакомились
в этом модуле. Можно вынести часть приложения, с которой непосредственно работают пользователи, в отдельное небольшое
приложение и назвать его, допустим, `**gateway**` (англ. «шлюз»). В нём будет выполняться вся валидация запросов —
некорректные будут исключаться.

Поскольку для этой части работы не требуется базы данных и каких-то особых ресурсов, приложение `gateway` будет
легковесным. При необходимости его получится легко масштабировать. Например, вместо одного экземпляра `gateway` можно
запустить целых три — чтобы справиться с потоком запросов от пользователей.


После валидации в `gateway` запрос будет отправлен основному приложению, которое делает всю реальную работу — в том числе
обращается к базе данных. Также на стороне gateway может быть реализовано кэширование: например, если один и тот же
запрос придёт два раза подряд, `gateway` будет самостоятельно возвращать предыдущий ответ без обращения к основному
приложению.

## Формулируем задачу
Вся работа в этом спринте будет вестись в ветке `add-docker`. Вот ваши задачи:
* Разбить приложение `ShareIt` на два — `shareIt-server` и `shareIt-gateway`. Они будут общаться друг с другом через REST.
Вынести в `shareIt-gateway` всю логику валидации входных данных — кроме той, которая требует работы с БД.
* Настроить запуск `ShareIt` через Docker. Приложения `shareIt-server`, `shareIt-gateway` и база данных PostgreSQL должны
* запускаться в отдельном Docker-контейнере каждый. Их взаимодействие должно быть настроено через Docker Compose.

Приложение `shareIt-server` будет содержать всю основную логику и почти полностью повторять приложение, с которым вы
работали ранее, — за исключением того, что можно будет убрать валидацию данных в контроллерах.

Во второе приложение `shareIt-gateway` нужно вынести контроллеры, с которыми непосредственно работают пользователи, —
вместе с валидацией входных данных.

Каждое из приложений будет запускаться как самостоятельное Java-приложение, а их общение будет происходить через REST.
Чтобы сделать запуск и взаимодействие приложений более предсказуемым и удобным, разместите каждое из них в своём
Docker-контейнере. Также не забудьте вынести в Docker-контейнер базу данных.

## Ещё несколько технических моментов
Вам нужно разбить одно приложение `ShareIt` на два так, чтобы оба остались в том же репозитории и собирались одной
Maven-командой. Реализовать подобный механизм в Maven помогают многомодульные проекты (англ. multi-module project).
Такие проекты содержат в себе несколько более мелких подпроектов.

В нашем случае каждый из подпроектов будет представлять собой самостоятельное Java-приложение. Вообще же подпроект
может содержать любой набор кода или других сущностей, которые собираются с помощью Maven. Это может быть, например,
набор статических ресурсов — HTML-файлы, изображения и так далее.

Многомодульный проект содержит один родительский `pom`-файл для всего проекта, в котором перечисляются все модули или
подпроекты. Также для каждого из модулей создается собственный `pom`-файл со всей информацией о сборке отдельного
модуля. Когда в корневой директории проекта запустится команда сборки (например, `mvn clean install`), Maven соберёт
каждый из модулей и положит результирующий `jar`-файл в директорию `target` соответствующего модуля.

💡 Подробнее о том, как работать с многомодульными проектами, вы можете узнать
[из этого ресурса](https://spring.io/guides/gs/multi-module/).

Мы уже подготовили для вас шаблон многомодульного проекта — ищите его в ветке
[add-docker](https://github.com/yandex-praktikum/java-shareit/tree/add-docker). Всё, что остаётся сделать, —
распределить код вашего приложения между модулями, а также добавить в `shareIt-gateway` код для обращения к
`shareIt-server` через REST.

Чтобы вам было проще работать с REST, мы создали в `shareIt-gateway` класс `BaseClient`, который реализует базовый
механизм взаимодействия через REST. Вы можете использовать и дорабатывать этот класс по своему усмотрению. Ещё больше
деталей для работы с REST-вызовами вы найдёте [по ссылке](https://www.baeldung.com/rest-template), а также в
«[Дополнительных советах ментора](https://code.s3.yandex.net/Java/4mod17sprProject/Mentors_advice_shareIt_part4_12.08.22.pdf)».

Подготовьте `Dockerfile` для каждого из сервисов — `shareIt-server` и `shareIt-gateway`. Шаблон для этих файлов расположен
в корневой папке каждого модуля, его содержимое будет таким же, как и в теме про Docker. Затем опишите настройки
развёртывания контейнеров в файле `docker-compose.yaml` в корне проекта. Конфигурация развёртывания должна включать три
контейнера для следующих сервисов: `shareIt-server`, `shareIt-gateway` и `postgresql`.

💡 Для целей разработки вы по-прежнему можете запускать каждый из сервисов локально через IDE, а работу через Docker
проверять после завершения очередного этапа разработки. Перед тем как тестировать новую версию в Docker, обязательно
пересоберите код проекта и удалите старый Docker-образ!

Убедитесь, что ваше приложение успешно запускается командой `docker-compose up` и пользователи, как и прежде,
могут создавать и бронировать вещи.

## Тестирование
Как и всегда, воспользуйтесь нашей [Postman-коллекцией](https://github.com/yandex-praktikum/java-shareit/blob/add-docker/postman/sprint.json),
чтобы протестировать работу приложения.
</details>
