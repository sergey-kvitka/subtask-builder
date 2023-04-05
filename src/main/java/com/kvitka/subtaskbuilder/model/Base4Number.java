package com.kvitka.subtaskbuilder.model;

import java.util.Arrays;

public class Base4Number extends Number implements Comparable<Base4Number> {

    private static final Base4Number ZERO = new Base4Number(new byte[]{0});

    private final byte[] value;

    public Base4Number(String string) {
        value = bytesFromString(string);
    }

    public Base4Number(byte[] bytes) {
        int length = bytes.length;
        value = new byte[length];
        System.arraycopy(bytes, 0, value, 0, length);
    }

    public Base4Number(long decimalValue, int length) {
        value = new byte[length];
        int i = length - 1;
        while (decimalValue > 0) {
            value[i--] = (byte) (decimalValue % 4);
            decimalValue /= 4;
        }
    }

    public Base4Number(Base4Number base4Number) {
        this(base4Number.value);
    }

    public static Base4Number getByLongValue(long decimalValue) {
        int i = 1;
        int value = 4;
        while (decimalValue >= value) {
            value *= 4;
            i++;
        }
        return new Base4Number(decimalValue, i);
    }

    public byte[] getValue() {
        byte[] result = new byte[value.length];
        System.arraycopy(value, 0, result, 0, value.length);
        return result;
    }

    public int length() {
        return value.length;
    }

    private long toBase10Number() {
        int length = length();
        long result = 0;
        int multiplier = 1;
        for (int i = length - 1; i >= 0; i--) {
            result += (long) value[i] * multiplier;
            multiplier *= 4;
        }
        return result;
    }

    public long subtract(Base4Number number) {
        int comparison = this.compareTo(number);
        if (comparison == 0) return 0;
        if (comparison < 0) throw new IllegalArgumentException("Number to subtract must not be greater than this");

        int lenX = number.length();
        int lenY = this.length();

        if (lenX == lenY) return this.longValue() - number.longValue();
        long result = 0;
        for (int i = lenX + 1; i < lenY; i++) {
            result += Math.pow(4, i);
        }
        result += this.longValue() + 1;
        result += Math.pow(4, lenX) - number.longValue() - 1;

        return result;
    }

    public Base4Number add(long number) {
        if (number == 0) return new Base4Number(this.value);
        if (number < 0) throw new IllegalArgumentException("Number to add must not be less than zero");

        long sum = this.subtract(ZERO) + number;
        if (sum == 0) return ZERO;

        int length = 1;
        long newNumber = 0;
        long fourPower = 1;

        for (; ; ) {
            fourPower *= 4;
            if (newNumber + fourPower > sum) break;
            ++length;
            newNumber += fourPower;
        }
        byte[] result = new byte[length];
        int resLength = length;
        sum -= newNumber;
        if (sum == 0) return new Base4Number(result);
        byte[] sumBytes = Base4Number.getByLongValue(sum).value;
        length = sumBytes.length;

        boolean incrementNextDigit = false;
        byte currentDigit;
        int i;
        for (i = 1; i <= length; i++) {
            currentDigit = (byte) (sumBytes[length - i] + (incrementNextDigit ? 1 : 0));
            incrementNextDigit = currentDigit == 4;
            result[resLength - i] = (byte) (currentDigit % 4);
        }
        if (incrementNextDigit) result[resLength - i] = 1;
        return new Base4Number(result);
    }

    @Deprecated
    public Base4Number oldAdd(long number) {
        if (number == 0) return new Base4Number(this.value);
        if (number < 0) throw new IllegalArgumentException("Number to add must not be less than zero");

        byte[] _x = getByLongValue(number).value;
        int xLen = _x.length;

        byte[] _y = this.value;
        int yLen = _y.length;

        int length = Math.max(xLen, yLen);

        byte[] x = new byte[length];
        System.arraycopy(_x, 0, x, length - xLen, xLen);

        byte[] y = new byte[length];
        System.arraycopy(_y, 0, y, length - yLen, yLen);

        byte[] result = new byte[length];

        int currentIndex = length - 1;
        boolean incrementNextDigit = false;
        byte currentDigit;

        while (true) {
            if (currentIndex < 0 && incrementNextDigit) {
                result = addToBeginning(result, (byte) 0);
                break;
            }
            if (currentIndex < 0) break;

            currentDigit = (byte) (x[currentIndex] + y[currentIndex] + (incrementNextDigit ? 1 : 0));
            incrementNextDigit = (currentDigit / 4 == 1);
            result[currentIndex--] = (byte) (currentDigit % 4);
        }

        return new Base4Number(result);
    }

    private static byte[] bytesFromString(String string) {
        int length = string.length();
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = (byte) Character.getNumericValue(string.charAt(i));
        }
        return result;
    }

    @Override
    public int intValue() {
        return (int) toBase10Number();
    }

    @Override
    public long longValue() {
        return toBase10Number();
    }

    @Override
    public float floatValue() {
        return toBase10Number();
    }

    @Override
    public double doubleValue() {
        return toBase10Number();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (byte b : value) result.append(b);
        return result.toString();
    }

    @Override
    public int compareTo(Base4Number o) {
        int thisLength = length();
        int thatLength = o.length();

        if (thisLength < thatLength) return -1;
        if (thisLength > thatLength) return 1;

        for (int i = 0; i < thisLength; i++) {
            int x = this.value[i];
            int y = o.value[i];
            if (x < y) return -1;
            if (x > y) return 1;
        }
        return 0;
    }

    @SuppressWarnings("SameParameterValue")
    private static byte[] addToBeginning(byte[] elements, byte element) {
        int length = elements.length;
        byte[] newArray = Arrays.copyOf(elements, length + 1);
        newArray[0] = element;
        System.arraycopy(elements, 0, newArray, 1, length);
        return newArray;
    }
}
