# Radio2me
Online radio cast

# Mise en place de l'environnement de dev

## Exemple de config server.xml de base pour le serveur tomcat
	<Context docBase="OfficeDrive" path="/OfficeDrive"
	reloadable="true" source="org.eclipse.jst.jee.server:OfficeDrive"
	antiResourceLocking="false">

	<Resource name="jdbc/officedrive" auth="Container"
	type="javax.sql.DataSource" driverClassName="com.mysql.jdbc.Driver"
	url="jdbc:mysql://localhost:3306/officedrive" username="root"
	password="mypass" maxTotal="20" maxIdle="10"
	factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
	testOnBorrow="true" validationQuery="SELECT 1" />
	
	<Environment name="confFolder" description="dossier de configuration du serveur"
	value="C:/dev/java/officeDriveGWT/OfficeDrive/conf" type="java.lang.String" override="false" />

	<Environment description="" override="false"
	name="jdbc.officedrive.serverClass" type="java.lang.String"
	value="officedrive.server.persistence.MySqlPersistence" />
	</Context>

## Config DebugConfiguration Eclipse du Server Tomcat
-Dcatalina.base="C:\dev\java\officeDriveGWT\.metadata\.plugins\org.eclipse.wst.server.core\tmp0" -Dcatalina.home="C:\dev\tomcat-8.5.29" -Dwtp.deploy="C:\dev\java\officeDriveGWT\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps" -Djava.endorsed.dirs="C:\dev\tomcat-8.5.29\endorsed"

## Onglet servers Eclipse
ajouter le module de l'application avec un clique droit sur "Tomcat v8......" -> Add and Remove...

## Project Facets activé
- Dynamic Web Module (mettre war à la place de WebContent)
- Java
- JavaScript

## Config DebugConfiguration Eclipse du CodeServer
MainClass : com.google.gwt.dev.codeserver.CodeServer
Arguments : officedrive.OfficeDrive -style PRETTY

## Config DebugConfiguration SDBG
URL : http://localhost:8080/OfficeDrive?username=admin&adminMode=true
