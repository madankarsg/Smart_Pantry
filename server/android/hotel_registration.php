<?php 
$id=$_POST['mobile_number'];
$name = $_POST['hotel_name'];
$address=$_POST['address'];
$location=$_POST['location'];
$authority_name = $_POST['authority_name'];

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

    $qur = "SELECT * from hotels";

$flag=0;
 if($res=mysqli_query($conn,$qur))
                   {
                     if(mysqli_num_rows($res) > 0)
                     {
                        while($row = mysqli_fetch_array($res))
                        {
                          if($id==$row['id'])
                          {
                          	$flag=1;
                             $qur = "update hotels set name='$name',address='$address',location='$location' where id='$id'";
                                   if($result=$conn->query($qur))
                                       echo "YES";
                                    else
                                       echo "NO";
                          	break;
                          }

                        }
                      }
                    }
if($flag!=1)
{
  $qur = "INSERT into hotels values('$id', '$name','$address','$location','$authority_name')";
  if($result=$conn->query($qur))
    echo "YES";
  else
    echo "NO";
  
}

?>