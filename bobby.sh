#!/bin/bash
# BobbyRemaker - 兔子波比逆向工具链
# 用法: ./bobby.sh <command> [options]
#
# 命令:
#   analyze      分析JAR结构
#   extract      解压JAR
#   decompile    反编译Class
#   resources    分析资源文件
#   midlet       分析MIDlet结构
#   rebuild      重新打包JAR
#   all          执行完整分析流程
#   help         显示帮助

set -e

# ============================================
# 公共函数
# ============================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# 项目目录
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_DIR="$PROJECT_ROOT/jar"
EXTRACTED_DIR="$PROJECT_ROOT/extracted"
SRC_DIR="$PROJECT_ROOT/project/src/main/java"
RESOURCES_DIR="$PROJECT_ROOT/project/src/main/resources"
ANALYSIS_DIR="$PROJECT_ROOT/analysis"
OUTPUT_DIR="$PROJECT_ROOT/output"
CFR_JAR="$PROJECT_ROOT/scripts/lib/cfr.jar"

# 输出函数
log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1" >&2; }
log_section() {
    echo -e "\n${CYAN}════════════════════════════════════════════════════════${NC}"
    echo -e "${CYAN}  $1${NC}"
    echo -e "${CYAN}════════════════════════════════════════════════════════${NC}\n"
}

error_exit() { log_error "$1"; exit "${2:-1}"; }

# 工具函数
format_size() {
    local bytes=$1
    if [ $bytes -ge 1048576 ]; then
        echo "$(echo "scale=2; $bytes/1048576" | bc) MB"
    elif [ $bytes -ge 1024 ]; then
        echo "$(echo "scale=2; $bytes/1024" | bc) KB"
    else
        echo "$bytes B"
    fi
}

ensure_dir() { mkdir -p "$1"; }
clean_dir() { rm -rf "$1" 2>/dev/null; mkdir -p "$1"; }

get_jar_file() {
    local jar_file="${1:-}"
    if [ -z "$jar_file" ]; then
        jar_file=$(find "$JAR_DIR" -name "*.jar" -type f 2>/dev/null | head -1)
    fi
    [ -z "$jar_file" ] && error_exit "找不到JAR文件"
    [ ! -f "$jar_file" ] && error_exit "文件不存在: $jar_file"
    echo "$jar_file"
}

# ============================================
# 命令实现
# ============================================

cmd_analyze() {
    local jar_file=$(get_jar_file "${1:-}")

    log_section "JAR 文件分析"
    log_info "文件: $jar_file"

    local file_size=$(stat -f%z "$jar_file" 2>/dev/null || stat -c%s "$jar_file" 2>/dev/null)

    echo ""
    log_section "基本信息"
    echo "  文件名: $(basename "$jar_file")"
    echo "  大小: $(format_size $file_size)"
    echo "  类型: $(file -b "$jar_file")"

    local temp_dir=$(mktemp -d)
    trap "rm -rf $temp_dir" EXIT
    unzip -q "$jar_file" -d "$temp_dir"

    local total=$(find "$temp_dir" -type f | wc -l | tr -d ' ')
    local classes=$(find "$temp_dir" -name "*.class" | wc -l | tr -d ' ')
    local pngs=$(find "$temp_dir" -name "*.png" | wc -l | tr -d ' ')
    local mids=$(find "$temp_dir" -name "*.mid" -o -name "*.midi" | wc -l | tr -d ' ')
    local wavs=$(find "$temp_dir" -name "*.wav" | wc -l | tr -d ' ')

    echo ""
    log_section "文件统计"
    echo "  总文件: $total | Class: $classes | PNG: $pngs | MIDI: $mids | WAV: $wavs"

    local manifest=$(find "$temp_dir" -name "MANIFEST.MF" -o -name "manifest.mf" | head -1)
    if [ -f "$manifest" ]; then
        echo ""
        log_section "MANIFEST"
        cat "$manifest" | sed 's/^/  /'
    fi

    echo ""
    log_section "Class 文件"
    find "$temp_dir" -name "*.class" | sed "s|$temp_dir/||" | sort | sed 's/^/  /'

    log_info "分析完成"
}

