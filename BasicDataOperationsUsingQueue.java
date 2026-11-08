import java.util.Queue;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayDeque;

/**
 * Клас BasicDataOperationsUsingQueue реалізує роботу з колекціями типу Queue
 * для Short.
 *
 * <p>
 * Основні функції класу:
 * </p>
 * <ul>
 * <li>{@link #runDataProcessing()} - Запускає комплекс операцій з даними.</li>
 * <li>{@link #findInQueue()} - Пошук значення в черзі Short.</li>
 * <li>{@link #locateMinMaxInQueue()} - Знаходить граничні значення в
 * черзі.</li>
 * <li>{@link #performQueueOperations()} - Виконує операції peek і poll з
 * чергою.</li>
 * </ul>
 *
 */
public class BasicDataOperationsUsingQueue {
    private Short valueToSearch;
    private Queue<Short> valueQueue;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     *
     * @param valueToSearch Значення для пошуку
     * @param valueArray    Масив Short
     */
    BasicDataOperationsUsingQueue(Short valueToSearch, Short[] valueArray) {
        this.valueToSearch = valueToSearch;
        this.valueQueue = new ArrayDeque<>(Arrays.asList(valueArray));
    }

    /**
     * Запускає комплексну обробку даних з використанням черги.
     *
     * Метод виконує пошук і визначення граничних значень у черзі Short,
     * а потім операції peek і poll.
     */
    public void runDataProcessing() {
        findInQueue();
        locateMinMaxInQueue();

        // зберігаємо до файлу відсортовані значення з ArrayDeque (з повторами);
        // черга зберігає порядок додавання, а не порядок значень, тому сортуємо копію
        // її вмісту
        Short[] sortedQueueValues = valueQueue.toArray(new Short[0]);
        Arrays.sort(sortedQueueValues);
        DataFileHandler.writeArrayToFile(sortedQueueValues, BasicDataOperations.SORTED_FILE_PREFIX + ".queue.data");

        // Вилучення виконуємо після пошуку і запису файлу, щоб вони працювали з повним
        // складом черги.
        performQueueOperations();
    }

    /**
     * Здійснює пошук конкретного значення в черзі Short.
     */
    private void findInQueue() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + valueToSearch + "' в ArrayDeque<Short>";
        // вимірюємо час пошуку в черзі
        long timeStart = System.nanoTime();

        // ArrayDeque зберігає елементи в циклічному масиві в порядку додавання (FIFO),
        // без жодного впорядкування за значенням. Тому бінарний пошук неможливий:
        // contains() перебирає елементи від голови до хвоста й порівнює через equals();
        // O(n).
        boolean elementExists = this.valueQueue.contains(valueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (elementExists) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в Queue");
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в Queue.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в черзі Short.
     */
    private void locateMinMaxInQueue() {
        if (valueQueue == null || valueQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        // відстежуємо час пошуку граничних значень
        long timeStart = System.nanoTime();

        // Елементи ArrayDeque не впорядковані за значенням, тому найменше і найбільше
        // можуть бути будь-де. Collections.min() і Collections.max() обходять усі
        // елементи черги ітератором і порівнюють їх через compareTo(); кожен виклик
        // O(n).
        Short minValue = Collections.min(valueQueue);
        Short maxValue = Collections.max(valueQueue);

        PerformanceTracker.displayOperationTime(timeStart,
                "визначення мiнiмального i максимального значення в ArrayDeque<Short>");

        System.out.println("Найменше значення в Queue: " + minValue);
        System.out.println("Найбільше значення в Queue: " + maxValue);
    }

    /**
     * Виконує операції peek і poll з чергою Short.
     */
    private void performQueueOperations() {
        if (valueQueue == null || valueQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        // peek() повертає голову черги, тобто елемент, доданий першим, не видаляючи
        // його; O(1).
        Short headElement = valueQueue.peek();
        System.out.println("Головний елемент черги (peek): " + headElement);

        // poll() видаляє голову черги; новою головою стає наступний за порядком
        // додавання елемент; O(1).
        headElement = valueQueue.poll();
        System.out.println("Видалений елемент черги (poll): " + headElement);

        headElement = valueQueue.peek();
        System.out.println("Новий головний елемент черги: " + headElement);
    }
}
