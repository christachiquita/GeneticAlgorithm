/*
 * Hypothesis.java
 * created 15 May 2015
 * author : christachiquita , johnWilshire
 */
package main;

import java.awt.Component;
import java.io.IOException;
import java.util.*;

import GeneticAlgorithm.*;
import data.*;

public class GACV {

	private ArrayList<Hypothesis> population; //P
	private ArrayList<Hypothesis> newPopulation; //Ps
	private ArrayList<Hypothesis> bestHyps;
	

	private int p; //the number of hypotheses to be included in the population
	private double r; //the fraction of the population to be replaced by Crossover at each step
	private double m; //mutation rate
	private String arrfpath;
	private int task; //1 = balance-scale, 2 = mushroom
	private int lengthOfClass;
	private double fitnessTH; //fitness threshold
	private double totalFitness; //SUM(population.get(0..p).getfitness())
	private ArrayList<FitnessFunction> fitnessFuncs;
	private int NumOfBits;
	private DataSetProcessor dsp;
	private ArrayList<Integer> generations; 
	private int crossOverMethod; //0 = uniform, 1 = single point, 2 = two points
	private int folds;
	private ArrayList<Hypothesis> hypList;
	
		/* CONSTRUCTOR */
	public GACV(){
		
	}
	
	public GACV(int p, double r, double m, int task, FitnessFunction myFitnessFunc, double fitnessTH, int folds){
		this.p = p;
		this.r = r;
		this.m = m;
		this.folds = folds;
		bestHyps = new ArrayList<Hypothesis>();
		generations = new ArrayList<Integer>(folds);
		for(int i = 0; i < folds; i++){
			generations.add(new Integer(0));
		}
		if (task == 1){
			this.arrfpath = "../GA/arrf/balance-scale.arff";
		}
		else{
			this.arrfpath = "../GA/arrf/mushroom.arff";
		}
		this.task = task;
		this.dsp = new DataSetProcessor(this.arrfpath, task);
		Validation val = new Validation(dsp.getBinaryDataSet(), this.folds, this.task);
		if (task==1){
			lengthOfClass = 2;
		} else {
			lengthOfClass = 1;
		}
		this.fitnessFuncs = new ArrayList();
		this.NumOfBits = dsp.getBinaryDataSet().get(0).length();
		for (int i=0; i<this.folds; i++){
			myFitnessFunc.setTrainSet(val.getTrains().get(i));
			myFitnessFunc.setTestSet(val.getTests().get(i));
			this.fitnessFuncs.add(myFitnessFunc);
		}
		this.fitnessTH = fitnessTH;
	}
	
	/* GETTERS AND SETTERS */
	public ArrayList<Hypothesis> getPopulation() {
		return population;
	}

	public void setPopulation(ArrayList<Hypothesis> population) {
		this.population = population;
	}
	
	public ArrayList<Hypothesis> getBestHyps() {
		return bestHyps;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}
	
	public ArrayList<Hypothesis> getNewPopulation() {
		return newPopulation;
	}

	public void setNewPopulation(ArrayList<Hypothesis> newPopulation) {
		this.newPopulation = newPopulation;
	}

	public String getArrfpath() {
		return arrfpath;
	}

	public void setArrfpath(String arrfpath) {
		this.arrfpath = arrfpath;
	}

	public int getTask() {
		return task;
	}

	public void setTask(int task) {
		this.task = task;
	}

	public double getFitnessTH() {
		return fitnessTH;
	}

	public void setFitnessTH(double fitnessTH) {
		this.fitnessTH = fitnessTH;
	}

	public double getTotalFitness() {
		totalFitness = 0;
		for(Hypothesis hyp : population){
			totalFitness += hyp.getFitness();
		}
		return totalFitness;
	}

	public void setTotalFitness(double totalFitness) {
		this.totalFitness = totalFitness;
	}

	public int getNumOfBits() {
		return NumOfBits;
	}

	public void setNumOfBits(int numOfBits) {
		NumOfBits = numOfBits;
	}
	
	public int getLengthOfClass() {
		return lengthOfClass;
	}
	
	public ArrayList<Integer> getGenerations(){
		return generations;
	}

	public void setLengthOfClass(int lengthOfClass) {
		this.lengthOfClass = lengthOfClass;
	}

	public ArrayList<FitnessFunction> getFitnessFuncs() {
		return fitnessFuncs;
	}

	public void setFitnessFuncs(ArrayList<FitnessFunction> fitnessFuncs) {
		this.fitnessFuncs = fitnessFuncs;
	}

	public int getFolds() {
		return folds;
	}

	public void setFolds(int folds) {
		this.folds = folds;
	}
	/*
	public void setGeneration(int generation) {
		this.generations = generations;
	}
	*/

	public DataSetProcessor getDsp() {
		return dsp;
	}

