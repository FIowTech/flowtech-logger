package school.sptech.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.model.Embarcado;

import java.util.List;

public class EmbarcadoDAO {
    private final JdbcTemplate connection;

    public EmbarcadoDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public List<Embarcado> getAll() {
        String sql = "SELECT * FROM embarcado";
        return connection.query(sql, new BeanPropertyRowMapper<>(Embarcado.class));
    }

    public Embarcado getByIdAndEmpresaId(Integer idEmbarcado, Integer empresaId) {
        String sql = "SELECT * FROM embarcado WHERE id_embarcado = ? AND empresa_id = ?";
        return connection.queryForObject(sql, new BeanPropertyRowMapper<>(Embarcado.class), idEmbarcado, empresaId);
    }

    public Embarcado getByEnderecoMac(String enderecoMac) {
        String sql = "SELECT * FROM embarcado WHERE endereco_mac = ?";
        return connection.queryForObject(sql, new BeanPropertyRowMapper<>(Embarcado.class), enderecoMac);
    }
}
