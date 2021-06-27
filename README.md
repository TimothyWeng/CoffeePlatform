compile:
`javac -cp .:json-20210307.jar:sqlite-jdbc-3.32.3.2.jar -sourcepath src/ -d out/ src/*.java`

execute:
`java -cp .:json-20210307.jar:sqlite-jdbc-3.32.3.2.jar:out/ example`
