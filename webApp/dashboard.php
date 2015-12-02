<?php
	session_start();
?>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Simple Sidebar - Start Bootstrap Template</title>
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>   
    <link href="css/bootstrap.min.css" rel="stylesheet">  
    <link href="css/simple-sidebar.css" rel="stylesheet">
</head>

<body>

    <div id="wrapper">

        <!-- Sidebar -->
        <div id="sidebar-wrapper">
            <ul class="sidebar-nav">
                <li class="sidebar-brand">
                    <a href="#">
                        AWS Instance Monitoring
                    </a>
                </li>
                <li>
                    <a href="cpuUtilization.php">CPU Utilization</a>
                </li>
                <li>
                    <a href="memoryUtilization.php">Memory Utilization</a>
                </li>
                <li>
                    <a href="networkUtilization.php">Network I/O Utilization</a>
                </li>
				<li>
                    <a href="diskUtilization.php">Disk I/O Utilization</a>
                </li>
				<li>
                    <a href="httpUtilization.php">HTTP Utilization</a>
                </li>
				<li>
                    <a href="Logout.php">Log Out</a>
                </li>
            </ul>
        </div>
        <!-- /#sidebar-wrapper -->

        <!-- Page Content -->
        <div id="page-content-wrapper">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12">
                        <?php 

						include 'mysql_connect.php';

						extract( $_POST );

							if(isset($_POST['name']) && isset($_POST['password']))
								{
										$_SESSION["uname"]= $_POST['name'];
							            $uname = $_SESSION["uname"];
							            $_SESSION["pass"]= $_POST['password'];
							            $pass = $_SESSION["pass"];
									
							
										$query = "SELECT * FROM `users` WHERE `username` = '$uname' AND `password`='$pass'";
										$query_run = mysql_query($query);
						
										if ( !$query_run) 
										{
												
												echo '<h1> Invalid Username or password</h1><br><h3><a href="index.php">Click here to Log in again</a></h3>';
												
												print("<p>Result</p>$result");
												die(mysql_error());
													
										}
										else 
										{
												$_SESSION["username"] = $_POST["name"];
							                    $query2 = "SELECT awsinstanceid,instancenamesystem FROM instance WHERE `username` = '$uname'";
							                    $query_run2 = mysql_query($query2);
												echo '<h1> Welcome ' . $_SESSION["username"] . '</h1><br />';
												print("<h3>You have following instances registered.</h3><br /><br />");
												$instance = array();
												$instanceId = array();
												
												print("<table class ='table table-hover'cellspacing='1' border='1'>");
												print("<tr><th>Instance ID</th><th>Instance Name</th></tr>");
											
											for ( $counter = 0; $row = mysql_fetch_row( $query_run2 ); $counter++ )
												{
													print("<tr>");	
													foreach( $row as $key => $value )
													{
														
														
														print("<td width='25'>$value</td>");
														$temp = $value;
														
													}
													$instance[$counter] = $temp;
													print("</tr>");
												}
										
									
													
													$_SESSION["instance"] = $instance;
													//echo "ins1 - ".$_SESSION["instance"][0];
													//echo "ins2 - ".$_SESSION["instance"][1];

										}
								}
						
						?>
						
                    </div>
                </div>
            </div>
        </div>
        <!-- /#page-content-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Menu Toggle Script -->
    <script>
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
    </script>

</body>

</html>