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

### v0.4.1 (最新版本)

**发布日期**: 2026-05-11

**修复问题**:
- 修复 CI 构建失败问题（Room DAO 查询返回 Map 类型错误）
- 将 getAllSongPathsAndModifiedTimes 方法返回类型从 Map<String, Long> 改为 List<PathAndModifiedTime>
- 新增 PathAndModifiedTime 数据类用于 Room 查询结果映射

**下载**: [APK下载地址](https://github.com/g-ai-001/music-q36-qwen-code/releases/tag/v0.4.1)

### v0.1.0 - v0.4.0 (历史版本)

**发布日期**: 2026-05-11

**新增功能**:
- 实现增量扫描机制，只扫描新增/修改的文件，避免全量扫描
- 添加扫描进度反馈，UI实时显示扫描进度和文件数
- 实现歌曲排序功能（按标题、艺术家、时长、添加时间排序）
- 优化扫描结果去重和更新机制，处理文件移动、删除、重命名
- 完善按艺术家、专辑分组显示功能

**下载**: [APK下载地址](https://github.com/g-ai-001/music-q36-qwen-code/releases/tag/v0.4.0)

### v0.1.0 - v0.3.1 (历史版本)

**v0.3.1** (2026-05-11): 重构优化版本，修复关键Bug和优化代码结构
- 修复PlayerScreen歌词时间单位不一致Bug
- 修复MainActivity onDestroy中Logger未关闭问题
- 修复PlayerManager与ExoPlayer脱节问题
- 修复MusicService onIsPlayingChanged循环调用问题
- 修复LibraryViewModel searchSongs嵌套launchIn问题
- 修复PlayerViewModel toggleFavorite竞态条件问题
- 提取重复UI组件到CommonComponents
- 统一颜色常量，优化代码结构和可读性

**v0.3.0** (2026-05-11): 完善歌词解析与显示功能
- 实现歌词与播放进度同步显示
- 封面模式下显示当前歌词行
- 歌词模式下支持自动滚动到当前歌词行
- 歌词高亮跟随播放进度

**v0.2.0 - v0.2.2** (2026-05-11): 完善UI和修复CI构建问题
- 完善首页、我的页面、播放详情页完整UI
- 实现封面模式/歌词模式切换
- 实现迷你播放器跨页面持久化
- 修复多个CI构建失败问题

**v0.1.0** (2026-05-10): 初始版本
- 基础项目架构（Kotlin + Jetpack Compose + MVVM）
- 本地音乐扫描功能、音乐播放服务
- Room数据库存储、歌词解析器、日志系统

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

- [ ] 歌词解析与显示功能完善（0.3.0）
- [ ] 本地音乐扫描与筛选优化
- [ ] 歌单管理功能完善（创建、编辑、删除）
- [ ] 搜索功能优化
- [ ] 主题/装扮功能
- [ ] 性能优化与稳定性提升
- [ ] 正式发布版本（1.0.0）
