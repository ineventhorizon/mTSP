package yusufekremkecilioglu;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;
import yusufekremkecilioglu.Strategies.HeuristicSolveStrategy;
import yusufekremkecilioglu.Strategies.RandomSolveStrategy;

import static com.lexicalscope.jewel.cli.CliFactory.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public interface Args {
        @Option(shortName = "d") int getDepots();
        @Option(shortName = "s") int getSalesmen();
        @Option(shortName = "v", description = "Enable verbose output") boolean isVerbose();
    }
    public static void main(String[] args) {

        Args cli = parseArguments(Args.class, args);
        int d = cli.getDepots();
        int s = cli.getSalesmen();
        boolean verbose = cli.isVerbose();
        Solver solver = new Solver(d, s);

        solver.SetStrategy(new RandomSolveStrategy());
        solver.Solve(100000);
        solver.PrintBestSolution(verbose);

        solver.SetStrategy(new HeuristicSolveStrategy());
        solver.Solve(5000000);
        solver.PrintBestSolution(verbose);


        solver.WriteToJson(d,s, verbose);
    }
}