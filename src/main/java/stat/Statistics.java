package stat;

import stat.util.ExcelFileWriter;
import stat.util.PropertyProvider;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class Statistics {
    public static void main(String[] args) {
        PropertyProvider propertyProvider = new PropertyProvider();
        Properties prop = propertyProvider.getProp();

        String fileBasePath = (String) prop.get("folder.path");
        String dataFileName = (String) prop.get("file.name");
        String ext = ".xlsx";

        int totalDataSet = Integer.valueOf((String) prop.get("total.dataset"));
//        int totalDataSet = 100;
        double impValueCount = Double.valueOf((String) prop.get("important.value.count"));
//        double impValueCount = 10d;

        int[] cutoffs;
        Boolean cutoffCalculate = Boolean.valueOf((String) prop.get("calculate.cutoff"));
        if(cutoffCalculate) {
            cutoffs = new int[]{30, 40, 50, 60, 70, 80, 90};
        } else {
            cutoffs = new int[]{0};
        }

        Calculator calculator = new Calculator(fileBasePath + dataFileName + ext, impValueCount, cutoffs);

        Map<Integer, ExcelFileWriter> excelWriters = new TreeMap<Integer, ExcelFileWriter>();

        int start = 0;
        for(int i = 1; i <= totalDataSet ; i++) {
            start = calculator.readDataForDataset(i, start);
            calculator.calculate();

            for(int cutoff : cutoffs) {
                String outputFilePath = fileBasePath + dataFileName;
                outputFilePath += cutoffCalculate ? "_" + cutoff +"_cutoff" : "_output";
                outputFilePath += ext;

                if(!excelWriters.containsKey(cutoff)) {
                    excelWriters.put(cutoff, new ExcelFileWriter(outputFilePath));
                }

                excelWriters.get(cutoff).addToWriter(new String[] {
                        calculator.getSensitivity().get(cutoff),
                        calculator.getSpecificity().get(cutoff),
                        calculator.getAccuracy().get(cutoff),
                        calculator.getMisidentified().get(cutoff).toString()
                });
            }
        }

        for(Map.Entry<Integer, ExcelFileWriter> item : excelWriters.entrySet()) {
            item.getValue().writeFile();
        }
    }
}
