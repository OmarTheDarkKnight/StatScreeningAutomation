package stat;

import stat.util.ExcelFileReader;

import java.util.HashMap;
import java.util.Map;

public class Calculator {
    int[] cutoffs;
    double impValueCount;
    double totalValue;
    double remainingValue;
    int decimalPoint;

    String strFormat;

    HashMap<Integer, Integer> dataList;
    HashMap<Integer, String> sensitivity;
    HashMap<Integer, String> specificity;
    HashMap<Integer, Integer> misidentified;
    HashMap<Integer, String> accuracy;

    String dataSetFile;
    ExcelFileReader excelFileReader;

    public Calculator(String dataSetFile, double impValueCount) {
        this.impValueCount = impValueCount;
        this.totalValue = 2000d;
        this.remainingValue = this.totalValue - this.impValueCount;
        this.dataSetFile = dataSetFile;
        cutoffs = new int[]{30, 40, 50, 60, 70, 80, 90};
//        cutoffs = new int[]{30};
        this.excelFileReader = new ExcelFileReader(dataSetFile);

        decimalPoint = getDecimalPointLength((int)this.impValueCount);
        System.out.println(decimalPoint);
        this.strFormat = "%." + decimalPoint +"f";
    }

    private int getDecimalPointLength(int value) {
        int length = -1;
        while(value > 0) {
            length++;
            value = value / 10;
        }
        return length;
    }

    public int readDataForDataset(int dataset, int startRow) {
        this.dataList = new HashMap<>();

        int colA = this.excelFileReader.getIntCellData(startRow, 0);
        int colB = -1;
        int colC = -1;
        while (colA == dataset) {
            colB = this.excelFileReader.getIntCellData(startRow, 1);
            colC = this.excelFileReader.getIntCellData(startRow, 2);
            if(this.dataList.containsKey(colB)) {
                // check colC val
                int oldColC = this.dataList.get(colB);
                if(oldColC > colC) {
                    colC = oldColC;
                }
            }
            this.dataList.put(colB, colC);
            startRow++;
            colA = this.excelFileReader.getIntCellData(startRow, 0);
        }

        System.out.println(startRow);
        System.out.println(this.dataList);

        return startRow;
    }

    public void calculate() {
        sensitivity = new HashMap<>();
        specificity = new HashMap<>();
        misidentified = new HashMap<>();
        accuracy = new HashMap<>();

        for(int cutoff : this.cutoffs) {
            int SensitivityCounter = 0;
            int specificityCounter = 0;
            int misIdentifiedValue = 0;

            for(Map.Entry<Integer, Integer> item : this.dataList.entrySet()) {
                int colC = item.getValue();

                if(colC >= cutoff) {
                    int colB = item.getKey();
                    // for sensitivity
                    if(colB >=1 && colB <= this.impValueCount) {
                        SensitivityCounter++;
                    }
                    // for specificity
                    else if(colB > this.impValueCount) {
                        specificityCounter++;
                    }
                }
            }
            double d = (SensitivityCounter / this.impValueCount);
            String s = String.format("%." + decimalPoint +"f", d);
            System.out.println(s);
            sensitivity.put(cutoff, s);

            d = ((int)this.remainingValue - specificityCounter) / this.remainingValue;
            s = String.format("%.4f", d);
            specificity.put(cutoff, s);

            misIdentifiedValue = ( (int)this.impValueCount - SensitivityCounter) + specificityCounter;

            misidentified.put(cutoff, misIdentifiedValue);

            d = ((int)this.totalValue - misIdentifiedValue) / this.totalValue;
            s = String.format("%.4f", d);
            accuracy.put(cutoff, s);
        }
    }

    public int[] getCutoffs() {
        return cutoffs;
    }

    public void setCutoffs(int[] cutoffs) {
        this.cutoffs = cutoffs;
    }

    public HashMap<Integer, Integer> getDataList() {
        return dataList;
    }

    public void setDataList(HashMap<Integer, Integer> dataList) {
        this.dataList = dataList;
    }

    public HashMap<Integer, String> getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(HashMap<Integer, String> sensitivity) {
        this.sensitivity = sensitivity;
    }

    public HashMap<Integer, String> getSpecificity() {
        return specificity;
    }

    public void setSpecificity(HashMap<Integer, String> specificity) {
        this.specificity = specificity;
    }

    public HashMap<Integer, Integer> getMisidentified() {
        return misidentified;
    }

    public void setMisidentified(HashMap<Integer, Integer> misidentified) {
        this.misidentified = misidentified;
    }

    public HashMap<Integer, String> getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(HashMap<Integer, String> accuracy) {
        this.accuracy = accuracy;
    }
}
