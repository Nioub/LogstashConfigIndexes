# Logstash Config Indexes

## Status

This is a proof of concept to extract cluster indexes from Logstash config files, as well as convert such config file to a more generic format like JSON.

It relies heavily on [https://github.com/colinsurprenant/logstash-antlr-config](https://github.com/colinsurprenant/logstash-antlr-config), which is an unofficial conversion of the Logstash configuration grammar from Treetop to ANTRL.

*Disclaimer : code quality is not state of the art, use at your own risk.*

## Usage

To test :

1. Install Java and Maven

2. Build
```
cd LogstashConfigIndexes
mvn clean package
```

3. Run
```
java -cp target/LogstashConfigIndexes-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.logstash.ExportJson src/test/resource/basic.conf
```

## Further developments

* Handle environment variables in string values.
* Handle pipelines.yml files to find pipeline configurations, including embedded declarations.
* Add more tests :)
* Add better error handling
* Implement a linter to check for rules
* Implement a formatter (need to manage comments correctly)
