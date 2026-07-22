# Анализатор страниц / Page Analyzer

### Hexlet tests and linter status:
[![Actions Status](https://github.com/bedrevpaul23/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/bedrevpaul23/java-project-72/actions)

### Build status:
[![Build](https://github.com/bedrevpaul23/java-project-72/actions/workflows/main.yml/badge.svg)](https://github.com/bedrevpaul23/java-project-72/actions/workflows/main.yml)

### Maintainability and test coverage:
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=bedrevpaul23_java-project-72&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=bedrevpaul23_java-project-72)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=bedrevpaul23_java-project-72&metric=coverage)](https://sonarcloud.io/summary/new_code?id=bedrevpaul23_java-project-72)

### Deployed application:
https://java-project-72-yhix.onrender.com

---

## Описание

**Анализатор страниц** — веб-приложение для базовой SEO-проверки сайтов.

Приложение позволяет добавить сайт по URL, сохранить его в базе данных, запустить проверку доступности страницы и получить основные SEO-данные: HTTP-код ответа, содержимое тега `<h1>`, содержимое тега `<title>` и значение мета-тега `<meta name="description">`.

Проект выполнен как учебное Java-приложение в рамках курса Hexlet. Основной фокус проекта — работа с веб-фреймворком, шаблонами, базой данных, HTTP-запросами, парсингом HTML, тестированием и деплоем.

## Для чего это нужно

Приложение помогает быстро проверить, отвечает ли сайт на HTTP-запрос и содержит ли базовые SEO-элементы, которые важны для поисковой выдачи и предпросмотра страниц.

С помощью анализатора можно:

- сохранять список проверяемых сайтов;
- нормализовать URL до базового адреса сайта;
- запускать повторные проверки одного и того же сайта;
- видеть историю проверок;
- отслеживать последний HTTP-код ответа на странице списка сайтов;
- просматривать найденные SEO-данные на странице конкретного сайта.

## Как пользоваться сайтом

### 1. Добавление сайта

Откройте главную страницу приложения. В поле ввода укажите адрес сайта, например:

```text
https://example.com/some/page?query=value
```

После отправки формы приложение нормализует адрес и сохранит только базовый URL:

```text
https://example.com
```

Если URL некорректный, сайт не будет добавлен, а приложение покажет сообщение **Некорректный URL**.

Если такой сайт уже есть в базе, новая запись не создаётся. Приложение откроет страницу уже добавленного сайта и покажет сообщение **Страница уже существует**.

### 2. Просмотр списка сайтов

На странице **Сайты** отображаются все добавленные сайты. Для каждого сайта можно увидеть:

- ID;
- имя сайта;
- дату последней проверки;
- последний HTTP-код ответа.

Название сайта является ссылкой на страницу конкретного сайта.

### 3. Просмотр страницы сайта

На странице конкретного сайта отображаются основные данные:

- ID;
- нормализованный URL;
- дата добавления сайта.

Ниже находится блок **Проверки** с кнопкой **Запустить проверку** и таблицей истории проверок.

### 4. Запуск проверки

Нажмите **Запустить проверку**. Приложение выполнит HTTP-запрос к сайту, получит HTML-страницу и попробует извлечь SEO-данные.

После успешной проверки появится сообщение **Страница успешно проверена**, а в таблицу добавится новая строка с результатами.

Если сайт недоступен или возвращает ошибку, приложение покажет сообщение **Произошла ошибка при проверке**. В этом случае запись проверки не создаётся.

### 5. Результат проверки

Для каждой успешной проверки в таблице отображаются:

- ID проверки;
- HTTP-код ответа;
- найденный `h1`;
- найденный `title`;
- найденный `description`;
- дата проверки.

Длинные значения `h1`, `title` и `description` отображаются в обрезанном виде.

## Технологии

- Java 21
- Javalin
- JTE
- JDBC
- HikariCP
- PostgreSQL
- H2
- Unirest
- Jsoup
- Bootstrap
- Gradle
- JUnit 5
- MockWebServer
- Checkstyle
- JaCoCo
- SonarCloud
- GitHub Actions
- Render

## Локальный запуск

```bash
git clone git@github.com:bedrevpaul23/java-project-72.git
cd java-project-72/app
./gradlew run
```

По умолчанию приложение стартует на порту `7070` и использует H2 in-memory database.

Для запуска с внешней базой данных укажите переменную окружения `JDBC_DATABASE_URL`.

```bash
JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/page_analyzer ./gradlew run
```

## Тесты и проверка качества

```bash
./gradlew test
./gradlew check jacocoTestReport
```

---

# Page Analyzer

## Description

**Page Analyzer** is a web application for basic SEO analysis of websites.

The application allows users to add a website by URL, store it in a database, run an availability check, and collect basic SEO data: HTTP response code, `<h1>`, `<title>`, and the value of `<meta name="description">`.

This is an educational Java project built as part of the Hexlet curriculum. The project focuses on web development, templates, databases, HTTP requests, HTML parsing, testing, and deployment.

## Purpose

The application helps quickly check whether a website responds to an HTTP request and whether it contains basic SEO elements used by search engines and page previews.

With Page Analyzer, you can:

- store a list of websites;
- normalize a URL to the base website address;
- run repeated checks for the same website;
- view the check history;
- see the latest HTTP response code in the website list;
- inspect collected SEO data on a website page.

## How to use the application

### 1. Add a website

Open the main page and enter a website URL, for example:

```text
https://example.com/some/page?query=value
```

After submitting the form, the application normalizes the address and stores only the base URL:

```text
https://example.com
```

If the URL is invalid, the website is not added and the application shows **Некорректный URL**.

If the website already exists in the database, the application does not create a duplicate record. It opens the existing website page and shows **Страница уже существует**.

### 2. View the website list

The **Сайты** page displays all added websites. For each website, you can see:

- ID;
- website URL;
- latest check date;
- latest HTTP response code.

The website URL is a link to the website details page.

### 3. View a website page

A website page displays the main website data:

- ID;
- normalized URL;
- creation date.

Below this block, there is a **Проверки** section with the **Запустить проверку** button and the check history table.

### 4. Run a check

Click **Запустить проверку**. The application sends an HTTP request to the website, receives the HTML page, and tries to extract SEO data.

After a successful check, the application shows **Страница успешно проверена** and adds a new row to the checks table.

If the website is unavailable or returns an error, the application shows **Произошла ошибка при проверке**. In this case, no check record is created.

### 5. Check result

For every successful check, the table displays:

- check ID;
- HTTP response code;
- extracted `h1`;
- extracted `title`;
- extracted `description`;
- check date.

Long `h1`, `title`, and `description` values are truncated in the table.

## Tech stack

- Java 21
- Javalin
- JTE
- JDBC
- HikariCP
- PostgreSQL
- H2
- Unirest
- Jsoup
- Bootstrap
- Gradle
- JUnit 5
- MockWebServer
- Checkstyle
- JaCoCo
- SonarCloud
- GitHub Actions
- Render

## Local setup

```bash
git clone git@github.com:bedrevpaul23/java-project-72.git
cd java-project-72/app
./gradlew run
```

By default, the application starts on port `7070` and uses an H2 in-memory database.

To run the application with an external database, provide the `JDBC_DATABASE_URL` environment variable.

```bash
JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/page_analyzer ./gradlew run
```

## Tests and quality checks

```bash
./gradlew test
./gradlew check jacocoTestReport
```
