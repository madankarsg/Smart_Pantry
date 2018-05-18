<?php 
$id=$_POST['id'];

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
    $qur = "SELECT * from hotel_items where id=$id";
    $flag=0;
    $status=0;
     if($res=mysqli_query($conn,$qur))
                       {
                         if(mysqli_num_rows($res) > 0)
                         {
                            while($row = mysqli_fetch_array($res))
                            {
                              $item = $row['item_name'];
                              $quantity= $row['capacity']-$row['remaining_quantity'];
                              if($row['remaining_quantity']<= $row['refill_at'])
                              {

                                $qur2= "SELECT * from orders where buyer_id='".$id."' and item_name='".$item."'";
                                if($res2=mysqli_query($conn,$qur2))
                                {
                                  if(mysqli_num_rows($res2) == 0)
                                    {
                                      $status=1;
                                       
                                    }

                                }

                                $qur3= "SELECT * from orders_history where buyer_id='".$id."' and item_name='".$item."' and delivered=0 ";
                                if($res3=mysqli_query($conn,$qur3))
                                {
                                  if(mysqli_num_rows($res3) == 0 || $status==1)
                                    {
                                      if($status == 1)
                                      {
                                          $status=0;
                                          $flag = 1;
                                          $obj=new stdClass();
                                          $obj->item_name=$row['item_name'];
                                          $obj->quantity_to_buy=$quantity;
                                          $obj_arr[$i]=($obj);
                                          $i=$i+1;    
                                      }
                                    }

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