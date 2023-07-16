package GiftsBackend.Dtos;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContributeEventDto {

    private Long EventID;
    private String PaymentReff;
    private BigDecimal amount;

}
