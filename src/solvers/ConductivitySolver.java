package solvers;

import util.Constants;

import static java.lang.Math.*;

/**
 * Created by Ilya on 11/8/2017.
 */
public class ConductivitySolver {
    public double l = 0.5d;
    public double s = 0.000001d;
    public int T = 10;
    public double u0 = 0d;
    public double K = 0.0004d;
    public double c = 0.00000184d;
    public double a = 0.0000003d;
    public int N = Constants.N;
    public int n = 0;
    public ConductivitySolver(){
    }

    public double find_V(int n, int t) {
        this.n = n;
        double result = 0;
        result+=coef_An()*integral_An()*integral_e();
        return result;
    }

    double coef_An() {
        double numerator = 4d * PI*n;
        double denominator = l*(2*PI*n-PI);
        return numerator / denominator;
    }

    double integral_An() {
        double numerator = PI*l;
        double denominator = (PI*PI-(PI*n-PI/2)*(PI*n-PI/2));
        return numerator / denominator;
    }

    double integral_e(){
        double res = 0;
        for(int i =0;i<10;i++){
            res+= fun(n+i);
        }
        return res;
    }
    double fun(int N){
       return exp(-PI*PI*N*N*K*K*T/(l*l));
    }
}
