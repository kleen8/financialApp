# Dockerfile for Svelte application (Frontend)
FROM node:18 as builder

WORKDIR /app
COPY client/package.json client/package-lock.json ./
RUN npm install

COPY client/ ./
RUN npm run build

CMD ["npm", "run", "dev", "--", "--port", "5137"]

EXPOSE 5137
