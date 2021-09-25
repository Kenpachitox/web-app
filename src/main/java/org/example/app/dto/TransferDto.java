package org.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransferDto {
    private long senderCardId;
    private long recipientCardId;
    private long amount;

}