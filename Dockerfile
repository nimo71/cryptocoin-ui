FROM ubuntu:14.04
ARG GITHUB_PAT
ARG GIT_EMAIL
ARG GIT_NAME
MAINTAINER Nick Monk <nick@monk.software>
RUN apt-get install -y software-properties-common
RUN add-apt-repository ppa:webupd8team/java
RUN apt-get update
# Git 
RUN apt-get install -y ca-certificates git-core ssh
RUN apt-get install -y git
RUN git config --global user.email $GIT_EMAIL
RUN git config --global user.name $GIT_NAME
# Java 8
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java8-installer
# Emacs
RUN apt-get install -y emacs24-nox
RUN apt-get install -y rlwrap
ENV HOME /root
WORKDIR $HOME
RUN git clone https://$GITHUB_PAT@github.com/nimo71/clojure.emacs.d.git
RUN mv clojure.emacs.d .emacs.d
WORKDIR $HOME/.emacs.d
RUN git submodule init
RUN git submodule update
WORKDIR $HOME

# Leiningen
ENV LEIN_ROOT 1
WORKDIR /usr/bin
RUN wget https://raw.github.com/technomancy/leiningen/stable/bin/lein
RUN chmod +x lein
RUN mkdir ~/.lein
RUN echo '{:repl {:plugins [[cider/cider-nrepl "0.11.0"]]}}' >> ~/.lein/profiles.clj
RUN lein
WORKDIR $HOME
EXPOSE 3449
