# Extra Advancements（额外进度）

给 Minecraft Java 版 **26.1.2**（数据包格式 **101.1**）用的自定义进度包：
**49 条额外进度 + 1 条把它们串起来的根进度**，全部挂在进度菜单里独立的「额外进度」标签页下。
进度名与描述默认显示英文，装上配套资源包后显示简体中文。

## 仓库内容

| 目录 | 说明 |
|---|---|
| [`datapack/`](datapack/) | 数据包本体（**必装**）：进度、函数、谓词、标签 |
| [`resourcepack/`](resourcepack/) | 配套资源包（可选）：`zh_cn` 本地化 |
| [`mod/`](mod/) | 配套 Fabric 模组（可选）：补几个原版触发器判定不了的事件，见 [`mod/README.md`](mod/README.md) |

## 下载

打包好的产物在 [Releases](../../releases)：

- `extra_advancements-<版本>.zip` —— 数据包
- `extra_advancements_resourcepack-<版本>.zip` —— 资源包（中文）
- `extra-advancements-companion-<版本>.jar` —— 配套模组

## 安装

**数据包（必装）**：把 zip 丢进 `saves/<世界名>/datapacks/`（服务端是 `<服务端>/world/datapacks/`），
进游戏 `/reload`，用 `/datapack list` 确认已加载。

**资源包（可选）**：把 zip 丢进 `resourcepacks/`，在「选项 → 资源包」里启用；不装则进度显示英文。

**模组（可选）**：把 jar 丢进 `mods/`，需要 Fabric Loader ≥ 0.19.0 与 Fabric API
（26.1.2 对应 `0.155.3+26.1.2`）。单人存档同样可用；只连服务器的玩家**不需要**装，
纯原版客户端照常进服。哪些进度用到模组、用到哪些钩子，见 [`mod/README.md`](mod/README.md)。

## 从源码构建

模组需要 **JDK 25**，仓库自带 Gradle wrapper：

```bash
cd mod && ./gradlew build     # Windows 上是 gradlew.bat build
```

产物在 `mod/build/libs/`。数据包与资源包无需构建，`datapack/`、`resourcepack/` 目录本身就是成品，
各自打成 zip 即可（zip 的根直接是 `pack.mcmeta` 和 `data/` / `assets/`）。

## 关于本仓库

源码由内部仓库在每次提交后自动同步；`.zip` / `.jar` 只在发版（打 `v*` 标签）时构建并挂到
[Releases](../../releases)。用法说明与设计文档在内部仓库，不随本仓库公开。
