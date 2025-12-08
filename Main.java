import functions.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ ARRAY ===");
        testFunction(new ArrayTabulatedFunction(-3, 3, new double[]{9, 4, 1, 0, 1, 4, 9}));

        System.out.println("\n=== ТЕСТИРОВАНИЕ LINKEDLIST ===");
        testFunction(new LinkedListTabulatedFunction(-3, 3, new double[]{9, 4, 1, 0, 1, 4, 9}));

        System.out.println("\n=== ТЕСТИРОВАНИЕ ИСКЛЮЧЕНИЙ ===");
        testExceptions();
    }

    public static void testFunction(TabulatedFunction f) {
        System.out.println("Функция: " + f.getClass().getSimpleName());

        // вывод всех точек
        System.out.println("Все точки функции:");
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println("x = " + f.getPointX(i) + "  y = " + f.getPointY(i));
        }

        System.out.println("Границы: " + f.getLeftDomainBorder() + " - " + f.getRightDomainBorder());

        // тестирование значений функции
        System.out.println("f(-2) = " + f.getFunctionValue(-2));
        System.out.println("f(-4) = " + f.getFunctionValue(-4)); // за границей

        // проверка корректной работы интерполяции
        System.out.println("Проверка интерполяции:");
        System.out.println("f(-2.5) = " + f.getFunctionValue(-2.5)); // между точками
        System.out.println("f(0.5) = " + f.getFunctionValue(0.5));   // между точками
        System.out.println("f(2.5) = " + f.getFunctionValue(2.5));   // между точками

        System.out.println("Количество точек: " + f.getPointsCount());

        // добавление точки
        try {
            FunctionPoint p = new FunctionPoint(-2.5, 6);
            f.addPoint(p);
            System.out.println("Точка (-2.5, 6) успешно добавлена");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при добавлении точки: " + e.getMessage());
        }

        // изменение точки
        try {
            FunctionPoint o = new FunctionPoint(-2.1, 6.2);
            f.setPoint(1, o);
            System.out.println("Точка с индексом 1 успешно изменена");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка при изменении точки: " + e.getMessage());
        }

        // удаление точки
        try {
            f.deletePoint(3);
            System.out.println("Точка с индексом 3 удалена");
        } catch (IllegalStateException e) {
            System.out.println("Ошибка при удалении точки: " + e.getMessage());
        }

        System.out.println("Функция после операций:");
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.println("x = " + f.getPointX(i) + "  y = " + f.getPointY(i));
        }
        System.out.println("----------------------------");
    }

    public static void testExceptions() {
        System.out.println("1. Тестирование некорректных конструкторов:");

        try {
            TabulatedFunction f1 = new ArrayTabulatedFunction(5, 3, 4); // Левая > правой
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            TabulatedFunction f2 = new LinkedListTabulatedFunction(0, 5, 1); // Мало точек
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\n2. Тестирование выхода за границы индексов:");
        TabulatedFunction func = new ArrayTabulatedFunction(0, 5, 3);

        try {
            func.getPoint(-1); // отрицательный индекс
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Ошибка индекса: " + e.getMessage());
        }

        try {
            func.getPoint(10); // индекс больше размера
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Ошибка индекса: " + e.getMessage());
        }

        System.out.println("\n3. Тестирование нарушения упорядоченности:");

        try {
            func.setPointX(1, 4.5); // попытка установить x больше следующей точки
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка порядка: " + e.getMessage());
        }

        try {
            func.setPointX(1, -0.5); // попытка установить x меньше предыдущей точки
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка порядка: " + e.getMessage());
        }

        System.out.println("\n4. Тестирование добавления точки с существующим x:");

        try {
            FunctionPoint duplicatePoint = new FunctionPoint(2.5, 10);
            func.addPoint(duplicatePoint);
            func.addPoint(duplicatePoint); // дублирование
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка дублирования: " + e.getMessage());
        }

        System.out.println("\n5. Тестирование удаления при малом количестве точек:");
        TabulatedFunction smallFunc = new ArrayTabulatedFunction(0, 2, 2);

        try {
            smallFunc.deletePoint(0); // останется 1 точка
        } catch (IllegalStateException e) {
            System.out.println("Ошибка удаления: " + e.getMessage());
        }

        System.out.println("\n6. Тестирование методов setPoint:");

        try {
            FunctionPoint badPoint = new FunctionPoint(10, 5); // x вне диапазона
            func.setPoint(0, badPoint);
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка установки: " + e.getMessage());
        }

        System.out.println("\n7. Тестирование граничных случаев:");

        try {
            func.setPointX(-5, 10); // отрицательный индекс
        } catch (FunctionPointIndexOutOfBoundsException | InappropriateFunctionPointException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            func.deletePoint(15); // несуществующий индекс
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\n8. Тестирование с LinkedListTabulatedFunction:");
        TabulatedFunction linkedFunc = new LinkedListTabulatedFunction(0, 4, 3);

        try {
            linkedFunc.addPoint(new FunctionPoint(1.5, 5)); // успешно
            linkedFunc.addPoint(new FunctionPoint(1.5, 7)); // дублирование
        } catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка в LinkedList: " + e.getMessage());
        }

        System.out.println("\n9. Дополнительные тесты исключений:");

        try {
            func.setPointY(-1, 5); // отрицательный индекс
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("Ошибка setPointY: " + e.getMessage());
        }

        try {
            TabulatedFunction emptyFunc = new ArrayTabulatedFunction(0, 10, 2);
            emptyFunc.deletePoint(0);
            emptyFunc.deletePoint(0); // попытка удалить последние точки
        } catch (IllegalStateException e) {
            System.out.println("Ошибка удаления последних точек: " + e.getMessage());
        }

        System.out.println("\n10. Тестирование корректных операций:");
        try {
            TabulatedFunction goodFunc = new LinkedListTabulatedFunction(0, 10, 5);
            goodFunc.addPoint(new FunctionPoint(7.5, 15));
            goodFunc.setPointY(2, 25);
            System.out.println("Корректные операции выполнены успешно");
            System.out.println("Точек в функции: " + goodFunc.getPointsCount());
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}