package eu.albertomorales.calciteIntro;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatasetComparator {

	/**
	 * @param schema
	 */
	public DatasetComparator(String schema) {
		this.schema = schema;
	}
	
	/**
	 * Compara dos archivos CSV, columna a columna (por nombre, mismo nombre en ambos archivos).
	 * Antes de realizar la comparación, ordena por la(s) columna(s) de la PK, 
	 * si se pasa como argumento.
	 * 
	 * @param fileName1
	 * @param fileName2
	 * @param pkColumns
	 * @param columns
	 * @return
	 */
	public boolean compare(String fileName1, String fileName2, String pkColumns, String[] columns) {
		String orderBy = "";
		if (pkColumns != null) {
			orderBy = " order by "+pkColumns;
		}
		String listaColumnas = "";
		if (columns == null) {
			listaColumnas = " * ";
		} else {
			for (int i = 0; i < columns.length; i++) {
				if (i != 0) {
					listaColumnas+= ",";
				}
				listaColumnas += " "+ columns[i];
			}
		}
		String sentence1 = "select " + listaColumnas + " from " + fileName1 + orderBy;
		String sentence2 = "select " + listaColumnas + " from " + fileName2 + orderBy;
		return compareSentences(sentence1, sentence2);	
	}

	/**
	 * Compara dos archivos CSV, columna a columna (en el orden que se encuentren, primera con primera,
	 * segunda con segunda). Antes de realizar la comparación, ordena por la(s) columna(s) de la PK,
	 * si se pasa como argumento.
	 * 
	 * @param fileName1
	 * @param fileName2
	 * @param pkColumns lista de columnas que conforman la PK, separadas por comma
	 * 
	 * @return true si son idénticos, false en caso contrario. (obviando la ordenación de las filas)
	 */
	public boolean compare(String fileName1, String fileName2, String pkColumns) {
		return compare(fileName1, fileName2, pkColumns, null);
	}
	
	
	/**
	 * Compara dos archivos CSV, columna a columna (en el orden que se encuentren, primera con primera,
	 * segunda con segunda). En el orden que se encuentren las filas.
	 * 
	 * @param fileName1
	 * @param fileName2
	 * 
	 * @return true si son idénticos, false en caso contrario. No se ordenan, es decir, se comparan "tal cual"
	 */
	public boolean compare(String fileName1, String fileName2) {
		return compare(fileName1, fileName2, null);
	}
	
	private boolean compareSentences(String sqlSentence1, String sqlSentence2) {
		Connection connection = null;
		Statement statement1 = null;
		Statement statement2 = null;
		try {
			Properties info = new Properties();
			info.put("model", jsonPath(schema));
			connection = DriverManager.getConnection("jdbc:calcite:", info);
			statement1 = connection.createStatement();
			final ResultSet resultSet1 = statement1.executeQuery(sqlSentence1);
			statement2 = connection.createStatement();
			final ResultSet resultSet2 = statement2.executeQuery(sqlSentence2);	
			return compareResultSets(resultSet1, resultSet2);
		} catch (SQLException sqlE) {
			throw new RuntimeException(sqlE);
		} finally {
			close(connection, statement1);
			close(connection, statement2);
		}	
	}
	
	private boolean compareResultSets(ResultSet resultSet1, ResultSet resultSet2) throws SQLException {
        while (resultSet1.next()) {
            resultSet2.next();
            ResultSetMetaData resultSetMetaData = resultSet1.getMetaData();
            int count = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                if (!resultSet1.getObject(i).equals(resultSet2.getObject(i))) {
                    return false;
                }
            }
        }
        return true;
    }		
	
	private String jsonPath(String model) {
		return resourcePath(model + ".json");
	}

	private String resourcePath(String path) {
		// final URL url = CsvTest.class.getResource("/" + path);
		final URL url = DatasetComparatorTest.class.getResource("/" + path);		
		// URL converts a space to %20, undo that.
		try {
			String s = URLDecoder.decode(url.toString(), "UTF-8");
			if (s.startsWith("file:")) {
				s = s.substring("file:".length());
			}
			return s;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void close(Connection connection, Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// ignore
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// ignore
			}
		}
	}

	private String schema;
	
}
