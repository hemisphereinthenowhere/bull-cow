package com.example.bullcow.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NumberProcessorTest {


    @Test
    void generateNumber() {
        String generatedNumber = NumberProcessor.generateNumber();
        assertNotNull(generatedNumber);
    }

    @Test
    void verifyNumber() {
        String result1 = NumberProcessor.verifyNumber("1234", "1234");
        String result2 = NumberProcessor.verifyNumber("1234", "5678");
        assertEquals(result1, "4Б0К");
        assertEquals(result2, "0Б0К");
    }
}