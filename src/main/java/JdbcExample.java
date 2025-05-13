import java.sql.*;

public class JdbcExample {
    public static void main(String[] args) throws Exception {
        System.out.println("loading h2 driver...");
        Class.forName("org.h2.Driver");
        System.out.println("h2 driver loaded!");

        System.out.println("establishing connection...");
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
            System.out.println("connection established!");

            try (Statement statement = connection.createStatement()) {
                // TODO Erzeuge Tabelle(n)
                statement.execute("create table ...");

                // TODO Befülle Tabelle(n)
                statement.execute("insert into ...");
                statement.execute("insert into ...");
                statement.execute("insert into ...");

                // TODO Stelle eine Anfrage an die Datenbank
                try (ResultSet resultSet = statement.executeQuery("select ... from ... where ...")) {
                    // TODO Zeige Ergebnisse an
                    System.out.println("----------------------------------------");
                    while (resultSet.next()) {
                        int someInt = resultSet.getInt("...column name...");
                        String someString = resultSet.getString("...column name...");
                        System.out.println(someInt + " " + someString);
                    }
                    System.out.println("----------------------------------------");
                }
            }
        }
    }
}
