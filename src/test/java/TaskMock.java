public class TaskMock implements Task
{
    private boolean taskExecuted = false;
    private String fileName = "";
    private int timesRun = 0;

    @Override
    public void Execute(String fileName)
    {
        timesRun++;
        this.fileName = fileName;
        this.taskExecuted = true;
    }

    public boolean isTaskExecuted()
    {
        return taskExecuted;
    }

    public String getFileName()
    {
        return fileName;
    }

    public int getTimesRun()
    {
        return timesRun;
    }
}
