package GiftsBackend.Model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@Table(name = "Payment_Tbl")
public class Payments {

    @Id
    @SequenceGenerator(
            name = "Payments_Sequence",
            sequenceName = "Payments_Sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "Payments_Sequence"
    )
    private Long id;


    private String merchantRequestID;


    private String responseCode;


    private String customerMessage;


    private String checkoutRequestID;


    private String responseDescription;

    private String MpesaReceiptNumber;

    private String PhoneNumber;

    private String Amount;

    private String ResultDesc;


    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}
