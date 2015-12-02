<?php
 session_start();

include 'mysql_connect.php';

	extract( $_POST );
	if(isset($_POST['InputName']) && isset($_POST['InputEmailFirst'])&& isset($_POST['Username'])&& isset($_POST['password'])&& isset($_POST['AwsAccessKey'])&& isset($_POST['AwsSecretKey']))
	{
		$inputname = $_POST['InputName'];
		$inputemailfirst = $_POST['InputEmailFirst'];
		$username = $_POST['Username'];
		$password = $_POST['password'];
		$awsaccesskey = $_POST['AwsAccessKey'];
		$awssecretkey = $_POST['AwsSecretKey'];
		
	//$query = "SELECT * FROM `users` WHERE `username` = '$uname' AND `password`='$pass'";
	$query = "INSERT INTO `users` (`name`, `email`, `username`, `password`, `awsAccessKey`,`awsSecretKey`) VALUES ('$inputname', '$inputemailfirst', '$username', '$password','$awsaccesskey','$awssecretkey')";
                      
	$query_run = mysql_query($query);
	//$query1 = mysql_query("INSERT INTO `instance` (`name`, `email`, `username`, `password`,`awsAccessKey`,`awsSecretKey`) VALUES ('$name', '$email', '$username', '$password','$awsaccesskey','$awssecretkey')");
    //$query_run1 = mysql_query($query1);
	
	}
	else
	{
	echo "Please Enter all fields";
	}
header('Location: index.php'); 
?>