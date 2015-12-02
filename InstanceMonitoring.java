import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Properties;
import java.util.TreeMap;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;

public class InstanceMonitoring {

    public static void main(String[] args) {
        final String awsAccessKey = args[0]; 
        final String awsSecretKey = args[1]; 
        final String instanceId = args[2]; 
        final String instanceName = args[3];
        String metricName[] = {"CPUUtilization","NetworkIn","NetworkOut","DiskReadOps","DiskWriteOps","MemoryUtilization"};
        final String nameSpace[] = {"AWS/EC2","AWS/EC2","AWS/EC2","AWS/EC2","AWS/EC2","System/Linux"};
        int i;
        LocalDateTime prevMailSentTime =null;
        
        final AmazonCloudWatchClient client = client(awsAccessKey, awsSecretKey);
        System.out.println("connection established");
       
        while(true){
         StringBuilder sb = new StringBuilder();
        
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
            Date date = new Date();
            sb.append(instanceName+";"+dateFormat.format(date)+";");
            for(i = 0;i < metricName.length;i++){
             final GetMetricStatisticsRequest request = request(instanceId,nameSpace[i],metricName[i]); 
                final GetMetricStatisticsResult result = result(client, request);
                String value = displayStats(result, instanceId);
               Double doublevalue= Double.parseDouble(value);
          sb.append(value);
                if (!(i == metricName.length-1)){
                 sb.append(";");
                }
                
             // cpu 80 => i=0
                // mmy 90 => i=5
                
                if (i==0 && (doublevalue> 85)){
                	// mail will be sent only after 30 mins since previous mail
                	if (prevMailSentTime ==null ||(ChronoUnit.MINUTES.between(prevMailSentTime,LocalDateTime.now())>30)){
						prevMailSentTime=LocalDateTime.now();
						InstanceMonitoring.sendAlertMail(instanceId);
                	}
                }
                
                if (i==5 && (doublevalue>90)){
                	if (prevMailSentTime ==null ||(ChronoUnit.MINUTES.between(prevMailSentTime,LocalDateTime.now())>30)){
						prevMailSentTime=LocalDateTime.now();
						InstanceMonitoring.sendAlertMail(instanceId);
                	}
                }
            }
            appendDatatoLog(sb.toString(),instanceName);
            
            
        }       
    }

    private static void sendAlertMail(String instanceId){

		   Properties props = new Properties();
		   props.put("mail.smtp.host", "smtp.mail.yahoo.com");
		    props.put("mail.smtp.port", "587");
		   props.put("mail.stmp.user", "cmpe273@yahoo.com");
		   //To use TLS
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.password", "273project");
		   Session session = Session.getDefaultInstance(props, new Authenticator() {
		 @Override
		   protected PasswordAuthentication getPasswordAuthentication() {
		       return new PasswordAuthentication("cmpe273@yahoo.com", "273project");
		   }
		});
		  String to = "bhumik.gandhi05@gmail.com";
		  String from = "cmpe273@yahoo.com";
		  String subject = "Alert your Instance" + instanceId +" needs to be checked" ;
		  MimeMessage msg = new MimeMessage(session);
		  try {
		   msg.setFrom(new InternetAddress(from));
		   msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
		   msg.setSubject(subject);
		   msg.setText("Your Instance is under stress");
		   Transport transport = session.getTransport("smtp");
		   System.out.println("sending mail");
		   transport.send(msg);
		 }  catch(Exception exc) {
		      System.out.println(exc);
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
    // List<Datapoint> dplist = result.getDatapoints();
     //System.out.println("dp.size" + dp.size());
        //System.out.println(dp.get(0).getMaximum());
     TreeMap<Date, Double> map = new TreeMap<Date, Double>(); 
     		
     	for (Datapoint dp : result.getDatapoints()) {
     	    map.put(dp.getTimestamp(), dp.getMaximum());   
     	}
     	//System.out.println("last key"+ map.lastKey() + "map.get(map.lastKey())" +map.get(map.lastKey()));    
     	return numberFormat.format(map.get(map.lastKey()));
        
    }
    
    private static void appendDatatoLog(String data, String instanceName){
     try{
    	 //String s="D:/test.txt";
    	 String s= "/home/ubuntu/test.log";
      File file =new File(s);
      
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
