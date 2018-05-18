<?php 
$id=$_POST['id'];
$item_name = $_POST['item_name'];
$qty_to_buy = $_POST['qty_to_buy'];
$seller_id = $_POST['seller_id'];
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


    $obj_arr=array();
    $pos=0;
    $i=0;
    $flag=0;  
    $qur = "INSERT into orders values('$id','$seller_id','$item_name','$qty_to_buy',NULL)";
    if ($conn->query($qur) == TRUE) {
        $flag =1;
    }
   if($flag==1)
      echo "YES";
    else
      echo "NO";
   
?>