/*
 * Hypothesis.java
 * created 13 May 2015
 * author : christachiquita
 * 
 * Variables :
 * 1. (String)BitsHypothesis : BitString represents the hypothesis
 * 2. (Hypothesis)parent1 : BitString represents parent1
 * 3. (Hypothesis)parent2 : BitString represents parent2
 * 4. (double)fitness : represent hypothesis fitness
 * 5. (FitnessFunction)fitnessfunc
 */

package GeneticAlgorithm;

public class Hypothesis implements Comparable {
	
	private String BitsHypothesis;
	private Hypothesis parent1;
	private Hypothesis parent2;
	double fitness;
	FitnessFunction fitnessfunc;
	private int lengthOfClass;
	private double probability; //Pr
	
	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public String getBitsHypothesis() {
		return BitsHypothesis;
	}

	public void setBitsHypothesis(String bitsHypothesis) {
		BitsHypothesis = bitsHypothesis;
	}

	public Hypothesis getParent1() {
		return parent1;
	}

	public void setParent1(Hypothesis parent1) {
		this.parent1 = parent1;
	}

	public Hypothesis getParent2() {
		return parent2;
	}

	public void setParent2(Hypothesis parent2) {
		this.parent2 = parent2;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public FitnessFunction getFitnessfunc() {
		return fitnessfunc;
	}

	public void setFitnessfunc(FitnessFunction fitnessfunc) {
		this.fitnessfunc = fitnessfunc;
	}

	public Hypothesis(String BitStrg, int lengthOfClass){
		this.BitsHypothesis = BitStrg;
		this.lengthOfClass = lengthOfClass;
	}

	public int getLengthOfClass(){
		return lengthOfClass;
	}
	
	public void setClass(int task){
		if (task == 1){ //balance-scale
			this.setBitsHypothesis(this.getBitsHypothesis().substring(0, this.getBitsHypothesis().length()/3-2) + "10" + this.getBitsHypothesis().substring(this.getBitsHypothesis().length()/3, this.getBitsHypothesis().length()/3*2-2) + "11" + this.getBitsHypothesis().substring(2*this.getBitsHypothesis().length()/3, 3*this.getBitsHypothesis().length()/3-2) + "01");
		}else{ //mushroom
			this.setBitsHypothesis(this.getBitsHypothesis().substring(0, this.getBitsHypothesis().length()/2-1) + "0" + this.getBitsHypothesis().substring(this.getBitsHypothesis().length()/2, 2*this.getBitsHypothesis().length()/2-1) + "1");
		}
	}
	
	@Override
	public int compareTo(Object h){
			return -(Double.compare(fitness, (((Hypothesis) h).getFitness())));
	}
	
}
