import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Scanner;


public class Main {

    private final static Schedule SCHEDULE = new Schedule();
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.MM.yyyy");
    private final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            label:
            while (true) {
                printMenu();
                System.out.print("Выберите пункт меню: ");
                if (scanner.hasNextInt()) {
                    int menu = scanner.nextInt();
                    switch (menu) {
                        case 1:
                            addTask(scanner);
                            break;
                        case 2:
                            removeTask(scanner);
                            break;
                        case 3:
                            printTaskForDate(scanner);
                            break;
                        case 0:
                            break label;
                    }
                } else {
                    scanner.next();
                    System.out.println("Выберите пункт меню из списка!");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println(
                """
                        1. Добавить задачу
                        2. Удалить задачу
                        3. Получить задачу на указанный день
                        0. Выход
                        """
        );
    }
    //Добавить задачу
    private static void addTask(Scanner scanner){
        String tittle = readString("Введите название задачи: ", scanner);
        String description = readString("Введите описание задачи: ", scanner);
        LocalDateTime taskDate = readDateTime(scanner);
        TaskType taskType = readType(scanner);
        Repeatability repeatability = readRepeatability(scanner);
        Task task = switch (repeatability){
            case SINGLE -> new SingleTask(tittle,description,taskDate,taskType);
            case DAILY -> new DailyTask(tittle,description,taskDate,taskType);
            case WEEKLY -> new WeeklyTask(tittle,description,taskDate,taskType);
            case MONTHLY -> new MonthlyTask(tittle,description,taskDate,taskType);
            case YEARLY -> new YearlyTask(tittle,description,taskDate,taskType);
        };
        SCHEDULE.addTask(task);
    }

    private static Repeatability readRepeatability(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Выберите тип повторяемости: ");
                for (Repeatability repeatability : Repeatability.values()) {
                    System.out.println(repeatability.ordinal() + ", " + localizeRepeatability(repeatability));
                }
                System.out.print("Введите тип повторяемости задачи: ");
                String ordinalLine = scanner.nextLine();
                int ordinal = Integer.parseInt(ordinalLine);
                return Repeatability.values()[ordinal];
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный тип повторяемости");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Тип повторяемости не найден");
            }
        }
    }

    private static TaskType readType(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Выберите тип задачи: ");
                for (TaskType taskType : TaskType.values()) {
                    System.out.println(taskType.ordinal() + ", " + localizeType(taskType));
                }
                System.out.print("Введите тип задачи: ");
                String ordinalLine = scanner.nextLine();
                int ordinal = Integer.parseInt(ordinalLine);
                return TaskType.values()[ordinal];
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный номер типа задачи");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Тип задачи не найден");
            }
        }
    }

    private static LocalDateTime readDateTime(Scanner scanner) {
        LocalDate localDate = readDate(scanner);
        LocalTime localTime = readTime(scanner);
        return localDate.atTime(localTime);
    }


    private static String readString(String message, Scanner scanner) {
        while (true) {
            System.out.printf(message);
            String readString = scanner.nextLine();
            if (readString == null || readString.isBlank()) {
                System.out.println("Введено пустое значение");
            } else {
                return readString;
            }
        }
    }


    //удаление задачи
    public static void removeTask(Scanner scanner) {
        System.out.println("Все задачи:");
        for (Task task : SCHEDULE.getAllTasks()) {
            System.out.printf("%d. %s [%s](%s)%n",
                    task.getId(),
                    task.getTittle(),
                    localizeType(task.getTaskType()),
                    localizeRepeatability(task.getRepeatabilityType()));
        }
        while (true) {
            try {
                System.out.print("Выберите задачу для удаления: ");
                String idLine = scanner.nextLine();
                int id = Integer.parseInt(idLine);
                SCHEDULE.removeTask(id);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный id задачи");
            } catch (TaskNotFoundException e) {
                System.out.println("Задача для удаления не найдена");
            }
        }
    }

    //Задача на указанный день
    public static void printTaskForDate(Scanner scanner) {
        LocalDate localDate = readDate(scanner);
        Collection<Task> taskForDate = SCHEDULE.getTasksForDate(localDate);
        System.out.println("Задачи на " + localDate.format(DATE_TIME_FORMATTER));
        for (Task task : taskForDate) {
            System.out.printf("[%s]%s: %s (%s)%n",
                    localizeType(task.getTaskType()),
                    task.getTittle(),
                    task.getTaskDateTime().format(TIME_FORMATTER),
                    task.getDescription());
        }

    }


    private static LocalDate readDate(Scanner scanner) {
        while (true) {
            try {
                System.out.printf("Введите дату задачи в формате dd.mm.yyyy: ");
                String dateLine = scanner.nextLine();
                return LocalDate.parse(dateLine, DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Введена дата в неверном формате");
            }

        }

    }

    private static LocalTime readTime(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Введите время задачи в формате hh.mm : ");
                String dateLine = scanner.nextLine();
                return LocalTime.parse(dateLine, TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Введено время в неверном формате");
            }

        }

    }

    private static String localizeType(TaskType taskType) {
        return switch (taskType) {
            case WORK -> "Рабочая задача";
            case PERSONAL -> "Персональная задача";
            default -> "Неизвестная задача";
        };
    }

    private static String localizeRepeatability(Repeatability repeatability) {
        return switch (repeatability) {
            case SINGLE -> "Однокраная задача";
            case DAILY -> "Ежедневная задача";
            case WEEKLY -> "Еженедельная задача";
            case MONTHLY -> "Ежемесячная задача";
            case YEARLY -> "Ежегодная задача";
            default -> "Неизвестно";
        };
    }
}