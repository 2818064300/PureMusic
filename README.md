# PureMusic

追求简洁且优雅的音乐App.

由**Kotlin**语言搭建

## 🎵官网

> [Pure Music](http://www.puremusic.com.cn/)

## 🌈环境要求

- 需要 Android 10.0 以上 环境

## 🚀 特点功能

- 极致简约的设计
- 支持**网易云音乐**账号登录
- 采用**Material Design**与**IOS**设计风格
- 支持**QQ音乐源**搜索
- 支持**FLAC无损音质**播放

## 🧩 截图

![PureMusic/screenshot_1.jpg at master · 2818064300/PureMusic (github.com)](https://github.com/2818064300/PureMusic/blob/master/app/src/main/assets/screenshot_1.jpg)

![PureMusic/screenshot_2.jpg at master · 2818064300/PureMusic (github.com)](https://github.com/2818064300/PureMusic/blob/master/app/src/main/assets/screenshot_2.jpg)

![PureMusic/screenshot_3.jpg at master · 2818064300/PureMusic (github.com)](https://github.com/2818064300/PureMusic/blob/master/app/src/main/assets/screenshot_3.jpg)

## 📃更新日志

- **2022/8/19**

1. 新增每日推荐
2. 新增我的云盘
3. 重新绘制预搜索界面
4. 修复bug

- **2022/8/18**

1. 重构排行榜界面
2. 新歌速递功能
3. 优化播放界面歌词显示
4. 歌词增加翻译
5. 异步请求数据,减少崩溃概率

- **2022/8/17**

1. 调整数据结构
2. 优化请求方式(大幅度降低崩溃概率)
3. 重构界面跳转逻辑(使更多数据类型被通用)

- **2022/8/16**

1. 优化单选框切换逻辑
2. 更新cookie
3. 调整歌曲请求速度(保证效率的同时提高稳定性)

- **2022/8/10**

1. 加入缓存功能
2. 修复bug
3. 优化跳转逻辑

- **2022/8/05**

1. 采用异步请求方式,减少bug发生率
2. 完善应用内版本更新检测
3. 修复播放概率闪退bug
4. 修复播放列表异常bug
5. 改善页面跳转逻辑
6. 优化App性能

- **2022/8/02**

1. 重新绘制侧滑栏
2. 支持版本更新检测
3. 修复播放列表异常bug

- **2022/7/20**

1. 使用Kotlin语言重构项目
2. 重新设计音乐界面
3. 重新设计主页界面
4. 歌单界面增加PlayBar
5. 修复云端歌曲异常bug

## 🐛已知bug

- **2022/8/17**

1. ~~用户信息无法更新~~(已修复)
2. ~~搜索结果异常~~(已修复)

- **2022/8/12**

1. ~~歌曲详情请求过快导致API接口崩溃~~(已修复)

- **2022/8/10**

1. 已缓存歌单无法更新内容

- **2022/8/05**

1. 排行榜界面无法进入(正在跟进)
2. ~~界面跳转异常~~(已修复)

- **2022/7/16**

1. 不同搜索源相同关键词结果异常
2. ~~播放概率闪退~~(已修复)
3. 歌曲封面旋转异常(正在跟进)

## 💎灵感来自

### 设计师网站

[Dribbble](https://dribbble.com/)

[Material design 中文文档](https://www.mdui.org/design/)

[花瓣网](https://huaban.com/)

### API接口

[Binaryify/NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi)

[jsososo/QQMusicApi](https://github.com/jsososo/QQMusicApi)

### 开源框架

[scwang90/SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)

[hdodenhof/CircleImageView](https://github.com/hdodenhof/CircleImageView)

[GrenderG/Toasty](https://github.com/GrenderG/Toasty)

[gyf-dev/ImmersionBar](https://github.com/gyf-dev/ImmersionBar)

[cymcsg/UltimateRecyclerView](https://github.com/cymcsg/UltimateRecyclerView)

[CameraKit/blurkit-android](https://github.com/CameraKit/blurkit-android)

[square/okhttp](https://github.com/square/okhttp)

[lingochamp/FileDownloader](https://github.com/lingochamp/FileDownloader)

[bumptech/glide](https://github.com/bumptech/glide)

[wasabeef/glide-transformations](https://github.com/wasabeef/glide-transformations)

[wangchenyan/lrcview](https://github.com/wangchenyan/LrcView)

[jenly1314/ArcSeekBar](https://github.com/jenly1314/ArcSeekBar)

[mxn21/FlowingDrawer](https://github.com/mxn21/FlowingDrawer)

[linwg1988/LCardView](https://github.com/linwg1988/LCardView)

### 参考项目

[Moriafly/DsoMusic](https://github.com/Moriafly/DsoMusic)
