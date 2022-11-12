FROM openjdk:12.0.2
EXPOSE 8061/tcp
EXPOSE 8061/udp
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ADD ./*.json /
#ADD ./*.bin /
#ADD ./*.txt /
ENTRYPOINT ["bash", "/it.unibo.systemoutsonar-1.0/bin/it.unibo.systemoutsonar"]
