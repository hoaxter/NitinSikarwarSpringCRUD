package com.nitin.enrollhub.persistence;

import com.nitin.enrollhub.domain.Enrollee;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Talks to PostgreSQL using {@link NamedParameterJdbcTemplate}.
 * Named parameters make queries more readable than positional '?' markers.
 *
 * @author Nitin
 */
@Repository
public class JdbcEnrolleeLedger implements EnrolleeLedger {

    private final NamedParameterJdbcTemplate namedJdbc;

    public JdbcEnrolleeLedger(NamedParameterJdbcTemplate namedJdbc) {
        this.namedJdbc = namedJdbc;
    }

    /* ---- row mapper helper ---- */

    private Enrollee mapRow(ResultSet rs, int position) throws SQLException {
        return new Enrollee(
                rs.getLong("enrollee_id"),
                rs.getString("full_name"),
                rs.getString("email_address"),
                rs.getString("program")
        );
    }

    /* ---- CRUD ---- */

    @Override
    public Enrollee persist(Enrollee enrollee) {
        String sql = "INSERT INTO enrollees (full_name, email_address, program) " +
                     "VALUES (:fullName, :emailAddress, :program)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("fullName", enrollee.getFullName())
                .addValue("emailAddress", enrollee.getEmailAddress())
                .addValue("program", enrollee.getProgram());

        KeyHolder holder = new GeneratedKeyHolder();
        namedJdbc.update(sql, params, holder, new String[]{"enrollee_id"});

        Number generatedKey = holder.getKey();
        if (generatedKey != null) {
            enrollee.setEnrolleeId(generatedKey.longValue());
        }
        return enrollee;
    }

    @Override
    public List<Enrollee> retrieveAll() {
        String sql = "SELECT enrollee_id, full_name, email_address, program " +
                     "FROM enrollees ORDER BY enrollee_id";
        return namedJdbc.query(sql, this::mapRow);
    }

    @Override
    public Optional<Enrollee> retrieveById(long enrolleeId) {
        String sql = "SELECT enrollee_id, full_name, email_address, program " +
                     "FROM enrollees WHERE enrollee_id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource("id", enrolleeId);
        List<Enrollee> found = namedJdbc.query(sql, params, this::mapRow);
        return found.stream().findFirst();
    }

    @Override
    public Enrollee overwrite(long enrolleeId, Enrollee enrollee) {
        String sql = "UPDATE enrollees SET full_name = :fullName, " +
                     "email_address = :emailAddress, program = :program " +
                     "WHERE enrollee_id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("fullName", enrollee.getFullName())
                .addValue("emailAddress", enrollee.getEmailAddress())
                .addValue("program", enrollee.getProgram())
                .addValue("id", enrolleeId);

        namedJdbc.update(sql, params);
        enrollee.setEnrolleeId(enrolleeId);
        return enrollee;
    }

    @Override
    public int erase(long enrolleeId) {
        String sql = "DELETE FROM enrollees WHERE enrollee_id = :id";
        return namedJdbc.update(sql, new MapSqlParameterSource("id", enrolleeId));
    }
}