	public void setDsp(DataSetProcessor dsp) {
		this.dsp = dsp;
	}

	public int getCrossOverMethod() {
		return crossOverMethod;
	}

	public void setCrossOverMethod(int crossOverMethod) {
		this.crossOverMethod = crossOverMethod;
	}

	
	/* GENERATE POPULATION
	 * generate p hypothesis at random
	 */
	public void generatePopulation(int fold){
		this.population = new ArrayList();
		int x;
		if (task == 1){x=2;}
		else {x=1;}
		// adds a hypothesis of all 1's and another of all 0's
		String allOne = "";
		int len = 0;
		if (task == 1) len = 3;
		else len = 2;
		for(int i = 0; i < this.getNumOfBits()*len; i++){
			allOne += "1";
		}
		this.population.add(new Hypothesis(allOne , lengthOfClass));
		allOne = "";
		for(int i = 0; i < this.getNumOfBits()*len; i++){
			allOne += "0";
		}
		this.population.add(new Hypothesis(allOne, lengthOfClass));
	
		
		if (task == 1){
			for (int i = 0; i < this.p - x; i++){
				this.population.add(this.ConcatHypothesis(this.getNumOfBits()));
			}
		}
		else{
			for (int i = 0; i < this.p - x; i++){
				this.population.add(this.ConcatHypothesis(this.getNumOfBits()));
			}
		}
		for (Hypothesis H : this.population){
			double fit = fitnessFuncs.get(fold).evalTrainingSet(H);
			H.setFitness(fit);
		}
		sortedHypothesisByFitness();
	}
	public Hypothesis generateRandHypothesis(int bitslength){
		String bs = "";
		int x = 0;
		if (task == 1){
			x = 1;
		}
		for (int i=0; i<bitslength-x; i++){
			int rand = randomizer(0, 2);
			bs+=rand;
		}
		if (task == 1){
			if (bs.charAt(bitslength-2)=='0'){
				bs+='1';
			}
			else{
				int rand = randomizer(0, 2);
				bs+=rand;
			}
		}
		return new Hypothesis(bs, this.lengthOfClass);
	}
	
	public Hypothesis ConcatHypothesis(int bitslength){
		String hyp = "";
		int x = 1;
		if (task==1){
			x = 2;
			for (int i=0; i<bitslength-x; i++){
				int rand = randomizer(0, 2);
				hyp +=rand;
			}
			hyp += "10";
			for (int i=0; i<bitslength-x; i++){
				int rand = randomizer(0, 2);
				hyp +=rand;
			}
			hyp += "11";
			for (int i=0; i<bitslength-x; i++){
				int rand = randomizer(0, 2);
				hyp +=rand;
			}
			hyp += "01";
		}
		else {
			for (int i=0; i<bitslength-x; i++){
				int rand = randomizer(0, 2);
				hyp +=rand;
			}
			hyp += "0";
			for (int i=0; i<bitslength-x; i++){
				int rand = randomizer(0, 2);
				hyp +=rand;
			}
			hyp += "1";
		}
		return new Hypothesis(hyp, this.lengthOfClass);
	}
	
	public void printPopulation(){
		System.out.println("\n------------------------\nPopulation (with p = "+this.p+")\n------------------------");
		for (int i=0; i<this.population.size(); i++){
			// +this.population.get(i).getBitsHypothesis() + " 
			System.out.println(i+" : " + " fitness : "+ this.population.get(i).getFitness());
		}
	}
	
	public String printedPopulation(){
		String S = "";
		S += "\n----New Population----\n";
		for (int i=0; i<this.population.size(); i++){
			S += "\n"+i+" : "+this.population.get(i).getBitsHypothesis() + "\n>>>fitness : "+ this.population.get(i).getFitness()+"\n";
		}
		return S;
	}
	
