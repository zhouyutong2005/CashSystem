#!/bin/bash

echo "开始运行现金缴存系统测试..."

# 运行单元测试
echo "运行单元测试..."
mvn test

# 运行集成测试
echo "运行集成测试..."
mvn verify

# 生成测试报告
echo "生成测试报告..."
mvn site

# 运行覆盖率检查
echo "运行代码覆盖率检查..."
mvn jacoco:report

echo "测试完成！查看 target/site/index.html 获取详细报告"