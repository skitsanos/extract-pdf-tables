# PDF Tables Extractor

Showcasing the use of `tabula` to extract tables from PDF documents



```shell
JSON=$(java -jar "target/extract-pdf-tables-1.0.2-jar-with-dependencies.jar" "{{.FILE}}" | jq '[.[] | select(length > 0)]')

# store JSON in extracts.json file
echo $JSON > out/extracts.json

# display the first table found
echo $JSON | jq '.[0]'
```

