import java.sql.*;

void main() throws Exception {
    IO.println("loading h2 driver...");
    Class.forName("org.h2.Driver");
    IO.println("h2 driver loaded!");

    IO.println("establishing connection...");
    try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
        IO.println("connection established!");

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
                IO.println("----------------------------------------");
                while (resultSet.next()) {
                    int someInt = resultSet.getInt("...column name...");
                    String someString = resultSet.getString("...column name...");
                    IO.println(someInt + " " + someString);
                }
                IO.println("----------------------------------------");
            }
        }
    }
}
