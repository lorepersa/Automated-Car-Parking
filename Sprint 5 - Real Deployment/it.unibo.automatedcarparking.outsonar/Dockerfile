FROM arm32v7/openjdk:11
EXPOSE 8061/tcp
EXPOSE 8061/udp
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 0E98404D386FA1D9
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 648ACFD622F3D138
RUN apt-get update
RUN apt-get install -y python3 python3-pip
RUN pip3 install --no-cache-dir rpi.gpio
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ADD ./*.json /
ADD ./sonar.py /
ENTRYPOINT ["bash", "/it.unibo.systemoutsonar-1.0/bin/it.unibo.systemoutsonar"]



## launch with: docker run -it --rm --name outsonar -p8061:8061/tcp -p8061:8061/udp --privileged lorenzopersampieri/outsonar /bin/bash
