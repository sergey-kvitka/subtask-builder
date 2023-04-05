package com.kvitka.subtaskbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexDto {
    private int i;
    private int j;

    public void incrementI() {
        ++i;
    }

    public void incrementJ() {
        ++j;
    }

    public void decrementI() {
        --i;
    }

    public void decrementJ() {
        --j;
    }
}
