package ru.netology.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;

public class CardBalanceTest {

    @BeforeEach
    void shouldLogin() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    void shouldReturn(){
        val transferPage = new TransferPage();
        DashboardPage dashboardPage = new DashboardPage();
        int firstRemain = dashboardPage.getCardBalance(0) - 10_000;
        int secondRemain = dashboardPage.getCardBalance(1) - 10_000;
        if(dashboardPage.getCardBalance(1) < dashboardPage.getCardBalance(0)) {
            dashboardPage.transferButtonFirstToSecond();
            String cardNumber = String.valueOf(DataHelper.getFirstCard());
            transferPage.importTransferData(firstRemain, cardNumber);
        }
        else {
            dashboardPage.transferButtonSecondToFirst();
            String cardNumber = String.valueOf(DataHelper.getSecondCard());
            transferPage.importTransferData(secondRemain, cardNumber);
        }
    }

    @Test
    public void shouldTransferBoundaryValueToFirstCard() {
        String cardNumber = String.valueOf(DataHelper.getSecondCard());
        val dashboardPage = new DashboardPage();
        int value = dashboardPage.getCardBalance(0) - 1;
        var firstCardBalanceBefore = dashboardPage.getCardBalance(0);
        var secondCardBalanceBefore = dashboardPage.getCardBalance(1);
        dashboardPage.transferButtonSecondToFirst();
        val transferPage = new TransferPage();
        transferPage.importTransferData(value, cardNumber);
        Assertions.assertEquals(secondCardBalanceBefore - value, dashboardPage.getCardBalance(1));
        Assertions.assertEquals(firstCardBalanceBefore + value, dashboardPage.getCardBalance(0));
    }

    @Test
    public void shouldTransferBoundaryValueToFirstCard2() {
        String cardNumber = String.valueOf(DataHelper.getSecondCard());
        val dashboardPage = new DashboardPage();
        int value = dashboardPage.getCardBalance(0);
        var firstCardBalanceBefore = dashboardPage.getCardBalance(0);
        var secondCardBalanceBefore = dashboardPage.getCardBalance(1);
        dashboardPage.transferButtonSecondToFirst();
        val transferPage = new TransferPage();
        transferPage.importTransferData(value, cardNumber);
        Assertions.assertEquals(secondCardBalanceBefore - value, dashboardPage.getCardBalance(1));
        Assertions.assertEquals(firstCardBalanceBefore + value, dashboardPage.getCardBalance(0));
    }

    @Test
    public void notShouldTransferBoundaryValueToFirstCard3() {
        String cardNumber = String.valueOf(DataHelper.getSecondCard());
        val dashboardPage = new DashboardPage();
        int value = dashboardPage.getCardBalance(0) + 1;
        dashboardPage.transferButtonSecondToFirst();
        val transferPage = new TransferPage();
        transferPage.importTransferData(value, cardNumber);
        transferPage.checkNotification(Condition.visible);
    }

    @Test
    public void notShouldTransferZeroValueToFirstCard() {
        String cardNumber = String.valueOf(DataHelper.getSecondCard());
        val dashboardPage = new DashboardPage();
        int value = 0;
        var firstCardBalanceBefore = dashboardPage.getCardBalance(0);
        var secondCardBalanceBefore = dashboardPage.getCardBalance(1);
        dashboardPage.transferButtonSecondToFirst();
        val transferPage = new TransferPage();
        transferPage.importTransferData(value, cardNumber);
        transferPage.checkNotification(Condition.visible);
        Assertions.assertEquals(secondCardBalanceBefore - value, dashboardPage.getCardBalance(1));
        Assertions.assertEquals(firstCardBalanceBefore + value, dashboardPage.getCardBalance(0));
    }

    @Test
    public void notShouldTransferValueToSingleCard() {
        String cardNumber = String.valueOf(DataHelper.getSecondCard());
        val dashboardPage = new DashboardPage();
        int value = dashboardPage.getCardBalance(0)/2;
        var firstCardBalanceBefore = dashboardPage.getCardBalance(0);
        var secondCardBalanceBefore = dashboardPage.getCardBalance(1);
        dashboardPage.transferButtonSecondToFirst();
        val transferPage = new TransferPage();
        transferPage.importTransferData(value, cardNumber);
        transferPage.checkNotification(Condition.visible);
        Assertions.assertEquals(secondCardBalanceBefore - value, dashboardPage.getCardBalance(1));
        Assertions.assertEquals(firstCardBalanceBefore + value, dashboardPage.getCardBalance(0));
    }

    @Test
    public void notShouldTransferIfEmptyAmountForm() {
        String cardNumber = String.valueOf(DataHelper.getSecondCard());
        val dashboardPage = new DashboardPage();
        var firstCardBalanceBefore = dashboardPage.getCardBalance(0);
        var secondCardBalanceBefore = dashboardPage.getCardBalance(1);
        dashboardPage.transferButtonSecondToFirst();
        val transferPage = new TransferPage();
        transferPage.importTransferEmptyAmountData(cardNumber);
        transferPage.checkNotification(Condition.visible);
        Assertions.assertEquals(secondCardBalanceBefore, dashboardPage.getCardBalance(1));
        Assertions.assertEquals(firstCardBalanceBefore, dashboardPage.getCardBalance(0));
    }

    @Test
    public void notShouldTransferIfEmptyCardForm() {
        val dashboardPage = new DashboardPage();
        int value = dashboardPage.getCardBalance(0)/2;
        var firstCardBalanceBefore = dashboardPage.getCardBalance(0);
        var secondCardBalanceBefore = dashboardPage.getCardBalance(1);
        dashboardPage.transferButtonSecondToFirst();
        val transferPage = new TransferPage();
        transferPage.importTransferEmptyCardData(value);
        transferPage.checkNotification(Condition.visible);
        transferPage.dataTransferCancel();
        Assertions.assertEquals(secondCardBalanceBefore, dashboardPage.getCardBalance(1));
        Assertions.assertEquals(firstCardBalanceBefore, dashboardPage.getCardBalance(0));
    }

    @Test
    public void shouldCancelTransfer() {
        val dashboardPage = new DashboardPage();
        var firstCardBalanceBefore = dashboardPage.getCardBalance(0);
        var secondCardBalanceBefore = dashboardPage.getCardBalance(1);
        dashboardPage.transferButtonSecondToFirst();
        val transferPage = new TransferPage();
        transferPage.dataTransferCancel();
        dashboardPage.reloadCardsAmount();
        Assertions.assertEquals(secondCardBalanceBefore, dashboardPage.getCardBalance(1));
        Assertions.assertEquals(firstCardBalanceBefore, dashboardPage.getCardBalance(0));
    }

}
