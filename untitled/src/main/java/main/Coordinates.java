package main;

/**
 * Класс, показывающий координаты организации
 */
public class Coordinates {
    private Long x; //Максимальное значение поля: 435, Поле не может быть null
    private long y; //Максимальное значение поля: 67

    public Coordinates(Long x, long y) throws Exception {
        if (x == null || x > 435)
            throw new Exception("x координата должна быть <435");
        if (y > 67)
            throw new Exception("y координата должна быть <67");
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return Возвращает строковое представление координат
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     *
     * @return Строковое представление координат в формате csv
     */
    public String toCSV(){
        return x.toString()+','+y;
    }
}
