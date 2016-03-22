package GeneticAlgorithm;

import java.util.ArrayList;

public interface FitnessFunction {
	public double evalTestSet (Hypothesis h);
	public double evalTrainingSet(Hypothesis h);
	void setDataSet(ArrayList<String> dataSet);

	String[] getTestSet();

	void setTestSet(String[] testSet);

	String[] getTrainSet();

	void setTrainSet(String[] trainSet);

	
}