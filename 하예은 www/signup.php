<?php

$con=mysqli_connect("127.0.0.1","root","1234","android_test");



mysqli_set_charset($con,"utf8");



if (mysqli_connect_errno($con))

{

   echo "Failed to connect to MySQL: " . mysqli_connect_error();

}

$userid = $_POST['Id'];

$userpw = $_POST['Pw'];



$result = mysqli_query($con,"insert into custom_info (u_id,u_pw) values ('$userid','$userpw')");



  if($result){

    echo 'success';

  }

  else{

    echo 'failure';

  }



mysqli_close($con);

?>

