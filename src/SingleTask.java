import java.time.LocalDate;
import java.time.LocalDateTime;

public class SingleTask extends Task{

    public SingleTask(String tittle, String description, LocalDateTime taskDateTime, TaskType taskType) {
        super(tittle, description, taskDateTime, taskType);
    }

    @Override
    public boolean appearsIn(LocalDate localDate) {
        return localDate.equals(this.getTaskDateTime().toLocalDate());
    }

    @Override
    public Repeatability getRepeatabilityType() {
        return Repeatability.SINGLE;
    }


}
