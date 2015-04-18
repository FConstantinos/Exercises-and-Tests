import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.*;
import java.lang.Object;

	//In this program, we use the Hadoop framework to find the common friends of a two member friend relationship. The input is a file where each line is of the form A -> B C D ..., where B C D ... are friend of A. The output is of the form (A, B) -> C E W ... where C E W are common friends of A and B. 

//The mappers collect all possible common friends of this relationship. By possible common friends we denote the set of friends of the first member and those of the second member. The common friends of the relationship is the intersection of those two sets of possible common friends. The reducers apply the intersection operation to the two sets of possible common friends.

public class FacebookFriends {
 
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
		private Text word = new Text();		// Key to pass to the reducers.
		private String userID;  		// Identifier of the user whose friend status is described in each line of the input file.
		private String FriendID; 		// Identifier of a user who is a friend of userID.
		private String Friends;	     		// All friends of userID.
		private String PossibleCommonFriends; 	// All friends of userID, other than friendID. Value to pass to the reducers.
		
		// Key is the relationship (sorted), value is the set of friends that belong to one part of the relationship.
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			String line = value.toString(); 
			StringTokenizer tokenizer = new StringTokenizer(line);
			Text t;

			// Get the user id described by the input line.
			userID = new String(tokenizer.nextToken());
			tokenizer.nextToken();

			// Get all of the user's friends.
			Friends = line.substring(line.indexOf('>')+1);


			while (tokenizer.hasMoreTokens()) {
			   	FriendID = tokenizer.nextToken();

				// Sort the key relationship.
				if (userID.compareTo(FriendID)>0){
					word = new Text("(" + FriendID + ", " + userID + ")");
				}
				else{
					word = new Text("(" + userID + ", " + FriendID + ")");
				}

				// Get all friends of userID other than friendID.  
			   	PossibleCommonFriends = Friends.replaceAll("\\s" + FriendID + "\\s"," ");

				// Collect key,value output.
				output.collect(word,new Text(PossibleCommonFriends));
				
			}
		}
	} 
         
	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		        int setIterator;
			
			StringTokenizer tokenizer;
			String Friends;
			HashSet<String> firstSet = new HashSet<String>();
			HashSet<String> commonSet = new HashSet<String>();

			// Get the first available list of friends.
			Friends = values.next().toString();

			// If there is no other list of friends, the relationship is not reciprocal and the reducer should exit. 
			// Otherwise, the reducer continues.
			if ( !values.hasNext() ){
				return; 
			}

			// Get all friends of the list to the firstSet.
			tokenizer = new StringTokenizer(Friends);
			while (tokenizer.hasMoreTokens()){
				firstSet.add(tokenizer.nextToken());
			}

			// Get the second available list of friends.
			Friends = values.next().toString();

			// Get all friends of the list to the commonSet.
			tokenizer = new StringTokenizer(Friends);
			while (tokenizer.hasMoreTokens()){
				commonSet.add(tokenizer.nextToken());
			}

			// Set commonSet to be the intersection of the two sets.
			commonSet.retainAll(firstSet);

			// Collect key,value output.
			output.collect(key, new Text(commonSet.toString()));
		}
	}   
    
	public static void main(String[] args) throws Exception {
	   JobConf conf = new JobConf(FacebookFriends.class);
	   conf.setJobName("FacebookFriends");
	       
	   conf.setOutputKeyClass(Text.class);
	   conf.setOutputValueClass(Text.class);
	      
	   conf.setMapperClass(Map.class);
	   //conf.setCombinerClass(Reduce.class);
	   conf.setReducerClass(Reduce.class);
	       
	   conf.setInputFormat(TextInputFormat.class);
	   conf.setOutputFormat(TextOutputFormat.class);
	       
	   FileInputFormat.setInputPaths(conf, new Path(args[0]));
	   FileOutputFormat.setOutputPath(conf, new Path(args[1]));
	       
	   JobClient.runJob(conf);
	}
       
}
