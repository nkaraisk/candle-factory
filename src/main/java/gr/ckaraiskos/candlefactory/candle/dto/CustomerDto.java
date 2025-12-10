package gr.ckaraiskos.candlefactory.candle.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private Long customerId;
    private String customerName;
    private String customerPhone;
}
