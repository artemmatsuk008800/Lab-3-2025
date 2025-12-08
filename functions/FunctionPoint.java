package functions;

public class FunctionPoint {
    private double x;
    private double y;

    public FunctionPoint(double x, double y){ // Конструктор с заданными координатами
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point){ // Конструктор копирования
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint(){ // Конструктор по умолчанию в 0,0
        this(0.0, 0.0);
    }
    // Геттеры для доступа к приватным полям
    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
    // Сеттеры для изменения приватных полей
    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }
}
