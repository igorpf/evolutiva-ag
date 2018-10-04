/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otimizacao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 *
 * @author igor
 */
public class Otimization {

    public Random rand = new Random();

    public static int GENERATIONS = 10;
    public static int POPULATION_SIZE=1000;
    public static double ELITISM_PERCENTAGE=1;
    public static double MUTATION_RATE=0.4;

    private List<Item> items;

    public static void main(String[] args) {

        if (checkIfTheNumberOfArgumentsIsRight(args))
            return;

        List<Integer> weightsOfItens = Parser.readFile(args[1]);

        Bin.capacity = weightsOfItens.remove(0);

        Otimization otimization = new Otimization();
        long startTime = System.nanoTime();
        Solution bestSolution = otimization.GA(weightsOfItens, GENERATIONS);
        long elapsedTime = (System.nanoTime() - startTime) / 1000000;
        System.out.println("Total execution time: " + elapsedTime + " ms");
        System.out.println("Best solution found size: " + bestSolution.getSize());
        saveBestResultOnFile(outputFileName(args[1]), bestSolution);
        saveResultOnCSV(args[0],args[1],bestSolution.getSize(),elapsedTime);
    }

    private static void saveResultOnCSV(String fileName, String inputFileName, Integer solutionSize, long elapsedTime){
        File file = new File(fileName);
        StringBuilder string = new StringBuilder();
        string.append(inputFileName).append(";").append(solutionSize.toString()).append(";").append(elapsedTime).append(";\n");
        try {
            Files.write(Paths.get(fileName),string.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String outputFileName(String filename){
        return filename.split("\\.")[0] + ".out";
    }

    private static void saveBestResultOnFile(String arg, Solution solution) {
        File f = new File(arg);
        try {
            f.createNewFile();

            FileWriter fw = new FileWriter(f.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(solution.completeToString());
            bw.newLine();
            bw.write("Best solution size: " + solution.getSize());
            bw.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean checkIfTheNumberOfArgumentsIsRight(String[] args) {
        if (args.length != 5 && args.length != 2) {
            System.out.println("Usage: $ java -jar otimizacao.jar <outputFile> <inputFile> <alpha> <graspIterations> <localSearchIterations>");
            System.out.println("or: $ java -jar otimizacao.jar <outputFile> <inputFile> ");
            return true;
        }
        return false;
    }

    private List<Bin> firstSolution(){

        return this.getItems().stream().map(item -> new Bin(item)).collect(Collectors.toList());
    }

    private void createItems(List<Integer> values){
        this.items = values.stream().map(value -> new Item(value)).collect(Collectors.toList());
    }
    private Solution GA(List<Integer> values, int generations){
        this.createItems(values);
        Solution firstSolution = new Solution(firstSolution());
        List<Solution> solutions = Arrays.asList(firstSolution);
        for (int i = 0; i < generations; i++) {
            solutions = mutate(solutions);
            solutions = filterSolutions(solutions);
        }
        solutions.sort(Solution::compareTo);
        return solutions.get(0);
    }

    private List<Solution> filterSolutions(List<Solution> solutions){
        solutions.sort(Solution::compareTo);
        List<Solution> newSolutions = solutions.subList(0, (int) (Otimization.POPULATION_SIZE * Otimization.ELITISM_PERCENTAGE));
        for (int i = 0; i <  (int) (Otimization.POPULATION_SIZE * ( 1 - Otimization.ELITISM_PERCENTAGE)) ; i++) {
            newSolutions.add(getRandomFrom(solutions));
        }
        return newSolutions;
    }

    private List<Solution> mutate(List<Solution> solutions){
        List<Solution> newSolutions = new ArrayList<>();
        for(int i = 0; Otimization.POPULATION_SIZE > i; i++){
            newSolutions.addAll(this.mixingSolutions(getRandomFrom(solutions), getRandomFrom(solutions)));
        }
        return newSolutions;
    }

    private List<Solution> mixingSolutions(Solution solution1, Solution solution2){

        Integer numberOfBinsChangedOnSolution1 = rand.nextInt((int) Math.ceil(solution1.getSize() * Otimization.MUTATION_RATE));
        Integer numberOfBinsChangedOnSolution2 = rand.nextInt((int) Math.ceil(solution2.getSize() * Otimization.MUTATION_RATE));
        Solution newSolution1 = new Solution(solution1);
        Solution newSolution2 = new Solution(solution2);

        List<Bin> removedBinsOfSolution1 = newSolution1.getRandomBinOfSolution(numberOfBinsChangedOnSolution1);
        List<Bin> removedBinsOfSolution2 = newSolution2.getRandomBinOfSolution(numberOfBinsChangedOnSolution2);

        crossOverSolutionWithBinsOfAnotherSolution(newSolution1, removedBinsOfSolution2);
        crossOverSolutionWithBinsOfAnotherSolution(newSolution2, removedBinsOfSolution1);

        return Arrays.asList(newSolution1,newSolution2);
    }

    private void crossOverSolutionWithBinsOfAnotherSolution(Solution solution, List<Bin> binsFromAnotherSolution){
        solution.removeBinsWithItemsOfCrossingOver(binsFromAnotherSolution);
        solution.add(binsFromAnotherSolution);
        List<Item> missingItemsOnSolution1 = solution.getMissingItems(new ArrayList<>(this.items));
        solution.addItemsOnSolution(missingItemsOnSolution1);
    }

    public List<Item> getItems() {
        return items;
    }

    private <T> T getRandomFrom(List<T> list){
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
