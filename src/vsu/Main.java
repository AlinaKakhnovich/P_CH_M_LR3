package vsu;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // write your code here

        Scanner in = new Scanner(System.in);
        System.out.print("Input a X0: ");
        BigDecimal x0 = in.nextBigDecimal();
        System.out.print("Input a Xn: ");
        BigDecimal xN = in.nextBigDecimal();
        System.out.print("Input a h: ");
        BigDecimal h = in.nextBigDecimal();
        System.out.print("Input a n1: ");
        int n1 = in.nextInt();
        System.out.print("Input a n2: ");
        int n2 = in.nextInt();
        System.out.print("Input a x1: ");
        BigDecimal x1 = in.nextBigDecimal();
        System.out.print("Input a x2: ");
        BigDecimal x2 = in.nextBigDecimal();
        System.out.print("Input a x3: ");
        BigDecimal x3 = in.nextBigDecimal();

        List<BigDecimal> massX = new ArrayList<>();
        List<BigDecimal> massY = new ArrayList<>();


        setX(x0, xN, h, massX);
        setY(massX, massY);

        printMass(massX, "X");
        printMass(massY, "Y(x)");

        System.out.println();
        System.out.println("F(" + x1 + ")" + tochnoe(x1));
        System.out.println("F(" + x2 + ")" + tochnoe(x2));
        System.out.println("F(" + x3 + ")" + tochnoe(x3));

        System.out.println();
        fullLangranz(massX, massY, n1, x1);
        fullLangranz(massX, massY, n1, x2);
        fullLangranz(massX, massY, n1, x3);

        fullNuton(massX, massY, n2, x1);
        fullNuton(massX, massY, n2, x2);
        fullNuton(massX, massY, n2, x3);

    }

    public static void fullLangranz(List<BigDecimal> massX, List<BigDecimal> massY, int n, BigDecimal x) {
        BigDecimal sum = Langranz(massX, massY, n, x);
        BigDecimal pogreshnost = pogreshnost(massX, n, x);
        BigDecimal tochnoe = tochnoe(x);
        BigDecimal pogr = tochnoe.subtract(sum).abs();
        System.out.println();
        System.out.println("L(" + x + ")= " + sum);
        System.out.println("Погрешность: " + pogreshnost);
        System.out.println("Погрешность по F(" + x + ") - L(" + x + "): " + pogr);
    }
    public static void fullNuton(List<BigDecimal> massX, List<BigDecimal> massY, int n, BigDecimal x) {
        BigDecimal sum = Nuton(massX, massY, n, x);
        BigDecimal pogreshnost = pogreshnost(massX, n, x);
        BigDecimal tochnoe = tochnoe(x);
        BigDecimal pogr = tochnoe.subtract(sum).abs();
        System.out.println();
        System.out.println("N(" + x + ")= " + sum);
        System.out.println("Погрешность: " + pogreshnost);
        System.out.println("Погрешность по F(" + x + ") - N(" + x + "): " + pogr);
    }

    public static BigDecimal Langranz(List<BigDecimal> massX, List<BigDecimal> massY, int n, BigDecimal x) {
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i <= n; i++) {
            BigDecimal mul = BigDecimal.ONE;
            for (int j = 0; j <= n; j++) {
                if (i != j) {
                    mul = mul.multiply((x.subtract(massX.get(j)))
                            .divide(massX.get(i).subtract(massX.get(j)), 4, RoundingMode.HALF_UP));
                }
            }
            sum = sum.add(massY.get(i).multiply(mul));
        }
        return sum;
    }

    public static BigDecimal Nuton(List<BigDecimal> massX, List<BigDecimal> massY, int n, BigDecimal x) {
        BigDecimal sum = massY.get(0);
        for (int i = 1; i <= n; i++) {
            BigDecimal mul = razdRaznosti(massX, massY, 0, i + 1); // разделенные разновсти
            for (int j = 0; j < i; j++) {
                mul = mul.multiply(x.subtract(massX.get(j)));
            }
            sum = sum.add(mul);
        }
        return sum;
    }

    public static BigDecimal razdRaznosti(List<BigDecimal> massX, List<BigDecimal> massY, int indexStart, int count) {
        if (count == 1) return massY.get(indexStart);
        return (razdRaznosti(massX, massY, indexStart + 1, count - 1).subtract(razdRaznosti(massX, massY, indexStart, count - 1)))
                .divide(massX.get(indexStart + count - 1).subtract(massX.get(indexStart)), 9, RoundingMode.HALF_UP);
    }

    public static BigDecimal pogreshnost(List<BigDecimal> massX, int n, BigDecimal x) {
        BigDecimal result = getMax(massX, n);

        result = result.divide(BigDecimal.valueOf(fact(n + 1, 1)), 9, RoundingMode.HALF_UP);

        for (int i = 0; i < n + 1; i++) {
            result = result.multiply(x.subtract(massX.get(i)).abs());
        }

        return result;
    }

    public static int fact(int f, int rez) {
        if (f == 0) return 1;
        return fact(f - 1, rez * f);
    }

    public static BigDecimal getMax(List<BigDecimal> massX, int n) {

        if (n == 4) {
            BigDecimal max = null;
            for (int i = 0; i <= n; i++) {
                BigDecimal newMax = BigDecimal.valueOf(4)
                        .multiply(BigDecimal.valueOf(
                                Math.cos(massX.get(i).doubleValue())
                        )).abs();
                if (max == null || max.compareTo(newMax) == -1) {
                    max = newMax;
                }

            }

            return max;
        }

        if (n == 2) {
            BigDecimal max = null;
            for (int i = 0; i <= n; i++) {
                BigDecimal newMax = BigDecimal.valueOf(-4)
                        .multiply(BigDecimal.valueOf(
                                Math.cos(massX.get(i).doubleValue())
                        )).abs();
                if (max == null || max.compareTo(newMax) == -1) {
                    max = newMax;
                }

            }

            return max;
        }
        return null;
    }

    public static BigDecimal tochnoe(BigDecimal x) {
        return x.pow(2).add(BigDecimal.valueOf(4).multiply(BigDecimal.valueOf(Math.sin(x.doubleValue()))));
    }

    public static void setX(BigDecimal x0, BigDecimal xN, BigDecimal h, List<BigDecimal> massX) {
        while (x0.compareTo(xN) <= 0) {
            massX.add(x0.setScale(4, RoundingMode.HALF_UP));
            x0 = x0.add(h).setScale(4, RoundingMode.HALF_UP);
        }
    }

    public static void setY(List<BigDecimal> massX, List<BigDecimal> massY) {
        massX.forEach(x -> {
            BigDecimal y = x.pow(2).add(BigDecimal.valueOf(4).multiply(BigDecimal.valueOf(Math.sin(x.doubleValue()))));
            massY.add(y.setScale(4, RoundingMode.HALF_UP));
        });
    }

    public static void printMass(List<BigDecimal> mass, String name) {
        System.out.println();
        System.out.print("\t\t" + name);
        mass.forEach(x -> {
            System.out.print("\t\t" + x);
        });
    }
}
