# Kaltura parallel chunked upload test
Simple Java code that splits a big video file into smaller chunks and uploads it to the Kaltura server using the uploadToken API.
The code creates 5 threads to benefit from concurrent uploading of chunks. To change the number of concurrent threads, set threadCount to the desired number.

This repo is meant as a proof of concept on how to utilize the uploadToken.upload() API to upload large files on the Kaltura server.
Seeing how Flash has 2G limitation where it comes to uploading files and the uploaded file size allowed can also be limited in the PHP settings, big files should sometimes be split into smaller ones and uploaded in chunks.


## Contents
UploadTest.java - a simple test class which can be used to test chunked upload from the command line
chunkedupload/ParallelUpload.java - a help class which uses FileInputStream to read() a certain amount of bytes from the original file [thus spliting it] and upload it to the Kaltura server.


## Compiling the sample code
The code relies on the Kaltura Java client. When using the latest version, that can be obtained from:
http://search.maven.org/#search|ga|1|kaltura
or from:
https://github.com/kaltura/KalturaGeneratedAPIClientsJava

When using older versions of the Kaltura server, one should run:
\# php /opt/kaltura/app/generator/generate.php java
from the Kaltura server and then take the resulting package from /opt/kaltura/web/content/clientlibs/java

$ javac -cp /path/to/Kaltura/client/classes:/path/to/supporting/classes chunkedupload/ParallelUpload.java
$ javac -cp /path/to/Kaltura/client/classes:/path/to/supporting/classes UploadTest.java

## Testing from CLI:
$ java -cp /path/to/Kaltura/client/classes:/path/to/supporting/classes UploadTest [service URL] [partner ID] [partner admin secret] [/path/to/large/vid/file]

You can also use the run.sh wrapper.

Note that to make bootstraping easier, the repository contains the following supporting JARs:
```
commons-codec-1.12.jar
gson-2.8.5.jar
json-20180813.jar
kalturaApiClient-15.4.0.jar
kotlin-stdlib-1.3.21.jar
log4j-api-2.11.1.jar
log4j-core-2.11.1.jar
okhttp-3.14.1.jar
okio-2.2.2.jar
```
