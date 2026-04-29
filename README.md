# QA Portfolio

Портфолио по тестированию веб-приложения AutoSales.  
В репозитории представлены артефакты тестирования: чек-лист, тест-кейсы, баг-репорты, API-тестирование (Postman), анализ HTTP-запросов (Charles) и UI-автотесты на Cypress.

## О тестируемом приложении

AutoSales — веб-приложение автосалона, разработанное мной в учебных целях.  

Исходный код приложения:  
https://github.com/dimavarlamov/AutoSales

## Чек-лист тестирования

Чек-лист тестирования проекта AutoSales был составлен в Miro:

https://miro.com/app/board/uXjVGyszsQM=/?share_link_id=676938521386

## Что содержит данный репозиторий

- чек-лист тестирования
- тест-кейсы
- баг-репорты
- UI автотесты (Selenium + Java)
- API-тестирование (Postman)
- анализ HTTP-запросов (Charles)
- UI-автотесты (Cypress)

## Структура репозитория

checklists — чек-лист тестирования  
test-cases — тест-кейсы   
bug-reports — баг-репорты  
selenium-autosales - UI автотесты (Selenium + Java)   
postman — коллекция API-тестов  
charles — файлы перехвата HTTP-запросов  
cypress-autotests — UI-автотесты  

## Test-cases
Все тест-кейсы находятся в Google Sheets: https://docs.google.com/spreadsheets/d/1_Q-iFsHLe2fUODI-cjJ7lFffanbjUk9SKvfGxk74fnY/edit?usp=sharing

## UI автотесты (Selenium + Java)

Автотесты проверяют ключевую функциональность веб-приложения AutoSales.

📁 Папка: [selenium-autotests](https://github.com/dimavarlamov/QA-portfolio/tree/main/selenium-autotests)

### Что покрыто автотестами

- **Регистрация** – валидные/невалидные данные, дубликат email
- **Авторизация** – успешный вход, неверный пароль, выход
- **Каталог** – поиск, сброс фильтров, фильтрация по цене, марке, стране, рулю, типу двигателя, кузову, цвету, кондиционеру, XSS-безопасность
- **Карточка автомобиля** – проверка данных, добавление/удаление из избранного
- **Избранное** – добавление, удаление, защита от дублей (быстрые клики)
- **Покупка** – успешная покупка, появление заказа в «Моих покупках»
- **Безопасность** – недоступность админ-панели для обычного пользователя

### Запуск тестов

cd selenium-autotests  

mvn clean test

### Результат выполнения

Все автотесты успешно проходят ✅

<img width="937" height="386" alt="Прогон всех автотестов Selenium" src="https://github.com/user-attachments/assets/d786e8a3-c560-41d4-9381-4c3a8da1655f" width="500" />


## UI автотесты (Cypress)

Автотесты находятся в папке [cypress-autotests](./cypress-autotests)  

### Запуск тестов

1. Установить Node.js  
2. Перейти в папку `cypress-autotests`  
3. Установить зависимости: `npm install`
4. Запустить Cypress: `npx cypress open`

### Результат выполнения

Все автотесты успешно проходят ✅

<img src="https://github.com/user-attachments/assets/d594cdd7-a040-4e91-be65-0c37ae10dad4" width="500" />

## API-тестирование (Postman)

Коллекция Postman покрывает основные публичные эндпоинты, авторизацию и базовые проверки безопасности.

**Файлы:**
- [`postman/autosales-api-tests.postman_collection.json`](./postman/autosales-api-tests.postman_collection.json) — коллекция тестов
- [`postman/autosales-local.postman_environment.json`](./postman/autosales-local.postman_environment.json) — окружение для локального запуска

**Что проверяется:**
- Каталог автомобилей (GET /products)
- Поиск по марке/модели (GET /products?search=...)
- Обработка несуществующего автомобиля (редирект на логин)
- Авторизация (POST /auth/login)
- Доступ к панели администратора без прав
- Безопасность: SQL injection через поиск
- Безопасность: CSRF-защиты при обновлении профиля

### Результат выполнения

Все тесты проходят успешно ✅

<img src="https://github.com/dimavarlamov/QA-portfolio/blob/main/assets/postman-run-results-1.png?raw=true" width="500" />
<img src="https://github.com/dimavarlamov/QA-portfolio/blob/main/assets/postman-run-results-2.png?raw=true" width="500" />

## Bug-reports
Все bug-reports находятся в Google Sheets: https://docs.google.com/spreadsheets/d/1w1QIGRNknaOCof8CeeaMzweX_6It6n9vjTbJq2j38bw/edit?usp=sharing
