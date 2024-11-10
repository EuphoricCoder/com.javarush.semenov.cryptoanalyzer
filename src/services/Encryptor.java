package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Encryptor {

    private final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з',
            'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
            'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};
    private Path inputPath;
    private Path outputPath;
    private int key;
    private String mode;

    public Encryptor(int key, String mode) {
        this.key = key;
        this.mode = mode;
    }

    public Encryptor(Path inputPath, Path outputPath, int key, String mode) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.key = key;
        this.mode = mode;
    }

    // Создает новый файл с зашифрованным или расшифрованным текстом
    public void processFile() {
        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            while (reader.ready()) {
                String encryptedLine = encryptString(reader.readLine() + "\n");
                writer.write(encryptedLine);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом. Возможно, проблема с кодировкой.");
            e.printStackTrace();
        }
    }

    // Создает зашифрованный или расшифрованный алфавит в зависимости от режима
    private char[] generateCipherAlphabet() {
        char[] cipherAlphabet = new char[ALPHABET.length];
        int offset = key % ALPHABET.length;
        for (int i = 0; i < ALPHABET.length; i++) {
            if (offset >= ALPHABET.length) offset = 0;
            if (mode.equals("1")) {
                cipherAlphabet[offset] = ALPHABET[i];
            } else if (mode.equals("2")) {
                cipherAlphabet[i] = ALPHABET[offset];
            }
            offset++;
        }
        return cipherAlphabet;
    }

    // Зашифровывает строку, используя шифр на основе ключа
    public String encryptString(String input) {
        String lowerCaseInput = input.toLowerCase();
        StringBuilder resultBuilder = new StringBuilder(lowerCaseInput);
        char[] cipherAlphabet = generateCipherAlphabet();

        for (int i = 0; i < resultBuilder.length(); i++) {
            for (int j = 0; j < ALPHABET.length; j++) {
                if (resultBuilder.charAt(i) == ALPHABET[j]) {
                    resultBuilder.setCharAt(i, cipherAlphabet[j]);
                    break;
                }
            }
        }
        return resultBuilder.toString();
    }
}