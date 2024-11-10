import services.BruteForceDecipher;
import services.Encryptor;
import services.TextDecrypter;
import services.InputValidator;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Шифровальщик методом Цезаря. Вот что я умею:");
        System.out.println("1. Шифрование с ключом\n" +
                "2. Расшифровка с ключом\n" +
                "3. Brute Force\n" +
                "4. Автоматическая расшифровка без ключа с помощью статистического анализа\n" +
                "0. Выход");
        System.out.println("Выберите пункт меню:");

        InputValidator validator = new InputValidator();
        String menuChoice = validator.menuItem();
        Scanner scanner = new Scanner(System.in);

        String resultMessage = switch (menuChoice) {
            case "1", "2", "3", "4" -> {
                if (menuChoice.equals("4")) {
                    System.out.println("ВНИМАНИЕ! Данный алгоритм не работает с текстом из одного слова или текста, который слишком короткий.");
                }

                System.out.println("Введите путь к файлу для шифрования/расшифровки:");
                String inputFilePath = scanner.nextLine();
                Path inputPath = Path.of(inputFilePath);
                if (!validator.fileIsRegular(inputPath) || !validator.fileExists(inputPath)) {
                    yield "Некорректный путь к файлу.";
                }
                if (validator.fileIsEmpty(inputPath)) {
                    yield "Файл для шифрования/расшифровки пуст.";
                }

                System.out.println("Введите путь для сохранения зашифрованного/расшифрованного файла:");
                String outputFilePath = scanner.nextLine();
                Path outputPath = Path.of(outputFilePath);
                if (validator.fileIsDirectory(outputPath)) {
                    yield "Некорректный путь для сохранения.";
                }
                if (validator.fileExists(outputPath)) {
                    yield "Файл по указанному пути уже существует.";
                }

                if (menuChoice.equals("1") || menuChoice.equals("2")) {
                    System.out.println("Введите ключ для шифрования/расшифровки:");
                    int encryptionKey = validator.keyInt();
                    Encryptor encryptor = new Encryptor(inputPath, outputPath, encryptionKey, menuChoice);
                    encryptor.processFile();
                } else if (menuChoice.equals("3")) {
                    System.out.println("Процесс расшифровки методом Brute Force...");
                    BruteForceDecipher bruteForceDecipher = new BruteForceDecipher(inputPath, outputFilePath);
                    bruteForceDecipher.decryptWithBruteForce();
                } else if (menuChoice.equals("4")) {
                    System.out.println("Необходим файл FrequencyList(Lemm).txt в папке service.");
                    System.out.println("Введите путь к этому файлу:");
                    String lemmFilePath = scanner.nextLine();
                    Path lemmPath = Path.of(lemmFilePath);
                    if (!validator.fileIsRegular(lemmPath) || !validator.fileExists(lemmPath)) {
                        yield "Некорректный путь к файлу частот.";
                    }
                    if (validator.fileIsEmpty(lemmPath)) {
                        yield "Файл частот пуст.";
                    }
                    System.out.println("Процесс расшифровки с использованием статистического анализа...");
                    TextDecrypter textDecrypter = new TextDecrypter(inputPath, outputPath, lemmPath);
                    textDecrypter.analyzeAndDecrypt();
                }

                yield "Процесс завершен.";
            }
            case "0" -> "Завершение работы программы.";
            default -> {
                scanner.close();
                yield "Ошибка: неверный ввод.";
            }
        };

        System.out.println(resultMessage);
    }
}
