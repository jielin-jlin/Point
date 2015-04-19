<?php

$host="mysql1.000webhost.com";//hostname
$username="a7295748_admin";//username
$password="managea7295748";//database password
$database_name="a7295748_point";//database name

//connect to database
$connect = new mysqli("$host","$username","$password","$database_name");
if($connect->connect_errno)
{
echo "failed";
}

?>