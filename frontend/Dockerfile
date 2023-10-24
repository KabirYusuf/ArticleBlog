FROM node:18.16.0-alpine

WORKDIR /home/node/app

ADD package.json .
RUN npm install

CMD ["npm", "run", "dev", "--host"]
