import java.io.*;
import java.util.*;

/**
 * Created by Vlad on 16.02.2015.
 */
public class Juice {
   private ArrayList<String> list = new ArrayList<String>();
    private HashSet<String> set = new HashSet<String>();

    void readFile() throws IOException {
        String s;
        BufferedReader bf = new BufferedReader(new FileReader("fruits.in"));
        while((s = bf.readLine()) != null){
            StringTokenizer token = new StringTokenizer(s, " ");
            while(token.hasMoreTokens()) {
                list.add(token.nextToken());
            }
        }
    }

    void writeFile() throws IOException {
        String s1;
        BufferedReader bf = new BufferedReader(new FileReader("fruits.in"));
        while((s1 = bf.readLine()) != null){
            StringTokenizer token = new StringTokenizer(s1, " ");
            while(token.hasMoreTokens()) {
                set.add(token.nextToken());
            }
        }
        String s;
        BufferedWriter bw = new BufferedWriter(new FileWriter("juice1.out"));
        Iterator<String> it = set.iterator();
        while(it.hasNext()){
            s = it.next();
            bw.write(s);
            bw.newLine();
        }
        bw.close();
    }

    void countN() throws IOException {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if(o1.length() < o2.length())
                    return -1;
                if(o1.length() > o2.length())
                    return 1;
                else
                    return 0;
            }
        });

        String s;
        int count1 = 0;
        int index;
        int n = list.size();
        for (int i = 0; i < list.size(); i++) {
            s = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                if(list.get(j).contains(s)) {
                    count1++;
                    index = list.indexOf(s);
                    s = list.get(j);

                    list.remove(index);
                }

            }
        }

        BufferedWriter out = new BufferedWriter(new FileWriter("juice3.out"));
        out.write("Min: " + (n - count1));
        out.close();
    }
}
