# Лабораторная работа №6. Уведомления.

- **Выполнил:** Наумов 
- **Язык программирования:** Java  

---

## **Описание задачи**

Разработать мобильное приложение для установки и управления напоминаниями.  
Приложение должно поддерживать следующие функции:
1. Создание напоминаний с сохранением их в базу данных.
2. Просмотр установленных напоминаний через список.
3. Удаление напоминаний.
4. Установка даты и времени напоминания с использованием `DatePickerDialog` и `TimePickerDialog`.
5. Отображение напоминаний в **Notification Center** с кастомным стилем (значок и текст).
6. При нажатии на уведомление приложение должно открываться и показывать детали напоминания.

---

### Основные классы:
- **`MainActivity`** — отображение списка напоминаний и управление основными функциями.
- **`AddReminderActivity`** — экран добавления напоминаний.
- **`Reminder`** — модель данных напоминания.
- **`ReminderViewModel`** — управление данными напоминаний через Room.
- **`ReminderReceiver`** — приемник для запуска уведомлений.
- **`Utils`** — вспомогательные методы для работы с `AlarmManager` и уведомлениями.

---

## **Инструкция по запуску**

### 1. Настройка проекта
- Скачайте или клонируйте проект.
- Убедитесь, что Android Studio настроено для использования **Java**.
- Подключите зависимости в `build.gradle`:

```groovy
dependencies {
    implementation "androidx.room:room-runtime:2.5.0"
    annotationProcessor "androidx.room:room-compiler:2.5.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel:2.6.0"
    implementation "androidx.lifecycle:lifecycle-livedata:2.6.0"
    implementation "androidx.appcompat:appcompat:1.6.1"
    implementation "com.google.android.material:material:1.8.0"
}
```

- Синхронизируйте проект.

### 2. Запуск
1. Убедитесь, что на вашем устройстве или эмуляторе включены уведомления.
2. Соберите проект и запустите приложение.

---

## **Функциональность**

### **1. Главный экран (`MainActivity`)**
- Отображает список напоминаний в **RecyclerView**.
- Поддерживает:
  - Удаление напоминания.
  - Добавление нового напоминания через кнопку.

### **2. Экран добавления напоминания (`AddReminderActivity`)**
- Позволяет:
  - Установить заголовок и описание напоминания.
  - Выбрать дату через `DatePickerDialog`.
  - Выбрать время через `TimePickerDialog`.
- Сохраняет напоминание в базу данных и планирует его через `AlarmManager`.

### **3. Уведомления**
- Уведомления отправляются с помощью `AlarmManager` и `BroadcastReceiver`.
- Канал уведомлений создается с использованием `NotificationChannel`.
- Уведомление отображается в статус-баре, при нажатии на него открывается главное окно приложения.

### **4. База данных**
- Используется **Room** для хранения напоминаний локально:
  - Поля: `id`, `title`, `message`, `time`.
  - DAO предоставляет функции для добавления, удаления и получения списка напоминаний.

---

## **Ключевые файлы**

### **1. Основные классы**
- **`MainActivity.java`**: управление списком напоминаний.
- **`AddReminderActivity.java`**: экран добавления нового напоминания.
- **`Reminder.java`**: модель данных для напоминания.
- **`ReminderViewModel.java`**: ViewModel для управления напоминаниями.
- **`ReminderReceiver.java`**: приемник для обработки событий `AlarmManager`.

### **2. База данных**
- **`AppDatabase.java`**: класс базы данных.
- **`ReminderDao.java`**: интерфейс DAO для работы с напоминаниями.

### **3. Вспомогательный класс**
- **`Utils.java`**: методы для работы с `AlarmManager`, `NotificationManager` и проверкой разрешений.

---

## **Особенности реализации**

### **Архитектура MVVM**
- Разделение логики приложения на слои:
  - **UI Layer:** `MainActivity`, `AddReminderActivity`.
  - **ViewModel Layer:** `ReminderViewModel` управляет взаимодействием между базой данных и UI.
  - **Data Layer:** `Room` предоставляет локальное хранилище для напоминаний.

### **Уведомления**
- **Для Android 8.0+**:
  - Создается `NotificationChannel`.
  - Канал называется `reminders`, имеет высокий приоритет.
- **Для Android 13.0+**:
  - Проверяется статус разрешений на отправку точных будильников (`SCHEDULE_EXACT_ALARM`).
  - Если разрешение отключено, предлагается включить его.

---

## **Ключевые моменты реализации**

### **1. Настройка точных будильников**
```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        reminder.getTime(),
        pendingIntent
    );
} else {
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        reminder.getTime(),
        pendingIntent
    );
}
```

### **2. Проверка разрешений**
```java
if (!Utils.areNotificationsEnabled(context)) {
    Toast.makeText(context, "Уведомления отключены. Включите их в настройках.", Toast.LENGTH_LONG).show();
    Utils.openNotificationSettings(context);
}
```

### **3. Канал уведомлений**
```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    NotificationChannel channel = new NotificationChannel(
        "reminders",
        "Напоминания",
        NotificationManager.IMPORTANCE_HIGH
    );
    channel.setDescription("Канал для напоминаний");
    notificationManager.createNotificationChannel(channel);
}
```

---

## **Результат**

1. Приложение позволяет:
   - Добавлять напоминания.
   - Устанавливать точное время напоминания.
   - Получать уведомления даже в режиме Doze.
   - Удалять напоминания.
2. Реализовано корректное управление уведомлениями и точными будильниками для Android 13+.
3. Все данные напоминаний хранятся в локальной базе данных с помощью Room.

---

## **Заключение**

В ходе лабораторной работы реализовано приложение для работы с уведомлениями, включая настройку напоминаний, управление локальными данными и интеграцию с системными сервисами Android.
