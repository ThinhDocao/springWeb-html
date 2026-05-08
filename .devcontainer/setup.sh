#!/bin/bash

echo "Updating system and installing MariaDB (MySQL compatible)..."
sudo apt-get update
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y mariadb-server

echo "Starting MariaDB service..."
sudo service mariadb start

echo "Configuring database..."
# Thiết lập mật khẩu và tạo database
sudo mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '123321'; FLUSH PRIVILEGES;"
sudo mysql -u root -p123321 -e "CREATE DATABASE IF NOT EXISTS db;"

echo "Preparing Maven wrapper..."
cd aip-dma-service
chmod +x mvnw

echo "Setup complete!"
