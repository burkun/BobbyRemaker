# BobbyRemaker - 兔子波比逆向分析项目

本项目用于逆向分析J2ME游戏 "Bobby Carrot 5" (兔子波比)。

## 快速开始

```bash
# 查看帮助
./bobby.sh help

# 完整分析流程
./bobby.sh all

# 单步操作
./bobby.sh analyze      # 分析JAR结构
./bobby.sh extract      # 解压
./bobby.sh decompile    # 反编译
./bobby.sh resources    # 分析资源
./bobby.sh midlet       # 分析MIDlet
./bobby.sh rebuild      # 重打包
```

## 项目结构

```
BobbyRemaker/
├── bobby.sh                        # 主CLI工具 (统一入口)
├── jar/                            # 原始JAR文件
├── extracted/                      # 解压后的内容
├── project/
│   └── src/
│       ├── main/java/              # 反编译的源码
│       └── main/resources/         # 游戏资源
├── analysis/                       # 分析报告
├── output/                         # 重打包输出
└── scripts/lib/                    # 工具依赖 (cfr.jar)
```

## 命令详解

| 命令 | 功能 | 输出 |
|------|------|------|
| `analyze` | 分析JAR结构和清单 | 控制台输出 |
| `extract` | 解压并分离资源 | extracted/, resources/ |
| `decompile` | 反编译Class文件 | src/*.java |
| `resources` | 分析图片/音频资源 | 控制台输出 |
| `midlet` | 分析MIDlet结构 | analysis/midlet-analysis.txt |
| `rebuild` | 重新打包JAR | output/*.jar |
| `all` | 执行完整流程 | 全部输出 |

## 当前游戏信息

- **名称**: Bobby 5 Up 2
- **开发者**: FDGSoft
- **版本**: 1.2.9
- **主类**: Bobby (入口) + a (游戏逻辑, 83KB混淆)
- **资源**: 25 PNG + 14 MIDI

## 依赖

- Java JDK (已配置)
- unzip (macOS自带)
- CFR反编译器 (自动下载)