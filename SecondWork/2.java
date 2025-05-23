class Controller {
    private Model proc;
    private View ui;

    public Controller(Model proc, View ui) {
        this.proc = proc;
        this.ui = ui;
    }
    
    public void run() {
        String inp = ui.getInp();
        try {
            if (!proc.checkParentheses(inp)) {
                throw new RuntimeException("Несбалансированные скобки");
            }
            double out = proc.calc(inp);
            ui.showRes(out);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Model proc = new Model();
        View ui = new View();
        Controller handler = new Controller(proc, ui);
        handler.run();
    }
}

class Model {
    public boolean checkParentheses(String exp) {
        int balance = 0;
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }

    public double calc(String exp) {
        return new Object() {
            int i = -1, ch;
            int termCount = 0; // счетчик слагаемых

            void next() {
                ch = (++i < exp.length()) ? exp.charAt(i) : -1;
            }

            boolean chk(int c) {
                while (ch == ' ') next();
                if (ch == c) {
                    next();
                    return true;
                }
                return false;
            }

            double eval() {
                next();
                double res = expr();
                if (i < exp.length()) throw new RuntimeException("Ошибка: " + (char) ch);
                return res;
            }

            double expr() {
                double res = term();
                while (true) {
                    if (termCount >= 15) throw new RuntimeException("Превышено максимальное количество слагаемых (15)");
                    if (chk('+')) { res += term(); termCount++; }
                    else if (chk('-')) { res -= term(); termCount++; }
                    else return res;
                }
            }

            double term() {
                double res = factor();
                while (true) {
                    if (chk('*')) {
                        if (ch == '*') { // обработка **
                            next();
                            res = Math.pow(res, factor());
                        } else {
                            res *= factor();
                        }
                    }
                    else if (chk('/')) res /= factor();
                    else if (chk(':')) res = Math.floor(res / factor());
                    else return res;
                }
            }

            double factor() {
                if (chk('+')) return factor();
                if (chk('-')) return -factor();

                double res;
                int start = this.i;
                
                if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') next();
                    res = Double.parseDouble(exp.substring(start, this.i));
                    
                    if (chk('!')) { // факториал
                        if (res % 1 != 0 || res < 0) 
                            throw new RuntimeException("Факториал определен только для целых неотрицательных чисел");
                        long fact = 1;
                        for (int j = 2; j <= res; j++) fact *= j;
                        res = fact;
                    }
                } 
                else if (chk('(')) {
                    res = expr();
                    if (!chk(')')) throw new RuntimeException("Отсутствует закрывающая скобка");
                } 
                else if (chk('l') && chk('o') && chk('g') && chk('(')) { // log()
                    res = expr();
                    if (!chk(')')) throw new RuntimeException("Отсутствует закрывающая скобка для log");
                    res = Math.log(res) / Math.log(2); // логарифм по основанию 2
                }
                else if (chk('e') && chk('x') && chk('p') && chk('(')) { // exp()
                    res = expr();
                    if (!chk(')')) throw new RuntimeException("Отсутствует закрывающая скобка для exp");
                    res = Math.exp(res);
                }
                else {
                    throw new RuntimeException("Неизвестный символ: " + (char) ch);
                }

                if (chk('^')) res = Math.pow(res, factor());

                return res;
            }
        }.eval();
    }
}

class View {
    public String getInp() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите выражение (доступные операции: +-*/^:**! log() exp()): ");
        return sc.nextLine();
    }

    public void showRes(double res) {
        System.out.println("Результат: " + res);
    }
}
