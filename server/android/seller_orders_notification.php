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
    $qur = "SELECT * from orders where seller_id=$id";
    $flag=0;
     if($res=mysqli_query($conn,$qur))
                       {
                         if(mysqli_num_rows($res) > 0)
                         {
                            while($row = mysqli_fetch_array($res))
                            {
                              $item = $row['item_name'];
                              if($row['date']==NULL || $row['date']!=date('Y-m-d'))
                                {
                                  $date = date('Y-m-d');                                
                                  $sql = "update orders set date='".$date."' where seller_id='".$id."' and item_name='".$item."'";
                                   $conn->query($sql);
                            
                                  $qur2= "SELECT * from orders where buyer_id='".$id."' and item_name='".$item."'";
                                  if($res2=mysqli_query($conn,$qur2))
                                  {
                                    if(mysqli_num_rows($res2) == 0)
                                      {

                                            $flag = 1;
                                            $obj=new stdClass();
                                            $obj->item_name=$row['item_name'];
                                            $obj->quantity_to_buy=0;
                                            $obj_arr[$i]=($obj);
                                            $i=$i+1;
                                         
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