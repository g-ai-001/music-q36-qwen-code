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

### v0.3.0 (最新版本)

**发布日期**: 2026-05-11

**新增功能**:
- 完善歌词解析与显示功能，实现歌词与播放进度同步显示
- 封面模式下显示当前歌词行
- 歌词模式下支持自动滚动到当前歌词行
- 歌词高亮跟随播放进度
- 完善收藏功能（添加/移除收藏）

**下载**: [APK下载地址](https://github.com/g-ai-001/music-q36-qwen-code/releases/tag/v0.3.0)

### v0.2.2

**发布日期**: 2026-05-11

**修复**:
- 修复 CI 构建失败问题（多个编译错误：Composable引用、Hilt依赖、类型不匹配等）
- 修复 MainActivity.kt 缺少 mutableStateOf 和 Column 导入
- 修复 PlayerScreen.kt 时间参数类型从 Int 改为 Long
- 移除 DatabaseModule.kt 中未使用的 Hilt/Dagger 注解
- 修复 MusicService.kt 中 MediaSession Token 类型不匹配
- 修复 HomeScreen.kt 中 tabIndicatorOffset 在 Material3 中已移除
- 修复 Logger.kt 中 Array 不支持 removeAt
- 修复 ViewModel 中 const val 不能在类实例中使用

**下载**: [APK下载地址](https://github.com/g-ai-001/music-q36-qwen-code/releases/tag/v0.2.2)

### v0.2.1 (已发布)

**发布日期**: 2026-05-11

**修复**:
- 修复 CI 构建失败问题（PlaylistSongMap 实体添加复合主键注解）

**下载**: [APK下载地址](https://github.com/g-ai-001/music-q36-qwen-code/releases/tag/v0.2.1)

### v0.2.0

**发布日期**: 2026-05-11

**新增功能**:
- 首页完整UI：搜索栏、Tab布局（推荐/歌单/歌手/专辑）、最近播放、本地歌单、歌曲列表
- 我的页面完整UI：Header卡片（用户信息/VIP徽章）、统计网格、快捷操作、自建歌单
- 播放详情页（全屏播放器）：封面模式/歌词模式切换、动态模糊背景、专辑封面显示
- 封面模式：大圆角方形专辑封面、歌曲信息、歌词预览、进度条、播放控制
- 歌词模式：全屏滚动歌词、当前行高亮、平滑滚动动画
- 迷你播放器完善：跨页面持久化、点击展开全屏播放器
- 底部导航栏：首页/我的两个Tab切换

**下载**: [APK下载地址](https://github.com/g-ai-001/music-q36-qwen-code/releases/tag/v0.2.0)

### v0.1.0

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

- [ ] 歌词解析与显示功能完善（0.3.0）
- [ ] 本地音乐扫描与筛选优化
- [ ] 歌单管理功能完善（创建、编辑、删除）
- [ ] 搜索功能优化
- [ ] 主题/装扮功能
- [ ] 性能优化与稳定性提升
- [ ] 正式发布版本（1.0.0）
