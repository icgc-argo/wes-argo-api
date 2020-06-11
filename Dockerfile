FROM node:12.13.1-alpine

ENV APP_UID=9999
ENV APP_GID=9999
RUN apk --no-cache add shadow
RUN groupmod -g $APP_GID node 
RUN usermod -u $APP_UID -g $APP_GID node
RUN mkdir -p /appDir
RUN chown -R node /appDir
USER 9999
WORKDIR /appDir

COPY . .

RUN npm ci
RUN npm run build
EXPOSE 9000

CMD ["npm", "run", "start::prod"]
