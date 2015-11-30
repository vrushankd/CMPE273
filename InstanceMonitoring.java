import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;

public class InstanceMonitoring {

    public static void main(String[] args) {
        final String awsAccessKey = "AKIAIYQ4XOKXEOBJ4EMQ"; //args[0]; //"AKIAI7AB7SJG3PRAY62A"; //"AKIAIYQ4XOKXEOBJ4EMQ";
        final String awsSecretKey = "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"; //"WsKC5ROW0iBqnvmj18U1UWMdaccy2NUF/R0RWu/P"; //"j8qgn42G921P7eS/MtlpjFm3mJ1uiN04DR06Hl9L";
        final String instanceId = "i-766e0ab2";
        String metricName[] = {"CPUUtilization","NetworkIn","NetworkOut","DiskReadOps","DiskWriteOps","MemoryUtilization"};
        final String nameSpace[] = {"AWS/EC2","AWS/EC2","AWS/EC2","AWS/EC2","AWS/EC2","System/Linux"};
        int i;
        
        final AmazonCloudWatchClient client = client(awsAccessKey, awsSecretKey);
        System.out.println("connection established");
       
        while(true){
        	StringBuilder sb = new StringBuilder();
        	final String instanceName = "VM01";
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
            Date date = new Date();
            sb.append(instanceName+";"+dateFormat.format(date)+";");
            for(i = 0;i < metricName.length;i++){
            	final GetMetricStatisticsRequest request = request(instanceId,nameSpace[i],metricName[i]); 
                final GetMetricStatisticsResult result = result(client, request);
                String value = displayStats(result, instanceId);
             		
        		sb.append(value);
                if (i == metricName.length-1){
                }
                else{
                	sb.append(";");
                }
            }
            appendDatatoLog(sb.toString());
        }       
    }

    private static AmazonCloudWatchClient client(final String awsAccessKey, final String awsSecretKey) {
        final AmazonCloudWatchClient client = new AmazonCloudWatchClient(new BasicAWSCredentials(awsAccessKey, awsSecretKey));
        client.setEndpoint("https://monitoring.us-west-2.amazonaws.com");
        return client;
    }

    private static GetMetricStatisticsRequest request(final String instanceId, String nameSpace, String metriName) {
        final long twentyFourHrs = 1000 * 60 * 60 * 24;
        final int oneHour = 60 * 60;
        return new GetMetricStatisticsRequest()
            .withStartTime(new Date(new Date().getTime()- twentyFourHrs))
            .withNamespace(nameSpace)
            .withPeriod(oneHour)
            .withDimensions(new Dimension().withName("InstanceId").withValue(instanceId))
            .withMetricName(metriName)
            .withStatistics("Maximum")
            .withEndTime(new Date());
    }

    private static GetMetricStatisticsResult result(
            final AmazonCloudWatchClient client, final GetMetricStatisticsRequest request) {
         return client.getMetricStatistics(request);
    }

    private static String displayStats(final GetMetricStatisticsResult result, final String instanceId) {
    	DecimalFormat numberFormat = new DecimalFormat("#.00");
    	List<Datapoint> dp = result.getDatapoints();
        //System.out.println(dp.get(0).getMaximum());
        return numberFormat.format(dp.get(0).getMaximum());
    }
    
    private static void appendDatatoLog(String data){
    	try{    		
    		File file =new File("F:\\Sem3\\273\\test.txt");
    		
    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
    		
    		//true = append file
    		FileWriter fileWritter = new FileWriter(file,true);
    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    	        bufferWritter.write(data);
    	        bufferWritter.newLine();
    	        bufferWritter.close();
    	    
	        System.out.println("Done");
	        
    	}catch(IOException e){
    		e.printStackTrace();
    	}
 
    }
}