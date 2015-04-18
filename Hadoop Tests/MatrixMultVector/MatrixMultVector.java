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

	

public class MatrixMultVector {

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, DoubleWritable>, JobConfigurable{

		private static double[] inputVector;
		
		// Get vector.		
		public void configure(JobConf job) {
			String inputString = job.get("inputVector");
			
			Scanner input = new Scanner(job.get("inputVector"));
			int numberOfElements = Integer.parseInt(job.get("inputVectorSize"));
			
			inputVector = new double[numberOfElements];
			int i = 0;
			while(input.hasNextDouble()){
				inputVector[i] = input.nextDouble();
				i = i+1;
			}
		}		
		

		// Key is the row of the multiplication. Value is result of the multiplication.
		public void map(LongWritable key, Text value, OutputCollector<LongWritable, DoubleWritable> output, Reporter reporter) throws IOException {

			// Get input line.
			Scanner input = new Scanner(value.toString()); 

			// Get element value.
			double element = input.nextDouble();
					
			// Get row.
			input.next();
			int row = input.nextInt();			
			
			// Get column.
			int column = input.nextInt();
			
			// Multiply with the relevant vector element
			double result = element*inputVector[column-1];
			
			// Output ket/value pair
			output.collect(new LongWritable(row),new DoubleWritable(result));
		}
	} 
        
        // Key is row of product, value is element at that row
	public static class Reduce extends MapReduceBase implements Reducer<LongWritable, DoubleWritable, LongWritable, DoubleWritable>, JobConfigurable {

		public void reduce(LongWritable key, Iterator<DoubleWritable> values, OutputCollector<LongWritable, DoubleWritable> output, Reporter reporter) throws IOException {
			double sum; // sum of all side products
			
			sum = 0;
			while(values.hasNext()){
				sum = sum + Double.parseDouble(values.next().toString());
			}
			
			output.collect(key, new DoubleWritable(sum));
		}
	}   
    
	public static void main(String[] args) throws Exception {
	   JobConf conf = new JobConf(MatrixMultVector.class);
	   conf.setJobName("MatrixMultVector");
	       
	   conf.setOutputKeyClass(LongWritable.class);
	   conf.setOutputValueClass(DoubleWritable.class);
	      
	   conf.setMapperClass(Map.class);
	   // conf.setCombinerClass(Reduce.class);
	   conf.setReducerClass(Reduce.class);
	       
	   conf.setInputFormat(TextInputFormat.class);
	   conf.setOutputFormat(TextOutputFormat.class);
	       
	   FileInputFormat.setInputPaths(conf, new Path(args[0]));
	   FileOutputFormat.setOutputPath(conf, new Path(args[1]));
	   
	   // Get input.
	   Scanner input = new Scanner(System.in);
	   
	   // Get number of elements
	   int numberOfElements = input.nextInt();

	   // Construct the multiplying vector.	   
	   double[] vector = new double[numberOfElements];
	   
	   // Get the number of elements.
	   for(int i=0;i<numberOfElements;i++){
		vector[i] = input.nextDouble();
	   }
	   
	   // Pass vector to the mappers.
	   String inputVector = Arrays.toString(vector);
	   
	   // Trim passed String
	   inputVector = inputVector.replace('[',' ');
	   inputVector = inputVector.replace(']',' ');	  
	   inputVector = inputVector.replace(',',' ');	   

	   conf.set("inputVectorSize", (new Integer(numberOfElements)).toString());
	   conf.set("inputVector", inputVector);
	   
	   JobClient.runJob(conf);
	}

}
