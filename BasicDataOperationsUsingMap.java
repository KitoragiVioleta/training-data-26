import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Клас BasicDataOperationsUsingMap реалізує операції з колекціями типу Map для зберігання пар ключ-значення.
 *
 * <p>Ключ - record {@link Orchid} з характеристиками орхідеї, значення - назва квіткової крамниці.</p>
 *
 * <p>Операції, які виконуються для кожного типу Map:</p>
 * <ul>
 *   <li>пошук за ключем;</li>
 *   <li>пошук за значенням (сортування списку пар + binarySearch);</li>
 *   <li>виведення пар ключ-значення на консоль до і після сортування;</li>
 *   <li>сортування за ключами;</li>
 *   <li>визначення найменшого й найбільшого ключа;</li>
 *   <li>додавання пари ключ-значення;</li>
 *   <li>видалення за ключем;</li>
 *   <li>видалення за значенням.</li>
 * </ul>
 */
public class BasicDataOperationsUsingMap {
    private static final Orchid KEY_TO_SEARCH_AND_DELETE = new Orchid("Ванда", 15);
    private static final Orchid KEY_TO_ADD = new Orchid("Пафіопедилум", 2);

    private static final String VALUE_TO_SEARCH_AND_DELETE = "Флора";
    private static final String VALUE_TO_ADD = "Букет";

    /**
     * Компаратор для сортування Map.Entry за значеннями String (назвою квіткової крамниці).
     * Використовує природний порядок рядків - String.compareTo().
     */
    private static final Comparator<Map.Entry<Orchid, String>> FLOWER_SHOP_COMPARATOR = Map.Entry.comparingByValue();

    private HashMap<Orchid, String> hashMap;
    private LinkedHashMap<Orchid, String> linkedHashMap;

    /**
     * Прапорець: true, якщо linkedHashMap заповнена у відсортованому порядку ключів.
     * Лише тоді перший і останній записи LinkedHashMap є найменшим і найбільшим ключами.
     */
    private boolean linkedHashMapSorted = false;

    /**
     * Record Orchid зберігає характеристики орхідеї: рід (genus) і кількість квіток (flowerCount).
     *
     * <p>Record автоматично генерує:</p>
     * <ul>
     *   <li>приватні final поля genus і flowerCount та канонічний конструктор;</li>
     *   <li>методи доступу genus() і flowerCount();</li>
     *   <li>equals() і hashCode(), які враховують усі компоненти record (genus і flowerCount).</li>
     * </ul>
     *
     * <p>Вручну реалізовано:</p>
     * <ul>
     *   <li>compareTo() - природний порядок: рід за зростанням, потім кількість квіток за спаданням;</li>
     *   <li>toString() - формат Orchid{genus='...', flowerCount=...}, доповнений хеш-кодом.</li>
     * </ul>
     *
     * @param genus рід орхідеї, наприклад "Фаленопсис"
     * @param flowerCount кількість квіток
     */
    public record Orchid(String genus, Integer flowerCount) implements Comparable<Orchid> {

        /**
         * Компактний конструктор перевіряє, що характеристики задані.
         * Завдяки цьому в compareTo() не потрібно окремо обробляти значення null.
         */
        public Orchid {
            Objects.requireNonNull(genus, "genus не може бути null");
            Objects.requireNonNull(flowerCount, "flowerCount не може бути null");
        }

        /**
         * Порівнює цю орхідею з іншою для визначення природного порядку сортування.
         *
         * <p>Критерій порівняння: рід (genus) за зростанням, а для однакових родів -
         * кількість квіток (flowerCount) за спаданням.</p>
         *
         * <p>Метод використовується Collections.sort() і Collections.min()/max()
         * для впорядкування ключів.</p>
         *
         * <p>Метод узгоджений з equals(): compareTo() повертає 0 тоді й лише тоді,
         * коли genus і flowerCount однакові, тобто коли equals() повертає true.</p>
         *
         * @param other орхідея для порівняння
         * @return від'ємне число, якщо ця орхідея іде раніше за other; 0, якщо вони рівні;
         *         додатне число, якщо ця орхідея іде пізніше за other
         */
        @Override
        public int compareTo(Orchid other) {
            // Спочатку порівнюємо за родом (за зростанням)
            int genusComparison = this.genus.compareTo(other.genus);
            if (genusComparison != 0) {
                return genusComparison;
            }
            // Якщо роди однакові, порівнюємо за кількістю квіток (за спаданням - other порівнюється з this)
            return other.flowerCount.compareTo(this.flowerCount);
        }

