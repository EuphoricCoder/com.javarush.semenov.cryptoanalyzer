package services;

import java.nio.file.Path;

public class BruteForceDecipher {
    private Path inputPath;
    private String outputFileName;
    private final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з',
            'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
            'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};

    public BruteForceDecipher(Path inputPath, String outputFileName) {
        this.inputPath = inputPath;
        this.outputFileName = outputFileName;
    }

    // Метод выполняет перебор ключей и создает отдельный файл для каждой попытки расшифровки
    public void decryptWithBruteForce() {
        for (int key = 0; key < ALPHABET.length; key++) {
            String generatedFileName = outputFileName + "_key" + key + ".txt";
            Path outputPath = Path.of(generatedFileName);
            Encryptor encryptor = new Encryptor(inputPath, outputPath, key, "2");
            encryptor.processFile();
        }
    }
}
