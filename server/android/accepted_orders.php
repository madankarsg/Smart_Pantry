<?php 
$seller_id=$_POST['id'];

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

    $qur = "SELECT * from orders_history where seller_id = '".$seller_id."' and delivered=0";

$flag=0;
$i=0;
 if($res=mysqli_query($conn,$qur))
                   {

                     if(mysqli_num_rows($res) > 0)
                     {
                        while($row = mysqli_fetch_array($res))
                        {
                          $item_name = $row['item_name'];
                          $quantity = $row['quantity'];
                          $date = $row['date'];
                          $buyer_id = $row['buyer_id'];

                          $qur2 = "SELECT * from hotels where id = $buyer_id";
                          if($res2=mysqli_query($conn,$qur2))
                                {
                                  while($row2 = mysqli_fetch_array($res2))
                                  {
                                          $flag = 1;
                                          $obj=new stdClass();
                                          $obj->item_name=$item_name;
                                          $obj->quantity=$quantity;
                                          $obj->hotel_name = $row2['name'];
                                          $obj->address = $row2['address'];
                                          $obj->contact = $row2['id'];
                                          $obj->date = $date;
                                          $obj_arr[$i]=($obj);
                                          $i=$i+1;


                                  }
                                }
                        }
                      }
                    }

if($flag==1)
      echo json_encode($obj_arr);
    else
      echo "null";
?>