# music-q36-qwen-code

本地音乐播放器 - 纯离线Android应用

## 应用介绍

一款纯离线的本地音乐播放器应用，无需联网权限。自动扫描设备存储中的音频文件，提供歌单管理、歌词显示和高质量的播放体验。

### 核心特性

- **纯离线使用**：无需联网，保护隐私
- **本地音乐扫描**：自动扫描设备中的音频文件
- **歌单管理**：创建、编辑、删除歌单
- **歌词显示**：支持LRC格式歌词同步显示
- **后台播放**：支持锁屏控制和通知栏控制
- **Material Design**：现代化UI设计

## 版本介绍

### v0.1.0 (当前版本)

**发布日期**: 2026-05-10

**功能**:
- 基础项目架构（Kotlin + Jetpack Compose + MVVM）
- 本地音乐扫描功能（MediaStore）
- 音乐播放服务（Media3前台服务）
- Room数据库存储（歌曲、歌单、收藏）
- 歌词解析器（LRC格式）
- 日志系统（保存在外部存储目录）
- 首页歌曲列表
- 底部迷你播放器
- 我的页面

**技术栈**:
- 语言: Kotlin
- UI框架: Jetpack Compose
- 架构: MVVM
- 数据库: Room
- 音频引擎: Media3 (ExoPlayer)
- 后台任务: WorkManager

**下载**: [APK下载地址](https://github.com/g-ai-001/music-q36-qwen-code/releases/tag/v0.1.0)

## 项目结构

```
app/
├── src/main/java/app/music_q36_qwen_code/
│   ├── data/                  # 数据层
│   │   ├── dao/               # Room DAO
│   │   ├── database/          # Room Database
│   │   └── model/             # 数据模型
│   ├── di/                    # 依赖注入
│   ├── service/               # 后台服务
│   ├── ui/                    # UI层
│   │   ├── components/        # Compose组件
│   │   ├── screens/           # 屏幕页面
│   │   └── theme/             # 主题配置
│   ├── utils/                 # 工具类
│   ├── viewmodel/             # ViewModel
│   └── MainActivity.kt        # 主Activity
└── src/main/res/              # 资源文件
```

## 构建说明

本项目使用GitHub Actions自动构建APK。

### 手动构建

```bash
# 构建Debug版本
./gradlew assembleDebug

# 构建Release版本
./gradlew assembleRelease
```

构建产物位于: `app/build/outputs/apk/`

## 权限说明

- `READ_MEDIA_AUDIO` / `READ_EXTERNAL_STORAGE`: 扫描本地音频文件
- `FOREGROUND_SERVICE`: 后台播放服务
- `POST_NOTIFICATIONS`: 显示播放通知

## 未来规划

- [ ] 播放详情页（封面模式/歌词模式）
- [ ] 搜索功能优化
- [ ] 歌单管理功能完善
- [ ] 主题/装扮功能
- [ ] 性能优化与稳定性提升
