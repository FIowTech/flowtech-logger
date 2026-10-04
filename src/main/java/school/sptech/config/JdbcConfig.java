package school.sptech.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcConfig {
    public static JdbcTemplate getJdbcTemplate() {
        HikariConfig config = new HikariConfig();

        String dbHost = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_DATABASE");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        if(dbHost == null || dbPort == null || dbName == null || dbUser == null || dbPassword == null) {
            System.out.println("[ AVISO ] Não pude ler credenciais de conexão com o Banco de Dados");
        }

        String connectionString = "jdbc:mysql://%s:%s/%s".formatted(dbHost, dbPort, dbName);

        config.setJdbcUrl(connectionString);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        HikariDataSource dataSource = new HikariDataSource(config);
        return new JdbcTemplate(dataSource);
    }
}
