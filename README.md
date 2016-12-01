# Functional logging

Small experimental project to showcase elasticsearch, with kibana, and a log-generator using filebeat to push log-entries to elastic

# Elastic

Indexes are somewhat analogous to tables; A functional grouping for data.
Types describe the schema of rows, columns with their respective types. This is a very flexibile schema:
- Fields are always optional
- Whenever an additional field is registered with a message, this field simply is added to the type
- But, types cannot be changed once defined.

Indexes are sharded and replicated.

# Filebeat
Is per default configured to register log-entries under:
"filebeat-$DATESTAMP", creating a new index each day.

Maintenance of the automatically generated indexes can be done using [Curator](https://www.elastic.co/guide/en/elasticsearch/client/curator/current/index.html).