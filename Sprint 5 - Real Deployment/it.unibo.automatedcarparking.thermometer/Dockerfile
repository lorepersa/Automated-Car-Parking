FROM openjdk:12.0.2
EXPOSE 8062/tcp
EXPOSE 8062/udp
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ADD ./*.json /
#ADD ./*.bin /
#ADD ./*.txt /
ENTRYPOINT ["bash", "/it.unibo.systemthermometer-1.0/bin/it.unibo.systemthermometer"]