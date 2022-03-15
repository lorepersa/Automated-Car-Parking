FROM openjdk:12.0.2
EXPOSE 8062/tcp
EXPOSE 8062/udp
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ENTRYPOINT ["bash", "/it.unibo.first_problem_analysis.thermometer-1.0/bin/it.unibo.first_problem_analysis.thermometer"]