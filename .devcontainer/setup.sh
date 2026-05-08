#!/bin/bash

echo "Starting MySQL setup..."
# Chờ MySQL khởi động
until mysqladmin ping >/dev/null 2>&1; do
  echo "Waiting for MySQL to start..."
  sleep 2
done

echo "Configuring MySQL database..."
# Thiết lập mật khẩu và tạo database
mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '123321'; FLUSH PRIVILEGES;"
mysql -u root -p123321 -e "CREATE DATABASE IF NOT EXISTS db;"

echo "Preparing Maven wrapper..."
cd aip-dma-service
chmod +x mvnw

echo "Setup complete!"
