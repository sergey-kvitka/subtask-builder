package com.kvitka.subtaskbuilder.service;

import com.kvitka.subtaskbuilder.dto.FinalResultDto;
import com.kvitka.subtaskbuilder.dto.IndexDto;
import com.kvitka.subtaskbuilder.dto.SubtaskDto;
import com.kvitka.subtaskbuilder.model.Base4Number;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskPartitionService {

    @Value("${properties.manager-url}")
    private String managerUrl;
    @Value("${properties.subtask-method}")
    private String sendSubtasksMethod;

    private final SubtaskAmountCalculatorService subtaskAmountCalculatorService;
    private final SubtaskRegistryService subtaskRegistryService;
    private final RestTemplate restTemplate;

    public int splitAndSend(String jsonMatrixString, int matrixSize) {

        int subtasksAmount = subtaskAmountCalculatorService.calculateSubtasksAmount(matrixSize);

        subtaskRegistryService.registerSubtasks(subtasksAmount, matrixSize);

        List<SubtaskDto> subtasks = new ArrayList<>();

        Base4Number from = new Base4Number(new byte[]{0});
        byte[] bytesTo = new byte[matrixSize - 1];
        for (int i = 0; i < matrixSize - 1; i++) bytesTo[i] = 3;
        Base4Number to = new Base4Number(bytesTo);

        long subtaskSize = to.subtract(from) / subtasksAmount;

        Base4Number currentFrom = from;
        Base4Number currentTo;
        for (int i = 1; i <= subtasksAmount; i++) {
            currentTo = (i == subtasksAmount) ? to : currentFrom.add(subtaskSize);
            subtasks.add(new SubtaskDto(List.of(currentFrom.toString(), currentTo.toString(), jsonMatrixString)));
            currentFrom = currentTo.add(1);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.put(managerUrl + sendSubtasksMethod, new HttpEntity<>(subtasks, headers));

        return subtasksAmount;
    }

    public FinalResultDto getResult() {
        if (!subtaskRegistryService.isDone()) throw new RuntimeException();
        List<List<String>> results = subtaskRegistryService.getResults();
        double max = Long.MIN_VALUE;
        String index = null;
        for (List<String> result : results) {
            double sum = Double.parseDouble(result.get(1));
            if (sum > max) {
                max = sum;
                index = result.get(0);
            }
        }
        return resultFromIndexAndMax(index, max, subtaskRegistryService.getMatrixSize());
    }

    // ? 1st arg - answer (index), 2nd - sum, 3rd - time (ms)
    private FinalResultDto resultFromIndexAndMax(String index, double max, int matrixSize) {
        IndexDto from = new IndexDto(0, 0);
        IndexDto to = new IndexDto(matrixSize - 1, matrixSize - 1);
        FinalResultDto result = new FinalResultDto(from, to, max);
        if ("-1".equals(index)) return result;
        byte[] bytes = new Base4Number(index).getValue();
        for (byte b : bytes) {
            if (b == 0) {
                to.decrementI();
                to.decrementJ();
            } else if (b == 1) {
                to.decrementI();
                from.incrementJ();
            } else if (b == 2) {
                from.incrementI();
                from.incrementJ();
            } else if (b == 3) {
                from.incrementI();
                to.decrementJ();
            }
        }
        return result;
    }
}