FROM openjdk:12.0.2
EXPOSE 8065/tcp
EXPOSE 8065/udp
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ADD ./*.json /
#ADD ./*.bin /
ADD ./*.txt /
ENTRYPOINT ["bash", "/it.unibo.systembusinesslogic-1.0/bin/it.unibo.systembusinesslogic"]
