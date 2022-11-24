FROM openjdk:12.0.2
EXPOSE 8060/tcp
EXPOSE 8060/udp
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ADD ./*.json /
#ADD ./*.bin /
#ADD ./*.txt /
ENTRYPOINT ["bash", "/it.unibo.systemweightsensor-1.0/bin/it.unibo.systemweightsensor"]