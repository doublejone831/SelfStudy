import java.sql.*;

public class module {
    public static void main(String[] args) throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
            e.printStackTrace();
            return;
        } /// jdbc driver(.jar file)

        System.out.println("PostgreSQL JDBC Driver Registered!");

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/project_movie", "postgres", "cse3207");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }/// 비밀번호, 데이터베이스 명, user 명
      

        if (conn != null) {
            System.out.println(conn);
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
        try{
        Statement stmt = null;
        Statement stmt2 = null;

        ResultSet rs = null;
        stmt2 = conn.createStatement();
        stmt = conn.createStatement();
        // Initialize the DB
        stmt2.executeUpdate("DROP TABLE IF EXISTS actor, actor_award, award, customer, customer_rate, director, director_award, genre, movie, movie_actor, movie_award, movie_genre CASCADE");
        
        String create_quary = // Director Table Create SQL
        		"CREATE TABLE Director\r\n"
        		+ "(\r\n"
        		+ "    Director_ID      serial         NOT NULL, \r\n"
        		+ "    Director_name    varchar(50)    NULL, \r\n"
        		+ "    DOB              date           NULL, \r\n"
        		+ "    DOD              date           NULL, \r\n"
        		+ "    CONSTRAINT PK_Director PRIMARY KEY (Director_ID)\r\n"
        		+ ");\r\n"
        		+ "CREATE TABLE Movie\r\n"
        		+ "(\r\n"
        		+ "    Movie_ID       serial         NOT NULL, \r\n"
        		+ "    Movie_name     varchar(50)    NULL, \r\n"
        		+ "    Director_ID    int            NULL, \r\n"
        		+ "    Publisher      varchar(50)    NULL, \r\n"
        		+ "    Relate_date    date           NULL, \r\n"
        		+ "    AVGRate        numeric(3,2)            NULL, \r\n"
        		+ "    CONSTRAINT PK_Movie PRIMARY KEY (Movie_ID)\r\n"
        		+ ");\r\n"
        		+ "ALTER TABLE Movie\r\n"
        		+ "    ADD CONSTRAINT FK_Movie_Director_ID_Director_Director_ID FOREIGN KEY (Director_ID)\r\n"
        		+ "        REFERENCES Director (Director_ID) ON DELETE CASCADE;\r\n"
        		+ "CREATE TABLE Award\r\n"
        		+ "(\r\n"
        		+ "    Award_ID      serial         NOT NULL, \r\n"
        		+ "    Award_name    varchar(50)    NULL, \r\n"
        		+ "    CONSTRAINT PK_Award PRIMARY KEY (Award_ID)\r\n"
        		+ ");\r\n"
        		+ "CREATE TABLE Actor\r\n"
        		+ "(\r\n"
        		+ "    Actor_ID      serial         NOT NULL, \r\n"
        		+ "    Actor_name    varchar(50)    NULL, \r\n"
        		+ "    DOB           date           NULL, \r\n"
        		+ "    DOD           date           NULL, \r\n"
        		+ "    Gender        varchar(6)     NULL, \r\n"
        		+ "    CONSTRAINT PK_Actor PRIMARY KEY (Actor_ID)\r\n"
        		+ ");\r\n"
        		+ "CREATE TABLE Genre\r\n"
        		+ "(\r\n"
        		+ "    Genre_ID      serial         NOT NULL, \r\n"
        		+ "    Genre_name    varchar(50)    NULL, \r\n"
        		+ "    CONSTRAINT PK_Genre PRIMARY KEY (Genre_ID)\r\n"
        		+ ");\r\n"
        		+ "CREATE TABLE Customer\r\n"
        		+ "(\r\n"
        		+ "    Customer_ID      serial         NOT NULL, \r\n"
        		+ "    Customer_name    varchar(50)    NULL, \r\n"
        		+ "    DOB              date           NULL, \r\n"
        		+ "    Gender           varchar(6)     NULL, \r\n"
        		+ "    CONSTRAINT PK_Customer PRIMARY KEY (Customer_ID)\r\n"
        		+ ");\r\n"
        		+ "CREATE TABLE Movie_Actor\r\n"
        		+ "(\r\n"
        		+ "    Movie          int            NULL, \r\n"
        		+ "    Actor          int            NULL, \r\n"
        		+ "    acting_role    varchar(20)    NULL, \r\n"
        		+ "    CONSTRAINT PK_Movie_Actor PRIMARY KEY (Movie, Actor)\r\n"
        		+ ");\r\n"
        		+ "ALTER TABLE Movie_Actor\r\n"
        		+ "    ADD CONSTRAINT FK_Movie_Actor_Movie_Movie_Movie_ID FOREIGN KEY (Movie)\r\n"
        		+ "        REFERENCES Movie (Movie_ID) ON DELETE CASCADE;\r\n"
        		+ "ALTER TABLE Movie_Actor\r\n"
        		+ "    ADD CONSTRAINT FK_Movie_Actor_Actor_Actor_Actor_ID FOREIGN KEY (Actor)\r\n"
        		+ "        REFERENCES Actor (Actor_ID) ON DELETE CASCADE;\r\n"
        		+ "CREATE TABLE Movie_Genre\r\n"
        		+ "(\r\n"
        		+ "    Movie    int    NULL, \r\n"
        		+ "    Genre    int    NULL, \r\n"
        		+ "    CONSTRAINT PK_Movie_Genre PRIMARY KEY (Movie, Genre)\r\n"
        		+ ");\r\n"
        		+ "ALTER TABLE Movie_Genre\r\n"
        		+ "    ADD CONSTRAINT FK_Movie_Genre_Movie_Movie_Movie_ID FOREIGN KEY (Movie)\r\n"
        		+ "        REFERENCES Movie (Movie_ID) ON DELETE CASCADE;\r\n"
        		+ "ALTER TABLE Movie_Genre\r\n"
        		+ "    ADD CONSTRAINT FK_Movie_Genre_Genre_Genre_Genre_ID FOREIGN KEY (Genre)\r\n"
        		+ "        REFERENCES Genre (Genre_ID) ON DELETE CASCADE;\r\n"
        		+ "CREATE TABLE Director_Award\r\n"
        		+ "(\r\n"
        		+ "    Director_ID    int     NULL, \r\n"
        		+ "    Award_ID       int     NULL, \r\n"
        		+ "    Given_date     int     NULL, \r\n"
        		+ "    CONSTRAINT PK_Director_Award PRIMARY KEY (Director_ID, Award_ID)\r\n"
        		+ ");\r\n"
        		+ "ALTER TABLE Director_Award\r\n"
        		+ "    ADD CONSTRAINT FK_Director_Award_Director_ID_Director_Director_ID FOREIGN KEY (Director_ID)\r\n"
        		+ "        REFERENCES Director (Director_ID) ON DELETE CASCADE;\r\n"
        		+ "ALTER TABLE Director_Award\r\n"
        		+ "    ADD CONSTRAINT FK_Director_Award_Award_ID_Award_Award_ID FOREIGN KEY (Award_ID)\r\n"
        		+ "        REFERENCES Award (Award_ID) ON DELETE CASCADE;\r\n"
        		+ "CREATE TABLE Actor_Award\r\n"
        		+ "(\r\n"
        		+ "    Actor_ID      int     NULL, \r\n"
        		+ "    Award_ID      int     NULL, \r\n"
        		+ "    Given_date    int    NULL, \r\n"
        		+ "    CONSTRAINT PK_Actor_Award PRIMARY KEY (Actor_ID, Award_ID)\r\n"
        		+ ");\r\n"
        		+ "ALTER TABLE Actor_Award\r\n"
        		+ "    ADD CONSTRAINT FK_Actor_Award_Award_ID_Award_Award_ID FOREIGN KEY (Award_ID)\r\n"
        		+ "        REFERENCES Award (Award_ID) ON DELETE CASCADE;\r\n"
        		+ "ALTER TABLE Actor_Award\r\n"
        		+ "    ADD CONSTRAINT FK_Actor_Award_Actor_ID_Actor_Actor_ID FOREIGN KEY (Actor_ID)\r\n"
        		+ "        REFERENCES Actor (Actor_ID) ON DELETE CASCADE;\r\n"
        		+ "CREATE TABLE Movie_Award\r\n"
        		+ "(\r\n"
        		+ "    Movie_ID      int     NULL, \r\n"
        		+ "    Award_ID      int     NULL, \r\n"
        		+ "    Given_date    int    NULL, \r\n"
        		+ "    CONSTRAINT PK_Movie_Award PRIMARY KEY (Movie_ID, Award_ID)\r\n"
        		+ ");\r\n"
        		+ "ALTER TABLE Movie_Award\r\n"
        		+ "    ADD CONSTRAINT FK_Movie_Award_Award_ID_Award_Award_ID FOREIGN KEY (Award_ID) \r\n"
        		+ "        REFERENCES Award (Award_ID) ON DELETE CASCADE;\r\n"
        		+ "ALTER TABLE Movie_Award\r\n"
        		+ "    ADD CONSTRAINT FK_Movie_Award_Movie_ID_Movie_Movie_ID FOREIGN KEY (Movie_ID)\r\n"
        		+ "        REFERENCES Movie (Movie_ID) ON DELETE CASCADE;\r\n"
        		+ "CREATE TABLE CUSTOMER_RATE\r\n"
        		+ "(\r\n"
        		+ "    Customer_ID    int    NULL, \r\n"
        		+ "    Movie_ID       int    NULL, \r\n"
        		+ "    Rate           int    NULL, \r\n"
        		+ "    CONSTRAINT PK_CUSTOMER_RATE PRIMARY KEY (Customer_ID, Movie_ID)\r\n"
        		+ ");\r\n"
        		+ "ALTER TABLE CUSTOMER_RATE\r\n"
        		+ "    ADD CONSTRAINT FK_CUSTOMER_RATE_Customer_ID_Customer_Customer_ID FOREIGN KEY (Customer_ID)\r\n"
        		+ "        REFERENCES Customer (Customer_ID) ON DELETE CASCADE;\r\n"
        		+ "ALTER TABLE CUSTOMER_RATE\r\n"
        		+ "    ADD CONSTRAINT FK_CUSTOMER_RATE_Movie_ID_Movie_Movie_ID FOREIGN KEY (Movie_ID)\r\n"
        		+ "        REFERENCES Movie (Movie_ID) ON DELETE CASCADE;"
        		+ "INSERT INTO Director(Director_name,DOB,DOD)\r\n"
        		+ "	VALUES('Tim Burton','1958-08-25',NULL);\r\n"
        		+ "INSERT INTO Director(Director_name,DOB,DOD)\r\n"
        		+ "	VALUES('David Fincher','1962-08-28',NULL);\r\n"
        		+ "INSERT INTO Director(Director_name,DOB,DOD)\r\n"
        		+ "	VALUES('Christoper Nolan','1970-07-30',NULL);\r\n"
        		+ "INSERT INTO Customer(Customer_name,DOB,Gender)\r\n"
        		+ "	VALUES('Ethan','1997-11-14','MALE');\r\n"
        		+ "INSERT INTO Customer(Customer_name,DOB,Gender)\r\n"
        		+ "	VALUES('John','1978-01-23','MALE');\r\n"
        		+ "INSERT INTO Customer(Customer_name,DOB,Gender)\r\n"
        		+ "	VALUES('Hayden','1980-05-04','FEMALE');\r\n"
        		+ "INSERT INTO Customer(Customer_name,DOB,Gender)\r\n"
        		+ "	VALUES('Jill','1980-05-04','FEMALE');\r\n"
        		+ "INSERT INTO Customer(Customer_name,DOB,Gender)\r\n"
        		+ "	VALUES('Bell','1990-05-14','FEMALE');\r\n"
        		+ "INSERT INTO Actor(Actor_name,DOB,DOD,Gender)\r\n"
        		+ "	VALUES('Johnny Depp','1963-06-09', NULL, 'MALE');\r\n"
        		+ "INSERT INTO Actor(Actor_name,DOB,DOD,Gender)\r\n"
        		+ "	VALUES('Winona Ryder','1971-10-29', NULL, 'FEMALE');\r\n"
        		+ "INSERT INTO Actor(Actor_name,DOB,DOD,Gender)\r\n"
        		+ "	VALUES('Mia Wasikowska','1989-10-14', NULL, 'FEMALE');\r\n"
        		+ "INSERT INTO Actor(Actor_name,DOB,DOD,Gender)\r\n"
        		+ "	VALUES('Christian Bale','1974-01-30', NULL, 'MALE');\r\n"
        		+ "INSERT INTO Actor(Actor_name,DOB,DOD,Gender)\r\n"
        		+ "	VALUES('Heath Ledger','1979-04-04', '2008-01-22', 'MALE');\r\n"
        		+ "INSERT INTO Actor(Actor_name,DOB,DOD,Gender)\r\n"
        		+ "	VALUES('Jesse Eisenberg','1983-10-05', NULL, 'MALE');\r\n"
        		+ "INSERT INTO Actor(Actor_name,DOB,DOD,Gender)\r\n"
        		+ "	VALUES('Justin Timberlake','1981-01-31', NULL, 'MALE');\r\n"
        		+ "INSERT INTO Actor(Actor_name,DOB,DOD,Gender)\r\n"
        		+ "	VALUES('Fionn Whitehead','1997-07-18', NULL, 'MALE');\r\n"
        		+ "INSERT INTO Actor(Actor_name,DOB,DOD,Gender)\r\n"
        		+ "	VALUES('Tom Hardy','1977-09-15', NULL, 'MALE');\r\n"
        		+ "INSERT INTO Movie(Movie_name,Director_ID,Publisher,Relate_date)\r\n"
        		+ "	VALUES('Edward Scissorhands',1,'20th Century Fox Present','1991-06-29');\r\n"
        		+ "INSERT INTO Movie(Movie_name,Director_ID,Publisher,Relate_date)\r\n"
        		+ "	VALUES('Alice In Wonserland',1,'Korea Sony Pictures','2010-03-04');\r\n"
        		+ "INSERT INTO Movie(Movie_name,Director_ID,Publisher,Relate_date)\r\n"
        		+ "	VALUES('The Social Network',2,'Korea Sony Pictures','2010-11-18');\r\n"
        		+ "INSERT INTO Movie(Movie_name,Director_ID,Publisher,Relate_date)\r\n"
        		+ "	VALUES('The Dark Knight',3,'Warner Brothers Korea','2008-08-06');\r\n"
        		+ "INSERT INTO Movie(Movie_name,Director_ID,Publisher,Relate_date)\r\n"
        		+ "	VALUES('Dunkirk',3,'Warner Brothers Korea','2017-07-13'); \r\n"
        		+ "INSERT INTO Award(Award_name)\r\n"
        		+ "	VALUES('Best Supporting Actor');\r\n"
        		+ "INSERT INTO Award(Award_name)\r\n"
        		+ "	VALUES('Best Villian Actor');\r\n"
        		+ "INSERT INTO Award(Award_name)\r\n"
        		+ "	VALUES('Best Main Actor');\r\n"
        		+ "INSERT INTO Award(Award_name)\r\n"
        		+ "	VALUES('Best Fantasy Movie');\r\n"
        		+ "INSERT INTO Award(Award_name)\r\n"
        		+ "	VALUES('Best Picture');\r\n"
        		+ "INSERT INTO Award(Award_name)\r\n"
        		+ "	VALUES('Best Director');\r\n"
        		+ "INSERT INTO Movie_Actor(Movie, Actor, acting_role)\r\n"
        		+ "	VALUES(1,1,'Main actor');\r\n"
        		+ "INSERT INTO Movie_Actor(Movie, Actor, acting_role)\r\n"
        		+ "	VALUES(1,2,'Main actor');\r\n"
        		+ "INSERT INTO Movie_Actor(Movie, Actor, acting_role)\r\n"
        		+ "	VALUES(2,1,'Main actor');\r\n"
        		+ "INSERT INTO Movie_Actor(Movie, Actor, acting_role)\r\n"
        		+ "	VALUES(2,3,'Main actor');\r\n"
        		+ "INSERT INTO Movie_Actor(Movie, Actor, acting_role)\r\n"
        		+ "	VALUES(3,6,'Main actor');\r\n"
        		+ "INSERT INTO Movie_Actor(Movie, Actor, acting_role)\r\n"
        		+ "	VALUES(3,7,'Supporting actor');\r\n"
        		+ "INSERT INTO Movie_Actor(Movie, Actor, acting_role)\r\n"
        		+ "	VALUES(4,4,'Main actor');\r\n"
        		+ "INSERT INTO Movie_Actor(Movie, Actor, acting_role)\r\n"
        		+ "	VALUES(4,5,'Main actor');\r\n"
        		+ "INSERT INTO Movie_Actor(Movie, Actor, acting_role)\r\n"
        		+ "	VALUES(5,8,'Main actor');\r\n"
        		+ "INSERT INTO Movie_Actor(Movie, Actor, acting_role)\r\n"
        		+ "	VALUES(5,9,'Supporting Actor');\r\n"
        		+ "INSERT INTO Genre(Genre_name)\r\n"
        		+ "	VALUES('Fantasy');\r\n"
        		+ "INSERT INTO Genre(Genre_name)\r\n"
        		+ "	VALUES('Romance');\r\n"
        		+ "INSERT INTO Genre(Genre_name)\r\n"
        		+ "	VALUES('Adventure');\r\n"
        		+ "INSERT INTO Genre(Genre_name)\r\n"
        		+ "	VALUES('Family');\r\n"
        		+ "INSERT INTO Genre(Genre_name)\r\n"
        		+ "	VALUES('Drama');\r\n"
        		+ "INSERT INTO Genre(Genre_name)\r\n"
        		+ "	VALUES('Action');\r\n"
        		+ "INSERT INTO Genre(Genre_name)\r\n"
        		+ "	VALUES('Mystery');\r\n"
        		+ "INSERT INTO Genre(Genre_name)\r\n"
        		+ "	VALUES('Thriller');\r\n"
        		+ "INSERT INTO Genre(Genre_name)\r\n"
        		+ "	VALUES('War');\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(1, 1);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(1, 2);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(2, 1);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(2, 3);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(2, 4);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(3, 5);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(4, 6);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(4, 5);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(4, 7);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(4, 8);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(5, 6);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(5, 5);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(5, 8);\r\n"
        		+ "INSERT INTO Movie_Genre\r\n"
        		+ "	Values(5, 9);\r\n";
        stmt.executeUpdate(create_quary);
        
        System.out.println("Table_Created!!\r\n"
        		+ "Initial Data inputed!!\r\nAward is also included in initial data\r\n");
        rs = stmt.executeQuery("SELECT * FROM AWARD;");
        System.out.println("Award Table\r\nAward_ID               Award_Name\r\n"
        		+"-----------------------------------------------------");
        while(rs.next()) {
        	System.out.println(rs.getInt(1) + "\t" + rs.getString(2));
        }
        rs = null;
       
        //statement 하나씩 실행 2.1
        System.out.println("\r\n\r\nStatement 2.x start");
        System.out.println("Statement: Winona Ryder won the “Best supporting actor” award in 1994");
        System.out.println("Query : INSERT INTO Actor_Award VALUES (2,1,1994);");
        System.out.println("Actor_Award table");
        stmt.executeUpdate("INSERT INTO Actor_Award VALUES (2,1,1994);");
        System.out.println("----------------------------------------------------------");
        System.out.println("Award_ID	Actor_Name");
        rs = stmt.executeQuery("SELECT Award_ID,Actor_Name\r\n"
        		+ "FROM Actor_Award, Actor\r\n"
        		+ "WHERE Actor_Award.Actor_ID = Actor.Actor_ID;");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + "  	|	  " + rs.getString(2));
                }
        rs = null;
        //2.2
        System.out.println("Statement: Tom Hardy won the “Best supporting actor” award in 2018");
        System.out.println("Query : INSERT INTO Actor_Award VALUES (9,1,2018);");
        System.out.println("Actor_Award table");
        stmt.executeUpdate("INSERT INTO Actor_Award VALUES (9,1,2018);");
        System.out.println("----------------------------------------------------------");
        System.out.println("Award_ID	Actor_Name");
        rs = stmt.executeQuery("SELECT Award_ID,Actor_Name\r\n"
        		+ "FROM Actor_Award, Actor\r\n"
        		+ "WHERE Actor_Award.Actor_ID = Actor.Actor_ID;");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + "  	|	  " + rs.getString(2));
                }
        rs = null;
        //2.3
        System.out.println("Statement:Heath Ledger won the “Best villain actor” award in 2009");
        System.out.println("Query : INSERT INTO Actor_Award VALUES (5,2,2009);");
        System.out.println("Actor_Award table");
        stmt.executeUpdate("INSERT INTO Actor_Award VALUES (5,2,2009);");
        System.out.println("----------------------------------------------------------");
        System.out.println("Award_ID	Actor_Name");
        rs = stmt.executeQuery("SELECT Award_ID,Actor_Name\r\n"
        		+ "FROM Actor_Award, Actor\r\n"
        		+ "WHERE Actor_Award.Actor_ID = Actor.Actor_ID;");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + "  	|	  " + rs.getString(2));
                }
        rs = null;
        //2.4
        System.out.println("Statement:Johnny Depp won the “Best main actor” award in 2011");
        System.out.println("Query : INSERT INTO Actor_Award VALUES (1,3,2011);");
        System.out.println("Actor_Award table");
        stmt.executeUpdate("INSERT INTO Actor_Award VALUES (1,3,2011);");
        System.out.println("----------------------------------------------------------");
        System.out.println("Award_ID	Actor_Name");
        rs = stmt.executeQuery("SELECT Award_ID,Actor_Name\r\n"
        		+ "FROM Actor_Award, Actor\r\n"
        		+ "WHERE Actor_Award.Actor_ID = Actor.Actor_ID;");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + "  	|	  " + rs.getString(2));
                }
        rs = null;
        //2.5
        System.out.println("Statement:Edward Scissorhands won the “Best fantasy movie” award in 1991");
        System.out.println("Query : INSERT INTO Movie_Award VALUES (1,4,1991);");
        System.out.println("Movie_Award table");
        stmt.executeUpdate("INSERT INTO Movie_Award VALUES (1,4,1991);");
        System.out.println("----------------------------------------------------------");
        System.out.println("Award_ID	Movie_Name");
        rs = stmt.executeQuery("SELECT Award_ID,Movie_Name\r\n"
        		+ "FROM Movie_Award, Movie\r\n"
        		+ "WHERE Movie_Award.Movie_ID = Movie.Movie_ID;");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + "		  |		  " + rs.getString(2));
                }
        rs = null;
        //2.6
        System.out.println("Statement:Alice In Wonderland won the “Best fantasy movie” award in 2011");
        System.out.println("Query : INSERT INTO Movie_Award VALUES (2,4,2011);");
        System.out.println("Movie_Award table");
        stmt.executeUpdate("INSERT INTO Movie_Award VALUES (2,4,2011);");
        System.out.println("----------------------------------------------------------");
        System.out.println("Award_ID	Movie_Name");
        rs = stmt.executeQuery("SELECT Award_ID,Movie_Name\r\n"
        		+ "FROM Movie_Award, Movie\r\n"
        		+ "WHERE Movie_Award.Movie_ID = Movie.Movie_ID;");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + " 	 |	  " + rs.getString(2));
                }
        rs = null;
        //2.7
        System.out.println("Statement:The Dark Knight won the “Best picture” award in 2009");
        System.out.println("Query : INSERT INTO Movie_Award VALUES (4,5,2009);");
        System.out.println("Movie_Award table");
        stmt.executeUpdate("INSERT INTO Movie_Award VALUES (4,5,2009);");
        System.out.println("----------------------------------------------------------");
        System.out.println("Award_ID	Movie_Name");
        rs = stmt.executeQuery("SELECT Award_ID,Movie_Name\r\n"
        		+ "FROM Movie_Award, Movie\r\n"
        		+ "WHERE Movie_Award.Movie_ID = Movie.Movie_ID;");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + "  	|	  " + rs.getString(2));
                }
        rs = null;
        //2.8
        System.out.println("Statement:Christopher Nolan won the “Best director” award in 2018");
        System.out.println("Query : INSERT INTO Director_Award VALUES (3,6,2018);;");
        System.out.println("Director_Award table");
        stmt.executeUpdate("INSERT INTO Director_Award VALUES (3,6,2018);");
        System.out.println("----------------------------------------------------------");
        System.out.println("Award_ID	Director_Name");
        rs = stmt.executeQuery("SELECT Award_ID,Director_Name\r\n"
        		+ "FROM Director_Award, Director\r\n"
        		+ "WHERE Director_Award.Director_ID = Director.Director_ID;");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + " 	 |	  " + rs.getString(2));
                }
        rs = null;
        
        System.out.println("\r\n\r\n Statement 3.x Start");
        //3.1
        System.out.println("Statement : Ethan rates 5 to Dunkirk");
        System.out.println("Query : INSERT INTO Customer_Rate (Customer_ID, Movie_ID, Rate)\r\n"
        		+ "	        VALUES ((SELECT Customer.Customer_ID\r\n"
        		+ "			FROM Customer\r\n"
        		+ "			WHERE Customer.Customer_Name = 'Ethan'),\r\n"
        		+ "			(SELECT Movie_ID\r\n"
        		+ "			FROM Movie\r\n"
        		+ "			WHERE Movie.Movie_Name = 'Dunkirk'), 5);\r\n\r\n"
        		+ "Finding data That Match with the statement\r\n"
        		+ "Query : SELECT Customer_Name, Movie_Name, Rate\r\n"
        		+ "        FROM Customer, Customer_Rate, Movie\r\n"
        		+ "        WHERE Customer.Customer_ID = Customer_Rate.Customer_ID AND Movie.Movie_ID = Customer_Rate.Movie_ID");
        System.out.println("Customer_Rate table");
        stmt.executeUpdate("INSERT INTO Customer_Rate (Customer_ID, Movie_ID, Rate)\r\n"
        		+ "	VALUES ((SELECT Customer.Customer_ID\r\n"
        		+ "			FROM Customer\r\n"
        		+ "			WHERE Customer.Customer_Name = 'Ethan'),\r\n"
        		+ "			(SELECT Movie_ID\r\n"
        		+ "			FROM Movie\r\n"
        		+ "			WHERE Movie.Movie_Name = 'Dunkirk'), 5);");
        rs = stmt.executeQuery("SELECT Customer_Name, Movie_Name, Rate\r\n"
        		+ "FROM Customer, Customer_Rate, Movie\r\n"
        		+ "WHERE Customer.Customer_ID = Customer_Rate.Customer_ID AND Movie.Movie_ID = Customer_Rate.Movie_ID");
        System.out.println("----------------------------------------------------------");
        System.out.println("Customer_Name 	Movie_Name	rate");
        while(rs.next()) {
        	System.out.println(rs.getString(1) + " 	 |	  " + rs.getString(2)+ " 	 |	  " + rs.getInt(3));
        }
        rs = null;
        System.out.println("Movie Rate updated");
        System.out.println("Query : UPDATE Movie \r\n"
        		+ "        SET AVGRate = (SELECT AVG(Rate)\r\n"
        		+ "			              FROM Customer_Rate\r\n"
        		+ "			              WHERE Movie_ID = 5) \r\n"
        		+ "        WHERE Movie_ID = 5;");
        stmt.executeUpdate("UPDATE Movie \r\n"
        		+ "SET AVGRate = (SELECT AVG(Rate)\r\n"
        		+ "			   FROM Customer_Rate\r\n"
        		+ "			   WHERE Movie_ID = 5) \r\n"
        		+ "WHERE Movie_ID = 5;");
        System.out.println("Movie and Average_Rate");
        System.out.println("----------------------------------------------------------");
        System.out.println("Movie_ID	Movie_Name	AVGRate");
        rs = stmt.executeQuery("SELECT Movie_ID, Movie_Name, AVGRate FROM Movie order by Movie_ID");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + "    	|    " + rs.getString(2) + "    |    " + rs.getFloat(3) );
                }
        rs = null;
        
        //3.2
        System.out.println("Statement : Bell rates 5 to the movies whose director is Tim Burton");
        System.out.println("Query : INSERT INTO Customer_Rate (Customer_ID, Movie_ID, Rate)\r\n"
        		+ "	VALUES ((SELECT Customer.Customer_ID\r\n"
        		+ "			FROM Customer\r\n"
        		+ "			WHERE Customer.Customer_Name = 'Bell'),\r\n"
        		+ "			i, 5);\r\n"
        		+ "//i is INT variable refer Movie ID used in Java Loop \r\n\r\n"
        		+ "Finding data That Match with the statement\r\n"
        		+ "Query : SELECT Movie_ID\r\n"
        		+ "        FROM Movie, Director\r\n"
        		+ "        WHERE Movie.DIrector_ID = Director.Director_ID AND Director.Director_Name = 'Tim Burton'");
        
        rs = stmt.executeQuery("SELECT Movie_ID\r\n"
        		+ "FROM Movie, Director\r\n"
        		+ "WHERE Movie.DIrector_ID = Director.Director_ID AND Director.Director_Name = 'Tim Burton'");
        int i;
        
        while(rs.next()) {
        i = rs.getInt(1);
        stmt2.executeUpdate("INSERT INTO Customer_Rate (Customer_ID, Movie_ID, Rate)\r\n"
        		+ "	VALUES ((SELECT Customer.Customer_ID\r\n"
        		+ "			FROM Customer\r\n"
        		+ "			WHERE Customer.Customer_Name = 'Bell'),\r\n"
        		+ i +", 5);");
        stmt2.executeUpdate("UPDATE Movie \r\n"
        		+ "SET AVGRate = (SELECT AVG(Rate)\r\n"
        		+ "			   FROM Customer_Rate\r\n"
        		+ "			   WHERE Movie_ID = "+ i +") \r\n"
        		+ "WHERE Movie_ID ="+ i + ";");
        }
        rs = null;
        System.out.println("Movie Rate updated");
        System.out.println("Customer_Rate table");
        System.out.println("----------------------------------------------------------");
        System.out.println("Customer_Name 	Movie_Name	rate");
        rs = stmt.executeQuery("SELECT Customer_Name, Movie_Name, Rate\r\n"
        		+ "FROM Customer, Customer_Rate, Movie\r\n"
        		+ "WHERE Customer.Customer_ID = Customer_Rate.Customer_ID AND Movie.Movie_ID = Customer_Rate.Movie_ID");
        while(rs.next()) {
        	System.out.println(rs.getString(1) + " 	 |	  " + rs.getString(2)+ " 	 |	  " + rs.getInt(3));
        }
        rs = null;
        System.out.println("Movie and Average_Rate");
        System.out.println("----------------------------------------------------------");
        System.out.println("Movie_ID	Movie_Name	AVGRate");
        rs = stmt.executeQuery("SELECT Movie_ID, Movie_Name, AVGRate FROM Movie order by Movie_ID");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + "    	|    " + rs.getString(2) + "    |    " + rs.getFloat(3) );
                }
        rs = null;
        
      //3.3
        System.out.println("Statement : Jill rates 4 to the movies whose main actor is female");
        System.out.println("Query : INSERT INTO Customer_Rate (Customer_ID, Movie_ID, Rate)\r\n"
        		+ "		    VALUES ((SELECT Customer.Customer_ID\r\n"
        		+ "			FROM Customer\r\n"
        		+ "			WHERE Customer.Customer_Name = 'Bell'),\r\n"
        		+ "			i, 5);\r\n"
        		+ "//i is INT variable refer Movie ID used in Java Loop \r\n\r\n"
        		+ "Finding data That Match with the statement\r\n"
        		+ "Query : SELECT Movie_ID\r\n"
        		+ "        FROM Movie, Movie_Actor, Actor\r\n"
        		+ "        WHERE Movie.Movie_ID = Movie_Actor.Movie AND Actor.Actor_ID = Movie_Actor.Actor\r\n"
        		+ "        		AND Movie_Actor.Acting_role ='Main actor' AND Actor.Gender = 'FEMALE'");
        System.out.println("Customer_Rate table");
        rs = stmt.executeQuery("SELECT Movie_ID\r\n"
        		+ "FROM Movie, Movie_Actor, Actor\r\n"
        		+ "WHERE Movie.Movie_ID = Movie_Actor.Movie AND Actor.Actor_ID = Movie_Actor.Actor\r\n"
        		+ "        		AND Movie_Actor.Acting_role ='Main actor' AND Actor.Gender = 'FEMALE'");
        
        while(rs.next()) {
        i = rs.getInt(1);
        stmt2.executeUpdate("INSERT INTO Customer_Rate (Customer_ID, Movie_ID, Rate)\r\n"
        		+ "	VALUES ((SELECT Customer.Customer_ID\r\n"
        		+ "			FROM Customer\r\n"
        		+ "			WHERE Customer.Customer_Name = 'Jill'),\r\n"
        		+ i +", 4);");
        stmt2.executeUpdate("UPDATE Movie \r\n"
        		+ "SET AVGRate = (SELECT AVG(Rate)\r\n"
        		+ "			   FROM Customer_Rate\r\n"
        		+ "			   WHERE Movie_ID = "+ i +") \r\n"
        		+ "WHERE Movie_ID ="+ i + ";");
        }
        rs = null;
        System.out.println("Movie Rate updated");
        System.out.println("----------------------------------------------------------");
        System.out.println("Customer_Name 	Movie_Name	rate");
        rs = stmt.executeQuery("SELECT Customer_Name, Movie_Name, Rate\r\n"
        		+ "FROM Customer, Customer_Rate, Movie\r\n"
        		+ "WHERE Customer.Customer_ID = Customer_Rate.Customer_ID AND Movie.Movie_ID = Customer_Rate.Movie_ID");
        while(rs.next()) {
        	System.out.println(rs.getString(1) + " 	 |	  " + rs.getString(2)+ " 	 |	  " + rs.getInt(3));
        }
        rs = null;
        System.out.println("Movie and Average_Rate");
        System.out.println("----------------------------------------------------------");
        System.out.println("Movie_ID	Movie_Name	AVGRate");
        rs = stmt.executeQuery("SELECT Movie_ID, Movie_Name, AVGRate FROM Movie order by Movie_ID");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + "    	|    " + rs.getString(2) + "    |    " + rs.getFloat(3) );
                }
        rs = null;
        
        //3.4
        System.out.println("Statement : Hayden rates 4 to the fantasy movies");
        System.out.println("Query : INSERT INTO Customer_Rate (Customer_ID, Movie_ID, Rate)\r\n"
        		+ "	        VALUES ((SELECT Customer.Customer_ID\r\n"
        		+ "			FROM Customer\r\n"
        		+ "			WHERE Customer.Customer_Name = 'Hayden'),\r\n"
        		+ "			i, 4);\r\n"
        		+ "//i is INT variable refer Movie ID used in Java Loop \r\n\r\n"
        		+ "Finding data That Match with the statement\r\n"
        		+ "Query : SELECT Movie_ID\r\n"
        		+ "        FROM Movie, Movie_Genre, Genre\r\n"
        		+ "        WHERE Movie.Movie_ID = Movie_Genre.Movie AND Genre.Genre_ID = Movie_Genre.Genre\r\n"
        		+ "        		AND Genre.Genre_Name = 'Fantasy'");
        System.out.println("Customer_Rate table");
        rs = stmt.executeQuery("SELECT Movie_ID\r\n"
        		+ "FROM Movie, Movie_Genre, Genre\r\n"
        		+ "WHERE Movie.Movie_ID = Movie_Genre.Movie AND Genre.Genre_ID = Movie_Genre.Genre\r\n"
        		+ "        		AND Genre.Genre_Name = 'Fantasy'");
        
        while(rs.next()) {
        i = rs.getInt(1);
        stmt2.executeUpdate("INSERT INTO Customer_Rate (Customer_ID, Movie_ID, Rate)\r\n"
        		+ "	VALUES ((SELECT Customer.Customer_ID\r\n"
        		+ "			FROM Customer\r\n"
        		+ "			WHERE Customer.Customer_Name = 'Hayden'),\r\n"
        		+ i +", 4);");
        stmt2.executeUpdate("UPDATE Movie \r\n"
        		+ "SET AVGRate = (SELECT AVG(Rate)\r\n"
        		+ "			   FROM Customer_Rate\r\n"
        		+ "			   WHERE Movie_ID = "+ i +") \r\n"
        		+ "WHERE Movie_ID ="+ i + ";");
        }
        rs = null;
        System.out.println("Movie Rate updated");
        System.out.println("----------------------------------------------------------");
        System.out.println("Customer_Name 	Movie_Name	rate");
        rs = stmt.executeQuery("SELECT Customer_Name, Movie_Name, Rate\r\n"
        		+ "FROM Customer, Customer_Rate, Movie\r\n"
        		+ "WHERE Customer.Customer_ID = Customer_Rate.Customer_ID AND Movie.Movie_ID = Customer_Rate.Movie_ID");
        while(rs.next()) {
        	System.out.println(rs.getString(1) + " 	 |	  " + rs.getString(2)+ " 	 |	  " + rs.getInt(3));
        }
        rs = null;
        System.out.println("Movie and Average_Rate");
        System.out.println("----------------------------------------------------------");
        System.out.println("Movie_ID	Movie_Name	AVGRate");
        rs = stmt.executeQuery("SELECT Movie_ID, Movie_Name, AVGRate FROM Movie order by Movie_ID");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + "    	|    " + rs.getString(2) + "    |    " + rs.getFloat(3) );
                }
        rs = null;
        
        //3.5
        System.out.println("Statement : John rates 5 to the movies whose director won the “Best director” award\r\n");
        System.out.println("Query : INSERT INTO Customer_Rate (Customer_ID, Movie_ID, Rate)\r\n"
        		+ "	        VALUES ((SELECT Customer.Customer_ID\r\n"
        		+ "			FROM Customer\r\n"
        		+ "			WHERE Customer.Customer_Name = 'John'),\r\n"
        		+ "			i, 4);\r\n"
        		+ "//i is INT variable refer Movie ID used in Java Loop \r\n\r\n"
        		+"Finding data That Match with the statement\r\n"
        		+ "Query : SELECT Movie.Movie_ID\r\n"
        		+ "        FROM Movie, Director, Director_Award, Award\r\n"
        		+ "        WHERE Movie.Director_ID = Director.Director_ID AND Director.Director_ID = Director_Award.Director_ID\r\n"
        		+ "	       AND Director_Award.Award_ID = Award.Award_ID");
        System.out.println("Customer_Rate table");
        rs = stmt.executeQuery("SELECT Movie.Movie_ID\r\n"
        		+ "FROM Movie, Director, Director_Award, Award\r\n"
        		+ "WHERE Movie.Director_ID = Director.Director_ID AND Director.Director_ID = Director_Award.Director_ID\r\n"
        		+ "	AND Director_Award.Award_ID = Award.Award_ID");
        
        while(rs.next()) {
        i = rs.getInt(1);
        stmt2.executeUpdate("INSERT INTO Customer_Rate (Customer_ID, Movie_ID, Rate)\r\n"
        		+ "	VALUES ((SELECT Customer.Customer_ID\r\n"
        		+ "			FROM Customer\r\n"
        		+ "			WHERE Customer.Customer_Name = 'John'),\r\n"
        		+ i +", 5);");
        stmt2.executeUpdate("UPDATE Movie \r\n"
        		+ "SET AVGRate = (SELECT AVG(Rate)\r\n"
        		+ "			   FROM Customer_Rate\r\n"
        		+ "			   WHERE Movie_ID = "+ i +") \r\n"
        		+ "WHERE Movie_ID ="+ i + ";");
        }
        rs = null;
        System.out.println("Movie Rate updated");
        System.out.println("----------------------------------------------------------");
        System.out.println("Customer_Name 	Movie_Name	rate");
        rs = stmt.executeQuery("SELECT Customer_Name, Movie_Name, Rate\r\n"
        		+ "FROM Customer, Customer_Rate, Movie\r\n"
        		+ "WHERE Customer.Customer_ID = Customer_Rate.Customer_ID AND Movie.Movie_ID = Customer_Rate.Movie_ID");
        while(rs.next()) {
        	System.out.println(rs.getString(1) + " 	 |	  " + rs.getString(2)+ " 	 |	  " + rs.getInt(3));
        }
        rs = null;
        System.out.println("Movie and Average_Rate");
        System.out.println("----------------------------------------------------------");
        System.out.println("Movie_ID	Movie_Name	AVGRate");
        rs = stmt.executeQuery("SELECT Movie_ID, Movie_Name, AVGRate FROM Movie order by Movie_ID");
        while(rs.next()) {
                	System.out.println(rs.getInt(1) + "    	|    " + rs.getString(2) + "    |    " + rs.getFloat(3) );
                }
        rs = null;
        
        //4
        System.out.println("\r\n\r\nStatement : Select the names of the movies whose actor are dead");
        System.out.println("Query : SELECT Movie_name\r\n"
        		+ "From Movie,Movie_actor, Actor\r\n"
        		+ "WHERE Movie.Movie_ID = Movie_Actor.Movie AND Movie_Actor.Actor = Actor.Actor_ID\r\n"
        		+ "	AND not Actor.DOD IS NULL;");
        rs = stmt.executeQuery("SELECT Movie_name\r\n"
        		+ "From Movie,Movie_actor, Actor\r\n"
        		+ "WHERE Movie.Movie_ID = Movie_Actor.Movie AND Movie_Actor.Actor = Actor.Actor_ID\r\n"
        		+ "	AND not Actor.DOD IS NULL;");
        System.out.println("Movie name : ");
        while(rs.next()) {
        	System.out.println(rs.getString(1));
        }
        System.out.println("\r\nStatement : Select the names of the directors who cast the same actor more than once");
        System.out.println("Will Find whose actor count are less than Movie count*2\r\n\r\n"+"Query : SELECT Director_Name, Count(distinct Actor_ID), count(distinct Movie_ID)\r\n"
        		+ "From Movie,Movie_actor, Actor, Director\r\n"
        		+ "WHERE Movie.Movie_ID = Movie_Actor.Movie AND Movie_Actor.Actor = Actor.Actor_ID\r\n"
        		+ "	AND Movie.Director_ID = Director.Director_ID\r\n"
        		+ "GROUP BY Director.Director_NAME");
        rs = stmt.executeQuery("SELECT Director_Name, Count(distinct Actor_ID), count(distinct Movie_ID)\r\n"
        		+ "From Movie,Movie_actor, Actor, Director\r\n"
        		+ "WHERE Movie.Movie_ID = Movie_Actor.Movie AND Movie_Actor.Actor = Actor.Actor_ID\r\n"
        		+ "	AND Movie.Director_ID = Director.Director_ID\r\n"
        		+ "GROUP BY Director.Director_NAME");
        System.out.println("Director name : ");
        while(rs.next()) {
        	if(rs.getInt(2) < (rs.getInt(3)*2)){
        		System.out.println(rs.getString(1));
        	}
        }
        rs = null;
        //6
        System.out.println("Statement : Select the names of the movies and the genres, where movies have the common genre.\r\n"
        		+ "Query : SELECT Fir.Movie_Name, Sec.Movie_Name, Genre_name \r\n"
        		+ "FROM Movie as Fir join Movie_Genre as Af on(Fir.Movie_ID = Af.Movie) , Movie  as Sec join Movie_Genre as Ad on(Sec.Movie_ID = Ad.Movie), Genre\r\n"
        		+ "WHERE Genre.Genre_ID = Af.Genre AND  Genre.Genre_ID = Ad.Genre AND Af.Genre = Ad.Genre AND NOT Fir.Movie_ID = Sec.Movie_ID AND Fir.Movie_ID < Sec.Movie_ID\r\n");
        rs = stmt.executeQuery("SELECT Fir.Movie_Name, Sec.Movie_Name, Genre_name \r\n"
        		+ "FROM Movie as Fir join Movie_Genre as Af on(Fir.Movie_ID = Af.Movie) , Movie  as Sec join Movie_Genre as Ad on(Sec.Movie_ID = Ad.Movie), Genre\r\n"
        		+ "WHERE Genre.Genre_ID = Af.Genre AND  Genre.Genre_ID = Ad.Genre AND Af.Genre = Ad.Genre AND Fir.Movie_ID < Sec.Movie_ID\r\n");
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Movie Name1 	|	 Movie Name2 	 |      Common Genre");
        while(rs.next()) {
        	System.out.println(rs.getString(1) + "  |  " + rs.getString(2) + "  |  " + rs.getString(3));
        }
        rs = null;
        
        //7
        System.out.println("\r\n\r\nStatement : . Delete the movies whose director or actor did not get any award and delete data from related tables.\r\n");
        System.out.println("Query : SELECT Director.Director_ID \r\n"
        		+ "FROM Director, Director_Award\r\n"
        		+ "WHERE NOT Director.Director_ID = Director_Award.Director_ID\r\n"
        		+ "And Using Loop in JAVA\r\n"
        		+ "Delete FROM Movie WHERE Movie.Director_ID = i ;\r\n");
        rs = stmt.executeQuery("SELECT Director.Director_ID \r\n"
        		+ "FROM Director, Director_Award\r\n"
        		+ "WHERE NOT Director.Director_ID = Director_Award.Director_ID");
        while(rs.next()) {
        	i = rs.getInt(1);
        	stmt2.executeUpdate("Delete \r\n"
        			+ "FROM Movie\r\n"
        			+ "WHERE Movie.Director_ID = "+ i +" ;");
        }
        rs = stmt.executeQuery("SELECT Movie_id, Movie_name, Director_Name\r\n"
        		+ "FROM Director, Movie\r\n"
        		+ "WHERE Movie.Director_ID = Director.Director_ID\r\n"
        		+ "ORDER BY Movie_ID;");
        System.out.println("Movie");
        System.out.println("-----------------------------------------------------------");
        System.out.println("Movie_ID    Movie_Name            Director_Name");
        while(rs.next()) {
        	System.out.println(rs.getInt(1) +"    |    "+rs.getString(2) + "    |    " + rs.getString(3));
        }
        
        
        //8
        System.out.println("Statement : Delete all customers and delete data from related tables");
        System.out.println("Query: Delete FROM Customer;");
        System.out.println("Related data deleted.");
        System.out.println("Customer");
        System.out.println("--------------------------------------");
        System.out.println("Customer_ID         Customer_Name        DOB       gender");
        stmt.executeUpdate("Delete FROM Customer;");
        rs = stmt.executeQuery("SELECT * FROM Customer;");
        while(rs.next()) {
        	System.out.println(rs.getInt(1)+"|"+rs.getString(2)+"|"+rs.getDate(3)+"|"+rs.getString(4) );
        }
        System.out.println("Customer_rate");
        System.out.println("--------------------------------------");
        System.out.println("Customer_ID         Movie_ID        Rate");
        rs = stmt.executeQuery("SELECT * FROM Customer_rate;");
        while(rs.next()) {
        	System.out.println(rs.getInt(1)+"|"+rs.getInt(2)+"|"+rs.getInt(3) );
        }
        
        //9
        System.out.println("Statement : Clear the Database");
        System.out.println("Query : DROP TABLE IF EXISTS actor, actor_award, award, customer, customer_rate, director, director_award, genre, movie, movie_actor, movie_award, movie_genre CASCADE");
        stmt2.executeUpdate("DROP TABLE IF EXISTS actor, actor_award, award, customer, customer_rate, director, director_award, genre, movie, movie_actor, movie_award, movie_genre CASCADE");
        System.out.println("DataBase Cleared.\r\nProgram Finished.");
        
        
        
        
        rs.close();
        stmt2.close();
        stmt.close();

        }catch(SQLException sqle) {
        	sqle.printStackTrace();
        }
        
        conn.close();
    }
}