        /**
         * Повертає рядкове представлення орхідеї.
         * Хеш-код додано, щоб у логах було видно, від чого залежить порядок елементів у HashMap.
         *
         * @return рядок у форматі Orchid{genus='...', flowerCount=..., hashCode=...}
         */
        @Override
        public String toString() {
            return "Orchid{genus='" + genus + "', flowerCount=" + flowerCount + ", hashCode=" + hashCode() + "}";
        }
    }

    /**
     * Конструктор, який ініціалізує об'єкт готовими даними.
     *
     * @param hashMap HashMap з початковими даними (ключ: Orchid, значення: квіткова крамниця)
     * @param linkedHashMap LinkedHashMap з початковими даними (ключ: Orchid, значення: квіткова крамниця)
     */
    BasicDataOperationsUsingMap(HashMap<Orchid, String> hashMap, LinkedHashMap<Orchid, String> linkedHashMap) {
        this.hashMap = hashMap;
        this.linkedHashMap = linkedHashMap;
    }

    /**
     * Виконує комплекс операцій з HashMap і LinkedHashMap: пошук, виведення, сортування, додавання, видалення.
     */
    public void executeDataOperations() {
        // Спочатку працюємо з HashMap
        System.out.println("========= Операції з HashMap =========");
        System.out.println("Початковий розмір HashMap: " + hashMap.size());

        // Пошук до сортування
        findByKeyInHashMap();
        findByValueInHashMap();
        findMinMaxKeyInHashMap();

        printHashMap();
        sortHashMap();
        printHashMap();

        // Пошук після сортування
        findByKeyInHashMap();
        findByValueInHashMap();
        findMinMaxKeyInHashMap();

        addEntryToHashMap();

        removeByKeyFromHashMap();
        removeByValueFromHashMap();

        System.out.println("Кінцевий розмір HashMap: " + hashMap.size());

        // Потім обробляємо LinkedHashMap
        System.out.println("\n\n========= Операції з LinkedHashMap =========");
        System.out.println("Початковий розмір LinkedHashMap: " + linkedHashMap.size());

        // Пошук до сортування
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();
        findMinMaxKeyInLinkedHashMap();

        printLinkedHashMap();
        sortLinkedHashMap();
        printLinkedHashMap();

        // Пошук після сортування
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();
        findMinMaxKeyInLinkedHashMap();

        addEntryToLinkedHashMap();

        removeByKeyFromLinkedHashMap();
        removeByValueFromLinkedHashMap();

        System.out.println("Кінцевий розмір LinkedHashMap: " + linkedHashMap.size());
    }

    // ===== Методи для HashMap =====

    /**
     * Виводить вміст HashMap.
     * HashMap не гарантує жодного порядку елементів: він залежить від hashCode() ключів.
     */
    private void printHashMap() {
        System.out.println("\n=== Пари ключ-значення в HashMap ===");
        long timeStart = System.nanoTime();

        for (Map.Entry<Orchid, String> entry : hashMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в HashMap");
    }

    /**
     * Сортує HashMap за ключами.
     * Використовує Collections.sort() з природним порядком Orchid (Orchid.compareTo())
     * і перезаписує hashMap новою HashMap, заповненою у відсортованому порядку.
     *
     * <p>Увага: HashMap не зберігає порядок додавання, тому після сортування порядок
     * виведення не зміниться. Зберегти відсортований порядок може LinkedHashMap.</p>
     */
    private void sortHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список ключів і сортуємо за природним порядком Orchid
        List<Orchid> sortedKeys = new ArrayList<>(hashMap.keySet());
        Collections.sort(sortedKeys);

        // Створюємо нову HashMap і додаємо пари у порядку відсортованих ключів
        HashMap<Orchid, String> sortedHashMap = new HashMap<>();
        for (Orchid key : sortedKeys) {
            sortedHashMap.put(key, hashMap.get(key));
        }

        // Перезаписуємо оригінальну hashMap
        hashMap = sortedHashMap;

        PerformanceTracker.displayOperationTime(timeStart, "сортування HashMap за ключами");
    }

