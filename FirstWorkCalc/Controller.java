class Controller {
    private MProc proc;
    private UsInterf ui;

    public CalcHand(MProc proc, UsInterf ui) {
        this.proc = proc;
        this.ui = ui;
    }
    public void run() {
        String inp = ui.getInp();
        try {
            double out = proc.calc(inp);
            ui.showRes(out);
        } catch (Exception e) {
            System.out.println("Ошибка:" + e.getMessage());
        }
    }
}
