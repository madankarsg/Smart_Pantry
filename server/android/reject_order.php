<?php 
$seller_id=$_POST['seller_id'];
$hotel_id = $_POST['hotel_id'];
$item_name=$_POST['item_name'];

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
    $pos=0;

    $qur = "delete from orders where seller_id = '".$seller_id."' and buyer_id='".$hotel_id."' and item_name='".$item_name."'";

    if($result=$conn->query($qur))
    {
      $qur = "update hotel_items set date=NULL where id = '".$hotel_id."' and item_name='".$item_name."'";
    if($result=$conn->query($qur))
        {echo "YES";}
    }
    else
      echo "NO";
                          

?>