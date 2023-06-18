package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {}

    // По условиям задания на данные, вводимые в поле "Дата встречи", установлены ограничения -
    // дата должна быть позднее 3-х и более дней по отношению к текущей.
    // Параметр "shift" - кол-во дней по отношению к дню текущей даты, через которое возможно
    // планировать встречу клиента с сотрудником банка.
    // Реализуем необходимую для тестов логику самостоятельно
    public static String generateDate(int shift) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    // По условиям задания на данные, вводимые в поле "Ваш город", установлены ограничения -
    // город должен быть выбран из заранее заданного списка.
    // Мы не знаем насколько корректно Faker сможет сгенерировать наименование города, поэтому
    // реализуем необходимую для тестов логику самостоятельно
    public static String generateCity(String locale) {
        var random = new Random();
        var list = new String[]{"Астрахань", "Белгород", "Воронеж", "Курск", "Краснодар",
                "Москва", "Новосибирск", "Пенза", "Пермь", "Ростов-на-Дону", "Томск", "Самара",
                "Хабаровск", "Якутск"};                 // думаю для тестов такого набора хватит
        return list[random.nextInt(list.length)];
    }

    // По данным, вводимым в поле "Фамилия и имя" условия задания носят достаточно общий характер.
    // Для формирования тестовых данных воспользуемся Faker-om
    public static String generateName(String locale) {
        // Инициализируем экземпляр Faker-a
        Faker faker = new Faker(new Locale(locale));
        // Сформируем для тестов строку вида - "Фамилия Имя"
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    // По данным, вводимым в поле "Телефон" условия задания также носят общий характер.
    // Для формирования тестовых данных воспользуемся Faker-om
    public static String generatePhone(String locale) {
        // Инициализируем экземпляр Faker-a
        Faker faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber();
    }

    public static class Registration {
        private Registration() {
        }

        // Создаем пользователя под тесты
        public static UserInfo generateUser(String locale) {
            return new UserInfo(
                    generateCity(locale),
                    generateName(locale),
                    generatePhone(locale));
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
