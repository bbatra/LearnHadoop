import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

class Result {
    public static void main(String[] args)
        throws IOException
    {
        final String curDir = Paths.get(".").toAbsolutePath().normalize().toString();
        final String resultPath = curDir + "/answer";

        File file = new File(resultPath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        // Get the entries from the file
        ArrayList<Entry> entries = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null)
        {
            String[] chars = line.split("");

            String key = "";
            String value = "";
            boolean hasParsedKey = false;
            for (String c : chars)
            {
                if (!Objects.equals(c, " ") && !Objects.equals(c, "\t"))
                {
                    if (!hasParsedKey)
                    {
                        key += c;
                    }
                    else
                    {
                        value += c;
                    }
                }
                else
                {
                    hasParsedKey = true;
                }
            }
            entries.add(new Entry(key, Integer.parseInt(value)));
        }

        Collections.sort(entries);

        int totalNumBigrams = 0;
        for (Entry e : entries)
        {
            totalNumBigrams += e.value;
        }

        int tenPercent = (int) Math.ceil(totalNumBigrams * 0.1);

        // Print the total number of bigrams
        System.out.println("Total number of bigrams: " + totalNumBigrams);

        // Print the most common bigram
        Entry mostCommon = entries.size() > 0 ? entries.get(0) : null;
        String mcOutput = mostCommon != null ? mostCommon.toString() : "No bigrams";
        System.out.println("Most common bigram: " + mcOutput);

        int numberToTen = 0;
        int bigramsCounted = 0;
        for (Entry e : entries)
        {
            if (bigramsCounted >= tenPercent)
                break;
            bigramsCounted += e.value;
            numberToTen += 1;
        }

        // Print the number of bigrams to get to 10% of the bigrams
        System.out.println("Number of bigrams to get to 10%: " + numberToTen);
    }

    static class Entry implements Comparable
    {
        public String key;
        public int value;

        Entry(String key, int value)
        {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "" + key + ": " + value;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof Entry))
                throw new ClassCastException();
            Entry other = (Entry) o;
            return other.value - this.value;
        }
    }
}
