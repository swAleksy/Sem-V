import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

public class Z3 {
    public static void main(String[] args) throws IOException {
        UUID id = UUID.randomUUID();
        File file;
        
        if (args.length > 0){
            file = new File(args[0]);
            if (!file.exists()){
                System.out.println("brak pliku");
                return;
            }
        }
        else {
            file =  File.createTempFile("z3temp", ".tmp", new File("."));
            System.out.println("utworzono plik");
        }

        Thread readerThread = new Thread(new FileReaderThread(file));
        readerThread.setDaemon(true);
        readerThread.start();

        // Uruchomienie wątku do zapisu wiadomości
        Thread writerThread = new Thread(new FileWriterThread(file, id));
        writerThread.start();

    }
}

class FileReaderThread implements Runnable {
    private final File file;
    private long lastKnownLen = 0;

    public FileReaderThread(File file){
        this.file = file;
    }

    @Override
    public void run() {
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")){
            while (true) {
                if (raf.length() > lastKnownLen) {
                    raf.seek(lastKnownLen);
                    String line;
                    while ((line = raf.readLine()) != null){
                        System.out.println(line);
                    }
                    lastKnownLen = raf.length();
                }
            }            
        } catch (IOException e) {
            e.printStackTrace();
        }           
    }
}

class FileWriterThread implements Runnable {
    private final File file;
    private UUID id;

    public FileWriterThread(File file, UUID id){
        this.file = file;
        this.id = id;
    }

    @Override
    public void run() {
        try (FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            Scanner sc = new Scanner(System.in)){
            while (true) {
                System.out.println("wpisz wiad: ");
                String msg = sc.nextLine();

                if (msg.equalsIgnoreCase("exir")){
                    System.out.println("END");
                    break;
                }

                String timeStamp = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date());
                bw.write(String.format("[%s %s]: %s%n", timeStamp, id, msg));
                bw.flush();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        
    }
}