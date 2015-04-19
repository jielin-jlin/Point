<?php
include('connect_config.php');
$id = $_POST['id'];
$file = "EventChats/".$id.".txt";
$rhandle=fopen($file,'r');
while (($line = fgets($rhandle)) !== false) 
	{
        echo $line;
    }
fclose($rhandle);
?>