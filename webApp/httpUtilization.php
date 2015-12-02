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

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/simple-sidebar.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div id="wrapper">

        <!-- Sidebar -->
        <div id="sidebar-wrapper">
            <ul class="sidebar-nav">
                <li class="sidebar-brand">
                    <a href="dashboard.php">
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
						<h2> HTTP requests monitoring</h2>

						<?php
						$iFrameUrl = "http://52.24.4.178:5601/#/dashboard/HTTP-Req-username?embed&_g=(time:(from:now-1h,mode:quick,to:now))&_a=(filters:!(),panels:!((col:3,id:instance1-HTTP-Request,row:1,size_x:7,size_y:3,type:visualization),(col:3,id:instance2-HTTP-Request,row:4,size_x:7,size_y:3,type:visualization)),query:(query_string:(analyze_wildcard:!t,query:'*')),title:'HTTP%20Req%20-%20username')";
						$replaceName = array("instance1","instance2");
						$instanceName = $_SESSION["instance"];
						//echo "ins1".$_SESSION["instance"][0];
						//echo "ins2".$_SESSION["instance"][1];
						//print("<br />");
						$iFrame1 = str_replace($replaceName, $instanceName, $iFrameUrl);
						//echo "".$iFrame1;
						//print("<br />");
						$userName = $_SESSION["username"];
						$iFrame = str_replace("username",$userName,$iFrame1);
						//echo "".$iFrame;
						//print("$iFrame");
						
						print("<div>
						<iframe src=$iFrame height='1000' width='1000' frameborder='0'></iframe>
						</div>");
						
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
