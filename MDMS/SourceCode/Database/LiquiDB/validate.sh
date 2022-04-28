#!/bin/bash
set +vx

export LIQUIBASE_HOME="/home/jaspervdb/Software/DB/liquibase-3.5.1-bin"

# Valiadte Liquibase scripts
sh $LIQUIBASE_HOME/liquibase --defaultsFile liquibase_DEV.properties --changeLogFile masterlog/db.changelog.master.xml --classpath $LIQUIBASE_HOME/lib/ojdbc7.jar validate
# Execute scripts
#../LiquiBase/liquibase --defaultsFile liquibase_DEV.properties --changeLogFile masterlog/db.changelog.master.xml --classpath ../LiquiBase/lib/ojdbc7.jar update
