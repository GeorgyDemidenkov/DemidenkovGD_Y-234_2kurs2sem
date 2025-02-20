import java.util.*;
class View {
    public String getInp() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите выражение(символы+-*/^:):");
        return sc.nextLine();
    }

    public void showRes(double res) {
        System.out.println("Результат:" + res);
    }
}
