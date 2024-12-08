import java.util.stream.*;

public class Z1 {
    public static void main(String[] args) throws Exception {
        String[] arr = {"Polska", "Dania", "Niemcy", "Rosja", "Rumunia", "Hiszpania", "Francja", "Dania"};

        for (String i : arr) {
            if (i.charAt(0) == 'R' || i.charAt(0) == 'D'){
                System.out.println(i);
            }
        }

        Stream.of(arr).filter(e -> e.charAt(0) == 'R' || e.charAt(0) == 'D').forEach(System.out::println);
  
    }
}
