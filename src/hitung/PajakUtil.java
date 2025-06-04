package hitung;

public class PajakUtil {
    public static double hitungTotal(double harga, String cc) {
        double pkb = 0.009 * harga;
        double opsen = 0.66 * pkb;
        double stnk = 100000;
        double plat = 60000;
        double swdkllj = cc.equals("50-250") ? 35000 : 50000;
        return pkb + opsen + stnk + plat + swdkllj;
    }
}