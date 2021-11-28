
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import static org.chocosolver.solver.search.strategy.Search.minDomLBSearch;
import static org.chocosolver.util.tools.ArrayUtils.append;

public class CocoAirlines {

	
	IntVar[] dividers;

	Model model;

	

	public void solve(Instance inst, long timeout, boolean allSolutions) {

		buildModel(inst);
		model.getSolver().limitTime(timeout);
		StringBuilder st = new StringBuilder(
				String.format(model.getName() + "-- %s\n", inst.nb_dividers, " X ", inst.capacity));

		while (model.getSolver().solve()) {

			st.append("\t");
			for (int i = 0; i < inst.nb_dividers; i++) {
				st.append(dividers[i]).append("\t\t");
			}
		//	System.out.println(st.toString());
			st.setLength(0);
			if (!allSolutions)
				break;
		}
		
		model.getSolver().printStatistics();

		
	}

	public void buildModel(Instance inst) {
		// A new model instance
		model = new Model("Aircraft Class Divider ");

		// VARIABLES
		// set of marks that should be put on the ruler
		dividers = model.intVarArray("a", inst.nb_dividers, 0, inst.capacity, false);
		// set of distances between two distinct marks
		IntVar[] diffs = model.intVarArray("d", (inst.nb_dividers * (inst.nb_dividers - 1)) / 2, 0, inst.capacity,
				false);

		// CONSTRAINTS
		// the first mark is set to 0
		model.arithm(dividers[0], "=", 0).post();
		model.arithm(dividers[inst.nb_dividers - 1], "=", inst.capacity).post();

		// exits constraints:
		for (int i : inst.exits)
			for (int j = 0; j < inst.nb_dividers; j++)
				model.arithm(dividers[j], "!=", i).post();

		for (int i = 0, k = 0; i < inst.nb_dividers - 1; i++) {
			// // the mark variables are ordered
			model.arithm(dividers[i + 1], ">", dividers[i]).post();
			for (int j = i + 1; j < inst.nb_dividers; j++, k++) {
				// declare the distance constraint between two distinct marks
				model.scalar(new IntVar[] { dividers[j], dividers[i] }, new int[] { 1, -1 }, "=", diffs[k]).post();
				// redundant constraints on bounds of diffs[k]
				model.arithm(diffs[k], ">=", (j - i) * (j - i + 1) / 2).post();
				model.arithm(diffs[k], "<=", dividers[inst.nb_dividers - 1], "-",
						((inst.nb_dividers - 1 - j + i) * (inst.nb_dividers - j + i)) / 2).post();
			}
		}
		// all distances must be distinct
		model.allDifferent(diffs, "BC").post();
		// symmetry-breaking constraints
		model.arithm(diffs[0], "<", diffs[diffs.length - 1]).post();

		// business class constraint
		model.arithm(diffs[0], ">", 1).post();

	}




	public void configureSearch() {
		model.getSolver().setSearch(minDomLBSearch(append(dividers)));

	}


}
