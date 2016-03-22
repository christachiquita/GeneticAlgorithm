package GeneticAlgorithm;

import java.util.ArrayList;

import data.DataSetProcessor;

public class Validation {

	private String rule;
	private ArrayList<String> binaryDataSet;
	private ArrayList<String[]> trains;
	private ArrayList<String[]> tests;
	private int k;
	private int task;
	
	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public ArrayList<String> getBinaryDataSet() {
		return binaryDataSet;
	}

	public void setBinaryDataSet(ArrayList<String> binaryDataSet) {
		this.binaryDataSet = binaryDataSet;
	}

	public ArrayList<String[]> getTrains() {
		return trains;
	}

	public void setTrains(ArrayList<String[]> trains) {
		this.trains = trains;
	}

	public ArrayList<String[]> getTests() {
		return tests;
	}

	public void setTests(ArrayList<String[]> tests) {
		this.tests = tests;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public Validation(){
		
	}
	
	public Validation(ArrayList<String> binaryDS, int k, int task){
		//this.rule = rule;
		this.binaryDataSet = binaryDS;
		this.k = k;
		this.task = task;
		this.trains = new ArrayList();
		this.tests = new ArrayList();
		this.DivideDataSet();
		/*for (int i=0; i<k; i++){
			System.out.println("train - "+i+" = "+this.trains.get(i).length);
			System.out.println("test - "+i+" = "+this.tests.get(i).length);
		}*/
	}
	
	public void DivideDataSet(){
		int each = this.binaryDataSet.size()/k;
		for (int i=0; i<k; i++){
			this.trains.add(this.assignTrainSet(i, each));
			this.tests.add(this.assignTestSet(i, each));
		}
	}
	
	public String[] assignTestSet(int i, int each){
		String[] S = new String[each];
		int x=0;
		for (int j=0; j<this.binaryDataSet.size(); j++){
			if (j>=(i*each) && j<(i+1)*each){
				S[x] = this.binaryDataSet.get(j);
				x++;
			}
		}
		return S;
	}
	
	public String[] assignTrainSet(int i, int each){
		String[] S = new String[this.binaryDataSet.size()-each];
		int x=0;
		for (int j=0; j<this.binaryDataSet.size(); j++){
			if (j>=(i*each) && j<(i+1)*each){}
			else {
				S[x] = this.binaryDataSet.get(j);
				x++;
			}
		}
		return S;
	}
	
	
}
