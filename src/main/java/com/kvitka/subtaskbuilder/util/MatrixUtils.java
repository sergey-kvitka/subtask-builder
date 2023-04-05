package com.kvitka.subtaskbuilder.util;

import com.kvitka.subtaskbuilder.model.Base4Number;

public class MatrixUtils {

    public static long numbersAmount(Base4Number indexTo, int matrixSize) {
        int length = indexTo.length();
        if (length >= matrixSize) throw new IllegalArgumentException("Too big index");
        long longValue = indexTo.longValue() + 1;
        long result = 0;
        double multiplier = 1;
        for (int i = 1; i <= length; i++) {
            multiplier = (i == length) ? longValue : multiplier * 4;
            result += multiplier * ((long) (matrixSize - i) * (matrixSize - i));
        }
        return result;
    }

    public static long numbersAmount(Base4Number indexFrom, Base4Number indexTo, int matrixSize) {
        if (indexTo.compareTo(indexFrom) < 0) throw new IllegalArgumentException("indexFrom < indexTo");
        int fromLength = indexFrom.length();
        return numbersAmount(indexTo, matrixSize) - numbersAmount(indexFrom, matrixSize)
                + ((long) (matrixSize - fromLength) * (matrixSize - fromLength));
    }

    public static Base4Number indexToByAmount(Base4Number indexFrom, long amount, int matrixSize) {
        int length = indexFrom.length();
        long longValue = indexFrom.longValue();
        long checkedAmount = 0;
        long lengthPow = (long) Math.pow(4, length);
        long matrixNumbersAmount;
        long localAmount;

        check:
        {
            if (longValue == 0) break check;
            matrixNumbersAmount = ((long) (matrixSize - length)) * ((long) (matrixSize - length));
            localAmount = (lengthPow - longValue) * matrixNumbersAmount;
            if (amount > localAmount) {
                checkedAmount += localAmount;
                break check;
            }
            return addIndexDifference(amount, matrixNumbersAmount, indexFrom);
        }

        for (; ; ) {
            lengthPow *= 4;
            ++length;
            matrixNumbersAmount = ((long) (matrixSize - length)) * ((long) (matrixSize - length));
            localAmount = lengthPow * matrixNumbersAmount;
            if (amount < checkedAmount + localAmount) break;
            checkedAmount += localAmount;
        }
        return addIndexDifference(amount - checkedAmount, matrixNumbersAmount, new Base4Number(new byte[length]));
    }

    private static Base4Number addIndexDifference(long amount, long matrixNumbersAmount, Base4Number from) {
        long indexDifference = (long) Math.ceil(((double) amount) / matrixNumbersAmount);
        if (indexDifference > 0) --indexDifference;
        return from.add(indexDifference);
    }
}
