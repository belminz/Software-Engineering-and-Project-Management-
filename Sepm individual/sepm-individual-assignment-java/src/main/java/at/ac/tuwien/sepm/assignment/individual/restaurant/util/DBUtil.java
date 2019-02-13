package at.ac.tuwien.sepm.assignment.individual.restaurant.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

        private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

        private static Connection conn = null;


        public static Connection getConnection(){
            if (conn == null){
                conn = openConnection();
            }
            return conn;
        }

        private static Connection openConnection(){
            Connection connection = null;
            try {
                Class.forName("org.h2.Driver");
            } catch (ClassNotFoundException e) {
                LOG.error("org.h2.Driver");
            }
            try {
                connection = DriverManager.getConnection("jdbc:h2:~/sepm;INIT=RUNSCRIPT FROM 'classpath:create.sql'","sa","");
            } catch (SQLException e) {
            LOG.error(e.getMessage());
            }

            return connection;
        }

        public static void closeConnection(){
            try {
                if (conn != null) {
                    conn.close();
                    LOG.debug("Connection closed");
                }
            } catch (SQLException e) {
                LOG.error("closing DataBase connection");

            }
        }
    }
