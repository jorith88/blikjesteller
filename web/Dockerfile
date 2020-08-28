### Build
FROM node:10-alpine as build

COPY _dev /src/_dev
COPY gulpfile.js /src/
COPY package*.json /src/

WORKDIR /src/

RUN npm i -g gulp@3.9.1
RUN npm ci
RUN gulp
RUN gulp cacheBuster

### Run
FROM httpd:2.4-alpine

COPY --from=build /src/target/ /usr/local/apache2/htdocs/