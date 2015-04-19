<?php
include('connect_config.php');

$first_name=$_POST['first_name'];
$last_name=$_POST['last_name'];
$email=$_POST['email'];
$passphrase=$_POST['passphrase'];
$zipcode=$_POST['zipcode'];
$sec_question1=$_POST['petname'];
$sec_question2=$_POST['schoolname'];
$blob=$_POST['blob'];

$findemail="SELECT email FROM users WHERE email='$email'";
$checkemail=mysqli_query($connect,$findemail);
$checkresult=mysqli_fetch_array($checkemail);
if($checkresult==0)
{
	$query="INSERT INTO users(first_name,last_name,email,passphrase,zipcode,sec_question1,sec_question2,icon)
	VALUE('$first_name','$last_name','$email','$passphrase','$zipcode','$sec_question1','$sec_question2','$blob')";
	$result=mysqli_query($connect,$query);
	if($result)
	{
		echo "successful";
	}
	else
	{
		echo "failed";
	}
}
else
{
	echo "email_in_use";
}
mysqli_close($connect);

?>