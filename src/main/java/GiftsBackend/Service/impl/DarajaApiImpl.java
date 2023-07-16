package GiftsBackend.Service.impl;

import GiftsBackend.Config.MpesaConfiguration;
import GiftsBackend.Dtos.*;
import GiftsBackend.Model.Event;
import GiftsBackend.Model.MpesaPayBillResponse;
import GiftsBackend.Model.Payments;
import GiftsBackend.Repository.EventRepository;
import GiftsBackend.Repository.MpesaPayBillResponseRepo;
import GiftsBackend.Repository.PaymentsRepository;
import GiftsBackend.Service.DarajaApi;
import GiftsBackend.Utils.HelperUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static GiftsBackend.Utils.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class DarajaApiImpl implements DarajaApi {

    private final MpesaConfiguration mpesaConfiguration;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final EventRepository eventRepository;
    private final MpesaPayBillResponseRepo mpesaPayBillResponseRepo;
    private final PaymentsRepository paymentsRepository;


    @Override
    public AccessTokenResponse getAccessToken() {
        // get the Base64 rep of consumerKey + ":" + consumerSecret
        String encodedCredentials = HelperUtility.toBase64String(String.format("%s:%s", mpesaConfiguration.getConsumerKey(),
                mpesaConfiguration.getConsumerSecret()));
        System.out.println("preparing request");
        Request request = new Request.Builder()
                .url(String.format("%s?grant_type=%s", mpesaConfiguration.getOauthEndpoint(), mpesaConfiguration.getGrantType()))
                .get()
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BASIC_AUTH_STRING, encodedCredentials))
                .addHeader(CACHE_CONTROL_HEADER, CACHE_CONTROL_HEADER_VALUE)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;

            // use Jackson to Decode the ResponseBody ...
            return objectMapper.readValue(response.body().string(), AccessTokenResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not get access token. -> %s", e.getLocalizedMessage()));
            return null;
        }
    }

    @Override
    public RegisterUrlResponse registerUrl() {
        AccessTokenResponse accessTokenResponse = getAccessToken();

        RegisterUrlRequest registerUrlRequest = new RegisterUrlRequest();
        registerUrlRequest.setConfirmationURL(mpesaConfiguration.getConfirmationUrL());
        registerUrlRequest.setResponseType(mpesaConfiguration.getResponseType());
        registerUrlRequest.setShortCode(mpesaConfiguration.getShortCode());
        registerUrlRequest.setValidationURL(mpesaConfiguration.getValidationUrL());

        System.out.println(mpesaConfiguration.getConfirmationUrL()); System.out.println(mpesaConfiguration.getResponseType()); System.out.println(mpesaConfiguration.getShortCode()); System.out.println(mpesaConfiguration.getValidationUrL());


        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(registerUrlRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getRegisterUrlEndpoint())
                .post(body)
                .addHeader("Authorization", String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();


        try {

            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            // use Jackson to Decode the ResponseBody ...

            System.out.println();
            return objectMapper.readValue(response.body().string(), RegisterUrlResponse.class);

        } catch (IOException e) {
            log.error(String.format("Could not register url -> %s", e.getLocalizedMessage()));
            return null;
        }
    }


    @Override
    public SimulateTransactionResponse simulateC2BTransaction(SimulateTransactionRequest simulateTransactionRequest) {
        AccessTokenResponse accessTokenResponse = getAccessToken();
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(simulateTransactionRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getSimulateTransactionEndpoint())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            // use Jackson to Decode the ResponseBody ...

            return objectMapper.readValue(response.body().string(), SimulateTransactionResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not simulate C2B transaction -> %s", e.getLocalizedMessage()));
            return null;
        }

    }

    @Override
    public AcknowledgeResponse savePayBillResponse(MpesaValidationResponse mpesaValidationResponse) {

        Optional<Event> event = Optional.ofNullable(eventRepository.findByPaymentRef(mpesaValidationResponse.getBillRefNumber()));


        if(event.isPresent()){
            Event eventToSave = event.get();

//save Paybill Response
            MpesaPayBillResponse mpesaPayBillResponse = MpesaPayBillResponse.builder()
                    .billRefNumber(mpesaValidationResponse.getBillRefNumber())
                    .businessShortCode(mpesaValidationResponse.getBusinessShortCode())
                    .transTime(mpesaValidationResponse.getTransTime())
                    .transID(mpesaValidationResponse.getTransID())
                    .transactionType(mpesaValidationResponse.getTransactionType())
                    .transAmount(mpesaValidationResponse.getTransAmount())
                    .invoiceNumber(mpesaValidationResponse.getInvoiceNumber())
                    .orgAccountBalance(mpesaValidationResponse.getOrgAccountBalance())
                    .thirdPartyTransID(mpesaValidationResponse.getThirdPartyTransID())
                    .msisdn(mpesaValidationResponse.getMSISDN())
                    .lastName(mpesaValidationResponse.getLastName())
                    .firstName(mpesaValidationResponse.getFirstName())
                    .middleName(mpesaValidationResponse.getMiddleName())
                    .event(eventToSave)
                    .build();

            mpesaPayBillResponseRepo.save(mpesaPayBillResponse);

            eventToSave.getPayBillResponses().add(mpesaPayBillResponse);
            eventRepository.save(eventToSave);
            AcknowledgeResponse acknowledgeResponse = new AcknowledgeResponse();
            acknowledgeResponse.setResultCode("00");
            acknowledgeResponse.setResultDescription("Accepted");
            return acknowledgeResponse;
        }

        AcknowledgeResponse acknowledgeResponse = new AcknowledgeResponse();
        acknowledgeResponse.setResultCode("C2B00011");
        acknowledgeResponse.setResultDescription("Rejected");
        return acknowledgeResponse;
    }


    @Override
    public StkPushSyncResponse performStkPushTransaction(InternalStkPushRequest internalStkPushRequest) {

        Event event  = eventRepository.findById(internalStkPushRequest.getEventId()).get();

        ExternalStkPushRequest externalStkPushRequest = new ExternalStkPushRequest();
        externalStkPushRequest.setBusinessShortCode(mpesaConfiguration.getStkPushShortCode());

        String transactionTimestamp = HelperUtility.getTransactionTimestamp();
        String stkPushPassword = HelperUtility.getStkPushPassword(mpesaConfiguration.getStkPushShortCode(),
                mpesaConfiguration.getStkPassKey(), transactionTimestamp);
        externalStkPushRequest.setPassword(stkPushPassword);
        externalStkPushRequest.setTimestamp(transactionTimestamp);
        externalStkPushRequest.setTransactionType(CUSTOMER_PAYBILL_ONLINE);
        externalStkPushRequest.setAmount(internalStkPushRequest.getAmount());
        externalStkPushRequest.setPartyA(internalStkPushRequest.getPhoneNumber());
        externalStkPushRequest.setPartyB(mpesaConfiguration.getStkPushShortCode());
        externalStkPushRequest.setPhoneNumber(internalStkPushRequest.getPhoneNumber());
        externalStkPushRequest.setCallBackURL(mpesaConfiguration.getStkPushRequestCallbackUrl());
        externalStkPushRequest.setAccountReference(event.getPaymentRef());
        externalStkPushRequest.setTransactionDesc(String.format("%s Transaction", internalStkPushRequest.getPhoneNumber()));

        AccessTokenResponse accessTokenResponse = getAccessToken();

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(externalStkPushRequest)));


        Request request = new Request.Builder()
                .url(mpesaConfiguration.getStkPushRequestUrl())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
           StkPushSyncResponse stkPushSyncResponse = objectMapper.readValue(response.body().string(), StkPushSyncResponse.class);
            System.out.println(stkPushSyncResponse.getResponseDescription());

            Payments payments = new Payments();
            payments.setMerchantRequestID(stkPushSyncResponse.getMerchantRequestID());
            payments.setResponseCode(stkPushSyncResponse.getResponseCode());
            payments.setCustomerMessage(stkPushSyncResponse.getCustomerMessage());
            payments.setCheckoutRequestID(stkPushSyncResponse.getCheckoutRequestID());
            payments.setResponseDescription(stkPushSyncResponse.getResponseDescription());
            log.info("payments Object {}",objectMapper.writeValueAsString(payments));
            payments.setEvent(event);
            paymentsRepository.save(payments);

            return stkPushSyncResponse;

        } catch (IOException e) {
            log.error(String.format("Could not perform the STK push request -> %s", e.getLocalizedMessage()));
            return null;
        }

    }

    @Override
    public void saveMpesaCallbackResponse(StkPushAsyncResponse stkPushAsyncResponse) {

        Payments payments = paymentsRepository.findByMerchantRequestID(stkPushAsyncResponse.getBody().getStkCallback().getMerchantRequestID());
        payments.setAmount(stkPushAsyncResponse.getBody().getStkCallback().getCallbackMetadata().getItem().get(0).getValue());
        payments.setPhoneNumber(stkPushAsyncResponse.getBody().getStkCallback().getCallbackMetadata().getItem().get(4).getValue());
        payments.setMpesaReceiptNumber(stkPushAsyncResponse.getBody().getStkCallback().getCallbackMetadata().getItem().get(1).getValue());
        payments.setResultDesc(stkPushAsyncResponse.getBody().getStkCallback().getResultDesc());

        Event event = eventRepository.findById(payments.getEvent().getId()).get();

        System.out.println(BigDecimal.valueOf(Double.valueOf(stkPushAsyncResponse.getBody().getStkCallback().getCallbackMetadata().getItem().get(0).getValue())));
        BigDecimal sum  = event.getContributedAmount().add(BigDecimal.valueOf(Double.valueOf(stkPushAsyncResponse.getBody().getStkCallback().getCallbackMetadata().getItem().get(0).getValue())));
        System.out.println(sum);


        event.setContributedAmount(sum);

        event.getStkPushPayments().add(payments);
        eventRepository.save(event);
        paymentsRepository.save(payments);





    }

}


