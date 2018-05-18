<?php


    $servername = "localhost";
    $username = "root";
    $password = "";
    $dbname = "smart_pantry";
    
    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) 
    {
        die("Connection failed: " . $conn->connect_error);
    }

$id=$_POST['id'];
$location="null";

$obj_arr=array();
$i=0;


$sql = "SELECT location From hotels where id='$id'";
$result = $conn->query($sql);


if ($result->num_rows > 0)
 {
    // output data of each row
    while($row = $result->fetch_assoc()) 
    { 
       $location=$row["location"];
    }
}
else
{
  echo "error";
}

$sql = "SELECT id,name,address From sellers where location='$location'";
$result = $conn->query($sql);


if ($result->num_rows > 0)
 {
    // output data of each row
    while($row = $result->fetch_assoc()) 
    {	
      	$obj=new stdClass();  			
        $obj->name=$row['name'];
        $obj->address = $row['address'];
        $obj->id = $row['id'];
  			$obj_arr[$i]=($obj);
  			$i=$i+1;
    }
}
else
{
  echo "error";
}

echo json_encode($obj_arr);

?>