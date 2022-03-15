FROM openjdk:12.0.2
EXPOSE 8060/tcp
EXPOSE 8060/udp
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ENTRYPOINT ["bash", "/it.unibo.first_problem_analysis.weightsensor-1.0/bin/it.unibo.first_problem_analysis.weightsensor"]