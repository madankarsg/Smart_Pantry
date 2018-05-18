<?php
$con=mysqli_connect("localhost","root","","smart_pantry");
if($con)
{
    echo "connected successfull";
}


   $gar=$_POST['garlic'];
   $oni=$_POST['onion'];
   $val1=$_POST['garlic_val'];
   $val2=$_POST['onion_val'];
     //$sql="INSERT INTO `sps`(`id`, `percent`) VALUES ('','$val1')";
    $sql1="update hotel_items set remaining_quantity='$val1' where id='7020167558' and item_name='Onion'";
    $res=mysqli_query($con,$sql1);
    if($res)
    {
        echo "Onion update";
    }
    $sql2="update hotel_items set remaining_quantity='$val2' where id='7020167558' and item_name='Garlic'";
    $res=mysqli_query($con,$sql2);
    if($res)
    {
        echo "Garlic update";
    }

 



/*echo $_POST['one'];
$var=$_POST['one'];
    
    $sql="INSERT INTO `sps`(`id`, `percent`) VALUES ('','$var')";
    $res=mysqli_query($con,$sql);
    if($res)
    {
        echo "Insert successfully";
    }
else
    
{
    echo "Not";
}
*/

?>
   
