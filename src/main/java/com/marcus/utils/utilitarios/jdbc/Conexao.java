package com.marcus.utils.utilitarios.jdbc;

import tech.tablesaw.api.Table;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Conexao {
    static final String URL_BD03 = "xxxx";
    static final String USER_BD03 = "xxxx";
    static final String PASS_BD03 = "xxxx";



    public static void main(String[] args) throws SQLException {


    }

    public static Connection createConnection(String server) throws SQLException {
        if (EnumServer.BD03.getNome().equalsIgnoreCase(server)) {
            return bd03();
        }
        return null;
    }

    public static Statement createStatement(Connection conn) throws SQLException {
        return conn.createStatement();
    }
    public static Connection bd03() throws SQLException {
        Connection conn;
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL_BD03, USER_BD03, PASS_BD03);
            if (conn != null) {
                printMetaDataConnection(conn);
                return conn;
            }
           throw new RuntimeException("Conexão não criada!");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private static void printMetaDataConnection(Connection conn) throws SQLException {
        DatabaseMetaData dm = conn.getMetaData();
        System.out.println("Driver name: " + dm.getDriverName());
        System.out.println("Driver version: " + dm.getDriverVersion());
        System.out.println("Product name: " + dm.getDatabaseProductName());
        System.out.println("Product version: " + dm.getDatabaseProductVersion());
        System.out.println("================================================");
    }

    public static void executeSelect(String server,String select) throws SQLException {
        printTable(createStatement(Objects.requireNonNull(createConnection(server)))
                .executeQuery(select));
    }

    public static void executeSelectAndSaveInFile(String server,String select,String extensionFile) throws SQLException {
        printFile(createStatement(Objects.requireNonNull(createConnection(server))).executeQuery(select),extensionFile);
    }

    public static void printTable(ResultSet resultSet) throws SQLException {
        System.out.println(formatResultSetInTable(resultSet));
    }

    private static String formatResultSetInTable(ResultSet resultSet) throws SQLException {
        return Table.read().db(resultSet).printAll();
    }

    public static void printFile(ResultSet resultSet, String extensionFile) throws SQLException {

        try {
            createFile(resultSet, extensionFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void createFile(ResultSet resultSet, String extensionFile) throws IOException, SQLException {
        String fileSuffix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File file = new File("C:\\TEMP\\resultado"+ fileSuffix+"." + extensionFile);
        if(file.createNewFile()){
            OutputStream os = Files.newOutputStream(file.toPath());
            os.write(formatResultSetInTable(resultSet).getBytes(StandardCharsets.UTF_8));
            os.close();
            System.out.println("File created: " + file.getCanonicalPath() );
        }
    }

    public enum EnumServer{
        BD03("xxx"),
        BD03_INSTANCIA("xxxx")

        ;
        private String nome;


        EnumServer(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }


    }


}
