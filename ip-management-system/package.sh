#!/bin/bash

# Define variables
PROJECT_DIR="$(git rev-parse --show-toplevel)/ip-management-system"
TARGET_DIR="/var/lib/ipms"
JAR_NAME="ip-management-system-0.0.1-SNAPSHOT.jar"

# Navigate to the project directory
cd "$PROJECT_DIR" || { echo "Project directory not found!"; exit 1; }

# Clean and package the Maven project
echo "Building the project..."
mvn clean package

# Check if the build was successful
if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit 1
fi

# Create the target directory if it doesn't exist
if [ ! -d "$TARGET_DIR" ]; then
    echo "Creating target directory: $TARGET_DIR"
    sudo mkdir -p "$TARGET_DIR"
    sudo chown $USER:$USER "$TARGET_DIR"  # Change ownership to the current user
fi

# Move the JAR file to the target directory
echo "Moving JAR file to $TARGET_DIR..."
sudo mv "target/$JAR_NAME" "$TARGET_DIR/"

# Check if the move was successful
if [ $? -eq 0 ]; then
    echo "JAR file successfully moved to $TARGET_DIR"
else
    echo "Failed to move JAR file!"
    exit 1
fi

echo "Packaging and deployment completed successfully."
echo "Restarting the service"
sudo systemctl restart ipms-web-app.service
echo "Service restarted"
