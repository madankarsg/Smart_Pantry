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
    $flag=0;  
    $qur = "SELECT * from orders where buyer_id=$id ";
     if($res=mysqli_query($conn,$qur))
                       {
                         if(mysqli_num_rows($res) > 0)
                         {
                            while($row = mysqli_fetch_array($res))
                            {
                                          $seller_id=$row['seller_id'];
                                          $flag = 1;
                                          $obj=new stdClass();
                                          $obj->item_name=$row['item_name'];
                                          $obj->quantity=$row['quantity'];
                                          $seller_name="";
                                          $seller_address="";

                                          $qur2= "SELECT * from sellers where id='".$seller_id."'";
                                          if($res2=mysqli_query($conn,$qur2))
                                          {
                                            if(mysqli_num_rows($res2)>0)
                                              {
                                                while($row2 = mysqli_fetch_array($res2))
                                                {
                                                  $seller_name=$row2['name'];
                                                  $seller_address=$row2['address'];
                                                }
                                              }
                                          }

                                          $obj->seller_name=$seller_name;
                                          $obj->seller_address=$seller_address;
                                          $obj->status_accepted=0;
                                          $obj_arr[$i]=($obj);
                                          $i=$i+1;
                              

                            }
                          }
                        }


    $qur = "SELECT * from orders_history where buyer_id=$id and delivered=0";
     if($res=mysqli_query($conn,$qur))
                       {
                         if(mysqli_num_rows($res) > 0)
                         {
                            while($row = mysqli_fetch_array($res))
                            {
                                          $seller_id=$row['seller_id'];
                                          $flag = 1;
                                          $obj=new stdClass();
                                          $obj->item_name=$row['item_name'];
                                          $obj->quantity=$row['quantity'];
                                          $seller_name="";
                                          $seller_address="";

                                          $qur2= "SELECT * from sellers where id='".$seller_id."'";
                                          if($res2=mysqli_query($conn,$qur2))
                                          {
                                            if(mysqli_num_rows($res2)>0)
                                              {
                                                while($row2 = mysqli_fetch_array($res2))
                                                {
                                                  $seller_name=$row2['name'];
                                                  $seller_address=$row2['address'];
                                                }
                                              }
                                          }

                                          $obj->seller_name=$seller_name;
                                          $obj->seller_address=$seller_address;
                                          $obj->status_accepted=1;
                                          $obj_arr[$i]=($obj);
                                          $i=$i+1;
                              

                            }
                          }
                        }
    

   if($flag==1)
      echo json_encode($obj_arr);
    else
      echo "null";
   
?>