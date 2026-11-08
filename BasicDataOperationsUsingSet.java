import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Set;

/**
 * Клас BasicDataOperationsUsingSet реалізує операції з множиною TreeSet для
 * Short.
 *
 * <p>
 * Методи класу:
 * </p>
 * <ul>
 * <li>{@link #executeDataAnalysis()} - Запускає аналіз даних.</li>
 * <li>{@link #findInSet()} - Пошук значення в множині Short.</li>
 * <li>{@link #locateMinMaxInSet()} - Знаходить мінімальне і максимальне
 * значення в множині.</li>
 * <li>{@link #analyzeDuplicates()} - Порівнює кількість вхідних значень і
 * елементів множини.</li>
 * </ul>
 */
public class BasicDataOperationsUsingSet {
    Short valueToSearch;
    int sourceValueCount;
    Set<Short> valueSet;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     *
     * @param valueToSearch Значення для пошуку
     * @param valueArray    Масив Short
     */
    BasicDataOperationsUsingSet(Short valueToSearch, Short[] valueArray) {
        this.valueToSearch = valueToSearch;
        this.sourceValueCount = valueArray.length;
        this.valueSet = new TreeSet<>(Arrays.asList(valueArray));
    }

    /**
     * Запускає комплексний аналіз даних з використанням множини TreeSet.
     *
     * Метод виконує пошук, визначення граничних значень і аналіз повторів у множині
     * Short.
     */
    public void executeDataAnalysis() {
        findInSet();
        locateMinMaxInSet();
        analyzeDuplicates();

        // зберігаємо до файлу відсортовані унікальні значення з TreeSet (без повторів);
        // TreeSet уже впорядкований, тож Arrays.sort тут лише гарантує порядок для
        // будь-якої реалізації Set
        Short[] sortedSetValues = valueSet.toArray(new Short[0]);
        Arrays.sort(sortedSetValues);
        DataFileHandler.writeArrayToFile(sortedSetValues, BasicDataOperations.SORTED_FILE_PREFIX + ".set.data");
    }

    /**
     * Здійснює пошук конкретного значення в множині Short.
     */
    private void findInSet() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + valueToSearch + "' в TreeSet<Short>";
        long timeStart = System.nanoTime();

        // TreeSet зберігає елементи у збалансованому (червоно-чорному) дереві,
        // впорядкованому за compareTo(). contains() спускається від кореня:
        // на кожному вузлі порівнює шукане значення і йде вліво або вправо.
        // Тому пошук займає O(log n), а не перебирає всі елементи.
        boolean elementExists = this.valueSet.contains(valueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (elementExists) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в TreeSet");
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в TreeSet.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в множині Short.
     */
    private void locateMinMaxInSet() {
        if (valueSet == null || valueSet.isEmpty()) {
            System.out.println("TreeSet є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();

        // TreeSet уже впорядкований, тож мінімум і максимум лежать на краях дерева
        // (їх дали б first() і last()). Але Collections.min() і Collections.max()
        // про це не знають: обходять усю множину ітератором і порівнюють через
        // compareTo(); O(n).
        Short minValue = Collections.min(valueSet);
        Short maxValue = Collections.max(valueSet);

        PerformanceTracker.displayOperationTime(timeStart,
                "визначення мiнiмального i максимального значення в TreeSet<Short>");

        System.out.println("Найменше значення в TreeSet: " + minValue);
        System.out.println("Найбільше значення в TreeSet: " + maxValue);
    }

    /**
     * Порівнює кількість вхідних значень і елементів множини: різниця — це
     * відкинуті повтори.
     */
    private void analyzeDuplicates() {
        System.out.println("Кiлькiсть вхiдних значень: " + sourceValueCount);
        System.out.println("Кiлькiсть елементiв в TreeSet: " + valueSet.size());
        System.out.println("Повторних входжень, вiдкинутих TreeSet: " + (sourceValueCount - valueSet.size()));
    }
}