    /**
     * Здійснює пошук елемента за ключем в HashMap.
     * Використовує Orchid.hashCode() для вибору кошика та Orchid.equals() для порівняння ключів.
     */
    void findByKeyInHashMap() {
        long timeStart = System.nanoTime();

        boolean found = hashMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в HashMap");

        if (found) {
            String value = hashMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Квіткова крамниця: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в HashMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в HashMap.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список пар та сортуємо його за значеннями
        List<Map.Entry<Orchid, String>> entries = new ArrayList<>(hashMap.entrySet());
        Collections.sort(entries, FLOWER_SHOP_COMPARATOR);

        // Тимчасова пара для пошуку: ключ не потрібен, порівнюється лише значення
        Map.Entry<Orchid, String> searchEntry = new AbstractMap.SimpleEntry<>(null, VALUE_TO_SEARCH_AND_DELETE);

        int position = Collections.binarySearch(entries, searchEntry, FLOWER_SHOP_COMPARATOR);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в HashMap");

        if (position >= 0) {
            Map.Entry<Orchid, String> foundEntry = entries.get(position);
            System.out.println("Квіткову крамницю '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Орхідея: " + foundEntry.getKey());
        } else {
            System.out.println("Квіткова крамниця '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутня в HashMap.");
        }
    }

    /**
     * Визначає найменший і найбільший ключі в HashMap.
     * HashMap не зберігає порядок ключів, тому потрібен перебір усіх ключів - O(n),
     * і сортування HashMap цього не змінює.
     */
    void findMinMaxKeyInHashMap() {
        if (hashMap.isEmpty()) {
            System.out.println("HashMap порожня: найменшого й найбільшого ключа немає.");
            return;
        }

        long timeStart = System.nanoTime();

        Orchid minKey = Collections.min(hashMap.keySet());
        Orchid maxKey = Collections.max(hashMap.keySet());

        PerformanceTracker.displayOperationTime(timeStart, "визначення найменшого й найбільшого ключа в HashMap");

        System.out.println("Найменший ключ: " + minKey + ", найбільший ключ: " + maxKey);
    }

    /**
     * Додає новий запис до HashMap.
     */
    void addEntryToHashMap() {
        long timeStart = System.nanoTime();

        hashMap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до HashMap");

        System.out.println("Додано новий запис: орхідея='" + KEY_TO_ADD + "', квіткова крамниця='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з HashMap за ключем.
     */
    void removeByKeyFromHashMap() {
        long timeStart = System.nanoTime();

        String removedValue = hashMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з HashMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Квіткова крамниця була: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з HashMap за значенням.
     * Спочатку збирає ключі в окремий список, а потім видаляє їх: видалення під час
     * перебору entrySet() викликало б ConcurrentModificationException.
     */
    void removeByValueFromHashMap() {
        long timeStart = System.nanoTime();

        List<Orchid> keysToRemove = new ArrayList<>();
        for (Map.Entry<Orchid, String> entry : hashMap.entrySet()) {
            if (VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue())) {
                keysToRemove.add(entry.getKey());
            }
        }

        for (Orchid key : keysToRemove) {
            hashMap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з HashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з квітковою крамницею '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    // ===== Методи для LinkedHashMap =====

    /**
     * Виводить вміст LinkedHashMap.
     * LinkedHashMap зберігає порядок додавання пар: до сортування він залежить від порядку,
     * у якому Map.of() віддала пари, а після сортування збігається з порядком ключів.
     */
    private void printLinkedHashMap() {
        System.out.println("\n=== Пари ключ-значення в LinkedHashMap ===");
        long timeStart = System.nanoTime();

        for (Map.Entry<Orchid, String> entry : linkedHashMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в LinkedHashMap");
    }

    /**
     * Сортує LinkedHashMap за ключами.
     * Використовує Collections.sort() з природним порядком Orchid (Orchid.compareTo())
     * і перезаписує linkedHashMap новою LinkedHashMap, заповненою у відсортованому порядку.
     *
     * <p>LinkedHashMap запам'ятовує порядок додавання, тому після сортування пари
     * виводяться в порядку ключів (рід за зростанням, кількість квіток за спаданням).</p>
     */
    private void sortLinkedHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список ключів і сортуємо за природним порядком Orchid
        List<Orchid> sortedKeys = new ArrayList<>(linkedHashMap.keySet());
        Collections.sort(sortedKeys);

        // Створюємо нову LinkedHashMap і додаємо пари у порядку відсортованих ключів
        LinkedHashMap<Orchid, String> sortedLinkedHashMap = new LinkedHashMap<>();
        for (Orchid key : sortedKeys) {
            sortedLinkedHashMap.put(key, linkedHashMap.get(key));
        }

        // Перезаписуємо оригінальну linkedHashMap
        linkedHashMap = sortedLinkedHashMap;
        // Тепер порядок додавання збігається з порядком ключів
        linkedHashMapSorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування LinkedHashMap за ключами");
    }

    /**
     * Здійснює пошук елемента за ключем в LinkedHashMap.
     * Використовує Orchid.hashCode() для вибору кошика та Orchid.equals() для порівняння ключів.
     */
    void findByKeyInLinkedHashMap() {
        long timeStart = System.nanoTime();

        boolean found = linkedHashMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в LinkedHashMap");

        if (found) {
            String value = linkedHashMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Квіткова крамниця: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в LinkedHashMap.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInLinkedHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список пар та сортуємо його за значеннями
        List<Map.Entry<Orchid, String>> entries = new ArrayList<>(linkedHashMap.entrySet());
        Collections.sort(entries, FLOWER_SHOP_COMPARATOR);

        // Тимчасова пара для пошуку: ключ не потрібен, порівнюється лише значення
        Map.Entry<Orchid, String> searchEntry = new AbstractMap.SimpleEntry<>(null, VALUE_TO_SEARCH_AND_DELETE);

        int position = Collections.binarySearch(entries, searchEntry, FLOWER_SHOP_COMPARATOR);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в LinkedHashMap");

        if (position >= 0) {
            Map.Entry<Orchid, String> foundEntry = entries.get(position);
            System.out.println("Квіткову крамницю '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Орхідея: " + foundEntry.getKey());
        } else {
            System.out.println("Квіткова крамниця '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутня в LinkedHashMap.");
        }
    }

    /**
     * Визначає найменший і найбільший ключі в LinkedHashMap.
     * LinkedHashMap реалізує SequencedMap (Java 21), але її порядок - це порядок додавання.
     * Тому firstEntry() і lastEntry() дають найменший і найбільший ключі лише після сортування - O(1);
     * до сортування потрібен перебір усіх ключів - O(n).
     */
    void findMinMaxKeyInLinkedHashMap() {
        if (linkedHashMap.isEmpty()) {
            System.out.println("LinkedHashMap порожня: найменшого й найбільшого ключа немає.");
            return;
        }

        long timeStart = System.nanoTime();

        Orchid minKey;
        Orchid maxKey;
        if (linkedHashMapSorted) {
            // Порядок додавання збігається з порядком ключів - перебір не потрібен
            minKey = linkedHashMap.firstEntry().getKey();
            maxKey = linkedHashMap.lastEntry().getKey();
        } else {
            // Порядок додавання не пов'язаний з compareTo() - потрібен перебір
            minKey = Collections.min(linkedHashMap.keySet());
            maxKey = Collections.max(linkedHashMap.keySet());
        }

        PerformanceTracker.displayOperationTime(timeStart, "визначення найменшого й найбільшого ключа в LinkedHashMap");

        System.out.println("Найменший ключ: " + minKey + ", найбільший ключ: " + maxKey);
    }

    /**
     * Додає новий запис до LinkedHashMap.
     * Новий ключ додається в кінець, тому порядок ключів порушується і прапорець скидається.
     */
    void addEntryToLinkedHashMap() {
        long timeStart = System.nanoTime();

        linkedHashMap.put(KEY_TO_ADD, VALUE_TO_ADD);
        linkedHashMapSorted = false;

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до LinkedHashMap");

        System.out.println("Додано новий запис: орхідея='" + KEY_TO_ADD + "', квіткова крамниця='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з LinkedHashMap за ключем.
     */
    void removeByKeyFromLinkedHashMap() {
        long timeStart = System.nanoTime();

        String removedValue = linkedHashMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з LinkedHashMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Квіткова крамниця була: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з LinkedHashMap за значенням.
     * Спочатку збирає ключі в окремий список, а потім видаляє їх: видалення під час
     * перебору entrySet() викликало б ConcurrentModificationException.
     */
    void removeByValueFromLinkedHashMap() {
        long timeStart = System.nanoTime();

        List<Orchid> keysToRemove = new ArrayList<>();
        for (Map.Entry<Orchid, String> entry : linkedHashMap.entrySet()) {
            if (VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue())) {
                keysToRemove.add(entry.getKey());
            }
        }

        for (Orchid key : keysToRemove) {
            linkedHashMap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з LinkedHashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з квітковою крамницею '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    /**
     * Головний метод для запуску програми.
     */
    public static void main(String[] args) {
        // Початкові дані (ключ: Orchid, значення: квіткова крамниця).
        // Map.of() створює незмінну Map, тому з неї створюються копії, які можна змінювати.
        Map<Orchid, String> orchids = Map.of(
            new Orchid("Фаленопсис", 12), "Флора",
            new Orchid("Дендробіум", 20), "Букет",
            new Orchid("Каттлея", 5), "Лотос",
            new Orchid("Фаленопсис", 8), "Камелія",
            new Orchid("Ванда", 15), "Флора",
            new Orchid("Онцидіум", 30), "Магнолія",
            new Orchid("Дендробіум", 14), "Едельвейс",
            new Orchid("Цимбідіум", 10), "Квітковий рай",
            new Orchid("Мільтонія", 7), "Ромашка",
            new Orchid("Каттлея", 3), "Верес"
        );

        HashMap<Orchid, String> hashMap = new HashMap<>(orchids);
        LinkedHashMap<Orchid, String> linkedHashMap = new LinkedHashMap<>(orchids);

        // Створюємо об'єкт і виконуємо операції
        BasicDataOperationsUsingMap operations = new BasicDataOperationsUsingMap(hashMap, linkedHashMap);
        operations.executeDataOperations();
    }
}