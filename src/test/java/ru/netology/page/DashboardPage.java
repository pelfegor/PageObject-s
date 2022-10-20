package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Integer.parseInt;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement reload = $("[data-test-id=action-reload]");
    private ElementsCollection cards = $$(".list__item div");

    private SelenideElement transferToFirstButton = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']").find("[data-test-id='action-deposit']");
    private SelenideElement transferToSecondButton = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']").find("[data-test-id='action-deposit']");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public void reloadCardsAmount() {
        reload.click();
    }

    public void transferButtonSecondToFirst() {
        transferToFirstButton.click();
    }

    public void transferButtonFirstToSecond() {
        transferToSecondButton.click();
    }

    public int getCardBalance(int indexCard) {
        val text = cards.get(indexCard).toString().split(" ");
        return parseInt(text[6]);
    }
}
