Heraclitus - Network Monitor
================================================
An extensible real-time processing system, able to extract status information of monitored resources and process them with low latency. The monitoring system is designed to run on a distributed network of computers (cluster) to ensure scalability. Furthermore, communication between the monitored computing environments and the central processing unit can take place in a secure manner using digital certificates (SSL / TLS) to authenticate and encrypt the data exchanged between the machines. Only open-source tools were used in this project, namely Apache Storm, Apache Kafka and the Netty API.

The proposed solution was developed in two architectures. The first provides a direct communication channel between the monitored resource and the monitoring server. This solution was designed mainly to monitor computing environments that require their status information to be treated with low latency.

The second architecture was designed to monitor assets that need to transmit a large amount of data per unit of time to represent its status information, such as routers and switches. Thus, it was decided to place an intermediate message queue between the monitored resource and the monitoring server to allow asynchronous processing of the information. Although it presents a larger latency, the second architecture allows a greater number of machines to be simultaneously monitored than the first, since message queues are capable of coping with a large number of concurrent transactions.

Creating a tool that can help these type of systems to be less human dependent, making their own decisions based on the data collected from themselves and warning the system administrators of any suspicious behavior, contribute to the Information Technol- ogy community by providing a reliable tool to systems administrators. This framework doesn’t deal with all the received data yet, but with further work, this software could be used to monitor and analyze all kinds of data received from remote machines and networks.


-> StormEngine
================================================

Use of Apache Storm to monitor network assets and process their data in real-time.

    - The communication between the producers and Storm is made by JSON.

Three types of producers are currently supported:

    [Netty] Direct communication between the asset and Storm. We used Netty (with TLS enabled) as the communication tool. 

    [Kafka] Second scenario we use a queue system (Kafka) between the client and Storm. No SSL is used.

    [RabbitMQ] Work in progress...

The output of Storm is currently going to a folder inside the project called STORM_OUTPUT. The next step is to output JSON files to be consumed by webpages using D3 library.


-> ResourceMonitor
================================================

Monitor the client resources and send a JSON for each monitored topic to the Storm Topology or a Queue broker.

This is a Maven project. In order to run this project, the executable jar inside the target folder must be in the same folder as the corresponding lib of the system. For example, if you run the program in a 64-bit MacOsX plataform, the executable jar must be in the same folder of the libsigar-universal64-macosx.dylib. Relax, the maven project is configured to auto unzip these libraries inside the target folder and the base project folder so you don't have to worry about this.

Three types of connections (Storm Producer) are currently supported:

    [Netty] Direct communication. Needs the IP and Port of Storm. Also, the Storm must be running a compatible topology (NettyTopology).

    [Kafka] Second scenario we use a queue system (Kafka) between the client and Storm. No SSL is used. Needs the IP of the Zookeeper running in the Kafka Broker machine. Also, the Storm must be running a compatible topology (KafkaTopology).

    [RabbitMQ] Work in progress...


    - Instructions to run the project

Go to the target folder of the project and type: 
<p> java -jar ResourceMonitor-{PUT THE CURRENT VERSION HERE}-executable.jar</p>
This will start the program for you. Make sure that the Storm topology (Netty connector case) or the Queue system (Kafka and RabbitMQ case) is running, otherwise you are going to get the connection refused error.

    - Build the project

Use an IDE like eclipse for example and import this project as a maven project. After that you click: Run as maven... maven install.
ps. If you change the pom.xml don't forget to right-click the project in eclipse => Maven => Update Project... in order to download and organize all the dependencies!


If you don't like IDEs go to the base folder of the project (the one where the readme file is) and type: 
<p> mvn install</p>
This will build the project for you.

PacketCapture pluggin

dependece:
libpcap for linux or mac
winpcap for windows

pcap library used: pcap4j

-> Heraclitus.ui
===============================================
To run on terminal:
sudo JAVA_HOME="$JAVA_HOME" mvn spring-boot:run