	public int randomizer(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min)) + min;
	    return randomNum;
	}
	/**
	 * Sorts the population by fitness.
	 */
	public void sortedHypothesisByFitness(){
		Collections.sort(this.population);
	}
	
	/**
	 * Selects the fittest from the population and adds them to new population
	 */
	public void select(){
		int num = (int)((1-this.r)*p);
		for (int i=0; i<num; i++){
			this.newPopulation.add(this.population.get(i));
		}
	}
	
	public void handleOddNumber(){
		if ((this.population.size() - this.newPopulation.size())%2 != 0){//odd
			this.newPopulation.add(this.population.get(this.newPopulation.size()));
		}
	}
	
	/**
	 * Select (r*p/2)pairs of hypothesis from P according to rank of each hypothesis
	 *	for each pair, apply crossover and add new offspring to this.newPopulation
	 */
	public void crossOver(){ 
		CrossOver CO = new CrossOver();
		int num = (this.population.size() - this.newPopulation.size())/2;
		for (int i=0; i<num; i++){
			int parent1 = randomizer(0, num+1);
			int parent2 = randomizer(num, (num*2)+1);
			int j = i*2;
			Hypothesis[] H = new Hypothesis[2];
			if (this.crossOverMethod == 0){
				H = CO.uniform(this.population.get(parent1), this.population.get(parent2));
			}
			else if (this.crossOverMethod == 1){
				H = CO.singlePoint(this.population.get(parent1), this.population.get(parent2));
			}
			else{
				H = CO.twoPoint(this.population.get(parent1), this.population.get(parent2));
			}
			this.newPopulation.add(H[0]);
			this.newPopulation.add(H[1]);
		}
	}
	/**
	 * Performs Point mutation on a given Hypothesis
	 * @param H, the Hypothesis
	 * @return a new Hypothesis that has mutations
	 */
	public Hypothesis mutationProcess(Hypothesis H){
		//Hypothesis Hm = H;
		int rand = randomizer(0, H.getBitsHypothesis().length());
		String flip = "";
		if (H.getBitsHypothesis().charAt(rand)=='0'){flip = "1";}
		else {flip = "0";}
		Hypothesis Hm = new Hypothesis(H.getBitsHypothesis().substring(0, rand)+flip+H.getBitsHypothesis().substring(rand+1), lengthOfClass);
		return Hm;
	}
	
	public void mutatePopulation(){
		//choose m% of members of this.newPopulation to be mutated --> call mutationProcess method for each chosen hypothesis 
		int num = (int)(m*p);
		ArrayList<Integer> index = new ArrayList();
		for (int i=0; i<this.newPopulation.size(); i++){
			index.add(i);
		}
		for (int i=0; i< num; i++){
			int rand = randomizer(0, index.size());
			this.newPopulation.set(index.get(rand), this.mutationProcess(this.newPopulation.get(index.get(rand))));
		}
	}
	
	public void updatePopulation(){
		this.population = this.newPopulation;
	}
	/**
	 * Performs the Selection 
	 * and mutation and resorts the 
	 */
	public void evolution(int fold){ //return hypothesis with the highest fitness from the new population
		Integer gen = generations.get(fold);
		gen++;
		this.newPopulation = new ArrayList();
		this.select();
		this.crossOver();
		this.mutatePopulation();
		for (Hypothesis h : this.newPopulation){
			h.setClass(task);
			double fit = fitnessFuncs.get(fold).evalTrainingSet(h);
			h.setFitness(fit);
		}
		this.updatePopulation();
		this.sortedHypothesisByFitness();
	}
	
	public Hypothesis findBestRule(int fold){
		double maxFitness = this.population.get(0).getFitness();
		while (maxFitness < this.fitnessTH){
			evolution(fold);
			maxFitness = this.population.get(0).getFitness();
			
			
		}
		printPopulation();
		return this.population.get(0);
	}
	
	public void findBestHyps(){
		this.bestHyps = new ArrayList();
		for (int i=0; i<this.folds; i++){
			this.generatePopulation(i);
			Hypothesis H = findBestRule(i);
			this.bestHyps.add(H);
		}
		for (int i=0; i<this.bestHyps.size();i++){
			System.out.println("Best Rule (k = "+i+") :\n    "+bestHyps.get(i).getBitsHypothesis()+
					"\n      fitness : "+bestHyps.get(i).getFitness()
				   +"\n test fitness : " + fitnessFuncs.get(i).evalTestSet(bestHyps.get(i)));
		}
	}
	
	public double testBestHyps(){
		double fit = 0;
		for (int i=0; i<this.folds; i++){
			fit += fitnessFuncs.get(i).evalTestSet(this.bestHyps.get(i));
		}
		return fit/this.folds;
	}
	
	public void printBestRules(Hypothesis best){
		if (this.task == 2){
			System.out.println("Best Rule :\n"+best.getBitsHypothesis().toString());
		}
		else {
			String b1 = best.getBitsHypothesis().substring(0, best.getBitsHypothesis().length()/3);
			String b2 = best.getBitsHypothesis().substring(best.getBitsHypothesis().length()/3, 2*best.getBitsHypothesis().length()/3);
			String b3 = best.getBitsHypothesis().substring(2*best.getBitsHypothesis().length()/3);
			System.out.println("Best Rule :\n1. "+b1+" length = "+b1.length()+" \n2. "+b2+" length = "+b2.length()+" \n3. "+b3+" length = "+b3.length());
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int p = 10;
		double r = 0.6;
		double m = 0.4;
		double fitnessTH = 0.99;
		int task = 1;
		int folds = 10;
		int COMethod = 1; //0 = uniform, 1 = single point, 2 = two points
		GACV ga = new GACV(p, r, m, task, new Accuracy(), fitnessTH, folds);
		ga.setCrossOverMethod(COMethod);
		
		ga.generatePopulation(1);
		ga.findBestHyps();
		System.out.println(ga.testBestHyps());
		
	}

}
