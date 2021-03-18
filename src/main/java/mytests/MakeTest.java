package mytests;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**Класс исполнения тестов с анотациями из пакета mytests
 * */
public class MakeTest {
    private static String ERROR_CLASS_NAME = "С именем %s не удалось найти класс";
    private static String ERROR_SUITE = "Методов с анотацией @AfterSuite и @BeforeSuite больше чем один или не одного, это не допустимо, данные методы должным быть в единичном экземпляре";
    private static String ERROR_TEST = "Должен быть один или более, метод с анотацией @Test";
    /**Исполнить тест по имени класса
     * @param testClassName Имя класса-тест
     * */
    public static void start(String testClassName) throws InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            start(Class.forName(testClassName));
        } catch (Exception e) {
            if(e instanceof ClassNotFoundException)
                throw new RuntimeException(String.format(ERROR_CLASS_NAME, testClassName));
            else
                throw e;
        }
    }
    /**Исполнить тест по типу класса теста
     * @param testClass тип класса-тест
     * */
    public static void start(Class testClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Подготовка необходимых объектов
        List<Method> methods = Arrays.asList(testClass.getMethods());
        List<Method> afterSuite = methodsOf(AfterSuite.class, methods);
        List<Method> beforeSuite = methodsOf(BeforeSuite.class, methods);
        if(afterSuite.size() != 1 || beforeSuite.size() != 1)
            throw new RuntimeException(ERROR_SUITE);
        Map<Integer, Method> tests = testsOfPriority(methods);
        if(tests.size() == 0)
            throw new RuntimeException(ERROR_TEST);
        // Создаем конструктор класса тест
        Object instance = testClass.getConstructor().newInstance();
        // Исполняем метод AfterSuite
        afterSuite.get(0).invoke(instance);
        // Исполняем методы Test согласно приоритетности
        for (int priority: tests.keySet().stream().sorted().collect(Collectors.toList())) {
            tests.get(priority).invoke(instance);
        }
        // Исполняем метод BeforeSuite
        beforeSuite.get(0).invoke(instance);
    }
    /**Получить коллекцию методов по типу анатации
     * @param annotationType Тип анатации
     * @param methods Коллекция методов которую следует отфильтровать по типу анатации
     * */
    private static List<Method> methodsOf(Class<? extends Annotation>  annotationType, List<Method> methods) {
        return methods.stream().filter(m -> Arrays.stream(m.getAnnotations())
                                                  .anyMatch(a -> a.annotationType().getName().equals(annotationType.getName())))
                               .collect(Collectors.toList());
    }
    /**Получить справочник тестов с приоритетом
     * @param methods Коллекция методов из которой следует сформировать справочник с проритетом
     * */
    private static Map<Integer, Method> testsOfPriority(List<Method> methods) {
        return methodsOf(Test.class, methods).stream().collect(Collectors.toMap(
                method -> method.getAnnotation(Test.class).priority(),
                method -> method));
    }
}
