# Задание 2 дипломного проекта QA Java.

## Описание проекта

Проект содержит автоматизированные тесты для проверки функциональности API сервиса Stellar Burgers. 
Тесты покрывают следующие сценарии:
- Регистрация и авторизация пользователей
- Создание заказов с авторизацией и без
- Валидация ошибок при создании заказов

## Технологии

| Технология | Версия |
|------------|--------|
| Java | 11 |
| JUnit | 4.13.2 |
| Maven | 3.9.0+ |
| RestAssured | 5.5.5 |
| Allure | 2.15.0 |
| Lombok | 1.18.30 |
| Apache HttpClient | 4.5.14 |
| Gson | 2.8.9 |
| DataFaker | 2.5.2 |

## Структура проекта

```
src/
├── main/
│   └── java/
│       ├── constants/          # Константы проекта
│       │   ├── Endpoints.java  # Константы эндпоинтов API
│       │   └── TestData.java   # Тестовые данные
│       ├── model/              # Модели данных
│       │   ├── Ingredient.java
│       │   ├── Ingredients.java
│       │   ├── Order.java
│       │   └── User.java
│       └── steps/              # Шаги для работы с API
│           ├── OrderClientSteps.java
│           └── UserClientSteps.java
└── test/
    └── java/
        ├── order/              # Тесты для заказов
        │   ├── BaseOrderAPITest.java
        │   └── OrderCreateTest.java
        └── user/               # Тесты для пользователей
            ├── BaseAPITest.java
            ├── UserCreateTest.java
            └── UserLoginTest.java
```

## Настройка

1. Убедитесь, что установлены:
   - Java 11 или выше
   - Maven 3.9.0 или выше

2. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/TimBulokhov/QA_Java_Final_2.git
   cd QA_Java_Final_2
   ```

3. Переключитесь на ветку develop2:
   ```bash
   git checkout develop2
   ```

## Запуск тестов

### Запуск всех тестов

```bash
mvn clean test
```

### Запуск тестов с генерацией Allure отчета

```bash
mvn clean test allure:serve
```

Команда `mvn clean test allure:serve` выполнит следующие действия:
1. Очистит предыдущие результаты (`clean`)
2. Запустит все тесты (`test`)
3. Сгенерирует и откроет Allure отчет в браузере (`allure:serve`)

### Запуск конкретного теста

```bash
mvn test -Dtest=OrderCreateTest
```

или

```bash
mvn test -Dtest=UserCreateTest
```

### Генерация Allure отчета без автоматического открытия

```bash
mvn clean test allure:report
```

Отчет будет доступен в директории `target/site/allure-maven-plugin/index.html`

## Особенности реализации

- **Lombok**: Используется для сокращения boilerplate кода в моделях (`@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`)
- **Endpoints**: Все пути API вынесены в отдельный класс `Endpoints` для удобства поддержки
- **HttpStatus**: Используются константы из `org.apache.http.HttpStatus` вместо числовых кодов для улучшения читаемости
- **Allure**: Интеграция с Allure для генерации красивых отчетов о тестировании

## Результаты тестирования

После запуска тестов результаты доступны в:
- Консольный вывод
- Allure отчет (HTML)
- JUnit отчеты в `target/surefire-reports/`



