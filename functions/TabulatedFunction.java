package functions;

public interface TabulatedFunction {
    int getPointsCount();
    double getPointX(int index);
    double getPointY(int index);
    void setPointX(int index, double x) throws InappropriateFunctionPointException;
    void setPointY(int index, double y);
    double getLeftDomainBorder();
    double getRightDomainBorder();
    double getFunctionValue(double x);
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
    void deletePoint(int index);
    FunctionPoint getPoint(int index);
    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;
}