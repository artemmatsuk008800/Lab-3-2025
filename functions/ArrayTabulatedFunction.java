package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] points;
    private int pointsCount;
    private static final double Epsilon = 1e-10;

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        this.points = new FunctionPoint[pointsCount];
        this.pointsCount = pointsCount;
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++){
            double x = leftX + i * step;
            this.points[i] = new FunctionPoint(x, 0.0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        int pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount];
        this.pointsCount = pointsCount;
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++){
            double x = leftX + i * step;
            this.points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder(){
        return points[pointsCount - 1].getX();
    }

    public double getFunctionValue(double x){
        if (x < getLeftDomainBorder() - Epsilon || x > getRightDomainBorder() + Epsilon) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount - 1; i++){
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();

            if (x >= x1 - Epsilon && x <= x2 + Epsilon){
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();

                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        return Double.NaN;
    }

    public int getPointsCount(){
        return pointsCount;
    }

    public FunctionPoint getPoint(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);
        }
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);
        }

        double newX = point.getX();
        if (index > 0 && newX <= points[index - 1].getX() + Epsilon) {
            throw new InappropriateFunctionPointException("X точки должен быть больше предыдущей точки");
        }
        if (index < pointsCount - 1 && newX >= points[index + 1].getX() - Epsilon) {
            throw new InappropriateFunctionPointException("X точки должен быть меньше следующей точки");
        }

        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);
        }
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);
        }

        if (index > 0 && x <= points[index - 1].getX() + Epsilon) {
            throw new InappropriateFunctionPointException("X точки должен быть больше предыдущей точки");
        }
        if (index < pointsCount - 1 && x >= points[index + 1].getX() - Epsilon) {
            throw new InappropriateFunctionPointException("X точки должен быть меньше следующей точки");
        }

        points[index].setX(x);
    }

    public double getPointY(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);
        }
        return points[index].getY();
    }

    public void setPointY(int index, double y){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);
        }
        points[index].setY(y);
    }

    public void deletePoint(int index){
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс выходит за границы: " + index);
        }
        if (pointsCount <= 2) {
            throw new IllegalStateException("Нельзя удалить точку: должно остаться минимум 2 точки");
        }

        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double newX = point.getX();
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(points[i].getX() - newX) < Epsilon) {
                throw new InappropriateFunctionPointException("Точка с таким X уже существует");
            }
        }

        if (pointsCount == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length * 2];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        int insertIndex = 0;
        while (insertIndex < pointsCount && points[insertIndex].getX() < newX - Epsilon) {
            insertIndex++;
        }

        System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);

        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }
}