cmd_extract() {
    local jar_file=$(get_jar_file "${1:-}")

    log_section "解压 JAR"
    log_info "文件: $jar_file"

    clean_dir "$EXTRACTED_DIR"
    clean_dir "$RESOURCES_DIR"

    log_info "解压中..."
    unzip -o "$jar_file" -d "$EXTRACTED_DIR" | tail -3

    log_info "分离资源..."
    cd "$EXTRACTED_DIR"
    find . -type f ! -name "*.class" | while read f; do
        local target="$RESOURCES_DIR/$(dirname "$f")"
        ensure_dir "$target"
        cp "$f" "$target/"
    done

    local classes=$(find "$EXTRACTED_DIR" -name "*.class" | wc -l | tr -d ' ')
    local resources=$(find "$RESOURCES_DIR" -type f | wc -l | tr -d ' ')

    echo ""
    log_section "结果"
    echo "  Class: $classes → $EXTRACTED_DIR"
    echo "  资源: $resources → $RESOURCES_DIR"

    # 生成文件树
    ensure_dir "$ANALYSIS_DIR"
    {
        echo "# 文件树 ($(date))"
        echo -e "\n## Class"
        find "$EXTRACTED_DIR" -name "*.class" | sed "s|$EXTRACTED_DIR/||" | sort
        echo -e "\n## 资源"
        find "$RESOURCES_DIR" -type f | sed "s|$RESOURCES_DIR/||" | sort
    } > "$ANALYSIS_DIR/file-tree.txt"

    log_info "完成"
}

cmd_decompile() {
    local jar_file=$(get_jar_file "${1:-}")

    log_section "反编译"
    log_info "JAR: $jar_file"

    # 检查/下载 cfr
    if [ ! -f "$CFR_JAR" ]; then
        log_info "下载 CFR 反编译器..."
        local cfr_url="https://github.com/leibnitz27/cfr/releases/download/0.152/cfr-0.152.jar"
        ensure_dir "$(dirname "$CFR_JAR")"
        curl -L -o "$CFR_JAR" "$cfr_url" 2>/dev/null || error_exit "CFR下载失败"
    fi

    clean_dir "$SRC_DIR"

    log_info "反编译中..."
    java -jar "$CFR_JAR" "$jar_file" --outputdir "$SRC_DIR" --silent true

    local java_files=$(find "$SRC_DIR" -name "*.java" | wc -l | tr -d ' ')
    local lines=$(find "$SRC_DIR" -name "*.java" -exec cat {} \; | wc -l | tr -d ' ')

    echo ""
    log_section "结果"
    echo "  Java文件: $java_files"
    echo "  代码行数: $lines"
    echo "  输出: $SRC_DIR"

    # 生成摘要
    ensure_dir "$ANALYSIS_DIR"
    {
        echo "# 反编译摘要 ($(date))"
        echo "- Java文件: $java_files"
        echo "- 代码行数: $lines"
        echo -e "\n## 文件列表"
        find "$SRC_DIR" -name "*.java" | sed "s|$SRC_DIR/||" | sort | sed 's/^/- /'
    } > "$ANALYSIS_DIR/decompile-summary.txt"

    log_info "完成"
}

cmd_resources() {
    [ ! -d "$RESOURCES_DIR" ] && error_exit "请先运行: $0 extract"

    log_section "资源分析"

    # 图片
    local pngs=$(find "$RESOURCES_DIR" -name "*.png" 2>/dev/null)
    if [ -n "$pngs" ]; then
        echo ""
        log_section "图片 ($(echo "$pngs" | wc -l | tr -d ' '))"
        echo "$pngs" | while read f; do
            local size=$(stat -f%z "$f" 2>/dev/null || stat -c%s "$f" 2>/dev/null)
            local dims=$(sips -g pixelWidth -g pixelHeight "$f" 2>/dev/null | grep pixel | awk '{print $2}' | tr '\n' 'x' | sed 's/x$//')
            printf "  %-35s %8s  %s\n" "$(echo "$f" | sed "s|$RESOURCES_DIR/||")" "$(format_size $size)" "${dims:-N/A}"
        done
    fi

    # 音频
    local audio=$(find "$RESOURCES_DIR" \( -name "*.wav" -o -name "*.mid" -o -name "*.midi" -o -name "*.mp3" \) 2>/dev/null)
    if [ -n "$audio" ]; then
        echo ""
        log_section "音频 ($(echo "$audio" | wc -l | tr -d ' '))"
        echo "$audio" | while read f; do
            local size=$(stat -f%z "$f" 2>/dev/null || stat -c%s "$f" 2>/dev/null)
            printf "  %-35s %8s  %s\n" "$(echo "$f" | sed "s|$RESOURCES_DIR/||")" "$(format_size $size)" "$(file -b "$f" | cut -c1-40)"
        done
    fi

    log_info "完成"
}

