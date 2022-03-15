FROM openjdk:12.0.2
EXPOSE 8061/tcp
EXPOSE 8061/udp
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ENTRYPOINT ["bash", "/it.unibo.first_problem_analysis.outsonar-1.0/bin/it.unibo.first_problem_analysis.outsonar"]