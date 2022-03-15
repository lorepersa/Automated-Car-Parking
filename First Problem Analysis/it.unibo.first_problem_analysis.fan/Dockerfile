FROM openjdk:12.0.2
EXPOSE 8063/tcp
EXPOSE 8063/udp
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ENTRYPOINT ["bash", "/it.unibo.first_problem_analysis.fan-1.0/bin/it.unibo.first_problem_analysis.fan"]