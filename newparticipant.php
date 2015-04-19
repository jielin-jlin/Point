<?php
include('connect_config.php');
$email=$_POST['email'];
$id=$_POST['id'];
$findemail=mysqli_query($connect,"SELECT activity".$id." FROM activityparticipant WHERE activity".$id."='$email'");
$numrow = mysqli_fetch_array($findemail);
if($numrow==0)
{
	mysqli_query($connect,"INSERT INTO activityparticipant(activity".$id.")
	VALUE('$email')");
	echo "successful";
	
}
else
{
	echo "alreadypart";
}
?>