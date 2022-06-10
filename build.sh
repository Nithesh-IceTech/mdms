#!/bin/bash

set +vx

WORKSPACE=`pwd`/MDMS
echo $WORKSPACE

if [ ! -d ${WORKSPACE}/SourceCode/Java/Deploy ]; then
  mkdir -p ${WORKSPACE}/SourceCode/Java/Deploy
fi

cd ${WORKSPACE}/SourceCode/Java/Deploy
pwd

echo "rm -Rf *.war"
ls -l *.war
rm -Rf *.war

echo "BEGIN Vaadin-Components"
echo " "
cd ${WORKSPACE}/../vaadin-components/vcomponents
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true

echo " "
echo "END vaadin-components"
echo " "
echo "BEGIN pjtk"
echo " "
cd ${WORKSPACE}/../pjtk/
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END pjtk"


echo "BEGIN toolkit"
echo " "
cd ${WORKSPACE}/../toolkit/
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END toolkit"


echo "BEGIN toolkit-db"
echo " "
cd ${WORKSPACE}/../toolkit-db
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END toolkit-db"
echo " "


echo "BEGIN toolkit-dao"
echo " "
cd ${WORKSPACE}/../toolkit-dao
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END toolkit-dao"
echo " "


echo "BEGIN ua-util"
echo " "
cd ${WORKSPACE}/../ua-util
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END ua-util"
echo " "


echo "BEGIN ua-util-ee"
echo " "
cd ${WORKSPACE}/../ua-util-ee
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END ua-util-ee"
echo " "


echo "BEGIN toolkit-ee"
echo " "
cd ${WORKSPACE}/../toolkit-ee
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END toolkit-ee"
echo " "


echo "BEGIN toolkit-locale"
echo " "
cd ${WORKSPACE}/../toolkit-locale
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END toolkit-locale"
echo " "


echo "BEGIN ice-lookup"
echo " "
cd ${WORKSPACE}/../ice-lookup
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END ice-lookup"
echo " "

echo "BEGIN canvas"
echo " "
cd ${WORKSPACE}/../canvas
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END canvas"
echo " "


echo "BEGIN toolkit-crud"
echo " "
cd ${WORKSPACE}/../toolkit-crud
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END toolkit-crud"
echo " "


echo "BEGIN ice-fss-dao"
echo " "
cd ${WORKSPACE}/SourceCode/Java/ICEFSS/SourceCode/Java/ice-fss-dao
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END ice-fss-dao"
echo " "


echo "BEGIN dm-dao"
echo " "
cd ${WORKSPACE}/SourceCode/Java/DM/SourceCode/Java/dm-components/dm-dao
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true
echo " "
echo "END dm-dao"
echo " "


echo "BEGIN MDMS"
echo " "
cd ${WORKSPACE}/
pwd
mvn -B clean
mvn -B versions:set -DnewVersion=${RELEASE}.${BUILD_NUMBER}
mvn -B install -s ${WORKSPACE}/settings.xml -DskipTests=true -f SourceCode/Java/pom.xml
echo " "
echo "END dm-dao"
echo " "



