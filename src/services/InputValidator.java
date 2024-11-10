package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class InputValidator {

    // Чтение выбора пункта меню с клавиатуры, проверка на корректность
    public String menuItem() {
        Scanner scanner = new Scanner(System.in);
        String keyboardInput = scanner.nextLine();
        while (!keyboardInput.matches("[0-4]")) {  // Проверка на корректные варианты "0" - "4"
            System.out.println("Введите пункт из меню:");
            keyboardInput = scanner.nextLine();
        }
        return keyboardInput;
    }

    // Проверка, что файл является обычным файлом (а не директорией)
    public boolean fileIsRegular(Path file) {
        return Files.isRegularFile(file);
    }

    // Проверка, что файл является директорией
    public boolean fileIsDirectory(Path file) {
        return Files.isDirectory(file);
    }

    // Проверка, что файл существует
    public boolean fileExists(Path file) {
        return Files.exists(file);
    }

    // Проверка, что файл пустой
    public boolean fileIsEmpty(Path file) {
        try {
            return Files.size(file) == 0;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при проверке размера файла", e);
        }
    }

    // Чтение положительного целого числа (максимум 6 цифр) с клавиатуры с ограничением по попыткам
    public int keyInt() {
        Scanner scanner = new Scanner(System.in);
        int attempts = 3;
        String input;
        int key = 0;
        while (attempts > 0) {
            input = scanner.nextLine();
            if (input.matches("\\d{1,6}") && Integer.parseInt(input) > 0) {  // Число до 6 символов, больше 0
                key = Integer.parseInt(input);
                break;
            } else {
                attempts--;
                System.out.println("Осталось " + attempts + " попытки. Введите положительное целое число (до 6 символов):");
            }
        }
        return key;
    }
}
