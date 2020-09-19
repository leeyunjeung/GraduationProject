<?php 
$conn = mysqli_connect(
	'54.180.117.60',
	'gkdidms',
	'pw123',
	'sys',
	'3306');
if(mysqli_connect_errno())
{
	echo "FAILED TO CONNECT TO MYSQL:" , mysqli_connect_error();
}
$sql = "SELECT VERSION()";
$result = mysqli_query($conn,$sql);
$row = mysqli_fetch_array($result);
print_r($row["VERSION()"]);
?>


