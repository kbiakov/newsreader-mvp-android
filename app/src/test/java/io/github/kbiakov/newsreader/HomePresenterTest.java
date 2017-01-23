package io.github.kbiakov.newsreader;

import org.junit.Test;
import org.mockito.InOrder;

import io.reactivex.Observable;
import io.reactivex.Single;

import static org.mockito.Mockito.doAnswer;

public class HomePresenterTest {

    @Test
    public void testShowLoadingOnError() {
        doAnswer(invocation -> Observable.error(new Throwable(TestConst.TEST_ERROR)))
                .when(model)
                .getRepoList(TestConst.TEST_OWNER);
        repoListPresenter.onSearchButtonClick();
        verify(mockView).showLoading();
    }

    @Test
    public void testHideLoadingOnError() {
        doAnswer(invocation -> Observable.error(new Throwable(TestConst.TEST_ERROR)))
                .when(model)
                .getRepoList(TestConst.TEST_OWNER);
        repoListPresenter.onSearchButtonClick();
        verify(mockView).hideLoading();
    }

    /*
    @Test
    public void sendTransfer_success() {
        // mock
        when(iTransferInteractor.sendTransfer(any(TransferFilledDataModel.class))).thenReturn(
                Single.fromCallable(() -> new TransferResultModel("success", "info")));
        // bindView
        transferPresenter.bindView(iTransferView);
        // sendTransfer
        transferPresenter.sendTransfer(mock(TransferFilledDataModel.class));
        // verify
        InOrder inOrder = inOrder(iTransferView);
        inOrder.verify(iTransferView).showProgress();
        inOrder.verify(iTransferView).hideProgress();
        inOrder.verify(iTransferView).showSuccess();
        //
        verify(iTransferView, never()).showAccountNumberError(anyString());
        verify(iTransferView, never()).showBIKError(anyString());
        verify(iTransferView, never()).showINNError(anyString());
        verify(iTransferView, never()).showOrgNameError(anyString());
        verify(iTransferView, never()).showAmountError(anyString());
        verify(iTransferView, never()).showCommonError();
    }

    @Test
    public void sendTransfer_validateError() {
        // mock
        when(iTransferInteractor.sendTransfer(any(TransferFilledDataModel.class))).thenReturn(
                Single.error(getTransferValidateException()));
        // bindView
        transferPresenter.bindView(iTransferView);
        // sendTransfer
        transferPresenter.sendTransfer(mock(TransferFilledDataModel.class));
        // verify
        InOrder inOrder = inOrder(iTransferView);
        inOrder.verify(iTransferView).showProgress();
        inOrder.verify(iTransferView).hideProgress();
        inOrder.verify(iTransferView).showOrgNameError("org name error");
        inOrder.verify(iTransferView).showBIKError("bik error");
        inOrder.verify(iTransferView).showINNError("inn error");
        inOrder.verify(iTransferView).showAccountNumberError("account number error");
        inOrder.verify(iTransferView).showAmountError("amount error");
        //
        verify(iTransferView, never()).showSuccess();
        verify(iTransferView, never()).showCommonError();
    }

    private TransferValidateException getTransferValidateException() {
        List<ValidateErrorModel> validateErrorList = asList(
                new ValidateErrorModel(ValidateErrorModel.Field.ORG_NAME, "org name error"),
                new ValidateErrorModel(ValidateErrorModel.Field.BIK, "bik error"),
                new ValidateErrorModel(ValidateErrorModel.Field.INN, "inn error"),
                new ValidateErrorModel(ValidateErrorModel.Field.ACCOUNT_NUMBER, "account number error"),
                new ValidateErrorModel(ValidateErrorModel.Field.AMOUNT, "amount error")
        );
        return new TransferValidateException(validateErrorList);
    }

    @Test
    public void sendTransfer_noValidateError() {
        // mock
        when(iTransferInteractor.sendTransfer(any(TransferFilledDataModel.class))).thenReturn(
                Single.error(new RuntimeException()));
        // bindView
        transferPresenter.bindView(iTransferView);
        // sendTransfer
        transferPresenter.sendTransfer(mock(TransferFilledDataModel.class));
        // verify
        InOrder inOrder = inOrder(iTransferView);
        inOrder.verify(iTransferView).showProgress();
        inOrder.verify(iTransferView).hideProgress();
        inOrder.verify(iTransferView).showCommonError();
        //
        verify(iTransferView, never()).showAccountNumberError(anyString());
        verify(iTransferView, never()).showBIKError(anyString());
        verify(iTransferView, never()).showINNError(anyString());
        verify(iTransferView, never()).showOrgNameError(anyString());
        verify(iTransferView, never()).showAmountError(anyString());
        verify(iTransferView, never()).showSuccess();
    }

    //

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test
    public void getLocalValidateSingle_errorValidation() {
        TransferFilledDataModel transferFilledDataModel = new TransferFilledDataModel(
                "name", "34250", "", "1234", "qwerty"
        );
        TestSubscriber<TransferRequestModel> testSubscriber = TestSubscriber.create();
        // subscribe to Single
        localTransferValidation.getLocalValidateSingle(transferFilledDataModel).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        // assert
        testSubscriber.assertError(TransferValidateException.class);
        TransferValidateException transferValidateException =
                (TransferValidateException) testSubscriber.getOnErrorEvents().get(0);
        assertThat(transferValidateException.getValidateErrorList())
                .isNotNull()
                .hasSize(3)
                .extracting("field", "description")
                .containsOnly((tuple(ValidateErrorModel.Field.INN, "INN is empty")),
                        (tuple(ValidateErrorModel.Field.ACCOUNT_NUMBER, "Account number length must be 20 symbols")),
                        (tuple(ValidateErrorModel.Field.AMOUNT, "Amount must be consist of only digits")));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void getLocalValidateSingle_successValidation() {
        TransferFilledDataModel transferFilledDataModel = new TransferFilledDataModel(
                "name", "34250", "1234", "12345678901234567890", "1000"
        );
        TestSubscriber<TransferRequestModel> testSubscriber = TestSubscriber.create();
        // subscribe to Single
        localTransferValidation.getLocalValidateSingle(transferFilledDataModel).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent();
        // assert
        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();
        // assert returned TransferRequestModel
        TransferRequestModel transferRequestModel = testSubscriber.getOnNextEvents().get(0);
        assertThat(transferRequestModel.getOrgName()).isEqualTo("name");
        assertThat(transferRequestModel.getBIK()).isEqualTo("34250");
        assertThat(transferRequestModel.getINN()).isEqualTo("1234");
        assertThat(transferRequestModel.getAccountNumber()).isEqualTo("12345678901234567890");
        assertThat(transferRequestModel.getAmount().doubleValue()).isEqualTo(1000);
    }
    */
}
