package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class TextDecrypter {
    private Path inputPath;
    private Path outputPath;
    private Path lemmaPath;
    private static final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з',
            'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
            'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};

    public TextDecrypter(Path inputPath, Path outputPath, Path lemmaPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.lemmaPath = lemmaPath;
    }

    // Метод подбирает ключ, используя частотный анализ, и записывает расшифрованный текст в outputPath
    public void analyzeAndDecrypt() {
        try {
            HashSet<String> lemmaSet = loadLemmas();
            int bestKey = 0;
            int maxMatches = 0;

            for (int i = 0; i < ALPHABET.length; i++) {
                int matches = countMatches(i, lemmaSet);
                if (matches > maxMatches) {
                    maxMatches = matches;
                    bestKey = i;
                }
            }
            decryptFile(bestKey);
            System.out.printf("Ключ = %d. Количество совпадений = %d\n", bestKey, maxMatches);
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлом. Возможно, проблема с кодировкой.");
            e.printStackTrace();
        }
    }

    // Загружает леммы в множество для быстрого поиска
    private HashSet<String> loadLemmas() throws IOException {
        HashSet<String> lemmas = new HashSet<>();
        try (BufferedReader reader = Files.newBufferedReader(lemmaPath)) {
            while (reader.ready()) {
                lemmas.add(reader.readLine());
            }
        }
        return lemmas;
    }

    // Подсчитывает количество совпадений для текущего ключа
    private int countMatches(int key, HashSet<String> lemmas) throws IOException {
        int matchCount = 0;
        try (BufferedReader reader = Files.newBufferedReader(inputPath)) {
            Encryptor encryptor = new Encryptor(key, "2");
            while (reader.ready()) {
                String encryptedLine = encryptor.encryptString(reader.readLine());
                for (String lemma : lemmas) {
                    if (encryptedLine.contains(lemma)) {
                        matchCount++;
                    }
                }
            }
        }
        return matchCount;
    }

    // Расшифровывает файл с использованием найденного ключа и сохраняет результат
    private void decryptFile(int key) throws IOException {
        Encryptor decryptor = new Encryptor(inputPath, outputPath, key, "2");
        decryptor.processFile();
    }
}
