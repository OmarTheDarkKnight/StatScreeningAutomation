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

        int totalDataSet = (int) prop.get("total.dataset");
        double impValueCount = (double) prop.get("important.value.count");

        int[] cutoffs;
        if((boolean)prop.get("calculate.cutoff")) {
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
                outputFilePath += (boolean)prop.get("calculate.cutoff") ? "_" + cutoff +"_cutoff" : "_output";
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
