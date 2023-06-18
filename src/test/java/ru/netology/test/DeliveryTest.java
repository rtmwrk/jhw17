package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeEach
    void setup() {
        // Устанавливаем сосединение с сервисом
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        // Создаем случайного пользователя для теста. Пользователь соответствует требованиям задания
        var validUser = DataGenerator.Registration.generateUser("ru");
        // Задаем первоначальную дату встречи
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        // Задаем дату переназначения встречи
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        // Тестируем опцию "Запланировать встречу"
        // Находим на странице элемент с классом "form"
        // Находим элемент "Город", устанавливаем значение для дочернего input
        $("[data-test-id=city] input").setValue(validUser.getCity());
        // Находим элемент "Дата", устанавливаем значение для дочернего input
        // Очищаем вручную значение поля "по умолчанию"
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        // Устанавливаем нужное значение даты - дату первой встречи
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        // Находим элемент "Имя клиента", устанавливаем значение для дочернего input
        $("[data-test-id=name] input").setValue(validUser.getName());
        // Находим элемент "Телефон клиента", устанавливаем значение для дочернего input
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        // Нажимаем чекбокс "Я соглашаюсь"
        $("[data-test-id=agreement]").click();
        // Нажимаем кнопку "Запланировать"
        $(".button").click();
        // Ожидаем появление элемента текста сообщения с датой встречи, что подтверждает
        // успешность регистрации заявки на оформление деюетовой карты и позволяет проверить
        // правильность даты встречи банковского сотрудника и клиента
        $("[data-test-id=success-notification] .notification__content")
                .shouldBe(visible, Duration.ofMillis(15000))
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        // Закрываем окошко - сообщение об успешном планировании
        $("[data-test-id=success-notification] .icon-button").click();

        // Тестируем опцию "Перепланировать встречу"
        // Возвращаемся к форме и заполняем ее теми же данными, что и на предыдущем шаге,
        // изменив только дату встречи
        // Находим элемент "Дата", устанавливаем значение для дочернего input
        // Очищаем вручную значение поля "по умолчанию"
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        // Устанавливаем нужное значение даты - дату перепланированной встречи
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        // Нажимаем кнопку "Забронировать"
        $(".button").click();

        // Ожидаем появление окна сообщения - подтверждения новой даты встречи
        $("[data-test-id=replan-notification] .notification__content")
                .shouldBe(visible, Duration.ofMillis(15000))
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        // Соглашаемя перепланировать встречу, нажимаем на кнопку "Перепланировать"
        $("[data-test-id=replan-notification] .button").click();

        // Ожидаем появление окна сообщения - подтверждения перепланирования встречи на
        // новую дату
        $("[data-test-id=success-notification] .notification__content")
                .shouldBe(visible, Duration.ofMillis(15000))
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }
}