FROM openjdk:11

ENV SBT_VERSION 1.7.3
RUN curl -L -o sbt-$SBT_VERSION.zip https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.zip
RUN unzip sbt-$SBT_VERSION.zip -d ops

WORKDIR /producer
COPY . /producer

CMD /ops/sbt/bin/sbt run

