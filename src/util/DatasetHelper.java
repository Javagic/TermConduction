package util;

import difference.DiffSolver;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import solvers.OldConductivitySolver;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class DatasetHelper {
    private OldConductivitySolver solver = new OldConductivitySolver();
    private DiffSolver diffSolver = new DiffSolver();
    private RealConstants realConstants;

    public DatasetHelper(RealConstants realConstants) {
        this.realConstants = realConstants;
    }

    private XYSeriesCollection dataset = new XYSeriesCollection();

    public void evaluate() {
        refreshSolvers();
        createDataset();
    }

    private void refreshSolvers() {
        solver.refresh(realConstants);
        diffSolver.refresh(realConstants);
    }

    public XYSeriesCollection createDataset() {
        final XYSeries series0 = new XYSeries("Начальная температура");
       // final XYSeries series3 = diffSolver.createFakeDataset(10);
        final XYSeries series1 = new XYSeries("Аналитическое решение");
        final XYSeries series2 = diffSolver.createDataset(10);
        double x = 0;
        for (int i = 0; i <= 10; i++) {
            double res = solver.find_V(x, solver.T);
            series1.add(x, res);
            series0.add(x, sin(PI * x / solver.l) + Constants.u0);
            System.out.println();
            x += solver.l / 10;
        }
        series0.remove(series0.getItemCount() - 1);
        series0.add(solver.l, 0);
        dataset.removeAllSeries();
        if (solver.T == 0) {
            dataset.addSeries(series0);
        } else {
            dataset.addSeries(series1);
        }

      //  dataset.addSeries(series3);
        //dataset.addSeries(series0);
        dataset.addSeries(series2);
        return dataset;
    }

}
