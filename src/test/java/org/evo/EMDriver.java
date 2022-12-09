package org.evo;

import org.evomaster.client.java.controller.AuthUtils;
import org.evomaster.client.java.controller.EmbeddedSutController;
import org.evomaster.client.java.controller.InstrumentedSutStarter;
import org.evomaster.client.java.controller.api.dto.AuthenticationDto;
import org.evomaster.client.java.controller.api.dto.SutInfoDto;
import org.evomaster.client.java.controller.api.dto.database.schema.DatabaseType;
import org.evomaster.client.java.controller.db.DbCleaner;
import org.evomaster.client.java.controller.db.SqlScriptRunner;
import org.evomaster.client.java.controller.internal.db.DbSpecification;
import org.evomaster.client.java.controller.problem.ProblemInfo;
import org.evomaster.client.java.controller.problem.RestProblem;
import org.traccar.Main;
import org.traccar.storage.DatabaseStorage;
import org.traccar.web.WebServer;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import static org.traccar.Main.getInjector;
import static org.traccar.Main.getServices;

public class EMDriver extends EmbeddedSutController {

    public static void main(String[] args) {

        int port = 40100;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        EMDriver controller = new EMDriver(port);
        InstrumentedSutStarter starter = new InstrumentedSutStarter(controller);

        starter.start();
    }


    private Connection sqlConnection;
    private final List<String> sqlCommands;
    private List<DbSpecification> dbSpecification;

    public EMDriver() {
        this(40100);
    }

    public EMDriver(int port) {
        setControllerPort(port);

        try (InputStream in = getClass().getResourceAsStream("/data.sql")) {
            sqlCommands = (new SqlScriptRunner()).readCommands(new InputStreamReader(in));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        new File("./target/database.mv.db").delete();
    }

    @Override
    public boolean isSutRunning() {
        return getServices() != null && ((WebServer) getServices().get(1)).getServer().isRunning();
    }

    @Override
    public String getPackagePrefixesToCover() {
        return "org.traccar";
    }

    @Override
    public List<AuthenticationDto> getInfoForAuthentication() {
        return Arrays.asList(
                AuthUtils.getForAuthorizationHeader("admin", "Basic YWRtaW46YWRtaW4=")
        );
    }

    @Override
    public ProblemInfo getProblemInfo() {
        return new RestProblem(
                "swagger.json",
                null
        );
    }

    @Override
    public SutInfoDto.OutputFormat getPreferredOutputFormat() {
        return SutInfoDto.OutputFormat.JAVA_JUNIT_4;
    }

    @Override
    public String startSut() {
        try {
            Main.main(new String[]{});
            sqlConnection = getInjector().getInstance(DatabaseStorage.class).getDataSource().getConnection();
//            Properties connectionProps = new Properties();
//            connectionProps.put("user", "sa");
//            connectionProps.put("password", "");
//            sqlConnection = DriverManager.getConnection("jdbc:h2:./target/database", connectionProps);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        dbSpecification = Arrays.asList(new DbSpecification(DatabaseType.H2, sqlConnection)
                .withDisabledSmartClean());

        resetStateOfSUT();

        return "http://localhost:8082";

    }

    @Override
    public void stopSut() {
        try {
            getServices().get(1).stop();
            sqlConnection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetStateOfSUT() {
        DbCleaner.clearDatabase_H2(sqlConnection);
        SqlScriptRunner.runCommands(sqlConnection, sqlCommands);
    }

    @Override
    public List<DbSpecification> getDbSpecifications() {
//        return dbSpecification;
        return null;
    }
}
