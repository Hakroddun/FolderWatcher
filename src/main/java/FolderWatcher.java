import java.io.IOException;
import java.nio.file.*;

public class FolderWatcher
{
    Path folderToWatch;
    Task taskToRun;

    public FolderWatcher()
    {
    }

    public FolderWatcher(Path folderToWatch)
    {
        this.folderToWatch = folderToWatch;
    }

    public FolderWatcher(Path folderToWatch, Task taskToRun)
    {
        this.folderToWatch = folderToWatch;
        this.taskToRun = taskToRun;
    }

    WatchEvent.Kind[] kind = new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE};

    public void WatchFolder() throws IOException, InterruptedException
    {
        loadWatchService(getWatchService());
    }

    private void loadWatchService(WatchService watchService) throws InterruptedException
    {
        WatchKey watchKey;
        while ((watchKey = watchService.take()) != null)
        {
            for (WatchEvent<?> event : watchKey.pollEvents())
            {
                for(int i = 0; i < kind.length; i++)
                {
                    if (event.kind() == kind[i])
                    {
                        taskToRun.Execute(event.context().toString());
                    }
                }
            }
            watchKey.reset();
        }
    }

    private WatchService getWatchService() throws IOException
    {
        WatchService watchService = folderToWatch.getFileSystem().newWatchService();
        folderToWatch.register(watchService, kind);
        return watchService;
    }

    public void setFolderToWatch(String folderToWatch)
    {
        this.folderToWatch = Paths.get(folderToWatch);;
    }

    public void setTaskToRun(Task taskToRun)
    {
        this.taskToRun = taskToRun;
    }

    public void setKind(WatchEvent.Kind[] kind)
    {
        this.kind = kind;
    }


}
