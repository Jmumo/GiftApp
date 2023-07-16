package GiftsBackend.Service;

import GiftsBackend.Dtos.*;

public interface DarajaApi {
    AccessTokenResponse getAccessToken();

    RegisterUrlResponse registerUrl();

    SimulateTransactionResponse simulateC2BTransaction(SimulateTransactionRequest simulateTransactionRequest);


    AcknowledgeResponse savePayBillResponse(MpesaValidationResponse mpesaValidationResponse);

    StkPushSyncResponse performStkPushTransaction(InternalStkPushRequest internalStkPushRequest);

    void saveMpesaCallbackResponse(StkPushAsyncResponse stkPushAsyncResponse);
}
