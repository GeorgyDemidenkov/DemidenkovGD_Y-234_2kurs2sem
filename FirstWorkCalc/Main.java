public class Main {
    public static void main(String[] args) {
        MProc proc = new MProc();
        UsInterf ui = new UsInterf();
        CalcHand handler = new CalcHand(proc, ui);
        handler.run();
    }
}
