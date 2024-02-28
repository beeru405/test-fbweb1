# fbweb-test
Used Software
------------
ubuntu 22.04
jenkins
Maven
MYSQL
Tomcat
---------------

#### Git commands
```
git clone <repo_url>
git add .
git commit -m "message"
git push origin -u <branch_name>
git branch 			#To check barnch name
git checkout -b <branch_name> 	#To create banch name
git checkout <branch-name> 	#To switch branch
```

#### Jenkins Installations in ubuntu22.04:

```
sudo apt update
sudo apt install openjdk-11-jdk -y
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key |sudo gpg --dearmor -o /usr/share/keyrings/jenkins.gpg
sudo sh -c 'echo deb [trusted=yes] https://pkg.jenkins.io/debian binary/ > /etc/apt/sources.list.d/jenkins.list'
sudo apt update
sudo apt install jenkins
sudo systemctl status jenkins
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```
Install plugins: Maven, deploy to container

Maven installation
-----------------
```
sudo apt install maven -y
```
configure JAVA_HOME

Configure MAVEN_HOME

Go to jenkins>manage Jenkins> Tools> Maven

Go to jenkins>manage Jenkins> Tools> jdk

Tomcat Installation:
------------------
```
wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.86/bin/apache-tomcat-9.0.86.tar.gz
tar -xvzf /opt/apache-tomcat-<version>.tar.gz
```
give executing permissions to startup.sh and shutdown.sh which are under bin. 
   ```sh
   chmod +x /opt/apache-tomcat-<version>/bin/startup.sh 
   chmod +x /opt/apache-tomcat-<version>/bin/shutdown.sh

#### Check point :
Access tomcat application from browser on port 8090  
 - http://<Public_IP>:8090

1. now application is accessible on port 8090. but tomcat application doesnt allow to login from browser. changing a default parameter in context.xml does address this issue
   ```sh
   #search for context.xml
   find / -name context.xml
   ```
1. above command gives 3 context.xml files. comment (<!-- & -->) `Value ClassName` field on files which are under webapp directory. 
After that restart tomcat services to effect these changes. 
At the time of writing this lecture below 2 files are updated. 
   ```sh 
   /opt/tomcat/webapps/host-manager/META-INF/context.xml
   /opt/tomcat/webapps/manager/META-INF/context.xml
   
   # Restart tomcat services
   ```
1. Update users information in the tomcat-users.xml file
goto tomcat home directory and Add below users to conf/tomcat-users.xml file
   ```sh
	<role rolename="manager-gui"/>
	<role rolename="manager-script"/>
	<user username="admin" password="admin" roles="manager-gui, manager-script"/>
	<user username="deployer" password="deployer" roles="manager-script"/>
	<user username="tomcat" password="tomcat" roles="manager-gui"/>
   ```
1. Restart serivce and try to login to tomcat application from the browser. This time it should be Successful

Now Setup tomcat in Jenkins
Go to mange jenkins>credentials>system

-------
Install MySQL:
--------------
```
sudo apt update
sudo apt install mysql-server -y
sudo systemctl start mysql.service
sudo mysql_secure_installation
```
mysql user creation
```
CREATE USER 'mysql'@'%' IDENTIFIED BY 'mysql' ;
```
Grant All Privileges:
```
GRANT ALL PRIVILEGES ON *.* TO 'mysql'@'%' WITH GRANT OPTION;
```
Flush Privileges:
```
FLUSH PRIVILEGES;
```
Create database 
```
create database myDB;
```
###### Creating Table
```
CREATE TABLE web (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    mobile VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);
```
