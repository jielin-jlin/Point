<?php
include('connect_config.php');
$first_name=$_POST['first_name'];
$last_name=$_POST['last_name'];
$email=$_POST['email'];
$title=$_POST['title'];
$activity=$_POST['activity'];
$date=$_POST['date'];
$starttime=$_POST['starttime'];
$endtime=$_POST['endtime'];
$location=$_POST['location'];
$zipcode=$_POST['zipcode'];
$query="INSERT INTO activity(first_name,last_name,email,title,activity,date,time_start,time_end,location,zipcode)
VALUE('$first_name','$last_name','$email','$title','$activity','$date','$starttime','$endtime','$location','$zipcode')";
$result=mysqli_query($connect,$query);
	if($result)
	{
		echo "successful";
		$id = mysqli_insert_id($connect);
		mysqli_query($connect,"ALTER TABLE activityparticipant ADD activity".$id." VARCHAR(30)");
		mysqli_query($connect,"INSERT INTO activityparticipant(activity".$id.")
		VALUE('$email')");
	}
	else
	{
		echo "failed";
	}
?>