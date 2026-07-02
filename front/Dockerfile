# ---------- ÉTAPE 1 : Build (l'atelier Angular) ----------
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration production

# ---------- ÉTAPE 2 : Runtime (Nginx) ----------
FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/front/browser /usr/share/nginx/html
EXPOSE 80