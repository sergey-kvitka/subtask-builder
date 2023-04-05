package com.kvitka.subtaskbuilder;

import com.kvitka.subtaskbuilder.model.Base4Number;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Base4NumberTest {

    @Test
    @SuppressWarnings("EqualsWithItself")
    void base4NumberTest() {

        assertEquals("0", new Base4Number("0").toString());
        assertEquals("1", new Base4Number("1").toString());
        assertEquals("3333", new Base4Number("3333").toString());
        assertEquals("0000121", new Base4Number("0000121").toString());
        assertEquals("11020203", new Base4Number("11020203").toString());
        assertEquals("012031023", new Base4Number("012031023").toString());

        assertEquals("0", new Base4Number(new byte[]{0}).toString());
        assertEquals("1", new Base4Number(new byte[]{1}).toString());
        assertEquals("1010122", new Base4Number(new byte[]{1, 0, 1, 0, 1, 2, 2}).toString());
        assertEquals("00031223", new Base4Number(new byte[]{0, 0, 0, 3, 1, 2, 2, 3}).toString());
        assertEquals("01303102", new Base4Number(new byte[]{0, 1, 3, 0, 3, 1, 0, 2}).toString());

        assertEquals("0", new Base4Number(0L, 1).toString());
        assertEquals("00", new Base4Number(0L, 2).toString());
        assertEquals("000", new Base4Number(0L, 3).toString());
        assertEquals("013", new Base4Number(7L, 3).toString());
        assertEquals("01000", new Base4Number(64L, 5).toString());
        assertEquals("001000", new Base4Number(64L, 6).toString());
        assertEquals("00002001", new Base4Number(129L, 8).toString());
        assertEquals("000000000010000", new Base4Number(256L, 15).toString());

        assertEquals("0", Base4Number.getByLongValue(0L).toString());
        assertEquals("1", Base4Number.getByLongValue(1L).toString());
        assertEquals("10", Base4Number.getByLongValue(4L).toString());
        assertEquals("1000", Base4Number.getByLongValue(64L).toString());
        assertEquals("3333", Base4Number.getByLongValue(255L).toString());
        assertEquals("10001", Base4Number.getByLongValue(257L).toString());

        assertEquals(1, new Base4Number("1").length());
        assertEquals(1, new Base4Number(new byte[]{0}).length());
        assertEquals(7, new Base4Number("0000121").length());
        assertEquals(8, new Base4Number("11020203").length());
        assertEquals(2, new Base4Number(0L, 2).length());
        assertEquals(3, new Base4Number(0L, 3).length());
        assertEquals(3, new Base4Number(7L, 3).length());
        assertEquals(2, Base4Number.getByLongValue(4L).length());
        assertEquals(8, new Base4Number(129L, 8).length());
        assertEquals(4, Base4Number.getByLongValue(64L).length());
        assertEquals(7, new Base4Number(new byte[]{1, 0, 1, 0, 1, 2, 2}).length());

        assertEquals(0, new Base4Number(0L, 1).intValue());
        assertEquals(21027, new Base4Number("11020203").intValue());
        assertEquals(0, new Base4Number(0L, 2).longValue());
        assertEquals(256L, new Base4Number(256L, 15).longValue());
        assertEquals(7378, new Base4Number(new byte[]{0, 1, 3, 0, 3, 1, 0, 2}).longValue());
        assertEquals(0., new Base4Number("0").doubleValue());
        assertEquals(129., new Base4Number(129L, 8).doubleValue());
        assertEquals(64f, Base4Number.getByLongValue(64L).floatValue());
        assertEquals(257f, Base4Number.getByLongValue(257L).floatValue());
        assertEquals(16383f, new Base4Number(new byte[]{3, 3, 3, 3, 3, 3, 3}).floatValue());

        Base4Number A1 = new Base4Number("000010");
        Base4Number a1 = new Base4Number("00333");
        Base4Number A2 = new Base4Number("00000");
        Base4Number a2 = new Base4Number("3333");
        Base4Number A3 = new Base4Number("12032023");
        Base4Number a3 = new Base4Number("12031023");
        Base4Number B1 = new Base4Number(4L, 6);
        Base4Number b1 = new Base4Number(63, 5);
        Base4Number B2 = new Base4Number(0L, 5);
        Base4Number b2 = Base4Number.getByLongValue(255L);
        Base4Number B3 = new Base4Number(new byte[]{1, 2, 0, 3, 2, 0, 2, 3});
        Base4Number b3 = new Base4Number(new byte[]{1, 2, 0, 3, 1, 0, 2, 3});

        assertTrue((A1).compareTo(a1) > 0);
        assertTrue((A2).compareTo(a2) > 0);
        assertTrue((A3).compareTo(a3) > 0);
        assertTrue((B1).compareTo(b1) > 0);
        assertTrue((B2).compareTo(b2) > 0);
        assertTrue((B3).compareTo(b3) > 0);

        assertTrue((a1).compareTo(A1) < 0);
        assertTrue((a2).compareTo(A2) < 0);
        assertTrue((a3).compareTo(A3) < 0);
        assertTrue((b1).compareTo(B1) < 0);
        assertTrue((b2).compareTo(B2) < 0);
        assertTrue((b3).compareTo(B3) < 0);

        assertEquals(0, (A1).compareTo(A1));
        assertEquals(0, (A2).compareTo(A2));
        assertEquals(0, (A3).compareTo(A3));
        assertEquals(0, (a1).compareTo(a1));
        assertEquals(0, (a2).compareTo(a2));
        assertEquals(0, (a3).compareTo(a3));
        assertEquals(0, (B1).compareTo(B1));
        assertEquals(0, (B2).compareTo(B2));
        assertEquals(0, (B3).compareTo(B3));
        assertEquals(0, (b1).compareTo(b1));
        assertEquals(0, (b2).compareTo(b2));
        assertEquals(0, (b3).compareTo(b3));

        assertEquals(0, (B1).compareTo(A1));
        assertEquals(0, (B2).compareTo(A2));
        assertEquals(0, (B3).compareTo(A3));
        assertEquals(0, (b1).compareTo(a1));
        assertEquals(0, (b2).compareTo(a2));
        assertEquals(0, (b3).compareTo(a3));
        assertEquals(0, (B1).compareTo(A1));
        assertEquals(0, (B2).compareTo(A2));
        assertEquals(0, (B3).compareTo(A3));
        assertEquals(0, (b1).compareTo(a1));
        assertEquals(0, (b2).compareTo(a2));
        assertEquals(0, (b3).compareTo(a3));

        assertEquals(0, new Base4Number("0").subtract(new Base4Number("0")));
        assertEquals(2, new Base4Number("01").subtract(new Base4Number("3")));
        assertEquals(0, new Base4Number("000").subtract(new Base4Number("000")));
        assertEquals(7, new Base4Number("0003").subtract(new Base4Number("330")));
        assertEquals(64, new Base4Number("0000").subtract(new Base4Number("000")));
        assertEquals(340, new Base4Number("00000").subtract(new Base4Number("0")));
        assertEquals(200, new Base4Number("02200").subtract(new Base4Number("3120")));
        assertEquals(638, new Base4Number("21003").subtract(new Base4Number("3011")));
        assertEquals(1024, new Base4Number("001000").subtract(new Base4Number("01000")));
        assertEquals(1849, new Base4Number("312123").subtract(new Base4Number("121202")));

        assertEquals("0", new Base4Number("0").add(0).toString());
        assertEquals("01", new Base4Number("3").add(2).toString());
        assertEquals("000", new Base4Number("000").add(0).toString());
        assertEquals("0003", new Base4Number("330").add(7).toString());
        assertEquals("0000", new Base4Number("000").add(64).toString());
        assertEquals("00000", new Base4Number("0").add(340).toString());
        assertEquals("02200", new Base4Number("3120").add(200).toString());
        assertEquals("21003", new Base4Number("3011").add(638).toString());
        assertEquals("001000", new Base4Number("01000").add(1024).toString());
        assertEquals("312123", new Base4Number("121202").add(1849).toString());
    }
}