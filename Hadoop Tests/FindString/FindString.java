import java.io.*;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.*;
import java.lang.Object;

	

public class FindString {

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, Text>, JobConfigurable {

		private static String FindStringInput;
		
		// Get FindString parameter.		
		public void configure(JobConf job) {
			FindStringInput = job.get("FindStringInput");
		}		
		

		// Key is the input file byte offset. Value is the output of FindString.
		public void map(LongWritable key, Text value, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
			
			// Get input line.
			String line = value.toString(); 
			
			// Get FindString result.
			String result;
			if (line.indexOf(FindStringInput)>=0) {
			
				// Output ket,value pair
				result = line;
				Text word = new Text(result);
				output.collect(key,word);
			}
		}
	} 
         
	public static class Reduce extends MapReduceBase implements Reducer<LongWritable, Text, LongWritable, Text>, JobConfigurable {

		public void reduce(LongWritable key, Iterator<Text> values, OutputCollector<LongWritable, Text> output, Reporter reporter) throws IOException {
			
			if(values.hasNext()){
				output.collect(key, values.next());
			}
		}
	}   
    
	public static void main(String[] args) throws Exception {

	   // Standard procedures to set up hadoop:
	   JobConf conf = new JobConf(FindString.class);
	   conf.setJobName("FindString");
	       
	   conf.setOutputKeyClass(LongWritable.class);
	   conf.setOutputValueClass(Text.class);
	      
	   conf.setMapperClass(Map.class);
	   conf.setReducerClass(Reduce.class);
	       
	   conf.setInputFormat(TextInputFormat.class);
	   conf.setOutputFormat(TextOutputFormat.class);
	       
	   FileInputFormat.setInputPaths(conf, new Path(args[0]));
	   FileOutputFormat.setOutputPath(conf, new Path(args[1]));
	   
	   // Set the FindString parameters property:
	   conf.set("FindStringInput", args[2]);
	   
	   JobClient.runJob(conf);
	}
       
}
