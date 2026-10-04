package school.sptech.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.model.Empresa;

import java.util.List;

public class EmpresaDAO {
    private final JdbcTemplate connection;

    public EmpresaDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public List<Empresa> getAll() {
        String sql = "SELECT * FROM empresa";
        return connection.query(sql, new BeanPropertyRowMapper<>(Empresa.class));
    }

    public Empresa getById(Integer idEmpresa) {
        String sql = "SELECT * FROM empresa WHERE id_empresa = ?";
        return connection.queryForObject(sql, new BeanPropertyRowMapper<>(Empresa.class), idEmpresa);
    }
}
