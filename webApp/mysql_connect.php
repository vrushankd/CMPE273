<?php
error_reporting(E_ALL ^ E_DEPRECATED);
$db_host = "localhost";
$db_username = "root";
$db_pass = "cmpe273";
$db_name = "cmpe273";

				//Connect to MySQL
				if(!( $database = mysql_connect($db_host,$db_username,$db_pass)))
					die("Could not connect to database!");
					
				//open Products database
				if(!mysql_select_db($db_name, $database))
					die("Could not open b16_13795936_moovees database!");
				
//@mysql_connect("$db_host", "$db_username", "$db_pass") or die ("Could not connect to MySQL");
//@mysql_select_db("$db_name") or die ("No database");

?>