# Radio2me
Online radio cast

#exemple de config de base pour le serveur tomcat
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

