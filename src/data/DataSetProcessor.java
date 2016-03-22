package data;
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class DataSetProcessor {

	private Instances instances;
	private String path;
	private int NumOfAttribute; //number of attribute + class
	private int NumOfInstances; //number of instances
	private int task; //task 1 : balance-scale, task 2 : mushroom
	private ArrayList<String> BinaryDataSet; //dataset converted to BitString
	private int bitStringLength;
	private String printedDataSet;
	private int seed;

	public int getBitStringLength() {
		return bitStringLength;
	}
	public void setBitStringLength(int bitStringLength) {
		this.bitStringLength = bitStringLength;
	}
	public String getPrintedDataSet() {
		return printedDataSet;
	}
	public void setPrintedDataSet(String printedDataSet) {
		this.printedDataSet = printedDataSet;
	}
	/* CONSTRUCTORS */
	public DataSetProcessor(){
			
	}
	/**
	 * 
	 * @param path, the path of the .arff file
	 * @param task, the task number, 1 is balance scale, 2 is mushrooms
	 */
	public DataSetProcessor(String path, int task){
		this.path = path;
		this.BinaryDataSet = new ArrayList();
		try {
			this.ReadArrfFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(task == 1){ 
			modifyInstances();
		}
		this.NumOfAttribute = instances.numAttributes();
		
		HashMap<String, String> codes = getCodes(instances);
		for(int i = 0; i < instances.numInstances(); i++){
			Instance inst = instances.instance(i);
			String dataPoint = "";
			for(int j = 0; j < inst.numAttributes(); j++){
				String key;
				try {
					 key = inst.attribute(j).name() + "@" + inst.stringValue(j);
				} catch (Exception e){
					key = inst.attribute(j).name() + "@" + String.valueOf(inst.value(j));
				}
				dataPoint += codes.get(key);
					
			}
			BinaryDataSet.add(dataPoint);
		}
	}
	
	/* GETTERS AND SETTERS */
	public Instances getInstances() {
		return instances;
	}

	public void setInstances(Instances instances) {
		this.instances = instances;
	}
	/*
	public ArrayList<int[]> getDatasets() {
		return datasets;
	}

	public void setDatasets(ArrayList<int[]> datasets) {
		this.datasets = datasets;
	}
	*/
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getNumOfAttribute() {
		return NumOfAttribute;
	}

	public void setNumOfAttribute(int numOfAttribute) {
		NumOfAttribute = numOfAttribute;
	}

	public int getNumOfInstances() {
		return NumOfInstances;
	}

	public void setNumOfInstances(int numOfInstances) {
		NumOfInstances = numOfInstances;
	}

	public int getTask() {
		return task;
	}

	public void setTask(int task) {
		this.task = task;
	}

	public ArrayList<String> getBinaryDataSet() {
		return BinaryDataSet;
	}

	public void setBinaryDataSet(ArrayList<String> binaryDataSet) {
		BinaryDataSet = binaryDataSet;
	}

	/* FILE METHOD */
	public void ReadArrfFile(String path) throws Exception{
		DataSource source = new DataSource(path);
		instances = source.getDataSet();
		this.printedDataSet = instances.toString();
		 // Make the last attribute be the class
		 instances.setClassIndex(instances.numAttributes() - 1);
		 
		 
		 // Print header and instances.
		 //System.out.println("\nDataset:\n");
		 //System.out.println(instances.get(0).numValues());
	}
	
	public String PrintedDataSetList(){
		String S = "";
		for (int i=0; i<this.instances.numInstances(); i++){
			S += i+" : {( ";
			for (int j=0; j<this.instances.numAttributes()-1; j++){
				S += this.instances.instance(i).value(j)+" ";
			}
			S += "), class : "+this.instances.instance(i).attribute(this.instances.numAttributes()-1)+" )}\n";
		}
		return S;
	}
	
	
	public String ProcessBitString(String bs, int len){
		while (bs.length()<len){
			bs = "0"+bs;
		}
		return bs;
	}
	
	public void PrintBitString(){
		for (int i=0; i<this.BinaryDataSet.size(); i++){

			System.out.println("Hypothesis "+i+" : "+this.BinaryDataSet.get(i));
		}
	}
	
	public String PrintedBitString(){
		String S = "";
		for (int i=0; i<this.BinaryDataSet.size(); i++){

			S += "\nHypothesis "+i+" : "+this.BinaryDataSet.get(i);
		}
		return S;
	}
	
	/*
	public void PrintDataSetAndBitString(){
		for (int i=0; i<this.datasets.size(); i++){
			System.out.print(i+" : {( ");
			for (int j=0; j<this.NumOfAttribute-1; j++){
				System.out.print(this.datasets.get(i)[j]+" ");
			}
			System.out.print("), class : "+this.datasets.get(i)[this.NumOfAttribute-1]+" )} --> "+this.BinaryDataSet.get(i)+"\n");
		}
	}
	*/
	
	/*
	 * we make a new set of Instances based off the old dataset for task 1
	 * instead of [lw,ld, rw, rd, class] we take
	 * 	rw*rd - lw*ld = torque 
	 * so the dataset becomes {torque, class} 
	 */
	private void modifyInstances() {
		FastVector torqueVal = new FastVector(49);
		
		LinkedList<Integer> list = new LinkedList<Integer>();
		// generate the possible values of torque
		for(int i = 1; i < 6; i++){
			for(int j = 1; j < 6; j++){
				for(int k = 1; k < 6; k++){
					for(int l = 1; l < 6; l++){
						int t = i*j - k*l;
						if(!list.contains(new Integer(t))) list.add(t);
					}	
				}
			}
		}
		
		for(Integer i : list){
			torqueVal.addElement(String.valueOf(i));
		}
		
		FastVector classVal = new FastVector(3);
		classVal.addElement("B");
		classVal.addElement("L");
		classVal.addElement("R");
		
		Attribute torque =  new Attribute("torque", torqueVal);
		Attribute classes =  new Attribute("class", classVal);
		
		FastVector myAttributes = new FastVector(2);
		myAttributes.addElement(torque);
		myAttributes.addElement(classes);
		Instances myInstances = new Instances("torque-balance", myAttributes, instances.numInstances());
		myInstances.setClassIndex(1);
		
		for(int i = 0; i < instances.numInstances(); i++){
			Instance old = instances.instance(i);
			Instance toBeAdded = new DenseInstance(2);
			int lw = (int)old.value(0);
			int ld = (int)old.value(1);
			int rw = (int)old.value(2);
			int rd = (int)old.value(3);
			toBeAdded.setValue((Attribute)myAttributes.elementAt(0),String.valueOf(lw*ld - rw*rd));
			toBeAdded.setValue((Attribute)myAttributes.elementAt(1), old.toString(4));
			//System.out.println("ins-"+i+ " : "+toBeAdded.value(0)+" class : "+toBeAdded.value(1));
			myInstances.add(toBeAdded);
		}
		instances = myInstances;
	}
	
	
	// we ignore the missing attribute entirely 
	private HashMap<String, String> getCodes(Instances instances) {
		HashMap<String , String > codes = new HashMap<String, String>(); 
		for(int i = 0; i < NumOfAttribute; i++){
			Attribute  atr = instances.attribute(i);
			boolean hasMissing = hasMissingValues(atr);
			if(hasMissing){
				codes.put(atr.name()+"@?","");
			}
			ArrayList attrList ;
			if(!atr.isNumeric()){ // if the value is numeric then it is the balance scale set
				attrList = Collections.list(atr.enumerateValues());
			} else{
				attrList = new ArrayList();
				for(int k = 1; k < 6; k++){
					attrList.add(String.valueOf(k));
				}
			}
			for(int j = 0; j < attrList.size(); j++){
				String key = atr.name() + "@" + (String)attrList.get(j);
				String value = "";
				if(i == NumOfAttribute - 1){ // ie it is the class that we are trying to predict
					if(attrList.get(j).equals("e")) value = "1";
					if(attrList.get(j).equals("p")) value = "0";
					
					if(attrList.get(j).equals("L")) value = "01";
					if(attrList.get(j).equals("R")) value = "10";
					if(attrList.get(j).equals("B")) value = "11";
					
				} else {
					if(!hasMissing){ // if the attribute has missing values then we ignore 
						for(int k = 0; k < attrList.size(); k++){
							if(k == j) value += "1"; 
							else value += "0";
						}
					}
				}
				codes.put(key, value);
			}
		}
		return codes;
	}
	
	public String getAsHumanReadable(String hyp){
		// for each attribute 
		String result = "";
		int position = 0;
		for(int i = 0; i < NumOfAttribute; i++){
			HashMap<String, String> codes = new HashMap<String, String>();
			Attribute  atr = instances.attribute(i);
			boolean hasMissing = hasMissingValues(atr);
			if(!hasMissing){
				ArrayList attrList ;
				if(!atr.isNumeric()){ // if the value is numeric then it is the balance scale set
					attrList = Collections.list(atr.enumerateValues());
				} else{
					attrList = new ArrayList();
					for(int k = 1; k < 6; k++){
						attrList.add(String.valueOf(k));
					}
				}
				for(int j = 0; j < attrList.size(); j++){
					String value = (String)attrList.get(j);
					String key = "";
					if(i == NumOfAttribute - 1){ // ie it is the class that we are trying to predict
						if(attrList.get(j).equals("e")) key = "1";
						if(attrList.get(j).equals("p")) key = "0";
						
						if(attrList.get(j).equals("L")) key = "01";
						if(attrList.get(j).equals("R")) key = "10";
						if(attrList.get(j).equals("B")) key = "11";
						
					} else {
						if(!hasMissing){ // if the attribute has missing values then we ignore 
							for(int k = 0; k < attrList.size(); k++){
								if(k == j) key += "1"; 
								else key += "0";
							}
						}
					}
					System.out.println(key + " --> " + value);
					codes.put(key, value);
				}
				String subHype = hyp.substring(position,attrList.size() - 1);
				for(int k = 0; k < attrList.size(); k++){
					
				}
				System.out.println(subHype);
				result += "\n allowed values for " + atr.name();
				
				position += attrList.size() -1;
				

					
				// from the rule extract the list of allowed values "11000" -> "10000" and "01000"
				
				// add the name followed by the allowed values
	
			}
		}
		return result;
	}
	
	private boolean hasMissingValues(Attribute attr){
		for(int i = 0; i < instances.numInstances(); i++){
			Instance myInst = instances.instance(i);
			if(myInst.isMissing(attr)) return true;
		}
		return false;
	}
	
	public Instances RandomizeData(){
		Random rand = new Random(seed);   // create seeded number generator
		Instances randData = this.getInstances();   // create copy of original data
		randData.randomize(rand);         // randomize data with number generator
		return randData;
	}
	
	public Instances[] DivideTrainingData(int fold, int n){
		Instances randData = this.RandomizeData();
		Instances[] insList = new Instances[2];
		Instances train = randData.trainCV(fold, n);
	    Instances test = randData.testCV(fold, n);
		return insList;
	}
	
}
