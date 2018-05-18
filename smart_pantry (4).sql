-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 16, 2018 at 08:25 AM
-- Server version: 10.1.19-MariaDB
-- PHP Version: 5.6.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `smart_pantry`
--

-- --------------------------------------------------------

--
-- Table structure for table `hotels`
--

CREATE TABLE `hotels` (
  `id` varchar(10) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `location` varchar(50) NOT NULL,
  `authority_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hotels`
--

INSERT INTO `hotels` (`id`, `name`, `address`, `location`, `authority_name`) VALUES
('7020167558', 'Annapurna', 'Vishrambag, Sangli.', 'Sangli', 'Shubham Koli'),
('8805299384', 'Sweet Flame', 'Solapur', 'Solapur', 'Vaibhav Thombre'),
('9823680503', 'Hotel Sai', 'Vijaynagar, in front Of Pearl Hotel', 'Sangli', 'Shubham Madankar'),
('9823680504', 'Annapurna', 'Bhande Square Plot, Nagpur', 'Nagpur', 'Pratik Desai\r\n');

-- --------------------------------------------------------

--
-- Table structure for table `hotel_items`
--

CREATE TABLE `hotel_items` (
  `id` varchar(10) DEFAULT NULL,
  `item_name` varchar(50) DEFAULT NULL,
  `capacity` int(11) DEFAULT NULL,
  `remaining_quantity` int(11) DEFAULT NULL,
  `refill_at` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hotel_items`
--

INSERT INTO `hotel_items` (`id`, `item_name`, `capacity`, `remaining_quantity`, `refill_at`, `date`) VALUES
('7020167558', 'Tomato', 150, 15, 30, '2018-04-16'),
('7020167558', 'Onion', 100, 76, 20, '2018-04-16');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `buyer_id` varchar(10) DEFAULT NULL,
  `seller_id` varchar(10) DEFAULT NULL,
  `item_name` varchar(50) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `orders_history`
--

CREATE TABLE `orders_history` (
  `buyer_id` varchar(10) DEFAULT NULL,
  `seller_id` varchar(10) DEFAULT NULL,
  `item_name` varchar(50) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `delivered` int(11) NOT NULL DEFAULT '0',
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `orders_history`
--

INSERT INTO `orders_history` (`buyer_id`, `seller_id`, `item_name`, `quantity`, `delivered`, `date`) VALUES
('7020167558', '9823450000', 'Sugar', 125, 1, '2018-04-15'),
('7020167558', '9823450000', 'Tomato', 135, 0, '2018-04-16');

-- --------------------------------------------------------

--
-- Table structure for table `sellers`
--

CREATE TABLE `sellers` (
  `id` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `location` varchar(50) NOT NULL,
  `contact` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sellers`
--

INSERT INTO `sellers` (`id`, `name`, `address`, `location`, `contact`) VALUES
('8805299384', 'Aniket Baad', 'Vijaynagar, Sangli.', 'Sangli', '8805299384'),
('9823450000', 'Shubham Madankar', 'Sant Dnyaneshwar Ward,Sangli.', 'Sangli', '9823450012'),
('9864532155', 'Akshay Kale', 'Market Yard, Solapur.', 'Solapur', '7578486545'),
('9874159815', 'Soham Shinde', 'Surana Market, Sangli.', 'Sangli', '8421224565');

-- --------------------------------------------------------

--
-- Table structure for table `seller_items`
--

CREATE TABLE `seller_items` (
  `id` varchar(10) DEFAULT NULL,
  `item_name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `seller_items`
--

INSERT INTO `seller_items` (`id`, `item_name`) VALUES
('9823450000', 'Garlic'),
('9823450000', 'Potato'),
('9864532155', 'Tomato'),
('9864532155', 'Onion'),
('9864532155', 'Garlic'),
('9864532155', 'Sugar');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `hotels`
--
ALTER TABLE `hotels`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `hotel_items`
--
ALTER TABLE `hotel_items`
  ADD KEY `id` (`id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD KEY `buyer_id` (`buyer_id`),
  ADD KEY `seller_id` (`seller_id`);

--
-- Indexes for table `orders_history`
--
ALTER TABLE `orders_history`
  ADD KEY `buyer_id` (`buyer_id`),
  ADD KEY `seller_id` (`seller_id`);

--
-- Indexes for table `sellers`
--
ALTER TABLE `sellers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `seller_items`
--
ALTER TABLE `seller_items`
  ADD KEY `id` (`id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `hotel_items`
--
ALTER TABLE `hotel_items`
  ADD CONSTRAINT `hotel_items_ibfk_1` FOREIGN KEY (`id`) REFERENCES `hotels` (`id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `hotels` (`id`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`seller_id`) REFERENCES `sellers` (`id`);

--
-- Constraints for table `orders_history`
--
ALTER TABLE `orders_history`
  ADD CONSTRAINT `orders_history_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `hotels` (`id`),
  ADD CONSTRAINT `orders_history_ibfk_2` FOREIGN KEY (`seller_id`) REFERENCES `sellers` (`id`);

--
-- Constraints for table `seller_items`
--
ALTER TABLE `seller_items`
  ADD CONSTRAINT `seller_items_ibfk_1` FOREIGN KEY (`id`) REFERENCES `sellers` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
