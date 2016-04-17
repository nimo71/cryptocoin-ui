FROM ubuntu:14.04
MAINTAINER Nick Monk <nick@monk.software>

RUN apt-get update
RUN apt-get install -y nginx
RUN rm -v /etc/nginx/nginx.conf
ADD deploy/nginx.conf /etc/nginx/
RUN echo "daemon off;" >> /etc/nginx/nginx.conf

ADD resources/public/index.html /usr/share/nginx/html/
ADD deploy/live/main.js /usr/share/nginx/js/

EXPOSE 80
CMD service nginx start

#TODO:
#-Configure nginx 
#-Deploy TeamLinkUp JavaScript artefacts to nginx
#-Deploy all static resources to nginx
