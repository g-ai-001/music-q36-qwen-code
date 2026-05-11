# 项目规划 - music-q36-qwen-code (本地音乐播放器)

## 版本历史
- **0.1.0** (已发布) - 初始版本开发完成 ✅
- **0.2.0** (已发布) - 完善首页、我的页面、播放详情页 UI ✅
- **0.2.1** (已发布) - 修复 CI 构建失败问题（Room 实体缺少主键注解） ✅
- **0.2.2** (已发布) - 修复 CI 构建失败问题（多个编译错误） ✅
- **0.3.0** (已发布) - 歌词解析与显示功能完善 ✅
- **0.3.1** (已发布) - 代码质量重构与性能优化 ✅
- **0.4.0** (已发布) - 本地音乐扫描与筛选优化 ✅
- **0.4.1** (开发中) - 修复 CI 构建失败问题（Room DAO 查询返回 Map 类型错误）

## 当前版本目标: 0.4.1

### 目标
修复 GitHub Actions CI 构建失败问题，解决 Room DAO 查询返回 Map 类型的编译错误。

### 任务清单
- [x] 修复 SongDao.kt 第 90 行 getAllSongPathsAndModifiedTimes 方法返回 Map 类型的编译错误
- [x] 确保 GitHub Actions workflow 无报错
- [x] 更新 versionName 到 0.4.1

## 当前版本目标: 0.4.0

### 目标
本地音乐扫描与筛选优化，提升扫描效率、用户体验和数据管理能力。

### 任务清单
- [x] 实现增量扫描机制（只扫描新增/修改的文件，避免全量扫描）
- [x] 添加扫描进度反馈（UI显示扫描进度和已扫描文件数）
- [x] 实现歌曲排序功能（支持按标题、艺术家、时长、添加时间排序）
- [x] 优化扫描结果去重和更新机制（处理文件移动、删除、重命名）
- [x] 完善按艺术家、专辑分组显示功能
- [x] 代码审查和优化
- [x] 更新版本号到 0.4.0
- [x] 确保 GitHub Actions workflow 无报错

## 当前版本目标: 0.3.1

### 目标
重构优化存量代码,提升代码质量、性能和稳定性,修复CI构建失败问题。

### 任务清单
- [x] 修复GitHub Actions CI构建失败问题
- [x] 优化代码结构和可读性
- [x] 完善日志系统实现
- [x] 性能优化和内存管理改进
- [x] 代码审查和优化
- [x] 更新版本号到 0.3.1
- [x] 确保 GitHub Actions workflow 无报错

## 当前版本目标: 0.3.0

### 目标
完善歌词解析与显示功能，实现歌词与播放进度同步显示。

### 任务清单
- [x] 完善 MainActivity 中歌词功能的集成（从 PlayerViewModel 加载歌词并传递给 PlayerScreen）
- [x] 完善 PlayerScreen 歌词模式的时间同步逻辑（歌词高亮跟随播放进度）
- [x] 代码审查和优化
- [x] 更新版本号到 0.3.0
- [x] 确保 GitHub Actions workflow 无报错

## 当前版本目标: 0.2.2

### 目标
修复 GitHub Actions CI 构建失败问题，解决所有编译错误。

### 任务清单
- [x] 修复 MainActivity.kt 编译错误（mutableStateOf、Column 等 Composable 引用、类型不匹配）
- [x] 修复 DatabaseModule.kt 编译错误（Hilt/Dagger 依赖注入问题）
- [x] 修复 MusicService.kt 编译错误（MediaSession Token 类型不匹配）
- [x] 修复 HomeScreen.kt 编译错误（tabIndicatorOffset 找不到）
- [x] 修复 Logger.kt 编译错误（removeAt 方法问题）
- [x] 修复 LibraryViewModel.kt 和 PlayerViewModel.kt 编译错误（const val 位置问题）
- [x] 确保 GitHub Actions workflow 无报错
- [x] 更新 versionName 到 0.2.2

## 版本规划

### 短期规划 (0.1.x - 0.3.x)
- **0.1.0**: 基础项目架构搭建，实现核心音乐播放功能
- **0.2.0**: 完善首页和我的页面UI
- **0.2.1**: 修复 CI 构建失败（PlaylistSongMap 缺少 @PrimaryKey）
- **0.2.2**: 修复 CI 构建失败（多个编译错误：Composable引用、Hilt依赖、类型不匹配等）
- **0.3.0**: 歌词解析与显示功能 ✅

