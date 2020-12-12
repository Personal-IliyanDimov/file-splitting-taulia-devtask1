Task: 

At Taulia we often deal with ingesting data and converting it into target formats for consumption
into different systems - source and destination systems vary from accounting systems, data-
extracts to REST APIs or SOAP calls. An example file is attached and contains invoice data
(amounts, identifiers etc) as well as a base64 encoded invoice image. The real files can be 2GB+.
Problem description:
We need to write a system that parses and ingests the given (large) file and has the ability to
produce the two different output formats specified below.
As a user of that system I need to be able to configure or otherwise specify which of the two
output formats should be produced.
The new output formats will then later on be ingested by other systems - the integrity of data and
files has to stay. The later ingestion of the newly produced files is not part of this exercise.
The two destination formats should be:
1. CSV file of the original data but split up by 'buyer'. So, if there are 10 different buyers overall
   there should be 10 different output files. The rest of the data in the CSV should be arranged in
   the same way as in the input file.
2. XML file of the original data split up by 'buyer'. The invoice image should not be part of the
   XML data but the single invoice files should be extracted from the CSV and be placed into the
   file-system. The format of the XML should loosely follow the input CSV in regards to node-
   names etc.You can decide any changes to folder-structure etc. of the output format.
   It is up to you what language you develop the solution in as long as we can see the solution
   running and walk through the code and output files you produced together with.
   Unit tests would be appreciated.
   
Implementation:

The solution is implemented in the form of application that can be run from the console/terminal in order to split file 
into parts grouped by buyer.
The solutions takes into account the following constrains:
- the maximum amount of simultaniously open handles that a process/application is allowed to use (in ubuntu that number is 1024)
- the amount of memory the application is allow to use when runned (JVM Xmx memory)
- the size of the input file

Basically the solution does not restrict the maximum size of input file and can handle file with any size, but 
the speed of processing will depend on amount handles and memory that are allowed to be used.

Transformation happens using different strategies and types of transformers. 
Each strategy is executed by a specific transformer and each transformer uses different amount of memory, 
handles and splitting criteria. Some overview of the solution is 
provided in file Taulia-Dev-Task-Design.pdf located in the same folder as this file.

The testing is not not implemented yet (due to lack of time) but a testing strategy has been selected and can be used.
In the test folders you will find a TestGenerator class that can generate inputs with different size and predictable 
content, which on the other side leads to predictable output which is easy to check.
If the reviewers are satisfied with the solution I will add the missing tests for few days. 

Some information about the structure of the code:
- transformer package is implemented in generic way and they can be reused not only for different file types of invoice 
but also for different type of input (any structured input)
- in the solution we are using some basic configuration adjustments, but they can be defined in a more precise manner
  in order to achieve optimal results 
- io package allows us to read/write csv/xml files with invoice info 


Notes: 
1) The solution is still raw and can be improved in many ways, but the idea here is to provide the general approach to 
   solve the problem.
2) The output files are still stored in base64 form and not as PNG as it is not mentioned anywhere explicitly that 
   all of them are in that format.
3) All questions related with the code and solution itself can be discussed during some of the next steps 
   in the interview process.

Commands Documentation:
1) CommandRunner command accepts only two params: 
   - inputFile that is about to be split
   - outputFolder that exists and is empty and where the results will be stored.
   
2) FileGenerator command accepts only a single param:
   - file where the generator will append the generated data 


