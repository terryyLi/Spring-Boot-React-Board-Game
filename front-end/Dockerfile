# Start with a base image
FROM node:18

# Set the working directory to /app
WORKDIR /app

# Copy the package.json and package-lock.json files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application
COPY . .

# Set the environment variable to configure the backend URL
ENV REACT_APP_BACKEND_URL=http://backend:8080

# Expose port 3000 for the application
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
