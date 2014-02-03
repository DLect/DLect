-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1:3306
-- Generation Time: Nov 29, 2013 at 06:42 AM
-- Server version: 5.5.34
-- PHP Version: 5.4.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `dlect`
--

-- --------------------------------------------------------

--
-- Table structure for table `lecture`
--

CREATE TABLE `lecture` (
  `lecureId` bigint(20) NOT NULL AUTO_INCREMENT,
  `subject` bigint(20) NOT NULL,
  `recordedDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `baseUrl` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`lecureId`),
  KEY `subject` (`subject`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `lecturestreams`
--

CREATE TABLE `lecturestreams` (
  `lecureId` bigint(20) NOT NULL,
  `streamId` bigint(20) NOT NULL,
  PRIMARY KEY (`lecureId`,`streamId`),
  KEY `streamId` (`streamId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `semester`
--

CREATE TABLE `semester` (
  `semId` bigint(20) NOT NULL AUTO_INCREMENT,
  `university` bigint(20) NOT NULL,
  `code` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `startDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`semId`),
  KEY `university` (`university`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `stream`
--

CREATE TABLE `stream` (
  `streamId` bigint(20) NOT NULL AUTO_INCREMENT,
  `subject` bigint(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`streamId`),
  KEY `subject` (`subject`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `streamdates`
--

CREATE TABLE `streamdates` (
  `stream` bigint(20) NOT NULL,
  `date_` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`stream`,`date_`),
  KEY `stream` (`stream`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `subject`
--

CREATE TABLE `subject` (
  `subjId` bigint(20) NOT NULL AUTO_INCREMENT,
  `university` bigint(20) NOT NULL,
  `code` varchar(20) NOT NULL,
  `displayName` varchar(150) NOT NULL,
  `description` varchar(300) NOT NULL,
  `semesterCode` bigint(20) NOT NULL,
  PRIMARY KEY (`subjId`),
  KEY `university` (`university`,`semesterCode`),
  KEY `semesterCode` (`semesterCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `university`
--

CREATE TABLE `university` (
  `Code` bigint(20) NOT NULL,
  `Name` varchar(150) NOT NULL,
  `Support` varchar(20) DEFAULT 'NONE',
  `URL` varchar(400) DEFAULT NULL,
  `Timeout` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `retries` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `university`
--

INSERT INTO `university` (`Code`, `Name`, `Support`, `URL`, `Timeout`, `retries`) VALUES
(91, 'Southern Cross University', 'HTTPS', 'https://learn.scu.edu.au/webapps/Bb-mobile-bb_bb60/', '2013-11-30 06:37:58', 0),
(92, '?', 'NONE', NULL, '2013-11-29 07:33:18', 0),
(921, 'The University of Queensland', 'HTTPS', 'https://learn.uq.edu.au/webapps/Bb-mobile-BBLEARN/', '2013-11-30 06:33:03', 0);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `lecture`
--
ALTER TABLE `lecture`
  ADD CONSTRAINT `lecture_ibfk_1` FOREIGN KEY (`subject`) REFERENCES `subject` (`subjId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `lecturestreams`
--
ALTER TABLE `lecturestreams`
  ADD CONSTRAINT `lectureStreams_ibfk_1` FOREIGN KEY (`streamId`) REFERENCES `stream` (`streamId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `lectureStreams_ibfk_2` FOREIGN KEY (`lecureId`) REFERENCES `lecture` (`lecureId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `semester`
--
ALTER TABLE `semester`
  ADD CONSTRAINT `semester_ibfk_1` FOREIGN KEY (`university`) REFERENCES `university` (`Code`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `stream`
--
ALTER TABLE `stream`
  ADD CONSTRAINT `stream_ibfk_1` FOREIGN KEY (`subject`) REFERENCES `subject` (`subjId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `streamdates`
--
ALTER TABLE `streamdates`
  ADD CONSTRAINT `streamDates_ibfk_1` FOREIGN KEY (`stream`) REFERENCES `stream` (`streamId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `subject`
--
ALTER TABLE `subject`
  ADD CONSTRAINT `subject_ibfk_1` FOREIGN KEY (`semesterCode`) REFERENCES `semester` (`semId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `subject_ibfk_2` FOREIGN KEY (`university`) REFERENCES `university` (`Code`) ON DELETE CASCADE ON UPDATE CASCADE;
