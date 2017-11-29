package pl.edu.pwr.wordnetloom.ui.application.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
public class DbMigrator {

    private final Logger log = Logger.getLogger(DbMigrator.class.getName());

    @Resource(lookup = "java:/datasources/wordnet")
    private DataSource dataSource;

    @PostConstruct
    private void onStartup() {
        if (dataSource == null) {
            log.severe("No datasource found to execute the db migrations!");
            throw new EJBException("No datasource found to execute the db migrations!");
        }

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        for (MigrationInfo i : flyway.info().all()) {
            log.log(Level.INFO, "Migrate task: {0} : {1} from file: {2}", new Object[]{i.getVersion(), i.getDescription(), i.getScript()});
        }
        flyway.migrate();

        // aby ustawił kierunki relacji z pliku, odkomentować i ustawić ścieżkę do pliku disp_relations.cfg
//        try {
//            TempRelationsDirectionsUpdater.run("/home/rdyszlewski/Projekty/wordnetloom3/WordnetLoom/wordnetloom-client/src/main/resources/disp_relations.cfg", dataSource.getConnection());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
