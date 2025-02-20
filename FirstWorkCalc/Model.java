class Model {
    public double calc(String exp) {
        return new Object() {
            int i = -1, ch;

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
                if (i < exp.length()) throw new RuntimeException("Err: " + (char) ch);
                return res;
            }

            double expr() {
                double res = term();
                while (true) {
                    if (chk('+')) res += term();//cложение
                    else if (chk('-')) res -= term();//вычитание
                    else return res;
                }
            }

            double term() {
                double res = factor();
                while (true) {
                    if (chk('*')) res *= factor();//умножение
                    else if (chk('/')) res /= factor();//деление
                    else if (chk(':')) res = Math.floor(res / factor());//целочисленное деление(:)
                    else return res;
                }
            }

            double factor() {
                if (chk('+')) return factor();//унарный плюс
                if (chk('-')) return -factor();//унарный минус

                double res;
                int start = this.i;
                if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') next();
                    res = Double.parseDouble(exp.substring(start, this.i));
                } else if (chk('(')) {
                    res = expr();
                    chk(')');
                } else {
                    throw new RuntimeException("Err: " + (char) ch);
                }

                if (chk('^')) res = Math.pow(res, factor());//степень

                return res;
            }
        }.eval();
    }
}
