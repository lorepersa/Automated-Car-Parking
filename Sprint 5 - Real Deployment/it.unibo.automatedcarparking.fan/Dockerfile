FROM arm32v7/openjdk:11
EXPOSE 8063/tcp
EXPOSE 8063/udp
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 0E98404D386FA1D9
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 648ACFD622F3D138
RUN apt-get update
RUN apt-get install -y python3 python3-pip
RUN pip3 install --no-cache-dir rpi.gpio
ADD ./build/distributions/*.tar /
ADD ./*.pl /
ADD ./*.json /
ADD ./fan.py /
ENTRYPOINT ["bash", "/it.unibo.systemfan-1.0/bin/it.unibo.systemfan"]



## launch with: docker run -it --rm --name fan -p8063:8063/tcp -p8063:8063/udp --privileged lorenzopersampieri/fan /bin/bash
