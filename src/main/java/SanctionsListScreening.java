import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SanctionsListScreening {
    private String sanctionsFile;
    private boolean categorised;
    private Map<String,Integer> sanctionsList = new HashMap<>();
    private final Map<Integer,String> entities = Stream.of(new Object[][] {
            {0,"Individual"},
            {1,"Organisation"},
            {2,"Country"},
            {3,"N/A"}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (String) data[1]));
    private int threshold;


    SanctionsListScreening() {
        this.sanctionsFile = "sancList.csv";
        this.threshold = 75;
        this.categorised = false;
    }

    SanctionsListScreening(String sanctionsFile, int threshold, boolean categorised) {
        this.categorised = categorised;
        this.sanctionsFile = sanctionsFile;
        this.threshold = threshold;
    }

    SanctionsListScreening(boolean categorised, String sanctionsFile) {
        this.sanctionsFile = sanctionsFile;
        this.threshold = 75;
        this.categorised = categorised;
    }

    void readSanctionsList() throws IOException {

        String cwd = new File("").getAbsolutePath();
        BufferedReader br = new BufferedReader(new FileReader(cwd + "/Dataset/" + sanctionsFile));
        String line;
        if(!categorised){
            line = br.readLine();
            String names[] = line.split(",");
            for (String name : names){
                sanctionsList.put(name,3);
            }
        }
        else{
            Map<String,Integer> reversed = reversemap(entities);
            while((line = br.readLine()) != null){
                String attr[] = line.split(",");
                sanctionsList.put(attr[0],reversed.get(attr[1]));
            }
        }
    }

    void verifySanctionsList(String name) {
        Map<Double,String> hit_percent = new TreeMap<>();
        Levenshtein lv = new Levenshtein();

        for(String sanctionedName : sanctionsList.keySet()){
            int mismatch_chars = lv.stringDiff(name,sanctionedName);
            if(mismatch_chars<=threshold){
                hit_percent.put((double) (100-(mismatch_chars*100/name.length())),sanctionedName);
            }
        }
        AtomicInteger count= new AtomicInteger(0);
        hit_percent.forEach((k,v) -> {
            if(k>=75){
                count.getAndIncrement();
                if(sanctionsList.get(v)<3) {
                    System.out.println("Hit, " + k + "% match; Matched with: " + v + ", Type: " + entities.get(sanctionsList.get(v)));
                }else {
                    System.out.println("Hit, " + k + "% match; Matched with: " + v);
                }
            }
        });
        if(count.intValue()==0){
            System.out.println("No Hit");
        }

    }

    public static Map<String,Integer> reversemap(Map<Integer, String> map) {
        return map.entrySet().stream().collect(Collectors.toMap(HashMap.Entry::getValue, Map.Entry::getKey));
    }
}