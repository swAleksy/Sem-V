import java.util.List;
import java.util.zip.*;
import java.io.*;
import java.util.ArrayList;

public class Z4 {
    public static void main(String[] args) throws IOException {        
        if (args.length < 2) {
            System.out.println("Usage: java Z4 -l|-r <path>");
            return;
        }

        String flag = args[0];
        String path = args[1];

        switch (flag) {
            case "-x":
                extractFiles(path);
                break;
            case "-z":
                zipFiles(path);
                break;
            case "-ls":
                listFilesInZip(path);
                break;
            default:
                break;
        }
    }

    private static void extractFiles(String path) throws IOException  {
        String destDir = "./output";
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs(); 
    
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(path);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {
            String fileName = ze.getName();
            File newFile = new File(destDir + File.separator + fileName);
            
            File parentDir = new File(newFile.getParent());
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
            }

            if (!ze.isDirectory()) {
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
            }
            ze = zis.getNextEntry();
        }
    }
    
    private static void zipFiles(String path) throws IOException {
        List<String> srcFiles = getFileNamesFromFolder(path);


        final FileOutputStream fos = new FileOutputStream("./compressed.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
    
        zipOut.close();
        fos.close();
    }

    private static List<String> getFileNamesFromFolder(String folderPath) {
        File folder = new File(folderPath);
        List<String> fileNames = new ArrayList<>();
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    fileNames.add(file.getAbsolutePath());
                }
            }
        }
        return fileNames;
    }
    
    private static void listFilesInZip(String zipFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(zipFilePath);
                ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            System.out.println("Contents of zip:");
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }
        } catch (FileNotFoundException e) {
            System.out.println("zip not found: " + zipFilePath);
        }
    }
}
