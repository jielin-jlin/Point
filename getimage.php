<?php
include('connect_config.php');
$email=$_POST['email'];
$getimage=mysqli_query($connect,"SELECT icon FROM users WHERE email='$email'");
$row = mysqli_fetch_array($getimage,MYSQLI_ASSOC);
foreach($row as $value)
		{
			echo $value ;
		}
?>