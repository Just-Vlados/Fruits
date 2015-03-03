import java.io.*;
import java.util.*;

/**
 * Created by Vlad on 16.02.2015.
 */
public class Juice {
    private ArrayList<String> listofFruits = new ArrayList<String>();
    private HashSet<String> setofdiffFruits = new HashSet<String>();

    void readFile() throws IOException {
        String fruits;
        BufferedReader bf = new BufferedReader(new FileReader("fruits.in"));
        while ((fruits = bf.readLine()) != null) {
            StringTokenizer token = new StringTokenizer(fruits, " ");
            while (token.hasMoreTokens()) {
                listofFruits.add(token.nextToken());
            }
        }
    }

    public void writeFile() throws IOException {
        String fruits;
        BufferedReader bf = new BufferedReader(new FileReader("fruits.in"));
        while ((fruits = bf.readLine()) != null) {
            StringTokenizer token = new StringTokenizer(fruits, " ");
            while (token.hasMoreTokens()) {
                setofdiffFruits.add(token.nextToken());
            }
        }
        String diffFruits;
        BufferedWriter bw = new BufferedWriter(new FileWriter("juice1.out"));
        Iterator<String> it = setofdiffFruits.iterator();
        while (it.hasNext()) {
            diffFruits = it.next();
            bw.write(diffFruits);
            bw.newLine();
        }
        bw.close();
    }

    public void countN() throws IOException {
        Collections.sort(listofFruits, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() < o2.length())
                    return -1;
                if (o1.length() > o2.length())
                    return 1;
                else
                    return 0;
            }
        });

        String fruits;
        int numofOperation = 0;
        int indexofFruit;
        int n = listofFruits.size();
        for (int i = 0; i < listofFruits.size(); i++) {
            fruits = listofFruits.get(i);
            for (int j = i + 1; j < listofFruits.size(); j++) {
                if (listofFruits.get(j).contains(fruits)) {
                    numofOperation++;
                    indexofFruit = listofFruits.indexOf(fruits);
                    fruits = listofFruits.get(j);
                    listofFruits.remove(indexofFruit);
                }

            }
        }
        BufferedWriter out = new BufferedWriter(new FileWriter("juice3.out"));
        out.write("Min: " + (n - numofOperation));
        out.close();
    }
}
