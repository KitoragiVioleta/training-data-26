import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Клас DataFileHandler управляє роботою з файлами даних Short.
 */
public class DataFileHandler {
    /**
     * Завантажує масив об'єктів Short з файлу.
     * 
     * @param filePath Шлях до файлу з даними.
     * @return Масив об'єктів Short.
     */
    public static Short[] loadArrayFromFile(String filePath) {
        Short[] temporaryArray = new Short[1000];
        int currentIndex = 0;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                // Видаляємо можливі невидимі символи та BOM
                currentLine = currentLine.trim().replaceAll("^\\uFEFF", "");
                if (!currentLine.isEmpty()) {
                    Short parsedValue = Short.valueOf(currentLine);
                    temporaryArray[currentIndex++] = parsedValue;
                }
            }
        } catch (IOException ioException) {
            throw new java.io.UncheckedIOException("Не вдалося прочитати " + filePath, ioException);
        }

        Short[] resultArray = new Short[currentIndex];
        System.arraycopy(temporaryArray, 0, resultArray, 0, currentIndex);

        return resultArray;
    }

    /**
     * Зберігає масив об'єктів Short у файл.
     * 
     * @param valueArray Масив об'єктів Short.
     * @param filePath   Шлях до файлу для збереження.
     */
    public static void writeArrayToFile(Short[] valueArray, String filePath) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            for (Short valueElement : valueArray) {
                fileWriter.write(valueElement.toString());
                fileWriter.newLine();
            }
        } catch (IOException ioException) {
            throw new java.io.UncheckedIOException("Не вдалося записати " + filePath, ioException);
        }
    }
}
