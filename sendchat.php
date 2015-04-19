<?php
include('connect_config.php');
$id = $_POST['id'];
$input=$_POST['input'];
$file = "EventChats/".$id.".txt";
$whandle=fopen($file,'a');
fwrite($whandle,$input."\n");
fclose($whandle);
$rhandle=fopen($file,'r');
while (($line = fgets($rhandle)) !== false) 
	{
        echo $line;
    }
fclose($rhandle);
?>