import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
// import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;
import java.util.Properties;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.HashSet;
import java.util.Set;

import java.lang.Math;

import java.awt.Font;
import java.awt.Color;

import java.io.FileWriter;
import java.io.IOException;

public class player36 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;

    public int evals = 0;

	public player36()
	{
		rnd_ = new Random();
	}

	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
			evaluation_ = evaluation;

			// Get evaluation properties
			Properties props = evaluation.getProperties();
	    // Get evaluation limit
	    evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));

			// Property keys depend on specific evaluation
			// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
	    boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
	    boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
	    boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

			// Do sth with property values, e.g. specify relevant settings of your algorithm
	    if(isMultimodal){
	        // Do sth
	    } else{
	        // Do sth else
	    }
    }

	public void run() {
		// Run your algorithm here

		// int evals = 0;


		writeToFile("meneer");


		// Create population of 100 ppl, each person has 10 gens
		double childrens[][] = create_population();
		int glb_best = 0;

		// Determine fitness for each child in population
		double survival_chances[][] = score_checker(childrens);

		// sort algorithm that sorts the children on fitness from min to max
		double sorted_survival_chances[][] = sort_survival_chances(survival_chances);

		// prints the sorted population
		// System.out.println("\n\n sorted population \n\n");
		// for (int i = 0; i < sorted_survival_chances.length; i++) {

		//  	System.out.println(Arrays.toString(sorted_survival_chances[i]));
		// }

		// Amount of random elements from population
		int n = 5;

		// Calculate fitness
		while (evals < evaluations_limit_) {

			// function input is the list of n random parents. It secelets 2 parents from the n input parents.
			double[][] parents = tournamen_parent_selection(10, 2,sorted_survival_chances);

//

//			System.out.println("\n\nParents\n\n");
//			for (int i = 0; i < parents.length; i++) {
//				for (int j = 0; j < 2; j++) {
//					System.out.println(parents[i][j]);
//				}
//			}
//
//			System.out.println("\n\n ouders die in de functie create_two children gaan \n\n");
//			System.out.println(parents[0][1]);
//			System.out.println(parents[1][1]);

			// geef de index nummers van de gekozen ouders mee

			double[][] new_children = create_n_children(childrens, parents);

//			new_children = mutation_swap_function(new_children);
			new_children = lil_mutation_function(new_children,1);

//			double[][] children = create_two_children(childrens[(int) parents[0][1]], childrens[(int) parents[1][1]]);
//			// children an array of 2 kids with each 10 gens
//
//
//
//			System.out.println("\n\n new kids on the block\n");
//
//			double[] boy = children[0], girl = children[1];
//
//			System.out.println("\n\nBoy\n");
//			for (int j = 0; j < boy.length; j++) {
//				System.out.println(boy[j]);
//			}
//			System.out.println("\n\nGirl\n");
//			System.out.println(girl.length);
//			for (int j = 0; j < girl.length; j++) {
//				System.out.println(girl[j]);
//			}
//
//			// childrens is the starting population
//			// boy and girl are the two new created kids
//			// sorted_survival .. is the sorted population from min to max
//			// this function replaces the two worst persons in population by the created kids
//			childrens = who_lives_who_dies(sorted_survival_chances, childrens, boy, girl);

			childrens = who_lives_who_dies(sorted_survival_chances, childrens, new_children);
			// Apply crossover / mutation operators

			// Check fitness of unknown fuction: determines your grade
			// needs real-value values but conversions before are okay
			// Double fitness = (double) evaluation_.evaluate(childrens[2]);

			double avg_fitness = 0;
			int best = 0;
			double best_value = -100;
			for (int i = 0; i < childrens.length; i++) {

				// calculate and save the fitness of this individual
				Double fitness = (double) evaluation_.evaluate(childrens[i]);
				avg_fitness += fitness;
				survival_chances[i][0] = fitness;
				survival_chances[i][1] = i;

				// save the best individual
				if (fitness > best_value) {
					best_value = fitness;
					best = i;
				}
				evals++;
			}
			// System.out.println("\n\nAverage\n");
			// System.out.println(avg_fitness / children.length);

			// Sort algorithm from min to max fitness
			sorted_survival_chances = sort_survival_chances(survival_chances);

			// Create
			// print out the individual and the score
			// for (int a = 0; a < sorted_survival_chances.length; a++){
			// 	System.out.println(Arrays.toString(sorted_survival_chances[a]));
			// 	System.out.println(Arrays.toString(childrens[(int) sorted_survival_chances[a][1]]));
			// }

			// Create average gene for best fitness and for each of population
			// for (int j = 0; j < childrens.length; j++) {
			// 	for (int c = 0; c < childrens[j].length; c++) {
			// 		childrens[j][c] = (childrens[j][c]+childrens[best][c])/2;
			// 	}
			// }

			// Have for child_n in range of popsize, mate 1 with 2 and 2 with 3 ... to n.
			// for (int j = survival_chances.length/2; j < sorted_survival_chances.length-1; j++) {
			// 	int index_val1 = (int) sorted_survival_chances[j][1];
			// 	int index_val2 = (int) sorted_survival_chances[j+1][1];
			// 	for (int gen_index = 0; gen_index < childrens[index_val1].length; gen_index++) {
			// 		//childrens[index_val1][c] = (childrens[index_val1][c]+childrens[index_val2][c])/2;
			// 		//recombine genes for best 50 with their +1 incremented counterparts
			// 		for (int i : printRandomNumbers(5,9)) {
   //      				childrens[index_val1][i] = childrens[index_val2][i];
   //  				}

   //  				// replace worst 50 children with best 50 with a slight mutation
			// 		int index_val_mutate = (int) sorted_survival_chances[j-sorted_survival_chances.length/2][1];
			// 		childrens[index_val_mutate] = childrens[index_val1];
			// 		for (int i : printRandomNumbers(2,9)) {
			// 			double random_double = get_random_double(-5, 5);
   //      				childrens[index_val_mutate][i] = random_double;
   //  				}
			// 	}
			// }

			glb_best = best;
			// System.out.println(glb_best);

		}

		//System.out.println("OHOHHOHO");

		// plot the scores of the last population
		// makeGraph();

		// print out global best
	// 	System.out.println(Arrays.toString(childrens[glb_best]));
	// 	for (double child : childrens[glb_best]) {
	// 		System.out.print(0.01*(int) Math.round(child*100));
	// 		System.out.print("\t");
	// 	}
	}

	public double[][] mutation_swap_function(double [][] new_kids ) {
		for (int i = 0; i < new_kids.length; i++){
			double individual_kid[] = new_kids[i];

			// maak een random getal tussen de 0 en de 9
			int random_numbers [] = printRandomNumbers(2, 9);

			// swap
			double temp = individual_kid[random_numbers[0]];
			individual_kid[random_numbers[0]] = individual_kid[random_numbers[1]];
			individual_kid[random_numbers[1]] = temp;

			new_kids[i] = individual_kid;

		}
		// let op ,... een swap is soms geen swao omdat je hetzewlfde getal terug krijgt. dit zou niet mogen.
		return new_kids;
	}

	//	Lil mutation
	public double[][] lil_mutation_function(double [][] new_kids, int num_of_mutations) {
		for (int i = 0; i < new_kids.length; i++){
			double individual_kid[] = new_kids[i];

			// create a random digit between 0 and 9
			int random_numbers [] = printRandomNumbers(num_of_mutations, 9);

			for (int j = 0; j < random_numbers.length; j++) {
				double r = get_random_double(-1,1);
				individual_kid[j] = individual_kid[j] + r;
				if (individual_kid[j] > 5 || individual_kid[j] < -5) {
					individual_kid[j] = individual_kid[j] - r - r;
				}
			}
			new_kids[i] = individual_kid;
		}
		// let op... een swap is soms geen swao omdat je hetzewlfde getal terug krijgt. dit zou niet mogen.
		return new_kids;
	}


	//
	public double[][] select_n_random_elements(int n, double[][] sort_list) {

		// Select n random integers between zero and a maximum value.
		int random_number_list[] = printRandomNumbers(n,99);

		double random_n_elements[][] = new double[n][];

		// Save the n random elements
		for (int i = 0; i < random_number_list.length; i++) {

			// Put the index number in a variable
			int index_number = random_number_list[i];
			random_n_elements[i] = sort_list[index_number];
		}
		return random_n_elements;
	}

	public double[][] select_parents(double[][] random_n_elements) {

		double[][] parents = new double[2][2];
		double[][] sorted = sort_survival_chances(random_n_elements);
		parents[0] = sorted[random_n_elements.length - 1];
		parents[1] = sorted[random_n_elements.length - 2];
		return parents;
	}

	public double[] select_single_parent(double[][] random_n_elements) {
		double[][] sorted = sort_survival_chances(random_n_elements);
		double[] parent = sorted[random_n_elements.length - 1];
		return parent;
	}

	public double[][] tournamen_parent_selection(int amount_of_parents, int n, double[][] sorted_survival_chances) {
		// amount of parents is how many parents should be selected to make
		// the same number of children. two parents make two children. chidlren have max two paretns.
		// n = how large the pool of randomly selected elements that will form the parents should be.

		double[][] parents = new double[amount_of_parents][2];
		for (int i = 0; i < amount_of_parents; i++) {
			double[][] parents_pool = select_n_random_elements(n, sorted_survival_chances);
			parents[i] = select_single_parent(parents_pool);

		}
		System.out.println("ALLAH HAKBAR");
		System.out.println(evals);
		for (int i = 0; i < parents.length; i++) {
			System.out.println(Arrays.toString(parents[i]));
		}

		return  parents;


		// dit alles twee keer
		// select n random kids from population
		// save best parent using select single parent function
		// make new kids via the best parents using the create two children function
		// replace the worst people in the population by the new kids


	}

	public double[][] create_n_children(double[][] childrens, double[][] parents) {
		double[][] children = new double[parents.length][10];

//		System.out.println("\n\nIk haat m'n ouders\n\n");
//
//		for (int i = 0; i < parents.length; i++) {
//			System.out.println(Arrays.toString(parents[i]));
//		}
		for (int i = 0; i < parents.length; i += 2) {
			double[][] temp_children = create_two_children(childrens[(int) parents[i][1]], childrens[(int) parents[i + 1][1]]);
			children[i] = temp_children[0];
			children[i + 1] = temp_children[1];
		}
		//			double[][] children = create_two_children(childrens[(int) parents[0][1]], childrens[(int) parents[1][1]]);

		return children;

	}

	public static void writeToFile(String args) {
		try (FileWriter writer = new FileWriter("test.txt")) {
			writer.write("Today is a sunny day, koalas, salsa");
			writer.write("Tobias, geitjes, smoothies");
			writer.write("GayVBeestje, Stijldansen");
		} catch (IOException e) {
			System.out.println(e);
		}
	}


	public double[][] score_checker( double[][] childrens) {

		// in this array we place the score and index

		double fitness_index_array[][] = new double[childrens.length][];

		for (int i = 0; i < childrens.length; i++) {

			double fit_index_array[] = new double [2];

			// calculate and save the fitness of this individual
			double fitness = (double) evaluation_.evaluate(childrens[i]);

			// on the ith array at the left side, place the fitness
			fit_index_array[0] = fitness;

			// on the ith array at the right side, place the index number
			fit_index_array[1] = i;

			// place the array in the big array
			fitness_index_array[i] = fit_index_array;

			evals++;
		}

		return fitness_index_array;
	}

	// TODO write better sorting algorithm
	public double[][] sort_survival_chances(double[][] survival_chances) {
		// sort algorithm that sorts the children on fitness from min to max

		double temp[] = new double[2];
		for (int n = 0; n < survival_chances.length; n++) {
			for (int m = 0; m < survival_chances.length; m++){
				if (survival_chances[n][0] < survival_chances[m][0]) {
					temp = survival_chances[n];
					survival_chances[n] = survival_chances[m];
					survival_chances[m] = temp;
				}
			}
		}

		return survival_chances;

	}

	public double[] make_half_half_child(double[] mom, double[] dad) {

		double[] child = new double[10];

		for (int i = 0; i < mom.length; i++) {
			if (i < mom.length / 2) {
				child[i] = mom[i];
			} else {
				child[i] = dad[i];
			}
		}

		return child;
	}

	// TODO write function with DNA library

	// TODO write function who lives, who dies, who tells your story
	// Slechtste twee per rondje gaan dood (want komen er twee bij)

	public double[][] who_lives_who_dies(double[][] sorted_survival_chances, double[][] children, double[][] new_children) {

		for (int i = 0; i < new_children.length; i += 2) {
			// Get index of those to replace
			int boy_index = (int) sorted_survival_chances[i][1];
			int girl_index = (int) sorted_survival_chances[i + 1][1];

			//System.out.println(Arrays.toString(children[boy_index]));

			// Replace worst ones with boy and girl
			children[boy_index] = new_children[i];
			children[girl_index] = new_children[i + 1];
		//	System.out.println(Arrays.toString(new_children[i]));

		}

		return children;

	}

	// This is a function that generates random numbers between a range, without repetition
	// http://www.codecodex.com/wiki/Generate_Random_Numbers_Without_Repetition
	public static final Random gen = new Random();
    public static int[] printRandomNumbers(int n, int maxRange) {

    	// n equals amount of numbers
    	// Maxrange equals the maximum number in range

        assert n <= maxRange : "There aren't more unique numbers in this range";

        int[] result = new int[n];
        Set<Integer> used = new HashSet<Integer>();

        for (int i = 0; i < n; i++) {

            int newRandom;
            do {
                newRandom = gen.nextInt(maxRange+1);
            } while (used.contains(newRandom));
            result[i] = newRandom;
            used.add(newRandom);
        }
        return result;
    }

	// Returns a random double between certain values
	public double get_random_double(int min, int max) {

		return (Math.random() * (max - min)) + min;
	}

	// This is a function that initializes the population with random individuals
	public double[][] create_population() {

		// define population size 100 people with each person has 10 gens
		int pop_size = 100;
		int dim = 10;

		double children[][] = new double[pop_size][];

		// initialize population randomly
		for (int i = 0; i < pop_size; i++) {
			double child[] = new double[dim];

			for (int j = 0; j < dim; j++) {
				double random_double = get_random_double(-5, 5);
				child[j] = random_double;

			}
			children[i] = child;
			// System.out.println(Arrays.toString(children[i]));
		}
		// double[] curr_top = new double[] {-1.1891876987039534, 3.802704764222131, -0.7045031488811008, -3.214820204008321, 0.9342012108821499, -1.6280075915460186, 1.1164003795515511, -0.7854278491364934, 1.7478175537980336, -0.45014115827163426};
		// children[1] = curr_top;
		return children;
	}

	// CODE EMMA@@@@@@@@
	// Creates two children that mirror each other, and together can form their parents.



	public double[][] create_two_children(double[] mom, double[] dad) {
		// mom/dad is a parents with 10 genes

		// make 10 places per child for the genes
    	double[] boy = new double[10];
	    double[] girl = new double[10];


	    // make 5 int in an array from 0 - 9
	    int[] parent_indices = printRandomNumbers(5, 9);
	    // System.out.println(Arrays.toString(parent_indices));

	    for (int i = 0; i < mom.length; i++) {
	       if (in_parent_indices(parent_indices, i)) {
	          boy[i] = mom[i];
	          girl[i] = dad[i];
	       } else {
	          boy[i] = dad[i];
	          girl[i] = mom[i];
	       }
	    }
	  	// In order to return two lists, make them into one list.
		// IMPORTANT: must take two lists apart again!!!
		double[][] boygirl = new double[2][10];
	   	boygirl[0] = boy;
	   	boygirl[1] = girl;
	   	return boygirl;
	}

	public boolean in_parent_indices(int[] parent_indices, int n) {
    	for (int i = 0; i < parent_indices.length; i++) {
    		if (n == parent_indices[i]) {
	             return true;
    		}
    	}
    	return false;
	}
}
