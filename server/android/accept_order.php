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

    $qur = "SELECT * from orders where seller_id = '".$seller_id."' and buyer_id='".$hotel_id."' and item_name='".$item_name."'";

$flag=0;
 if($res=mysqli_query($conn,$qur))
                   {
                     if(mysqli_num_rows($res) > 0)
                     {
                        while($row = mysqli_fetch_array($res))
                        {
                          $quantity = $row['quantity'];
                          $date = $row['date'];
                          $qur = "delete from orders where seller_id = '".$seller_id."' and buyer_id='".$hotel_id."' and item_name='".$item_name."'";

                          if($result=$conn->query($qur))
                          {
                            $qur = "INSERT into orders_history value('$hotel_id','$seller_id','$item_name','$quantity',0,'$date')";
                            if($result=$conn->query($qur))
                            { $flag=1;
                              echo "YES";
                            } 
                            else{$flag=1;
                              echo "NO";
                            }


                          }
                          else
                          {$flag=1;
                            echo "NO";
                          }

                        }
                      }
                    }

  if($flag == 0)
    echo "NO";
?>