### 中期规划 (0.4.x - 0.6.x)
- **0.3.1**: 重构优化（代码质量提升、性能优化）
- **0.4.0**: 本地音乐扫描与筛选优化
- **0.5.0**: 歌单管理功能（创建、编辑、删除）
- **0.6.0**: 播放详情页完善（封面模式/歌词模式切换优化）

### 长期规划 (0.7.x+)
- **0.7.0**: 搜索功能优化
- **0.8.0**: 主题/装扮功能
- **0.9.0**: 性能优化与稳定性提升
- **1.0.0**: 正式发布版本

## 当前版本目标: 0.2.1

### 目标
修复 GitHub Actions CI 构建失败问题。

### 任务清单
- [x] 修复 PlaylistSongMap.kt 缺少 @PrimaryKey 注解（添加复合主键 playlistId + songId）
- [x] 确保 GitHub Actions workflow 无报错
- [x] 更新 versionName 到 0.2.1

## 当前版本目标: 0.2.0

### 目标
完善首页、我的页面、播放详情页的完整 UI 实现，提供高质量的沉浸式用户体验。

### 任务清单
- [x] 实现首页完整 UI
  - [x] 顶部搜索栏（圆角矩形，浅灰色背景，"搜索本地音乐..."占位文本）
  - [x] Tab 布局（推荐、歌单、歌手、专辑）
  - [x] 最近播放区域（水平滚动，专辑封面 + 歌曲标题）
  - [x] 本地歌单区域（所有歌曲、我的收藏、新建歌单）
  - [x] 歌曲列表区域（垂直列表，专辑封面 + 歌曲标题 + 艺术家 + 时长）
- [x] 实现我的页面完整 UI
  - [x] Header 卡片（浅绿/薄荷渐变背景，用户头像，本地用户名，VIP 徽章）
  - [x] 统计网格（收藏、本地、有声、已购，4列布局）
  - [x] 最近播放区域（水平滚动）
  - [x] 自建歌单区域（列表视图，歌单封面 + 名称 + 歌曲数量）
- [x] 实现播放详情页完整 UI（全屏 Activity）
  - [x] 动态模糊背景（基于专辑封面高斯模糊 + 暗色渐变遮罩）
  - [x] 顶部栏（透明叠加，返回箭头，歌曲标题/艺术家，收藏图标）
  - [x] 封面模式（大圆角方形专辑封面 + 歌曲标题 + 艺术家 + 歌词预览 + 进度条 + 播放控制）
  - [x] 歌词模式（全屏滚动歌词，当前行高亮，平滑滚动动画）
  - [x] 模式切换手势
- [x] 实现迷你播放器（跨页面持久化）
  - [x] 小专辑封面 + 歌曲标题 + 艺术家
  - [x] 播放/暂停按钮 + 歌单按钮
  - [x] 点击展开全屏播放页
- [x] 底部导航栏（首页/我的两个 Tab）
- [x] 完善 ViewModel 与 UI 数据绑定
- [x] 确保代码量少于 10000 行
- [x] 更新 versionName 到 0.2.0
- [x] 确保 GitHub Actions workflow 无报错

## 当前版本目标: 0.1.0

### 目标
搭建Android项目基础架构，实现核心音乐播放功能。

### 任务清单
- [x] 创建Android项目结构（Kotlin + Jetpack Compose）
- [x] 配置Gradle构建文件（versionName: 0.1.0, 包名: app.music_q36_qwen_code）
- [x] 配置必要的依赖（Media3/ExoPlayer, Room, Compose）
- [x] 实现日志系统（保存在context.getExternalFilesDir(null)）
- [x] 实现本地音乐扫描功能（MediaStore扫描）
- [x] 实现音乐播放服务（MediaSessionService前台服务）
- [x] 实现基础UI（首页歌曲列表 + 底部迷你播放器）
- [x] 配置权限（READ_MEDIA_AUDIO）
- [x] 配置GitHub Actions构建workflow
- [x] 完善README.md
