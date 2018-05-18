<?php 
$seller_id=$_POST['id'];
$buyer_id = $_POST['hotel_id'];
$item_name = $_POST['item_name'];


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

    $qur = "update orders_history set delivered=1 where seller_id = '".$seller_id."' and buyer_id = '".$buyer_id."' and item_name = '".$item_name."' and delivered = 0";
    if($conn->query($qur))
    {
      echo "YES";
    }
    else
    {
      echo "NO";
    }

?>