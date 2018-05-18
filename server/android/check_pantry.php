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


    $id = $_POST['id'];
    $obj_arr=array();
    $i=0;
    

     $i=0;
     $qur = "SELECT * from hotel_items";

     $flag=0;
     if($res=mysqli_query($conn,$qur))
                       {
                         if(mysqli_num_rows($res) > 0)
                         {
                            while($row = mysqli_fetch_array($res))
                            {
                              if($row['id'] == $id)
                              {
                                $flag = 1;
                                $obj=new stdClass();
                                $obj->item_name=$row['item_name'];
                                $obj->remaining_qty=$row['remaining_quantity'];
                                $obj->capacity=$row['capacity'];
                                $obj->refill_at = $row['refill_at'];
                                $obj_arr[$i]=($obj);
                                $i=$i+1;
                              
                              }

                            }
                          }
                        }


if($flag==1)
      echo json_encode($obj_arr);
    else
      echo "null";

?>