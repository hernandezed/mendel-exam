package com.edh.mendelexam.api.dtos.responses;

public class SumResponseDto {
    private final double sum;

    public SumResponseDto(double sum) {
        this.sum = sum;
    }

    public double getSum() {
        return sum;
    }
}
