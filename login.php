<?php
include('connect_config.php');
$email = $_POST['email'];
$passphrase = $_POST['passphrase'];

$findemail=mysqli_query($connect,"SELECT email, passphrase FROM users WHERE email='$email'");
$findemailrow = mysqli_fetch_array($findemail);
if($findemailrow==0)
{
	echo "wrong_username";
	
}
else
{
	$matchpass=mysqli_query($connect,"SELECT email, passphrase FROM users WHERE (email='$email' AND passphrase='$passphrase')");
	$matchpassrow = mysqli_fetch_array($matchpass);
	if($matchpassrow==0)
	{
		echo "wrong_passphrase";
		
	}
	else
	{
		mysqli_query($connect,"UPDATE users SET login=1 WHERE email ='$email'");
		$getinfo = mysqli_query($connect,"SELECT first_name, last_name, zipcode FROM users WHERE email='$email'");
		$inforow = mysqli_fetch_array($getinfo,MYSQLI_ASSOC);
		foreach($inforow as $value)
		{
			echo "$value ";
		}
	}
}	
?>

