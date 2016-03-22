package GeneticAlgorithm;

import java.awt.List;
import java.util.ArrayList;
import java.util.Random;

public class CrossOver {
	/**
	 * Constructor
	 */
	public CrossOver(){
		
	}
	
	/**
	 * Single point Crossover mutation
	 * ie 
	 *  p1 = 1111000
	 *  p2 = 0000111
	 *  
	 *  output could be 
	 *   1111111 or 0000000 or 11100111 ect
	 *   
	 */
	public Hypothesis[] singlePoint(Hypothesis parent1, Hypothesis parent2){
		Random rand = new Random();
		String head;
		String tail;
		if(rand.nextInt(2) % 2 == 0){ // choose head and tail randomly
			head = parent1.getBitsHypothesis();
			tail = parent2.getBitsHypothesis();
		} else {
			head = parent2.getBitsHypothesis();
			tail = parent1.getBitsHypothesis();
		}
		int splitAt = rand.nextInt(head.length());
		String result1 = head.substring(0, splitAt);
		result1 += tail.substring(splitAt);
		String result2 = tail.substring(0, splitAt);
		result2 += head.substring(splitAt);
		Hypothesis output1 = new Hypothesis(result1, parent1.getLengthOfClass());
		Hypothesis output2 = new Hypothesis(result2, parent1.getLengthOfClass());
		output1.setParent1(parent1);
		output1.setParent2(parent2);
		output2.setParent1(parent1);
		output2.setParent2(parent2);
		output1.setFitnessfunc(parent1.getFitnessfunc());
		output2.setFitnessfunc(parent1.getFitnessfunc());
		Hypothesis[] OffSprings = new Hypothesis[2];
		OffSprings[0] = output1;
		OffSprings[1] = output2;
		return OffSprings;
	}
	
	public Hypothesis[] twoPoint(Hypothesis parent1, Hypothesis parent2){
		Random rand = new Random();
		String head;
		String tail;
		int length = parent1.getBitsHypothesis().length()/2;
		if(rand.nextInt(2) % 2 == 0){ // choose head and tail randomly
			head = parent1.getBitsHypothesis();
			tail = parent2.getBitsHypothesis();
		} else {
			head = parent2.getBitsHypothesis();
			tail = parent1.getBitsHypothesis();
		}
		int max = parent1.getBitsHypothesis().length()/2-1;
		int p1 = randomizer(0, max);
		int p2 = p1 + length;
		System.out.println("p1 : "+p1+" --- p2 : "+p2);
		String result1 = head.substring(0, p1) + tail.substring(p1, p2) + head.substring(p2);
		String result2 = tail.substring(0, p1) + head.substring(p1, p2) + tail.substring(p2);
		Hypothesis output1 = new Hypothesis(result1, parent1.getLengthOfClass());
		Hypothesis output2 = new Hypothesis(result2, parent1.getLengthOfClass());
		output1.setParent1(parent1);
		output1.setParent2(parent2);
		output2.setParent1(parent1);
		output2.setParent2(parent2);
		output1.setFitnessfunc(parent1.getFitnessfunc());
		output2.setFitnessfunc(parent1.getFitnessfunc());
		Hypothesis[] OffSprings = new Hypothesis[2];
		OffSprings[0] = output1;
		OffSprings[1] = output2;
		return OffSprings;
	}
	
	public Hypothesis[] uniform(Hypothesis parent1, Hypothesis parent2){
		Random rand = new Random();
		int[] minpattern = {1,0,0,1,1,0};
		String head;
		String tail;
		int length = parent1.getBitsHypothesis().length()/2;
		if(rand.nextInt(2) % 2 == 0){ // choose head and tail randomly
			head = parent1.getBitsHypothesis();
			tail = parent2.getBitsHypothesis();
		} else {
			head = parent2.getBitsHypothesis();
			tail = parent1.getBitsHypothesis();
		}
		
		ArrayList<Integer> pattern = new ArrayList();
		for (int i=0; i<parent1.getBitsHypothesis().length(); i++){
			pattern.add(minpattern[i%6]);
		}
		
		String result1 = "";
		String result2 = "";
		
		for (int i=0; i<parent1.getBitsHypothesis().length(); i++){
			if (pattern.get(i)==1){
				result1 += head.charAt(i);
				result2 += tail.charAt(i);
			}
			else {
				result2 += head.charAt(i);
				result1 += tail.charAt(i);
			}
		}
		
		Hypothesis output1 = new Hypothesis(result1, parent1.getLengthOfClass());
		Hypothesis output2 = new Hypothesis(result2, parent1.getLengthOfClass());
		output1.setParent1(parent1);
		output1.setParent2(parent2);
		output2.setParent1(parent1);
		output2.setParent2(parent2);
		output1.setFitnessfunc(parent1.getFitnessfunc());
		output2.setFitnessfunc(parent1.getFitnessfunc());
		Hypothesis[] OffSprings = new Hypothesis[2];
		OffSprings[0] = output1;
		OffSprings[1] = output2;
		return OffSprings;
	}
	
	public int randomizer(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min)) + min +1;
	    return randomNum;
	}
}
