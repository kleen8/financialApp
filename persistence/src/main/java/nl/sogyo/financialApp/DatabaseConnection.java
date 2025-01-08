package nl.sogyo.financialApp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {


    private static HikariDataSource dataSource;

    static {
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");
        String dbName = System.getenv("DB_NAME");
        String dbHost = System.getenv("DB_HOST");
        String url = String.format("jdbc:postgresql://%s:5432/%s", dbHost, dbName);        
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(180000);
        config.setConnectionTimeout(10000);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closePool(){
        if (dataSource != null){
            dataSource.close();
        }
    }
    
}
