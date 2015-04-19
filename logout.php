<?php
include('connect_config.php');
$email = $_POST['email'];
mysqli_query($connect,"UPDATE users SET login=0 WHERE email ='$email'");
?>