Java project created in Eclipse Mars 2 Release (4.5.2)

Java jdk1.8.0_101 (64 bits) ==> Mandatory. 
(I have been using functionalities of Java 1.8 such as Collection.streams())

Maven 3.3
JUnit 4.9

Example of Execution (Windows 10 64Bits Version):

java -jar .\GromenauerJava-0.0.1-SNAPSHOT.jar es.gromenauer.Main .\input_2

WARNING!

Into input folder; the program generate an auxiliary folder with all the processed information.

In the example; we can see a folder .\input_2\index with four files in it:

termFiles.idx ==> list of files where we can find a term.
termFilesFields.idx ==> information of every field in every file.
termFilesFieldsCounts.idx ==> number of findings of a term in every file.
totalCountData.idx ==> total number of term by file

