import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Z2 {
    public static void main(String[] args) {
        File fileR = new File("data.txt");
        File fileS = new File("rev.txt");

        try (
            Scanner scan = new Scanner(fileR);
            FileWriter fw = new FileWriter(fileS)
        ) {
            while (scan.hasNextLine()) {
                StringBuilder res = new StringBuilder();
                String line = scan.nextLine();
                res.append(line);
                fw.write(res.reverse().toString() + System.lineSeparator());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
