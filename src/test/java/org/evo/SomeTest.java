package org.evo;

import org.evomaster.client.java.controller.db.DbCleaner;
import org.evomaster.client.java.controller.db.SqlScriptRunner;
import org.evomaster.client.java.controller.internal.db.DbSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import org.traccar.Main;
import org.traccar.storage.DatabaseStorage;
import org.traccar.web.WebServer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.List;

import static org.traccar.Main.getInjector;
import static org.traccar.Main.getServices;

public class SomeTest {

    private Connection sqlConnection;
    private static List<String> sqlCommands;
    private List<DbSpecification> dbSpecification;

    @BeforeClass
    public static void setUp() {
        try (InputStream in = SomeTest.class.getResourceAsStream("/data.sql")) {
            sqlCommands = (new SqlScriptRunner()).readCommands(new InputStreamReader(in));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void someTest() throws Exception {
        System.out.println("STATE OF THE SERVER: " + (getServices() != null && ((WebServer) getServices().get(1)).getServer().isRunning()));

        Main.main(new String[]{});
        sqlConnection = getInjector().getInstance(DatabaseStorage.class).getDataSource().getConnection();

        System.out.println("STATE OF THE SERVER: " + (getServices() != null && ((WebServer) getServices().get(1)).getServer().isRunning()));

        System.out.println("START");
        Thread.sleep(10000);

        DbCleaner.clearDatabase_H2(sqlConnection);
        SqlScriptRunner.runCommands(sqlConnection, sqlCommands);

        System.out.println("RESET");
        Thread.sleep(10000);

        DbCleaner.clearDatabase_H2(sqlConnection);
        SqlScriptRunner.runCommands(sqlConnection, sqlCommands);

        System.out.println("RESET");
        Thread.sleep(10000);

        getServices().get(1).stop();
        sqlConnection.close();

        System.out.println("STATE OF THE SERVER: " + getServices() != null && ((WebServer) getServices().get(1)).getServer().isRunning());

        System.out.println("STOP");
        Thread.sleep(10000);
    }
}
