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
    $item_name = $_POST['item_name'];
    $capacity = $_POST['capacity'];
    $refill_at = $_POST['refill_at'];
    $remaining_quantity = 0;


     $i=0;
     $qur = "SELECT * from hotel_items";

     $flag=0;
     if($res=mysqli_query($conn,$qur))
                       {
                         if(mysqli_num_rows($res) > 0)
                         {
                            while($row = mysqli_fetch_array($res))
                            {
                              if($row['id'] == $id && $row['item_name'] == $item_name)
                              {
                                $flag = 1;
                                $qur = "UPDATE hotel_items SET capacity ='.$capacity.' , refill_at = '.$refill_at.' WHERE id = '".$row['id']."' and item_name = '".$row['item_name']."'";

                                if($result=$conn->query($qur))
								    echo "YES";
								else
								    echo "NO";
								break;
                              }

                            }
                          }
                        }



    if($flag == 0)
    {
    	$qur = "INSERT INTO hotel_items values('$id','$item_name','$capacity','$remaining_quantity','$refill_at')";

              if($result=$conn->query($qur))
				echo "YES";
			  else
				 echo "NO";
    }

?>