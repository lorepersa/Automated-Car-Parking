FROM openjdk:12.0.2
EXPOSE 8063/tcp
EXPOSE 8063/udp
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ADD ./*.json /
#ADD ./*.bin /
#ADD ./*.txt /
ENTRYPOINT ["bash", "/it.unibo.systemfan-1.0/bin/it.unibo.systemfan"]