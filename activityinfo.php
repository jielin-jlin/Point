<?php
include('connect_config.php');
$id=$_POST['id'];
$getid=mysqli_query($connect,"SELECT * FROM activity WHERE id='$id'");
while($row=mysqli_fetch_row($getid))
	{
		$allrow[] = $row;
	}
$getparticipant=mysqli_query($connect,"SELECT activity".$id." FROM activityparticipant WHERE 1");
while($row=mysqli_fetch_row($getparticipant))
	{
		$allrow[] = $row;
	}
	header('Content-Type: application/json');
	echo json_encode($allrow);
?>

	