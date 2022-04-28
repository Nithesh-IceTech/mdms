#!/bin/bash
set +vx

export LIQUIBASE_HOME="../LiquiBase"

# Valiadte Liquibase scripts
../LiquiBase/liquibase --defaultsFile liquibase_DEV.properties --changeLogFile masterlog/db.changelog.master.xml --classpath ../LiquiBase/lib/ojdbc7.jar validate
# Execute scripts
#../LiquiBase/liquibase --defaultsFile liquibase_DEV.properties --changeLogFile masterlog/db.changelog.master.xml --classpath ../LiquiBase/lib/ojdbc7.jar update
