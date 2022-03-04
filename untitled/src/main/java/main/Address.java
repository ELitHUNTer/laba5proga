package main;

/**
 * Адрес организации
 */
public class Address {
    private String street; //Поле не может быть null
    private String zipCode; //Длина строки не должна быть больше 27, Поле не может быть null
    private Location town; //Поле может быть null

    public Address(String street, String zipCode, Long x, Long y, String name) throws Exception {
        if (street == null)
            throw new Exception("Улица не должна быть null");
        if (zipCode == null || zipCode.length() > 27)
            throw new Exception("Длина строки должны быть не больше 27 и сама строка не должна быть null");
        this.street = street;
        this.zipCode = zipCode;
        town = new Location(x, y, name);
    }

    /**
     *
     * @return Возвращает строковое представление адреса
     */
    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", town=" + town +
                '}';
    }

    /**
     *
     * @return Строковое представление адреса в формате csv
     */
    public String toCSV(){
        return street+','+zipCode+','+town.toCSV();
    }
}
