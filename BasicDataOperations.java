/** Координатор пошуку точного значення та значень у заданому інтервалі. */
public class BasicDataOperations {
    static final String PATH_TO_DATA_FILE = "list/short.data";
    static final String SORTED_FILE_PREFIX = "list/Short.sorted";

    public static void main(String[] args) {
        if (args.length != 3 && args.length != 4) {
            System.err.println(
                    "Використання: java BasicDataOperations <значення> <нижня-межа> <верхня-межа> [файл-даних]");
            System.err.println("Приклад: java BasicDataOperations -2679 -10000 10000");
            System.exit(1);
            return;
        }
        Short searched;
        Short lower;
        Short upper;
        try {
            searched = Short.valueOf(args[0]);
            lower = Short.valueOf(args[1]);
            upper = Short.valueOf(args[2]);
            if (lower.compareTo(upper) > 0) {
                throw new IllegalArgumentException("Нижня межа перевищує верхню");
            }
        } catch (RuntimeException exception) {
            System.err.println("Некоректні аргументи: " + exception.getMessage());
            System.exit(1);
            return;
        }

        String file = args.length == 4 ? args[3] : PATH_TO_DATA_FILE;
        try {
            Short[] values = DataFileHandler.loadArrayFromFile(file);
            System.out.println("Точне значення для пошуку: " + searched);
            System.out.println("Завантажено значень: " + values.length);
            System.out.println("LIST");
            new BasicDataOperationsUsingList(searched, values).executeDataOperations(lower, upper);
            System.out.println("SET");
            new BasicDataOperationsUsingSet(searched, values).executeDataAnalysis();
            System.out.println("QUEUE");
            new BasicDataOperationsUsingQueue(searched, values).runDataProcessing();
        } catch (Exception exception) {
            System.err.println("Помилка обробки даних: " + exception.getMessage());
            System.exit(1);
        }
    }
}
