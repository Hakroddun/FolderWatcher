import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

import static java.lang.System.out;

public class FolderWatcherTest
{
    private FolderWatcher watcher;
    private TaskMock task;

    @Before
    public void setUpWatcher()
    {
        watcher = new FolderWatcher();
        watcher.setFolderToWatch("D:\\Test\\Data");
    }

    @Test
    public void CreateFolder() throws InterruptedException, IOException
    {
        task = new TaskMock();
        watcher.setTaskToRun(task);
        WatchEvent.Kind[] kinds = new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE};
        watcher.setKind(kinds);
        Thread t1 = new Thread(() -> {
            out.println("Creating Create Watcher");
            try
            {
                watcher.WatchFolder();
            } catch (IOException | InterruptedException e)
            {
                e.printStackTrace();
            }
        });
        t1.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        Thread.sleep(10);
        out.println("Creating Folder");
        createFileFolder("D:\\Test\\Data\\CreateFile.txt");
        out.println(task.getFileName());
        Assert.assertTrue(task.isTaskExecuted());
    }

    @Test
    public void ModifyFolder() throws InterruptedException, IOException
    {
        task = new TaskMock();
        watcher.setTaskToRun(task);
        WatchEvent.Kind[] kinds = new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_MODIFY};
        watcher.setKind(kinds);
        createFileFolder("D:\\Test\\Data\\ModifyFile.txt");
        Thread t1 = new Thread(() -> {
            out.println("Creating Modify Watcher");
            try
            {
                watcher.WatchFolder();
            } catch (IOException | InterruptedException e)
            {
                e.printStackTrace();
            }
        });
        t1.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        Thread.sleep(10);
        out.println("Editing Folder");
        modifyFileFolder("D:\\Test\\Data\\ModifyFile.txt");
        out.println(task.getFileName());
        Assert.assertTrue(task.isTaskExecuted());
    }

    @Test
    public void DeleteFolder() throws InterruptedException, IOException
    {
        task = new TaskMock();
        watcher.setTaskToRun(task);
        WatchEvent.Kind[] kinds = new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_DELETE};
        watcher.setKind(kinds);
        createFileFolder("D:\\Test\\Data\\DeleteFile.txt");
        Thread t1 = new Thread(() -> {
            out.println("Creating Delete Watcher");
            try
            {
                watcher.WatchFolder();
            } catch (IOException | InterruptedException e)
            {
                e.printStackTrace();
            }
        });
        t1.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        Thread.sleep(10);
        out.println("Deleting Folder");
        deleteFileFolder("D:\\Test\\Data\\DeleteFile.txt");
        out.println(task.getFileName());
        Assert.assertTrue(task.isTaskExecuted());
    }



    @Test
    public void AllKindsFolder() throws InterruptedException, IOException
    {
        task = new TaskMock();
        watcher.setTaskToRun(task);
        WatchEvent.Kind[] kinds = new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE,StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.ENTRY_DELETE};
        watcher.setKind(kinds);
        Thread t1 = new Thread(() -> {
            out.println("Creating AllKinds Watcher");
            try
            {
                watcher.WatchFolder();
            } catch (IOException | InterruptedException e)
            {
                e.printStackTrace();
            }
        });
        t1.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        Thread.sleep(10);
        out.println("Creating Folder");
        createFileFolder("D:\\Test\\Data\\AllKindsFile.txt");
        out.println("Editing Folder");
        modifyFileFolder("D:\\Test\\Data\\AllKindsFile.txt");
        out.println("Deleting Folder");
        deleteFileFolder("D:\\Test\\Data\\AllKindsFile.txt");
        out.println(task.getFileName());
        Assert.assertEquals(5, task.getTimesRun());
    }

    private void createFileFolder(String path) throws IOException
    {
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();
    }

    private void modifyFileFolder(String path) throws IOException
    {
        FileWriter fileWriter = new FileWriter(path);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("Some Test String");
        printWriter.close();
    }

    private void deleteFileFolder(String path)
    {
        File file = new File(path);
        file.delete();
    }

    @After
    public void cleanUp()
    {
        File fileCreate = new File("D:\\Test\\Data\\CreateFile.txt");
        fileCreate.delete();
        File fileModify = new File("D:\\Test\\Data\\ModifyFile.txt");
        fileModify.delete();
        File fileDelete = new File("D:\\Test\\Data\\DeleteFile.txt");
        fileDelete.delete();
    }
}
