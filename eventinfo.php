<?php
include('connect_config.php');
$id=$_POST['id'];
$getid=mysqli_query($connect,"SELECT * FROM event WHERE id='$id'");
$row=mysqli_fetch_row($getid);
header('Content-Type: application/json');
echo json_encode($row);
?>

	