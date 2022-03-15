FROM openjdk:12.0.2
EXPOSE 8065/tcp
EXPOSE 8065/udp
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ENTRYPOINT ["bash", "/it.unibo.first_problem_analysis.businesslogic-1.0/bin/it.unibo.first_problem_analysis.businesslogic"]