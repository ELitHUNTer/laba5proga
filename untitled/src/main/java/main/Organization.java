package main;

import java.time.LocalDate;
import java.util.HashSet;

/**
 * Класс содержащий основную информацию об организации
 */
public class Organization implements Comparable {
    private static long counter = 0;
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int annualTurnover; //Значение поля должно быть больше 0
    private String fullName; //Значение этого поля должно быть уникальным, Поле не может быть null
    private OrganizationType type; //Поле может быть null
    private Address officialAddress; //Поле может быть null

    private HashSet<String> names = new HashSet<>();

    public Organization(String name, String fullName, Long x, long y, int annualTurnover, String organizationType, String street, String zipCode, Long location_x, Long location_y, String locationName) throws Exception {
        if (name == null || name.equals(""))
            throw new Exception("Строка не может быть пустой");
        if (annualTurnover <= 0)
            throw new Exception("annualTurnover должен быть больше 0");
        if (names.contains(fullName))
            throw new Exception("полное имя должно быть уникальным");
        if (organizationType == null)
            type = null;
        else
            type = OrganizationType.valueOf(organizationType);
        coordinates = new Coordinates(x, y);
        id = ++counter;
        this.fullName = fullName;
        names.add(fullName);
        this.name = name;
        this.annualTurnover = annualTurnover;
        creationDate = LocalDate.now();
        officialAddress = new Address(street, zipCode, location_x, location_y, locationName);
    }

    /**
     * Заменить организацию на предоставленную
     * @param organization предоставленная организация
     */
    public void update(Organization organization) {
        if (organization == null) {
            return;
        }
        name = organization.name;
        coordinates = organization.coordinates;
        creationDate = organization.creationDate;
        annualTurnover = organization.annualTurnover;
        fullName = organization.fullName;
        type = organization.type;
        officialAddress = organization.officialAddress;
    }

    /**
     *
     * @return Возвращает строковое представление данной организации
     */
    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", annualTurnover=" + annualTurnover +
                ", fullName='" + fullName + '\'' +
                ", type=" + type +
                ", officialAddress=" + officialAddress +
                ", names=" + names +
                '}';
    }

    /**
     *
     * @return Строковое представление данной организации в формате csv
     */
    public String toCSV(){
        return name+','+coordinates.toCSV()+','+creationDate+','+annualTurnover+','+fullName+','+type+','+officialAddress.toCSV();
    }

    /**
     *
     * @return id данной организации
     */
    public Long getID(){
        return id;
    }

    /**
     *
     * @return AnnualTurnover данной организации
     */
    public int getAnnualTurnover(){
        return annualTurnover;
    }

    /**
     *
     * @return Возвращает тип данной организации
     */
    public OrganizationType getType() {
        return type;
    }

    /**
     *
     * @return Возвращает название данной организации
     */
    public String getName() {
        return name;
    }

    /**
     * Сравнивает 2 организации
     * @param o организация, с которой сравнивает
     * @return результат сравнения
     */
    @Override
    public int compareTo(Object o) {
        return annualTurnover - ((Organization)o).annualTurnover;
    }
}
