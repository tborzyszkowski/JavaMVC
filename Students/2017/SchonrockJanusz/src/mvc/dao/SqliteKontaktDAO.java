package mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;

import mvc.model.Kontakt;

public class SqliteKontaktDAO implements IKontaktDAO {

    private static final Logger logger = Logger.getLogger(SqliteKontaktDAO.class);

    private static final String CREATE_QUERY_KONTAKT = "INSERT INTO kontakt(imie, nazwisko, adres, kraj) VALUES (?,?,?,?)";
    private static final String CREATE_QUERY_NUMER = "INSERT INTO numery(kontakt_id, numer, operator) VALUES(?,?,?)";
    private static final String READ_QUERY = "SELECT id, imie, nazwisko, adres, kraj, numer, operator FROM (kontakt JOIN numery ON kontakt.id = numery.kontakt_id) WHERE id = ?";
    private static final String READ_QUERY_ALL = "SELECT id, imie, nazwisko, adres, kraj, numer, operator FROM (kontakt JOIN numery ON kontakt.id = numery.kontakt_id) GROUP BY id";
    private static final String READ_QUERY_NUMERY = "SELECT idN, kontakt_id, numer, operator FROM numery WHERE kontakt_id = ?";
    private static final String UPDATE_QUERY_KONTAKT = "UPDATE kontakt SET imie=?, nazwisko=?, adres=?, kraj=? WHERE id=?";
    private static final String UPDATE_QUERY_NUMER_ID = "UPDATE numery SET numer=?, operator=? WHERE idN=?";
    private static final String DELETE_QUERY_KONTAKT = "DELETE FROM kontakt WHERE id=?";
    private static final String DELETE_QUERY_NUMER = "DELETE FROM numery WHERE kontakt_id = ?";
    private static final String DELETE_QUERY_NUMER_ID = "DELETE FROM numery WHERE idN = ?";
    private static final String CREATE_TABLE_KONTAKT = "CREATE TABLE kontakt(id INTEGER PRIMARY KEY AUTOINCREMENT, imie CHAR(20), nazwisko CHAR(20), adres CHAR(20), kraj CHAR(20))";
    private static final String CREATE_TABLE_NUMER = "CREATE TABLE numery(idN INTEGER PRIMARY KEY AUTOINCREMENT, kontakt_id INT, numer INT, operator CHAR(20), CONSTRAINT kontakt_FK FOREIGN KEY (kontakt_id) REFERENCES kontakt(id) ON DELETE CASCADE)";
    private static final String READ_ID = "SELECT max(id) FROM kontakt";

    @Override
    public int create(Kontakt kontakt) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        ResultSet result_id = null;

