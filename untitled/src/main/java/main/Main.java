package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

class Main {

    private static PriorityQueue<Organization> organizations;
    private static Scanner consoleScanner, dataFileScanner;
    public static void main(String[] args) {
        organizations = new PriorityQueue<>();
        onStart();

        while (true){
            executeCommand(consoleScanner.nextLine(), true, consoleScanner);
        }
    }

    /**
     * Действия, выполняемые при старте программы
     */
    private static void onStart(){
        consoleScanner = new Scanner(System.in);
        try {
            dataFileScanner = new Scanner(new File("input.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Отсутствует файл с данными");
            return;
        }
        while (dataFileScanner.hasNext()){
            String[] org = dataFileScanner.nextLine().split(",");
            for (int i = 0; i< org.length; i++)
                if (org[i].equals("\\n"))
                    org[i] = null;
            if (org.length != 11){
                System.out.println("Неверный формат представления данных в строке " + (organizations.size()+1));
                continue;
            }
            try {
                organizations.add(new Organization(org[0], org[1], Long.valueOf(org[2]), Long.parseLong(org[3]), Integer.parseInt(org[4]), org[5], org[6], org[7], Long.valueOf(org[8]), Long.valueOf(org[9]), org[10]));
            } catch (Exception exception){
                System.out.println(exception.getMessage());
                System.out.println("Неверный формат представления данных в строке " + (organizations.size()+1));
            }
        }
        dataFileScanner.close();
        //System.out.println(organizations.peek().toCSV());
    }

    /**
     * Выполнить команду
     * @param command команда
     * @param showMessage нужно ли показывать сообщение в консоли
     * @param scanner Откуда считывать команду
     */
    private static void executeCommand(String command, boolean showMessage, Scanner scanner){
        CommandType commandType;
        String[] splitedCommand = command.split(" ");
        try {
            commandType = CommandType.valueOf(splitedCommand[0]);
        } catch (Exception ex){
            System.out.println("Нет такой команды\nВведите help для вывода списка команд");
            return;
        }
        switch (commandType){
            case help:
                System.out.println(
                        "help : вывести справку по доступным командам\n"+
                        "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n"+
                        "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n"+
                        "add {element} : добавить новый элемент в коллекцию\n"+
                        "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n"+
                        "remove_by_id id : удалить элемент из коллекции по его id\n"+
                        "clear : очистить коллекцию\n"+
                        "save : сохранить коллекцию в файл\n"+
                        "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n"+
                        "exit : завершить программу (без сохранения в файл)\n"+
                        "remove_head : вывести первый элемент коллекции и удалить его\n"+
                        "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n"+
                        "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n"+
                        "filter_by_annual_turnover annualTurnover : вывести элементы, значение поля annualTurnover которых равно заданному\n"+
                        "filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки\n"+
                        "print_unique_type : вывести уникальные значения поля type всех элементов в коллекции\n");
                break;
            case info:
                System.out.println("Тип: " + organizations.getClass().getName() + "\n" +
                        "Количество элементов: " + organizations.size());
                break;
            case show:
                for (Organization o: organizations) {
                    System.out.println(o);
                }
                break;
            case add:
                organizations.add(createOrganization(showMessage, scanner));
                break;
            case update:
                for (Organization o : organizations){
                    if (splitedCommand.length == 2 && o.getID() == Long.valueOf(splitedCommand[1])){
                        o.update(createOrganization(showMessage, scanner));
                    }
                }
                break;
            case remove_by_id:
                for (Organization o : organizations){
                    if (splitedCommand.length == 2 && o.getID() == Long.valueOf(splitedCommand[1])){
                        organizations.remove(o);
                        break;
                    }
                }
                break;
            case clear:
                organizations.clear();
                break;
            case save:
                try {
                    PrintWriter pw = new PrintWriter(new File("output.txt"));
                    for (Organization o : organizations){
                        pw.println(o.toCSV());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case execute_script:
                try (Scanner scriptScanner = new Scanner(new File(splitedCommand[1]))) {
                    while (scriptScanner.hasNext()){
                        executeCommand(scriptScanner.nextLine(), false, scriptScanner);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Указанного файла не существует");
                }
                break;
            case exit:
                consoleScanner.close();
                System.exit(0);
                break;
            case remove_head:
                System.out.println(organizations.peek().toString());
                break;
            case add_if_max: {
                Organization o = createOrganization(showMessage, scanner);
                Object[] obj = organizations.toArray();
                if (o.getAnnualTurnover() > ((Organization)obj[obj.length-1]).getAnnualTurnover())
                    organizations.add(o);
                break;
            }
            case add_if_min: {
                Organization o = createOrganization(showMessage, scanner);
                if (o.getAnnualTurnover() < organizations.peek().getAnnualTurnover())
                    organizations.add(o);
                break;
            }
            case filter_by_annual_turnover:
                if (splitedCommand.length == 2){
                    try {
                        int at = Integer.parseInt(splitedCommand[1]);
                        for (Organization o : organizations) {
                            if (o.getAnnualTurnover() == at)
                                System.out.println(o.toString());
                        }
                    } catch (NumberFormatException ex){
                        System.out.println("Ошибка. " + splitedCommand[1] + " не является числом");
                    }
                }
                break;
            case filter_starts_with_name:
                if (splitedCommand.length == 2)
                    for (Organization o : organizations){
                        if (o.getName().startsWith(splitedCommand[1]))
                            System.out.println(o.toString());
                    }
                else
                    System.out.println("Неверный формат команды. Введите help для просмотра списка команд");
                break;
            case print_unique_type:
                for (Organization o : organizations)
                    System.out.println(o.getType());
                break;
            default:
                System.out.println("Неверный формат команды. Введите help для просмотра списка команд");
                break;
        }
    }

    /**
     * создать организацию
     * @param showMessage показывать ли сообщения в консоли
     * @param sc Откуда считывать параметры организации
     * @return организация
     */
    private static Organization createOrganization(boolean showMessage, Scanner sc){
        Organization o;
        String name;
        String fullName;
        Long x;
        long y;
        int annualTurnover;
        String organizationType;
        String street;
        String zipCode;
        Long location_x;
        Long location_y;
        String locationName;
        if (showMessage)
            System.out.println("Введите название организации");
        name = sc.nextLine();
        if (showMessage)
            System.out.println("Введите полное название организации");
        fullName = sc.nextLine();
        if (showMessage)
            System.out.println("Введите x координату организации");
        while (true) {
            try {
                x = Long.parseLong(sc.nextLine());
                break;
            } catch (Exception exception){
                if (showMessage)
                    System.out.println("Введенные данные не могут быть интерпретированы как Long. Повторите ввод");
            }
        }

        if (showMessage)
            System.out.println("Введите y координату организации");
        while (true) {
            try {
                y = Long.parseLong(sc.nextLine());
                break;
            } catch (Exception exception){
                if (showMessage)
                    System.out.println("Введенные данные не могут быть интерпретированы как long. Повторите ввод");
            }
        }

        if (showMessage)
            System.out.println("Введите annualTurnover");
        while (true) {
            try {
                annualTurnover = Integer.parseInt(sc.nextLine());
                break;
            } catch (Exception exception) {
                if (showMessage)
                    System.out.println("Введенные данные не могут быть интерпретированы как int. Повторите ввод");
            }
        }
        if (showMessage) {
            System.out.println("Введите Тип организации(COMMERCIAL, TRUST, PRIVATE_LIMITED_COMPANY, OPEN_JOINT_STOCK_COMPANY;)");
        }
        organizationType = sc.nextLine();
        if (showMessage) {
            System.out.println("Введите улицу");
        }
        street = sc.nextLine();
        if (showMessage) {
            System.out.println("Введите zip code");
        }
        zipCode = sc.nextLine();
        if (showMessage)
            System.out.println("Введите x координату организации");
        while (true) {
            try {
                location_x = Long.parseLong(sc.nextLine());
                break;
            } catch (Exception exception){
                if (showMessage)
                    System.out.println("Введенные данные не могут быть интерпретированы как Long. Повторите ввод");
            }
        }

        if (showMessage)
            System.out.println("Введите y координату организации");
        while (true) {
            try {
                location_y = Long.parseLong(sc.nextLine());
                break;
            } catch (Exception exception){
                if (showMessage)
                    System.out.println("Введенные данные не могут быть интерпретированы как long. Повторите ввод");
            }
        }
        if (showMessage) {
            System.out.println("Введите название локации");
        }
        locationName = sc.nextLine();
        try {
            o = new Organization(name, fullName, x, y, annualTurnover, organizationType, street, zipCode, location_x, location_y, locationName);
            return o;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Типы команд
     */
    private enum CommandType{
        help("help"),
        info("info"),
        show("show"),
        add("add"),
        update("update"),
        remove_by_id("remove_by_id"),
        clear("clear"),
        save("save"),
        execute_script("execute_script"),
        exit("exit"),
        remove_head("remove_head"),
        add_if_max("add_if_max"),
        add_if_min("add_if_min"),
        filter_by_annual_turnover("filter_by_annual_turnover"),
        filter_starts_with_name("filter_starts_with_name"),
        print_unique_type("print_unique_type");

        String value;

        CommandType(String value){
            this.value = value;
        }
    }

}