package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    private class FunctionNode {
        FunctionPoint point;
        FunctionNode prev;
        FunctionNode next;

        FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }

    private FunctionNode head;
    private int pointsCount;
    private static final double Epsilon = 1e-10;

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        if (pointsCount < 2) throw new IllegalArgumentException("Количество точек должно быть не менее 2");

        initHead();
        double xStep = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * xStep;
            addNodeToTail().point = new FunctionPoint(x, 0);
        }
        this.pointsCount = pointsCount;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        if (values.length < 2) throw new IllegalArgumentException("Количество точек должно быть не менее 2");

        initHead();
        double xStep = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * xStep;
            addNodeToTail().point = new FunctionPoint(x, values[i]);
        }
        this.pointsCount = values.length;
    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2) throw new IllegalArgumentException("Количество точек должно быть не менее 2");

        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i-1].getX()) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по X");
            }
        }

        initHead();
        for (FunctionPoint point : points) {
            addNodeToTail().point = new FunctionPoint(point);
        }
        this.pointsCount = points.length;
    }

    private void initHead() {
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        pointsCount = 0;
    }

    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(null);
        FunctionNode tail = head.prev;

        tail.next = newNode;
        newNode.prev = tail;
        newNode.next = head;
        head.prev = newNode;

        pointsCount++;
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);

        if (index == pointsCount) {
            return addNodeToTail();
        }

        FunctionNode newNode = new FunctionNode(null);
        FunctionNode target = getNodeByIndex(index);
        FunctionNode prevNode = target.prev;

        prevNode.next = newNode;
        newNode.prev = prevNode;
        newNode.next = target;
        target.prev = newNode;

        pointsCount++;
        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);
        if (pointsCount < 3)
            throw new IllegalStateException("Нельзя удалить точку: должно остаться минимум 2 точки");

        FunctionNode toDelete = getNodeByIndex(index);
        toDelete.prev.next = toDelete.next;
        toDelete.next.prev = toDelete.prev;
        pointsCount--;

        return toDelete;
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount)
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);

        FunctionNode current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }

    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        if (index > 0 && x <= getPointX(index - 1) + Epsilon)
            throw new InappropriateFunctionPointException("X должен быть больше предыдущей точки");
        if (index < pointsCount - 1 && x >= getPointX(index + 1) - Epsilon)
            throw new InappropriateFunctionPointException("X должен быть меньше следующей точки");

        node.point.setX(x);
    }

    public void setPointY(int index, double y) {
        getNodeByIndex(index).point.setY(y);
    }

    public double getLeftDomainBorder() {
        return head.next.point.getX();
    }

    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() - Epsilon || x > getRightDomainBorder() + Epsilon)
            return Double.NaN;

        FunctionNode current = head.next;

        // Сначала проверяем точное совпадение
        while (current != head) {
            if (Math.abs(current.point.getX() - x) < Epsilon) {
                return current.point.getY(); // Возвращаем сразу y, если x существует
            }
            current = current.next;
        }

        // Если точного совпадения нет, ищем интервал для линейной интерполяции
        current = head.next;
        while (current != head && current.next != head) {
            double x1 = current.point.getX();
            double x2 = current.next.point.getX();

            if (x >= x1 - Epsilon && x <= x2 + Epsilon) {
                double y1 = current.point.getY();
                double y2 = current.next.point.getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.next;
        }

        return Double.NaN;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double newX = point.getX();
        FunctionNode current = head.next;

        while (current != head) {
            if (Math.abs(current.point.getX() - newX) < Epsilon)
                throw new InappropriateFunctionPointException("Точка с таким X уже существует");
            current = current.next;
        }

        int insertIndex = 0;
        current = head.next;
        while (current != head && current.point.getX() < newX - Epsilon) {
            insertIndex++;
            current = current.next;
        }

        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.point = new FunctionPoint(point);
    }

    public void deletePoint(int index) {
        deleteNodeByIndex(index);
    }

    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        double newX = point.getX();

        if (index > 0 && newX <= getPointX(index - 1) + Epsilon)
            throw new InappropriateFunctionPointException("X должен быть больше предыдущей точки");
        if (index < pointsCount - 1 && newX >= getPointX(index + 1) - Epsilon)
            throw new InappropriateFunctionPointException("X должен быть меньше следующей точки");

        node.point = new FunctionPoint(point);
    }
}