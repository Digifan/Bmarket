package db;

import entity.Bicycle;
import entity.Customer;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DatabaseOperations {
    public static final String DROP_TABLE_IF_EXISTS_QUERY = "drop table if exists %s;\n";
    public static final String CREATE_TABLE_QUERY_PART = "create table %s (";
    public static final String NOT_NULL = "NOT NULL";
    public static final String PRIMARY_KEY = "PRIMARY KEY";
    // MySQLite feature for primary key
//    public static final String AUTOINCREMENT = "AUTO INCREMENT";

    public static final String ID_COLUMN_NAME = "id";
    public static final String FULL_NAME_COLUMN_NAME = "full_name";
    public static final String CUSTOMER_TABLE_NAME = "customer";
    public static final String BICYCLE_TABLE_NAME = "bicycle";
    public static final String ORDERS_TABLE_NAME = "orders";
    public static final String BUYERS_TABLE_NAME = "buyers";
    public static final String SELECT_FROM_CUSTOMER_QUERY = "SELECT * FROM customer";
    public static final String SELECT_FROM_BICYCLE_QUERY = "SELECT * FROM bicycle";
    public static final String SELECT_FROM_ORDERS_QUERY = "SELECT * FROM orders";
    public static final String SELECT_FROM_BUYERS_QUERY = "SELECT * FROM buyers";
    // SQLite
//    public static final String CONNECTION_URL_DRIVER = "jdbc:sqlite:sample.db";
    // Postgres
    //public static final String CONNECTION_URL_DRIVER =
      // "jdbc:postgresql://localhost/admin?user=admin&password=postgrespw&ssl=false";
    public static final String CREATE_TABLE_CLOSE_BRACKET = " );";
    public static final String INSERT_INTO_PATTERN = "insert into %s(";
    public static final String END_BRACKET = ")";
    public static final String VALUES_PATTERN = " values(";
    public static final String DELIMITER = ", ";
    private static final String SINGLE_QUOTE = "'";


    public static void fillCustomerTableData(Statement statement, HashMap<Integer, Customer> customers) throws SQLException {

        for (Map.Entry<Integer, Customer> customer : customers.entrySet()) {
            String id = customer.getKey().toString();
            List<Field> existedFields = getExistedFields(Customer.class, customer.getValue());
            String fieldsString = existedFields.stream()
                    .map(Field::getName)
                    .map(DatabaseOperations::getFieldName)
                    .collect(Collectors.joining(", "));
            String valuesString = existedFields.stream()
                    .map(x -> getValueFromField(x, customer.getValue()))
                    .collect(Collectors.joining(", "));
            statement.executeUpdate(insertQueryString(CUSTOMER_TABLE_NAME, "id, "+fieldsString, id+", "+valuesString));
        }
    }
    public static void fillBicycleTableData(Statement statement, HashMap<Integer, Bicycle> list) throws SQLException {

        for (Map.Entry<Integer, Bicycle> item : list.entrySet()) {
            String id = item.getKey().toString();
            List<Field> existedFields = getExistedFields(Bicycle.class, item.getValue());
            String fieldsString = existedFields.stream()
                    .map(Field::getName)
                    .map(DatabaseOperations::getFieldName)
                    .collect(Collectors.joining(", "));
            String valuesString = existedFields.stream()
                    .map(x -> getValueFromField(x, item.getValue()))
                    .collect(Collectors.joining(", "));
            statement.executeUpdate(insertQueryString(BICYCLE_TABLE_NAME, "id, "+fieldsString, id+", "+valuesString));
        }
    }
    public static void fillMapToTableData(Connection conn, @NonNull HashMap<Integer,  HashSet<Integer>> list, String tableName) throws SQLException {

        for (Map.Entry<Integer, HashSet<Integer>> item : list.entrySet()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into " + tableName +" (id, id_list) " + "VALUES (?, ?)");
            int id = item.getKey();
            pstmt.setInt(1, id);
            Array id_list = conn.createArrayOf("INTEGER", item.getValue().toArray());
            pstmt.setArray(2, id_list);
            pstmt.executeUpdate();
        }
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private static <T> String getValueFromField(@NonNull Field field, T object) {
        String result = "";
        try {
            result = new StringBuilder().append(SINGLE_QUOTE)
                    .append(field.get(object))
                    .append(SINGLE_QUOTE)
                    .toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("SameParameterValue")
    private static <T> List<Field> getExistedFields(Class<T> anyClass, T object) {
        return Arrays.stream(anyClass.getDeclaredFields())
                .filter(x -> {
                    boolean state = true;
                    try {
                        x.setAccessible(true);
                        state = Objects.nonNull(x.get(object));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return state;
                }).toList();


    }

    @SuppressWarnings({"StringBufferReplaceableByString", "SameParameterValue"})
    private static String insertQueryString(String tableName,
                                                String fields,
                                                String values
                                                ) {
        return new StringBuilder()
                .append(String.format(INSERT_INTO_PATTERN, tableName))
                .append(fields)
                .append(END_BRACKET)
                .append(VALUES_PATTERN)
                .append(values)
                .append(END_BRACKET)
                .toString();
    }
    private static String getFieldName(String s) {
        return s.substring(s.indexOf(".") + 1);
    }

    @SuppressWarnings("SameParameterValue")
    /*public static List<Person> getPersonData(String resourceFileName) throws URISyntaxException {
         return getDataFromCsv(resourceFileName, Person::parsePerson);
    }*/

    public static void createTable(Statement statement, String query) throws SQLException {
        statement.executeUpdate(query);
    }

    @SuppressWarnings("SameParameterValue")
//    public static Statement makeConnection(String connectionUrl) throws SQLException {
//        return Optional.of(DriverManager.getConnection(connectionUrl).createStatement())
//                .orElseThrow(() -> new SQLException(SQL_CONNECTION_ERROR));
//    }

    public static @NotNull List<String> browseData(@NotNull ResultSet resultSet, int endIndex) throws SQLException {
        List<String> result = new ArrayList<>();
        while (resultSet.next()) {

            result.add(IntStream.range(1, endIndex)
                    .mapToObj(columnIndex -> getResultSet(resultSet, columnIndex))
                    .map(String::valueOf)
                    .collect(Collectors.joining(DELIMITER)));
        }
        return result;
    }

    @Nullable
    private static String getResultSet(ResultSet resultSet, Integer columnIndex) {
        String result = "";
        try {
            result = resultSet.getString(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<QueryColumnDataStructure> createCustomerTableRequest() {
        return List.of(
                new QueryColumnDataStructure(
                        ID_COLUMN_NAME,
                        JDBCType.VARCHAR,
                        Optional.of(List.of(/*AUTOINCREMENT, */NOT_NULL, PRIMARY_KEY))),
                new QueryColumnDataStructure(
                        FULL_NAME_COLUMN_NAME,
                        JDBCType.VARCHAR,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "phone",
                        JDBCType.VARCHAR,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "address",
                        JDBCType.VARCHAR,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "moneyAmount",
                        JDBCType.REAL,
                        Optional.empty())) ;

    }
    public static List<QueryColumnDataStructure> createBicycleTableRequest() {
        return List.of(
                new QueryColumnDataStructure(
                        ID_COLUMN_NAME,
                        JDBCType.VARCHAR,
                        Optional.of(List.of(/*AUTOINCREMENT, */NOT_NULL, PRIMARY_KEY))),
                new QueryColumnDataStructure(
                        "brand",
                        JDBCType.VARCHAR,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "model",
                        JDBCType.VARCHAR,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "purpose",
                        JDBCType.VARCHAR,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "type",
                        JDBCType.VARCHAR,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "break_type",
                        JDBCType.VARCHAR,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "frame_material",
                        JDBCType.VARCHAR,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "frame_size",
                        JDBCType.REAL,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "wheel_size",
                        JDBCType.REAL,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "price",
                        JDBCType.REAL,
                        Optional.empty()),
                new QueryColumnDataStructure(
                        "speed_number",
                        JDBCType.INTEGER,
                        Optional.empty())) ;

    }
    public static List<QueryColumnDataStructure> createMapToTableRequest() {
        return List.of(
                new QueryColumnDataStructure(
                        ID_COLUMN_NAME,
                        JDBCType.INTEGER,
                        Optional.of(List.of(/*AUTOINCREMENT, */NOT_NULL, PRIMARY_KEY))),
                new QueryColumnDataStructure(
                        "id_list",
                        JDBCType.VARCHAR,
                        Optional.empty())) ;
    }

    @SuppressWarnings({"StringBufferReplaceableByString", "SameParameterValue"})
    public static String generateTableQuery(String tableName,
                                             List<QueryColumnDataStructure> queryColumnDescription) {
        return new StringBuilder()
                .append(String.format(DROP_TABLE_IF_EXISTS_QUERY, tableName))
                .append(String.format(CREATE_TABLE_QUERY_PART, tableName))
                .append(queryColumnDescription.stream()
                        .map(QueryColumnDataStructure::toString)
                        .collect(Collectors.joining(DELIMITER)))
                .append(CREATE_TABLE_CLOSE_BRACKET)
                .toString();
    }
}