        try {
            conn = SqliteDAOFactory.createConnection();
            preparedStatement = conn.prepareStatement(CREATE_QUERY_KONTAKT, Statement.RETURN_GENERATED_KEYS);

            //INSERT TABELA KONTAKT
            preparedStatement.setString(1, kontakt.getImie());
            preparedStatement.setString(2, kontakt.getNazwisko());
            preparedStatement.setString(3, kontakt.getAdres());
            preparedStatement.setString(4, kontakt.getKraj());
            preparedStatement.execute();

            // ODCZYT Z MAX(ID)
            preparedStatement = conn.prepareStatement(READ_ID);
            preparedStatement.execute();
            result_id = preparedStatement.getResultSet();
            int ID_kontaktu = result_id.getInt(1);

            // INSERT TABELA NUMER
            preparedStatement = conn.prepareStatement(CREATE_QUERY_NUMER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, ID_kontaktu);
            preparedStatement.setInt(2, kontakt.getNumer());
            preparedStatement.setString(3, kontakt.getOperator());
            preparedStatement.execute();

            result = preparedStatement.getGeneratedKeys();

            if (result.next() && result != null) {
                return result.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                logger.error(rse.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return -1;
    }

    @Override
    public Kontakt read(int id) {
        Kontakt kontakt = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;

        try {
            conn = SqliteDAOFactory.createConnection();
            preparedStatement = conn.prepareStatement(READ_QUERY);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();

            if (result.next() && result != null) {
                kontakt = new Kontakt(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getInt(6), result.getString(7));
            } else {
                // TODO
                logger.info("Brak kontaktu o ID = " + id);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                logger.error(rse.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return kontakt;
    }

    @Override
    public List<Kontakt> readAll() {
        List<Kontakt> lista_kontaktow = new ArrayList<Kontakt>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            conn = SqliteDAOFactory.createConnection();
            preparedStatement = conn.prepareStatement(READ_QUERY_ALL);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();

            if (result != null) {
                while (result.next())
                    lista_kontaktow.add(new Kontakt(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getInt(6), result.getString(7)));
            } else {
                // TODO
                logger.info("Brak kontaktow");

            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                logger.error(rse.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return lista_kontaktow;
    }

    @Override
    public boolean update(Kontakt kontakt) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = SqliteDAOFactory.createConnection();
            preparedStatement = conn.prepareStatement(UPDATE_QUERY_KONTAKT);
            preparedStatement.setString(1, kontakt.getImie());
            preparedStatement.setString(2, kontakt.getNazwisko());
            preparedStatement.setString(3, kontakt.getAdres());
            preparedStatement.setString(4, kontakt.getKraj());
            preparedStatement.setInt(5, kontakt.getId());
            preparedStatement.execute();
/*
            preparedStatement = conn.prepareStatement(UPDATE_QUERY_NUMER);
            preparedStatement.setInt(1, kontakt.getNumer());
            preparedStatement.setString(2, kontakt.getOperator());
            preparedStatement.setInt(3, kontakt.getId());
            preparedStatement.execute();
*/
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = SqliteDAOFactory.createConnection();
            preparedStatement = conn.prepareStatement(DELETE_QUERY_KONTAKT);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            preparedStatement = conn.prepareStatement(DELETE_QUERY_NUMER);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return false;
    }

    @Override
    public void createTable() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = SqliteDAOFactory.createConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(CREATE_TABLE_KONTAKT);
            stmt.executeUpdate(CREATE_TABLE_NUMER);

        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }


    }


    @Override
    public int createNumer(Kontakt numer) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;

        try {
            conn = SqliteDAOFactory.createConnection();

            // INSERT TABELA NUMER
            preparedStatement = conn.prepareStatement(CREATE_QUERY_NUMER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, numer.getKontakt_id());
            preparedStatement.setInt(2, numer.getNumer());
            preparedStatement.setString(3, numer.getOperator());
            preparedStatement.execute();

            result = preparedStatement.getGeneratedKeys();

            if (result.next() && result != null) {
                return result.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                logger.error(rse.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return -1;
    }

    @Override
    public List<Kontakt> readNumery(int id) {
        List<Kontakt> lista_numerow = new ArrayList<Kontakt>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            conn = SqliteDAOFactory.createConnection();
            preparedStatement = conn.prepareStatement(READ_QUERY_NUMERY);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();

            if (result != null) {
                while (result.next())
                    lista_numerow.add(new Kontakt(result.getInt(1), result.getInt(2), result.getInt(3), result.getString(4)));
            } else {
                // TODO
                logger.info("Brak numerow");

            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                logger.error(rse.getMessage());
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return lista_numerow;
    }

    @Override
    public boolean deleteNumber(int id) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = SqliteDAOFactory.createConnection();
            preparedStatement = conn.prepareStatement(DELETE_QUERY_NUMER_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean updateNumber(Kontakt numer) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = SqliteDAOFactory.createConnection();

            preparedStatement = conn.prepareStatement(UPDATE_QUERY_NUMER_ID);
            preparedStatement.setInt(3, numer.getNumer_id());
            preparedStatement.setInt(1, numer.getNumer());
            preparedStatement.setString(2, numer.getOperator());

            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                logger.error(sse.getMessage());
            }
            try {
                conn.close();
            } catch (Exception cse) {
                logger.error(cse.getMessage());
            }
        }
        return false;
    }

}






