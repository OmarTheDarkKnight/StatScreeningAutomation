package stat;

import stat.util.ExcelFileWriter;

import java.util.Map;
import java.util.TreeMap;

public class Statistics {
    public static void main(String[] args) {
        String fileBasePath = "C:\\Users\\jayed\\Documents\\B stat\\setting4\\";
        //Bon_error_mixture_05_1 , Bon_Chisquare_05_1, Bon_Cauchy_05_1
        String dataFileName = "tt_Cauchy_05_1";
        String ext = ".xlsx";

        Calculator calculator = new Calculator(fileBasePath + dataFileName + ext);

        Map<Integer, ExcelFileWriter> excelWriters = new TreeMap<Integer, ExcelFileWriter>();

        int start = 0;
        for(int i = 1; i <= 100 ; i++) {
            start = calculator.readDataForDataset(i, start);
            calculator.calculate();
//            System.out.println("Cutoff--SENSI----SPECI-----MISI-----ACCU");
//            System.out.println("=========================================");
            for(int cutoff : calculator.getCutoffs()) {
                if(!excelWriters.containsKey(cutoff)) {
                    excelWriters.put(cutoff, new ExcelFileWriter(fileBasePath + dataFileName
                            + "_" + cutoff +"_cutoff" + ext));
                }

                excelWriters.get(cutoff).addToWriter(new String[] {
                        calculator.getSensitivity().get(cutoff),
                        calculator.getSpecificity().get(cutoff),
                        calculator.getAccuracy().get(cutoff),
                        calculator.getMisidentified().get(cutoff).toString()
                });

//                System.out.println(cutoff + "      "
//                        + calculator.getSensitivity().get(cutoff) + "    "
//                        + calculator.getSpecificity().get(cutoff) + "      "
//                        + calculator.getMisidentified().get(cutoff) + "      "
//                        + calculator.getAccuracy().get(cutoff)
//                );
            }
        }

        for(Map.Entry<Integer, ExcelFileWriter> item : excelWriters.entrySet()) {
            item.getValue().writeFile();
        }
    }
}
