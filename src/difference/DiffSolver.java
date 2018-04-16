package difference;

import org.jfree.data.xy.XYSeries;
import util.Constants;
import util.RealConstants;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class DiffSolver {
    public double l = 0.5d;
    public double T0 = 1;
    double h = 0.01;
    double tau = 0.01;
    double t_end = 2;
    double k = 0.04;
    double[] T;
    double[] TT;
    public double a = 0.003;
    public double c = 1.84;
    double K = k / c;
    int N = 500;
    int t_N = 500;

    public XYSeries createDataset(int count) {
        XYSeries series = new XYSeries("Разностное решение");
        h = l / (N - 1);
        T = new double[N];
        TT = new double[N];
        tau = (0.25 * h * h) / a;
        for (int i = 0; i < T.length; i++) {
            T[i] = T0 * sin(PI * i * (h) / l);
        }
        diff3();
        series.add(0,0);
        for (int i = 1; i < count; i++) {
            series.add(i*l/count, T[i*N/count]);
        }
        series.add(l, T[N - 1]);
        return series;
    }

    private void diff3() {
        tau = t_end / (t_N - 1);
        for (int j = 0; j < t_N; j ++) {
            System.arraycopy(T, 0, TT, 0, TT.length);
            for (int i = 1; i < N - 1; i++) {
                T[i] = TT[i] + ((K * tau * (TT[i + 1] - 2.0 * TT[i] + TT[i - 1])) / (h * h));
            }
            right();
        }
    }

    private void right() {
        T[N - 1] = T[N - 2] / (1 + h * a / k);
    }

    public void refresh(RealConstants realConstants) {
        l = realConstants.l;
        t_end = realConstants.T;
        k = realConstants.k;
        c = realConstants.c;
        K = k/c;
        a = realConstants.a;
        N = realConstants.N;
        t_N = realConstants.t_N;
    }
}
