#!/bin/bash
set -e

echo "🔧 Installing required tools in Jenkins container..."

# Update package list
apt-get update -qq

# Install Maven
if ! command -v mvn &> /dev/null; then
    echo "📦 Installing Maven..."
    apt-get install -y -qq maven
fi

# Install MySQL client
if ! command -v mysql &> /dev/null; then
    echo "📦 Installing MySQL client..."
    apt-get install -y -qq default-mysql-client
fi

# Install netcat for port checking
if ! command -v nc &> /dev/null; then
    echo "📦 Installing netcat..."
    apt-get install -y -qq netcat-openbsd
fi

# Install curl
if ! command -v curl &> /dev/null; then
    echo "📦 Installing curl..."
    apt-get install -y -qq curl
fi

# Clean up
apt-get clean
rm -rf /var/lib/apt/lists/*

echo "✅ Tools installation completed!"
mvn -version
mysql --version || echo "MySQL client installed"
nc -h | head -1 || echo "Netcat installed"
curl --version | head -1 || echo "Curl installed"

