package liquibase.change;

import liquibase.database.Database;
import liquibase.database.sql.RawSqlStatement;
import liquibase.database.sql.SqlStatement;
import liquibase.database.structure.DatabaseObject;
import liquibase.database.structure.Table;
import liquibase.exception.UnsupportedChangeException;
import liquibase.util.SqlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Drops an existing table.
 */
public class DropTableChange extends AbstractChange {

    private String tableName;
    private Boolean cascadeConstraints;

    public DropTableChange() {
        super("dropTable", "Drop Table");
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Boolean isCascadeConstraints() {
        return cascadeConstraints;
    }

    public void setCascadeConstraints(Boolean cascadeConstraints) {
        this.cascadeConstraints = cascadeConstraints;
    }

    public SqlStatement[] generateStatements(Database database) throws UnsupportedChangeException {
        StringBuffer buffer = new StringBuffer(31);
        buffer.append("DROP TABLE ").append(SqlUtil.escapeTableName(getTableName(),database));
        if (isCascadeConstraints() != null && isCascadeConstraints()) {
            buffer.append(" CASCADE CONSTRAINTS");
        }
        return new SqlStatement[]{new RawSqlStatement(buffer.toString())};
    }

    public String getConfirmationMessage() {
        return "Table " + getTableName() + " dropped";
    }

    public Element createNode(Document currentChangeLogFileDOM) {
        Element element = currentChangeLogFileDOM.createElement("dropTable");
        element.setAttribute("tableName", getTableName());

        if (isCascadeConstraints() != null) {
            element.setAttribute("cascadeConstraints", isCascadeConstraints().toString());
        }

        return element;
    }

    public Set<DatabaseObject> getAffectedDatabaseObjects() {
        Table dbObject = new Table();
        dbObject.setName(tableName);

        return new HashSet<DatabaseObject>(Arrays.asList(dbObject));
    }

}
