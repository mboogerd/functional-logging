FROM java:8

# Download filebeat and unpack
# RUN curl -L -O https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-5.0.2-darwin-x86_64.tar.gz && tar xzvf filebeat-5.0.2-darwin-x86_64.tar.gz
RUN curl -L -O https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-5.0.2-amd64.deb
RUN dpkg -i filebeat-5.0.2-amd64.deb