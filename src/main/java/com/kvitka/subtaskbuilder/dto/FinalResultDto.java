package com.kvitka.subtaskbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalResultDto {
    private IndexDto from;
    private IndexDto to;
    private double sum;
}
