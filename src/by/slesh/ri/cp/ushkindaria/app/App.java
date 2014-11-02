package by.slesh.ri.cp.ushkindaria.app;

public class App {

    public static void main(String[] args) {
        int a = 1;
        int b = 5;
        changer(a, b);
        System.out.println("a = " + a + " b = " + b);
    }

    static void changer(Integer a, Integer b) {
        ++a;
        ++b;
        System.out.println("a = " + a + " b = " + b);
    }

}
