import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class BigramCountSort {

//    /**
//     * This comparator is used to sort the output of the dictionary in the
//     * descending order of the counts. The descending order enables to pick a
//     * dictionary of the required size by any aplication using the dictionary.
//     *
//     * @author UP
//     *
//     */
//    public static class SortKeyComparator extends WritableComparator {
//
//        protected SortKeyComparator() {
//            super(LongWritable.class, true);
//        }
//
//        /**
//         * Compares in the descending order of the keys.
//         */
//        @Override
//        public int compare(WritableComparable a, WritableComparable b) {
//            LongWritable o1 = (LongWritable) a;
//            LongWritable o2 = (LongWritable) b;
//            if(o1.get() < o2.get()) {
//                return 1;
//            }else if(o1.get() > o2.get()) {
//                return -1;
//            }else {
//                return 0;
//            }
//        }
//
//    }

    public static class TokenizerMapper
            extends Mapper<Object, Text, IntWritable, Text>{

        private final static IntWritable one = new IntWritable(1);
        private Text word1 = new Text();
        private Text word2 = new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());

            if(itr.countTokens() >= 1) {
                word1.set(itr.nextToken());
            }

            while (itr.hasMoreTokens()) {
                word2.set(itr.nextToken());
                if(word2.equals(word1))
                    context.write(one, word1);
                word1.set(word2);
            }
        }
    }

    public static class IntSumReducer
            extends Reducer<IntWritable, Text, IntWritable, Text> {
        private IntWritable result = new IntWritable();

        public void reduce(Iterable<IntWritable> values, Text key,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(result, key);
//            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Bigram count sorted");
        job.setJarByClass(BigramCountSort.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}