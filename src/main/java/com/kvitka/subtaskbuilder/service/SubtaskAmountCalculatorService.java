package com.kvitka.subtaskbuilder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SubtaskAmountCalculatorService {

    @SuppressWarnings("FieldCanBeLocal")
    private final double matrixCalculation = 7_000_000. / 300;
    @SuppressWarnings("FieldCanBeLocal")
    private final long subtaskTransferTime = 300;

    public int calculateSubtasksAmount(long subMatrixAmount) {
        int subtaskAmount = 1;
        System.out.println(subMatrixAmount);
        long allSubtasksTransfer = subtaskAmount * subtaskTransferTime;
        double subtaskCalculation = ((double) subMatrixAmount / subtaskAmount) / matrixCalculation;

        while (allSubtasksTransfer < subtaskCalculation) {

            log.info("{} {}", allSubtasksTransfer, subtaskCalculation);

            allSubtasksTransfer += subtaskTransferTime;

            subtaskCalculation = ((double) subMatrixAmount / subtaskAmount) / matrixCalculation;

            ++subtaskAmount;
        }

        return subtaskAmount;
    }
}
