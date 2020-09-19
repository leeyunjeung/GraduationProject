<?php
error_reporting(E_ALL); 
ini_set('display_errors',1); 
include('dbcon.php');

$url  = 'http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?ServiceKey=tAYKbEZoElKC6FZC2J0g67Zg6GD5505OL7EtVUedRPrlEQeRe%2BOFWR77a5erXsoiElwMZsQjREQe5EC9SiZWEw%3D%3D';

$data = file_get_contents($url);
$result_data = simplexml_load_string($data);

print_r($result_data);

$result = $result_data->body->items;

//echo $result->item[0]->filename;
?>