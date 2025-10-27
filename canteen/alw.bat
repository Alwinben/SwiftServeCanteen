@echo off
REM ============================================
REM  Canteen Order System - Project Setup Script
REM ============================================

echo 🚧 Creating directory structure...

mkdir src
mkdir src\com
mkdir src\com\canteen
mkdir src\com\canteen\app
mkdir src\com\canteen\model
mkdir src\com\canteen\service
mkdir src\com\canteen\repository
mkdir src\com\canteen\ui

echo ✅ Directories created.

echo 📄 Creating Java files...

type nul > src\com\canteen\app\CanteenOrderSystem.java
type nul > src\com\canteen\model\Order.java
type nul > src\com\canteen\service\OrderSystemLogic.java
type nul > src\com\canteen\repository\OrderRepository.java
type nul > src\com\canteen\repository\MySQLOrderRepository.java
type nul > src\com\canteen\ui\AdminUI.java
type nul > src\com\canteen\ui\UI.java
type nul > src\com\canteen\ui\AdminTokenSlip.java

echo ✅ Java files created.

echo 🏁 Project setup complete!
pause