import java.time.LocalDate;
import java.time.LocalDateTime;

public class DailyTask extends Task{

    public DailyTask(String tittle, String description, LocalDateTime taskDateTime, TaskType taskType) {
        super(tittle, description, taskDateTime, taskType);
    }

    @Override
    public boolean appearsIn(LocalDate localDate) {
        LocalDate taskDate = this.getTaskDateTime().toLocalDate();
        return localDate.equals(taskDate) || localDate.isAfter(taskDate);
    }

    @Override
    public Repeatability getRepeatabilityType() {
        return Repeatability.DAILY;
    }
}