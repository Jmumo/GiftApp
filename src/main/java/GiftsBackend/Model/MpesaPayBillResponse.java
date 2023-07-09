package GiftsBackend.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static jakarta.persistence.GenerationType.SEQUENCE;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "mpesa_payment_response")
@Data
public class MpesaPayBillResponse {
    @Id
    @SequenceGenerator(
            name = "Mpesa_Sequence",
            sequenceName = "Mpesa_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "Mpesa_Sequence"
    )
    private Long id;

    @JsonProperty("TransactionType")
    private String transactionType;

    @JsonProperty("TransID")
    @Column(columnDefinition = "VARCHAR(255) UNIQUE NOT NULL")
    private String transID;

    @JsonProperty("TransTime")
    private String transTime;

    @JsonProperty("TransAmount")
    private String transAmount;

    @JsonProperty("BusinessShortCode")
    private String businessShortCode;

    @JsonProperty("BillRefNumber")
    private String billRefNumber;

    @JsonProperty("InvoiceNumber")
    private String invoiceNumber;

    @JsonProperty("OrgAccountBalance")
    private String orgAccountBalance;

    @JsonProperty("ThirdPartyTransID")
    private String thirdPartyTransID;

    @JsonProperty("MSISDN")
    private String msisdn;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("MiddleName")
    private String middleName;

    @JsonProperty("LastName")
    private String lastName;

}
