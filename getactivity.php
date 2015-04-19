<?php
include('connect_config.php');
$query="SELECT * FROM activity WHERE 1";
$gettable=mysqli_query($connect,$query);
$array=mysqli_num_rows($gettable);
if($array==0)
{
	echo "nothing";
}
else
{
	while($row=mysqli_fetch_row($gettable))
	{
		$allrow[] = $row;
	}
	header('Content-Type: application/json');
	echo json_encode($allrow);
}
?>