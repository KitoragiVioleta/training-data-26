import java.util.Vector;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Клас BasicDataOperationsUsingList реалізує операції з колекціями типу Vector
 * для даних Short.
 * 
 * <p>
 * Методи класу:
 * </p>
 * <ul>
 * <li>{@link #executeDataOperations()} - Виконує комплекс операцій з
 * даними.</li>
 * <li>{@link #performArraySorting()} - Упорядковує масив елементів Short.</li>
 * <li>{@link #findInArray()} - Здійснює пошук елемента в масиві Short.</li>
 * <li>{@link #locateMinMaxInArray()} - Визначає найменше і найбільше значення в
 * масиві.</li>
 * <li>{@link #sortList()} - Сортує колекцію List з Short.</li>
 * <li>{@link #findInList()} - Пошук конкретного значення в списку.</li>
 * <li>{@link #locateMinMaxInList()} - Пошук мінімального і максимального
 * значення в списку.</li>
 * </ul>
 */
public class BasicDataOperationsUsingList {
    private boolean listSorted = false;
    private boolean arraySorted = false;
    private Short valueToSearch;
    private Short[] valueArray;
    private List<Short> valueList;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param valueToSearch Значення для пошуку
     * @param valueArray    Масив Short
     */
    BasicDataOperationsUsingList(Short valueToSearch, Short[] valueArray) {
        this.valueToSearch = valueToSearch;
        this.valueArray = valueArray.clone();
        this.valueList = new Vector<>(Arrays.asList(valueArray));
    }

    /**
     * Виконує комплексні операції з структурами даних.
     * 
     * Метод завантажує масив і список об'єктів Short,
     * здійснює сортування та пошукові операції.
     */
    public void executeDataOperations(Short lower, Short upper) {
        // спочатку працюємо з колекцією List
        System.out.println("List: до сортування");
        analyzeCondition(valueList, listSorted, lower, upper);
        findInList();
        locateMinMaxInList();

        sortList();
        System.out.println("List: після сортування");
        analyzeCondition(valueList, listSorted, lower, upper);

        findInList();
        locateMinMaxInList();

        // потім обробляємо масив Short
        System.out.println("Масив: до сортування");
        analyzeCondition(valueArray, arraySorted, lower, upper);
        findInArray();
        locateMinMaxInArray();

        performArraySorting();
        System.out.println("Масив: після сортування");
        analyzeCondition(valueArray, arraySorted, lower, upper);

        findInArray();
        locateMinMaxInArray();

        // зберігаємо до окремого файлу відсортований вміст Vector
        DataFileHandler.writeArrayToFile(valueList.toArray(new Short[0]),
                BasicDataOperations.SORTED_FILE_PREFIX + ".list.data");
    }

    /**
     * Упорядковує масив об'єктів Short за зростанням.
     * Фіксує та виводить тривалість операції сортування в наносекундах.
     */
    void performArraySorting() {
        long timeStart = System.nanoTime();

        Arrays.sort(valueArray);
        arraySorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування масиву Short[]");
    }

    /**
     * Здійснює пошук конкретного значення в масиві Short.
     */
    void findInArray() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + valueToSearch + "' в масивi Short[], "
                + (arraySorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position = -1;
        if (arraySorted) {
            position = Arrays.binarySearch(valueArray, valueToSearch);
        } else {
            for (int i = 0; i < valueArray.length; i++) {
                if (valueArray[i].compareTo(valueToSearch) == 0) {
                    position = i;
                    break;
                }
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (position >= 0) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в масивi за позицією: " + position);
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в масиві.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в масиві Short.
     */
    void locateMinMaxInArray() {
        if (valueArray == null || valueArray.length == 0) {
            System.out.println("Масив є пустим або не ініціалізованим.");
            return;
        }

        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "визначення мiнiмального i максимального значення в масивi Short[], "
                + (arraySorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        Short minValue = valueArray[0];
        Short maxValue = valueArray[0];

        if (arraySorted) {
            // у відсортованому масиві граничні значення лежать на його кінцях
            maxValue = valueArray[valueArray.length - 1];
        } else {
            for (Short currentValue : valueArray) {
                if (currentValue.compareTo(minValue) < 0) {
                    minValue = currentValue;
                }
                if (currentValue.compareTo(maxValue) > 0) {
                    maxValue = currentValue;
                }
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    /**
     * Шукає конкретне значення в колекції Vector.
     */
    void findInList() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + valueToSearch + "' в Vector<Short>, "
                + (listSorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position = -1;
        if (listSorted) {
            position = Collections.binarySearch(valueList, valueToSearch);
        } else {
            int index = 0;
            for (Short value : valueList) {
                if (value.compareTo(valueToSearch) == 0) {
                    position = index;
                    break;
                }
                index++;
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (position >= 0) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в Vector за позицією: " + position);
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в Vector.");
        }
    }

    /**
     * Визначає найменше і найбільше значення в колекції Vector.
     */
    void locateMinMaxInList() {
        if (valueList == null || valueList.isEmpty()) {
            System.out.println("Колекція Vector є пустою або не ініціалізованою.");
            return;
        }

        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "визначення мiнiмального i максимального значення в Vector<Short>, "
                + (listSorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        Short minValue;
        Short maxValue;

        if (listSorted) {
            // у відсортованому списку граничні значення лежать на його кінцях
            minValue = valueList.get(0);
            maxValue = valueList.get(valueList.size() - 1);
        } else {
            minValue = Collections.min(valueList);
            maxValue = Collections.max(valueList);
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в List: " + minValue);
        System.out.println("Найбільше значення в List: " + maxValue);
    }

    /**
     * Упорядковує колекцію List з об'єктами Short за зростанням.
     * Відстежує та виводить час виконання операції сортування.
     */
    void sortList() {
        long timeStart = System.nanoTime();

        Collections.sort(valueList);
        listSorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування Vector<Short>");
    }

    // BEGIN CONDITION
    /** вибірка даних; sorted=true лише для даних, упорядкованих за зростанням. */
    public int analyzeCondition(Short[] values, boolean sorted, Short lower, Short upper) {
        if (lower.compareTo(upper) > 0)
            throw new IllegalArgumentException("Нижня межа перевищує верхню");
        String conditionDescription = "значення в інтервалі [" + lower + "; " + upper + "] включно";
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("Масив: вибірка даних " + phase + ". Умова: " + conditionDescription);
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "вибірка даних: масив Short[], " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        Short[] matches = new Short[values.length];
        int count = 0;
        int inspected = 0;
        if (!sorted) {
            // Невпорядковані дані: перевіряємо кожне входження.
            for (Short value : values) {
                inspected++;
                if (value.compareTo(lower) >= 0 && value.compareTo(upper) <= 0)
                    matches[count++] = value;
            }
        } else {
            // Етап 1. Бінарним пошуком знаходимо перший елемент, не менший за нижню межу.
            // Масив упорядкований, тому спочатку йдуть елементи, менші за lower, а після
            // них решта.
            // Шукаємо індекс, де закінчується перша частина, і з кожним кроком
            // удвічі звужуємо проміжок [low; high), у якому цей індекс може бути.
            int low = 0;
            int high = values.length;
            while (low < high) {
                // середина проміжку; зсув >>> 1 ділить на 2 без переповнення int
                int mid = (low + high) >>> 1;
                inspected++;
                // values[mid] < lower: шуканий індекс лежить правіше mid, тож відкидаємо ліву
                // половину разом із mid;
                // інакше values[mid] може бути шуканим, тож лишаємо mid і відкидаємо праву
                // половину
                if (values[mid].compareTo(lower) < 0)
                    low = mid + 1;
                else
                    high = mid;
            }
            // Тепер low == high: це перший елемент >= lower (або values.length, якщо таких
            // немає).
            // Знадобилось близько log2(n) порівнянь замість перегляду всіх менших
            // елементів.

            // Етап 2. Від нижньої межі йдемо вправо до першого елемента, більшого за верхню
            // межу.
            // Усі елементи до нього вже >= lower, тому окремо перевіряти нижню межу не
            // треба.
            for (int i = low; i < values.length; i++) {
                Short value = values[i];
                inspected++;
                // далі всі елементи ще більші, тож решту масиву можна не переглядати
                if (value.compareTo(upper) > 0)
                    break;
                matches[count++] = value;
            }
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        // Друк результатів не входить у виміряний час пошуку.
        System.out.println("Переглянуто елементів: " + inspected);
        for (int i = 0; i < count; i++)
            System.out.println(matches[i]);
        System.out.println(
                "У масиві знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }

    /** вибірка даних; sorted=true лише для даних, упорядкованих за зростанням. */
    public int analyzeCondition(List<Short> values, boolean sorted, Short lower, Short upper) {
        if (lower.compareTo(upper) > 0)
            throw new IllegalArgumentException("Нижня межа перевищує верхню");
        String conditionDescription = "значення в інтервалі [" + lower + "; " + upper + "] включно";
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("List: вибірка даних " + phase + ". Умова: " + conditionDescription);
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "вибірка даних: Vector<Short>, " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        Short[] matches = new Short[values.size()];
        int count = 0;
        int inspected = 0;
        if (!sorted) {
            // Невпорядковані дані: перевіряємо кожне входження.
            for (Short value : values) {
                inspected++;
                if (value.compareTo(lower) >= 0 && value.compareTo(upper) <= 0)
                    matches[count++] = value;
            }
        } else {
            // Етап 1. Бінарним пошуком знаходимо перший елемент, не менший за нижню межу.
            // Список упорядкований, тому спочатку йдуть елементи, менші за lower, а після
            // них решта.
            // Шукаємо індекс, де закінчується перша частина, і з кожним кроком
            // удвічі звужуємо проміжок [low; high), у якому цей індекс може бути.
            // Доступ get(mid) швидкий (O(1)) для Vector і ArrayList; для LinkedList він
            // повільний.
            int low = 0;
            int high = values.size();
            while (low < high) {
                // середина проміжку; зсув >>> 1 ділить на 2 без переповнення int
                int mid = (low + high) >>> 1;
                inspected++;
                // values.get(mid) < lower: шуканий індекс лежить правіше mid, тож відкидаємо
                // ліву половину разом із mid;
                // інакше values.get(mid) може бути шуканим, тож лишаємо mid і відкидаємо праву
                // половину
                if (values.get(mid).compareTo(lower) < 0)
                    low = mid + 1;
                else
                    high = mid;
            }
            // Тепер low == high: це перший елемент >= lower (або values.size(), якщо таких
            // немає).
            // Знадобилось близько log2(n) порівнянь замість перегляду всіх менших
            // елементів.

            // Етап 2. Від нижньої межі йдемо вправо до першого елемента, більшого за верхню
            // межу.
            // Усі елементи до нього вже >= lower, тому окремо перевіряти нижню межу не
            // треба.
            java.util.ListIterator<Short> cursor0 = values.listIterator(low);
            while (cursor0.hasNext()) {
                Short value = cursor0.next();
                inspected++;
                // далі всі елементи ще більші, тож решту списку можна не переглядати
                if (value.compareTo(upper) > 0)
                    break;
                matches[count++] = value;
            }
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        // Друк результатів не входить у виміряний час пошуку.
        System.out.println("Переглянуто елементів: " + inspected);
        for (int i = 0; i < count; i++)
            System.out.println(matches[i]);
        System.out.println(
                "У List знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }
    // END CONDITION
}
