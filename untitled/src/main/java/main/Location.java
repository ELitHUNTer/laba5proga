package main;

/**
 * Часть адреса, описывающая текущую локацию
 */
public class Location {
    private Long x; //Поле не может быть null
    private Long y; //Поле не может быть null
    private String name; //Длина строки не должна быть больше 783, Поле не может быть null

    public Location(Long x, Long y, String name) throws Exception {
        if (x == null)
            throw new Exception("x координата не должна быть null");
        if (y == null)
            throw new Exception("y координата не должна быть null");
        if (name == null || name.length() > 783)
            throw new Exception("Длина строки должны быть не больше 783 и сама строка не должна быть null");
        this.x = x;
        this.y = y;
        this.name = name;
    }

    /**
     *
     * @return Возвращает строковое представление локации
     */
    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     *
     * @return Строковое представление локации в формате csv
     */
    public String toCSV(){
        return x.toString()+','+y.toString()+','+name;
    }
}
