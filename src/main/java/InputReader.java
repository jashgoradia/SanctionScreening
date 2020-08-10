import java.util.Scanner;

public class InputReader {
    //TODO possibly add REST API input
    public String CLIInput(){
        String name;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a name");
        name = sc.nextLine();
        return name;
    }
}
