package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;


public class Setting {
	
	static Connection connection;
	static Statement statement;
	
	static{
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
			statement = connection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		execute("DROP DATABASE IF EXISTS `sw3_Setting`;");
		execute("CREATE SCHEMA IF NOT EXISTS `sw3_Setting` DEFAULT CHARACTER SET utf8 ;");
		
		execute("CREATE TABLE IF NOT EXISTS `sw3_Setting`.`TBL_Customer` (\r\n"
				+ "  `cID` VARCHAR(6) NOT NULL,\r\n"
				+ "  `cPW` VARCHAR(4) NULL DEFAULT NULL,\r\n"
				+ "  `cName` VARCHAR(10) NULL DEFAULT NULL,\r\n"
				+ "  `cHP` VARCHAR(13) NULL DEFAULT NULL,\r\n"
				+ "  PRIMARY KEY (`cID`))\r\n"
				+ "ENGINE = InnoDB\r\n"
				+ "DEFAULT CHARACTER SET = utf8;");
		
		execute("CREATE TABLE IF NOT EXISTS `sw3_Setting`.`TBL_Bus` (\r\n"
				+ "  `bNumber` VARCHAR(4) NOT NULL,\r\n"
				+ "  `bDeparture` VARCHAR(5) NULL DEFAULT NULL,\r\n"
				+ "  `bArrival` VARCHAR(5) NULL DEFAULT NULL,\r\n"
				+ "  `vTime` TIME NULL DEFAULT NULL,\r\n"
				+ "  `bElapse` VARCHAR(10) NULL DEFAULT NULL,\r\n"
				+ "  `vCOUnt` VARCHAR(1) NULL DEFAULT NULL,\r\n"
				+ "  `bPrice` INT(6) NULL DEFAULT NULL,\r\n"
				+ "  PRIMARY KEY (`bNumber`))\r\n"
				+ "ENGINE = InnoDB\r\n"
				+ "DEFAULT CHARACTER SET = utf8;");
		
		execute("CREATE TABLE IF NOT EXISTS `sw3_Setting`.`TBL_Ticket` (\r\n"
				+ "  `bDate` DATE NOT NULL,\r\n"
				+ "  `bNumber` VARCHAR(4) NULL DEFAULT NULL,\r\n"
				+ "  `bNumber2` VARCHAR(5) NULL DEFAULT NULL,\r\n"
				+ "  `bSeat` INT(2) NULL DEFAULT NULL,\r\n"
				+ "  `cID` VARCHAR(6) NULL DEFAULT NULL,\r\n"
				+ "  `bPrice` INT(6) NULL DEFAULT NULL,\r\n"
				+ "  `bState` VARCHAR(1) NULL DEFAULT NULL)\r\n"
				+ "ENGINE = InnoDB\r\n"
				+ "DEFAULT CHARACTER SET = utf8;");

		execute("use sw3_Setting");
		execute("drop user if exists 'user'@'%'");
		execute("create user 'user'@'%' identified by '1234'");
		execute("grant select, insert, delete, update on `sw3_Setting`.* to 'user'@'%';");
		execute("flush privileges");
		
		
		for(String table:"TBL_BUS,TBL_CUSTOMER,TBL_TICKET".split(",")) {
			execute("load data local infile 'src/table/"+table+".txt' into table "+table+
					" fields terminated by '\t' lines terminated by '\n' ignore 1 lines");
		}
		
	}
	
	public static void execute(String sql) {
		try {
			statement.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
