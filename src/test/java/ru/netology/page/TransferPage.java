package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement transferSumField = $("[data-test-id=amount] input");
    private SelenideElement transferCardField = $("[data-test-id=from] input");
    private SelenideElement applyButton = $("[data-test-id=action-transfer]");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");
    private SelenideElement notification = $("[data-test-id=error-notification]");
    private SelenideElement notificationButton = $("[data-test-id=error-notification] button");

    public void importTransferData(int value, String cardNumber) {
        $("[data-test-id='amount'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        transferSumField.setValue(Integer.toString(value));
        $("[data-test-id='from'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        transferCardField.setValue(String.valueOf(cardNumber));
        applyButton.click();
    }

    public void dataTransferCancel() {
        cancelButton.click();
    }

    public void importTransferEmptyAmountData(String cardNumber) {
        $("[data-test-id='amount'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='from'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        transferCardField.setValue(String.valueOf(cardNumber));
        applyButton.click();
    }

    public void importTransferEmptyCardData(int value) {
        $("[data-test-id='amount'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        transferSumField.setValue(Integer.toString(value));
        $("[data-test-id='from'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        applyButton.click();
    }

    public DashboardPage checkNotification(Condition status) {
        notification.should(status);
        if (status.equals(visible)) {
            notificationButton.click();
            notification.should(hidden);
        }
        return new DashboardPage();
    }

}
