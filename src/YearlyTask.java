import java.time.LocalDate;
import java.time.LocalDateTime;

public class YearlyTask extends Task{

    public YearlyTask(String tittle, String description, LocalDateTime taskDateTime, TaskType taskType) {
        super(tittle, description, taskDateTime, taskType);
    }

    @Override
    public boolean appearsIn(LocalDate localDate) {
        LocalDate taskDate = getTaskDateTime().toLocalDate();
        return localDate.equals(taskDate) ||
                (localDate.isAfter(taskDate) && localDate.getDayOfMonth() == taskDate.getDayOfMonth() &&
                        localDate.getMonth().equals(taskDate.getMonth()));
    }

    @Override
    public Repeatability getRepeatabilityType() {
        return Repeatability.YEARLY;
    }
}
