package solvers;

import util.Constants;
import util.RealConstants;

import static java.lang.Math.*;

/**
 * Created by Ilya on 11/8/2017.
 */
public class OldConductivitySolver {
    public double l;
    public double s = 0.000001d;
    public int T;
    public double u0 = 0d;
    public double K;
    public double c;
    public double a;
    public double eps;
    public int N;
    double En = 0;
    double w = sqrt(En) * l;
    double v = -PI / 2 + eps;

    public OldConductivitySolver() {
    }
public void refresh(RealConstants realConstants){
    l = realConstants.l;
    T = realConstants.T;
    K = realConstants.K;
    c = realConstants.c;
    a = realConstants.a;
    N = realConstants.N;
    eps = realConstants.eps;
}
    public void find_En(int i) {
        int count = 0;
        double left = v + PI;
        double right = PI * i;
        double mid;
        while (abs(right - left) > eps) {
            count++;
            mid = (right + left) / 2;
            if (difference(mid) > 0) {
                right = mid;
            } else {
                left = mid;
            }
        }
        v = left;
//        System.out.println(count);
        En = pow(v / l, 2);
    }

    double difference(double mid) {
        return tan(mid) + c * l / (2 * a * mid);
    }

    public double find_V(double x, int t) {
        if (x == 0) return 0;
        double result = 0;
        double d = 0;
        for (int i = 1; i <= N; i++) {
            find_En(i);
            w = sqrt(En) * l;
            d = coef_An() * integral_An() * sin(w * x / l) * exp(-K * En * t);
            result += d;
            System.out.println("n=" + i + " Vn =" + d);
            if (abs(d) < -1E-30) {
                break;
            }
            System.out.println("En: " + En);
        }
        v = -PI / 2 + eps;
        return result;
    }

    double coef_An() {
        double numerator = 4d * w;
        double denominator = l * (2 * w - sin(2 * w));
        return numerator / denominator;
    }

    double integral_An() {
        double numerator = PI * l * sin(w);
        double denominator = (PI * PI - w * w);
        return numerator / denominator;
    }

    double ksi(double x) {
        return sin(PI * x / l) + u0;
    }
}
