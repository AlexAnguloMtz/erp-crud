# Stage 1: Build the Angular app
FROM node:18 AS build

# Add build arguments
ARG API_URL

# Set the working directory
WORKDIR /app

# Install Angular CLI globally
RUN npm install -g @angular/cli

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Install Python
RUN apt-get update && \
    apt-get install -y python3 python3-pip

# Copy everything else
COPY . .

# Make scripts executable
RUN chmod +x /app/scripts/create-env.py

# Run the Python script to create the environment file
RUN python3 /app/scripts/create-env.py API_URL

# Build the Angular app with the production configuration
RUN ng build --configuration production

# Stage 2: Serve the Angular app using Nginx
FROM nginx:alpine

# Copy nginx config
COPY ./scripts/default.conf /etc/nginx/conf.d/default.conf

# Copy the built Angular app from the build stage
COPY --from=build /app/dist/erp/browser /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]