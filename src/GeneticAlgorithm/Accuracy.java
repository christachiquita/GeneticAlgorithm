package GeneticAlgorithm;

import java.io.IOException;
import java.util.ArrayList;

public class Accuracy implements FitnessFunction {
	private ArrayList<String> dataSet;
	private String[] testSet;
	private String[] trainSet;

	@Override
	public String[] getTestSet() {
		return testSet;
	}

	@Override
	public void setTestSet(String[] testSet) {
		this.testSet = testSet;
	}

	@Override
	public String[] getTrainSet() {
		return trainSet;
	}

	@Override
	public void setTrainSet(String[] trainSet) {
		this.trainSet = trainSet;
	}

	public ArrayList<String> getDataSet() {
		return dataSet;
	}

	public Accuracy(){ // note if you use the empty constructor you must set the dataset manually
	}
	
	public Accuracy(ArrayList<String> dataSet){
		this.dataSet = dataSet;
	}
	
	/*
	 * ACCURACY
	 * (TP + TN) / N
	 */

	/*public double evalFitness (Hypothesis h){
		double correct = 0;
		String rule = h.getBitsHypothesis();
		for(String dataPoint : dataSet){
			if(isCorrectlyClassified(rule, dataPoint, h.getLengthOfClass())) correct++;
		}
		
		return correct/Double.valueOf(dataSet.size());
		//return Math.pow(correct/Double.valueOf(dataSet.size()),2);
	}*/
	
	public double evalTrainingSet (Hypothesis h){
		return evalFitness(h, trainSet);
		
	}
	public double evalTestSet (Hypothesis h){
		return evalFitness(h, testSet);
	}
	
	private double evalFitness (Hypothesis h, String[] set){
		double correct = 0;
		double result = 0;
		String rule = h.getBitsHypothesis();
		boolean isTask1 = h.getLengthOfClass() == 2;
		for(String dataPoint : set){
			if (isTask1){
				if(isEachCorrectlyClassified(rule.substring(0, rule.length()/3), dataPoint, h.getLengthOfClass())) correct+=0.46;
				if(isEachCorrectlyClassified(rule.substring(rule.length()/3, 2*rule.length()/3), dataPoint, h.getLengthOfClass())) correct+=0.08;
				if(isEachCorrectlyClassified(rule.substring(2*rule.length()/3), dataPoint, h.getLengthOfClass())) correct+=0.46;
			}
			else {
				//System.out.println("hyp len : "+h.getBitsHypothesis().length()+", dataset len : "+dataPoint.length());
				//System.out.println("class length = "+h.getLengthOfClass());
				if(isEachCorrectlyClassified(rule.substring(0,rule.length()/2), dataPoint, h.getLengthOfClass())) correct += .5;
				if(isEachCorrectlyClassified(rule.substring(rule.length()/2), dataPoint, h.getLengthOfClass())) correct += 0.5;
			}
		}
		result = correct / Double.valueOf(set.length);
		return result;
	}
	
	
	
	
	
	public boolean isEachCorrectlyClassified(String hyp, String dataPoint, int classLength){
		if (classLength==2){
			if (hyp.substring(hyp.length() - classLength).equals("00")){
				return false;
			}
		}
//		System.out.println(hyp.length() + ", "  + dataPoint.length());
		if ( hyp.substring(hyp.length() - classLength).equals(dataPoint.substring(hyp.length() - classLength) ) ){ // if rule class is the same as the dP class
			for(int i = 0; i < hyp.length() - classLength; i++){
				if((hyp.charAt(i) == '0') && (dataPoint.charAt(i) == '1')) { // if the rule doesn't allows the value in the dp
					return false;											 // and so the rule doesnt correctly classify the point
				}
			}
			return true;
		} else {
			for(int i = 0; i < hyp.length() - classLength ; i++){
				if((hyp.charAt(i) == '0') && (dataPoint.charAt(i) == '1')) { // if the rule doesn't allows the value in the dp
					return true;											 // and so the rule correctly classifies the point
				}
			}
			return false;
		}
	}

	@Override
	public void setDataSet(ArrayList<String> dataSet) {
		this.dataSet = dataSet;	
	}

}