cmd_midlet() {
    local jar_file=$(get_jar_file "${1:-}")

    log_section "MIDlet 分析"

    local temp_dir=$(mktemp -d)
    trap "rm -rf $temp_dir" EXIT
    unzip -q "$jar_file" -d "$temp_dir"

    local manifest=$(find "$temp_dir" -name "MANIFEST.MF" -o -name "manifest.mf" | head -1)

    if [ -f "$manifest" ]; then
        local name=$(grep -i "MIDlet-Name:" "$manifest" | cut -d: -f2- | xargs)
        local vendor=$(grep -i "MIDlet-Vendor:" "$manifest" | cut -d: -f2- | xargs)
        local version=$(grep -i "MIDlet-Version:" "$manifest" | cut -d: -f2- | xargs)
        local main=$(grep -i "MIDlet-1:" "$manifest" | cut -d: -f2-)

        echo ""
        log_section "信息"
        echo "  名称: $name"
        echo "  开发者: $vendor"
        echo "  版本: $version"

        # 提取主类名
        local main_class=$(echo "$main" | cut -d, -f3 | xargs 2>/dev/null)
        [ -n "$main_class" ] && echo "  主类: $main_class"
    fi

    echo ""
    log_section "类大小分布"
    find "$temp_dir" -name "*.class" -exec stat -f "%z %N" {} \; 2>/dev/null | \
        sort -rn | head -10 | while read line; do
            size=$(echo "$line" | awk '{print $1}')
            path=$(echo "$line" | awk '{print $2}' | sed "s|$temp_dir/||")
            printf "  %10s  %s\n" "$(format_size $size)" "$path"
        done

    ensure_dir "$ANALYSIS_DIR"
    {
        echo "# MIDlet分析 ($(date))"
        echo "- 名称: $name"
        echo "- 开发者: $vendor"
        echo "- 版本: $version"
        echo "- 主类: $main_class"
        echo -e "\n## 类列表"
        find "$temp_dir" -name "*.class" | sed "s|$temp_dir/||" | sort | sed 's/^/- /'
    } > "$ANALYSIS_DIR/midlet-analysis.txt"

    log_info "完成"
}

cmd_rebuild() {
    [ ! -d "$EXTRACTED_DIR" ] && error_exit "请先运行: $0 extract"

    log_section "重新打包"

    local output_name="${1:-rebuilt.jar}"
    ensure_dir "$OUTPUT_DIR"
    local output_file="$OUTPUT_DIR/$output_name"

    local original=$(find "$JAR_DIR" -name "*.jar" | head -1)
    if [ -f "$original" ]; then
        local orig_size=$(stat -f%z "$original" 2>/dev/null || stat -c%s "$original" 2>/dev/null)
        log_info "原始: $(basename "$original") ($(format_size $orig_size))"
    fi

    log_info "打包中..."
    cd "$EXTRACTED_DIR"

    if command -v jar &> /dev/null && jar --version &> /dev/null 2>&1; then
        jar cvf "$output_file" . 2>&1 | tail -5
    elif command -v zip &> /dev/null; then
        rm -f "$output_file" 2>/dev/null
        zip -r "$output_file" . 2>&1 | tail -5
    else
        error_exit "需要 jar 或 zip"
    fi

    local new_size=$(stat -f%z "$output_file" 2>/dev/null || stat -c%s "$output_file" 2>/dev/null)

    echo ""
    log_section "结果"
    echo "  输出: $output_file"
    echo "  大小: $(format_size $new_size)"
    [ -n "$orig_size" ] && echo "  变化: +$(format_size $((new_size - orig_size)))"

    log_info "完成"
}

cmd_all() {
    log_section "完整分析流程"

    cmd_analyze "$@"
    echo ""
    cmd_extract "$@"
    echo ""
    cmd_decompile "$@"
    echo ""
    cmd_resources
    echo ""
    cmd_midlet "$@"

    echo ""
    log_section "全部完成"
    echo "  源码: $SRC_DIR"
    echo "  资源: $RESOURCES_DIR"
    echo "  报告: $ANALYSIS_DIR"
}

cmd_help() {
    cat << EOF
BobbyRemaker - 兔子波比逆向工具链

用法: $0 <command> [options]

命令:
  analyze [jar]    分析JAR结构和清单
  extract [jar]    解压JAR到 extracted/ 和 resources/
  decompile [jar]  反编译Class到 src/
  resources        分析资源文件(需先extract)
  midlet [jar]     分析MIDlet结构
  rebuild [name]   重新打包JAR到 output/
  all [jar]        执行完整分析流程
  help             显示此帮助

示例:
  $0 analyze                    # 分析jar目录中的jar
  $0 decompile game.jar         # 反编译指定jar
  $0 all                        # 完整分析
  $0 rebuild my_game.jar        # 重打包

目录结构:
  jar/                          原始JAR文件
  extracted/                    解压内容
  project/src/main/java/        反编译源码
  project/src/main/resources/   游戏资源
  analysis/                     分析报告
  output/                       重打包输出
EOF
}

# ============================================
# 主入口
# ============================================

case "${1:-help}" in
    analyze)    shift; cmd_analyze "$@" ;;
    extract)    shift; cmd_extract "$@" ;;
    decompile)  shift; cmd_decompile "$@" ;;
    resources)  shift; cmd_resources "$@" ;;
    midlet)     shift; cmd_midlet "$@" ;;
    rebuild)    shift; cmd_rebuild "$@" ;;
    all)        shift; cmd_all "$@" ;;
    help|--help|-h) cmd_help ;;
    *)          error_exit "未知命令: $1\n运行 '$0 help' 查看帮助" ;;
esac
