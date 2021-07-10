## MXRetrv documentation
MXRetrv is a command line tool to get DNS MX documents for 
domains. 

#### Prerequisites
* `java` 
* `maven`

#### Installation
1. Clone this project via `git`:
    ```bash
    git clone https://github.com/egehurturk/mxretrv.git
    ```
2. Go into the project folder:
    ```bash
    cd mxretrv
    ```
3. Build the source code:
    ```bash
   mvn clean package
    ```
4. _(Optional)_ Make the `JAR` accessible:
    * Change `~/.bash_profile` if that doesn't exist on your macine 
    ```bash
   $ cp target/mxretrv-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/bin/
   ```
   * add `alias mxretrv="java -jar /usr/local/bin//mxretrv-1.0-SNAPSHOT-jar-with-dependencies.jar` to `~/.bahrc`
   
   ```bash
   $ source ~/.bashrc
   ```
5. Run the program:
    ```bash
   mxretrv --input ...
    ```
#### Usage
```
usage: mxretrv -i <input_file> [-o <output_file>] [-b <size>] [-m] [-v]
Save DNS MX records of domain names


 -i,--input <input_file>     a text file containing domain names delimited with "\n"

 -o,--output <output_file>   JSON file to output MX records for each domain name (otherwise stdout is used)

 -b,--batch-size <size>      the number of domain names to process in each thread (if multithreading is not enabled, the value of this

                             argument will be ignored)

 -m,--multi                  enable multithreading

 -v,--verbose                output extra information about the process```
```
**Note** that input file and output files must be _absolute paths_

Example:
```bash
mxretrv --input /Users/egehurturk/Downloads/domain.csv \
        --output /Users/egehurturk/Desktop/out.json \
        --multi --verbose \
        --batch-size 60 
```
