import java.io.*;
import java.util.Scanner;

public class Z5 {
    private static final String FILE_NAME = "students.ser";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Wprowadź dane studenta:");
        System.out.print("imie: ");
        String imie = scanner.nextLine();

        System.out.print("nazwisko: ");
        String nazwisko = scanner.nextLine();

        System.out.print("ID: ");
        Integer sid = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("emial: ");
        String email = scanner.nextLine();

        Student student = new Student(imie, nazwisko, sid, email);

        serializeStudent(student);

        Student deserializedStudent = deserializeStudent();
        if (deserializedStudent != null) {
            System.out.println("\nDane studenta po deserializacji:");
            System.out.println(deserializedStudent);
        }

        scanner.close();
    }

    private static void serializeStudent(Student student) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(student);
            System.out.println("Obiekt został zapisany do pliku: " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Błąd podczas serializacji: " + e.getMessage());
        }
    }

    private static Student deserializeStudent() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Student) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Błąd podczas deserializacji: " + e.getMessage());
            return null;
        }
    }
}
class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String imie;
    private String nazwisko;
    private Integer sid;
    private String email;

    // Konstruktor
    public Student(String imie, String nazwisko, Integer sid, String email) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.sid = sid;
        this.email = email;
    }

    // Gettery
    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public Integer getSid() {
        return sid;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", sid=" + sid +
                ", email='" + email + '\'' +
                '}';
    }
}
