package com.example.bullcow.service;

public class NumberProcessor {

    //Генерирует номера для пользователя при регистрации или успешной попытке
    public static String generateNumber() {
        StringBuilder stringBuilder = new StringBuilder();

        while (stringBuilder.length() < 4) {

           int randomNumber = (int) (Math.random() * 10.0);
           String number = Integer.toString(randomNumber);

           if (stringBuilder.indexOf(number) == -1) {

               stringBuilder.append(number);
           }
        }

        return stringBuilder.toString();
    }

    /*Проверяет соответствие выбранного числа числу, загаданному генератором, возвращает строку формата "1Б2К", где
    1Б - наличие 1 цифры из выбранного числа в загаданном числе с совпадением места, 2К - наличие
    2 цифр из выбранного числа в загаданном числе без совпадения места */
    public static String verifyNumber(String selectedNumber, String generatedNumber) {

        int bullCounter = 0;
        int cowCounter = 0;
        String result;

        for (int i = 0; i < 4; i++) {

            if (selectedNumber.charAt(i) == generatedNumber.charAt(i)) {
                ++bullCounter;
            } else if (generatedNumber.indexOf(selectedNumber.charAt(i)) != -1) {
                ++cowCounter;
            }
        }

        result = bullCounter + "Б" + cowCounter + 'К';
        return result;
    }
}
