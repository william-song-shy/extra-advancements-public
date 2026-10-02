# Extra Advancements Companion（配套模组）

数据包 `extra_advancements` 的**可选**配套模组。它只补原版触发器判定不了的那几个事件，
把所有判定尽量留在数据包里。

> **状态：已编译通过。**
> 26.1.2 要求 **Java 25**，仓库自带 Gradle wrapper（9.7.1），`./gradlew build` 即可出 jar。
> 下面所有 Minecraft / Fabric 的类名与方法签名都对着 26.1.2 的官方 jar 核过字节码
> （26.1 起 Minecraft 不再混淆，全部用 Mojang 官方名）——包括四个 mixin 注入点的
> 完整描述符，以及 `NoteBlockInstrument` 的全部取值。

## 它做什么

| 事件 | 用到的钩子 | 服务哪条进度 |
|---|---|---|
| 生物死亡（含伤害来源） | `ServerLivingEntityEvents.AFTER_DEATH` | 大卫与歌利亚、烈焰飞雪 |
| 生物转化（蛮兵 → 僵尸猪灵） | `ServerLivingEntityEvents.MOB_CONVERSION` | 蛮兵转职 |
| 玩家破坏方块 | `PlayerBlockBreakEvents.AFTER` | 除虫服务 |
| 玩家对方块使用物品 | `UseBlockCallback.EVENT` | 尼莫点 |
| 生物消耗不死图腾 | `@Inject` → `LivingEntity#checkTotemDeathProtection`（private） | 大逃生，狐胜亡！ |
| 潜影贝承受伤害 | `@Inject` → `Shulker#hurtServer` | 出口贸易 |
| 实体传送 | `@Inject` → `Entity#teleportTo(ServerLevel,DDDLjava/util/Set;FFZ)Z` | 扶摇直上（只负责清零数据包的计数器） |
| 音符盒发声 | `@Inject` → `NoteBlock#playNote(Entity, BlockState, Level, BlockPos)V`（private） | 一人乐队（红石 / 右键 / 左键三条路都经过它） |
| 每 100 tick | `ServerTickEvents.END_SERVER_TICK` | 向死而生（把世界出生点写进计分板 `ea.spawn`） |

## 它不做什么

- 不注册任何方块 / 物品 / 实体 / 附魔 / 伤害类型 / 配方
- 不取消事件、不改写返回值、不修改实体或世界状态
- 唯一的写操作是 `/advancement grant`，外加往数据包自己的两个计分板目标里写几个数
  （`ea.lstate` 清零、`ea.spawn` 里的世界出生点坐标）—— 不改玩家背包、不改方块、不改实体
- 没有客户端代码。`environment` 写的是 `"*"` 而不是 `"server"`，这样单人存档里也能用；
  连服务器的玩家**不需要**装它，纯原版客户端照常进服。

## 构建

需要 **JDK 25**；Gradle 不必另装，仓库自带 wrapper。

```bash
cd mod
./gradlew build       # Windows 上是 gradlew.bat build
```

产物在 `build/libs/extra-advancements-companion-1.0.0.jar`。
Loom 1.18.2 要求 **Gradle 9.7.0 以上**，wrapper 已锁到 9.7.1 —— 往后调 `loom_version`
时记得同步调 wrapper，Loom 会在配置阶段直接拒绝过低的 Gradle。

把 jar 丢进服务端（或客户端单人存档）的 `mods/` 目录，同时需要装
**Fabric Loader ≥ 0.19.0** 和 **Fabric API 0.155.3+26.1.2**。

开着服务器时会看到日志：`Extra Advancements companion 已就绪`。
数据包侧可以用 `scoreboard players get #mod ea.mod` 确认模组在岗（返回 1）。

## 版本对照（26.1 之后变化很大，别抄旧教程）

| 项目 | 26.1.2 的正确值 | 旧写法（已失效） |
|---|---|---|
| 映射 | 不需要 —— 26.1 起不再混淆 | Yarn `mappings` 依赖 |
| Loom 插件 ID | `net.fabricmc.fabric-loom` | `fabric-loom` |
| 依赖配置 | `implementation` | `modImplementation` |
| Java | 25 | 21 |
| Loader | 0.19.5 | 0.18.x |
| Loom | 1.18.2 | 1.15 |
| 伤害方法 | `hurtServer(ServerLevel, DamageSource, float)` | `hurt(DamageSource, float)` |
| 命令分发 | `MinecraftServer#getCommands()` / `performPrefixedCommand` | `getCommandManager()` / `executeWithPrefix` |
| 静默输出 | `CommandSourceStack#withSuppressedOutput()` | `withSilent()` |

## 哪些是真的非它不可

只有 4 条：**大卫与歌利亚、烈焰飞雪、蛮兵转职、出口贸易**。

另外 6 条装不装模组都能拿到，模组只是把近似升级成精确、或省掉每 tick 的轮询：

| 进度 | 无模组时数据包怎么做 | 装了模组 |
|---|---|---|
| 除虫服务 | 每 tick 比对虫蚀方块挖掘总数，只在刚增加那一 tick 查主手 | 方块破坏事件里直接看，零 tick 开销 |
| 尼莫点 | 海洋生物群系 + 头顶见天 + 放热带鱼桶 | 真的扫一遍 65×129×65 |
| 扶摇直上 | 单 tick 水平位移 > 10 格即判失败，竖直传送骗得过 | 传送入口精确清零 |
| 大逃生，狐胜亡！ | 轮询带吸收效果的狐狸 | 图腾生效点精确命中 |
| 向死而生（「从没死过」那一支） | 靠「tick 函数的命令源落在世界出生点上」这条假设做近似（待实测） | 读 `ServerLevel#getRespawnData()` 下发的精确坐标 |
| 一人乐队 | 只能观察到"玩家亲手敲了某个音色"，不要求同时（红石驱动的音符盒没有任何触发器） | 在发声源头按 tick 收集，26 种音色同一 tick 内听到才发 |

## 已知的粗糙处

- **出口贸易**：26.1.2 里潜影贝的复制逻辑内联在 `Shulker#hurtServer` 里，没有独立方法，
  只能前后各数一次附近的潜影贝数量。同一 tick 内有别的潜影贝生成会误判。
  这条被限定在下界生效，误差可以接受。
- **尼莫点**：体积扫描是 546,565 次 `getBlockState`。带提前退出（碰到第二个方块就返回），
  正常世界里几毫秒就结束；真在虚空上才会走满。
- **米辛配置的 `compatibilityLevel`** 写的是 `JAVA_25`，这个取值在 Fabric Loader 0.19.5
  带的 Mixin 0.17.4 里存在。若在更老的 Loader 上加载时报 “Unsupported compatibility
  level”，把它换成 `JAVA_21` 即可。
- **`Entity#teleportTo` 的注入** 对每个实体每次这种传送都会调用。已经加了
  「位移不超过 8 格就跳过」的门槛，但如果你发现正常移动也会把扶摇直上判失败，
  就调大 `EntityTeleportMixin.MIN_DISTANCE_SQR`。
- **音符盒那几处名字都核过字节码**：注入点 `NoteBlock#playNote(Entity, BlockState, Level, BlockPos)V`
  （private、实例方法）、音色属性 `NoteBlock.INSTRUMENT`（`EnumProperty<NoteBlockInstrument>`，
  也可写成 `BlockStateProperties.NOTEBLOCK_INSTRUMENT`）、音量常量 `NOTE_VOLUME = 3`
  （→ 可听半径 48 格）、以及 `NoteBlockInstrument` 的全部取值，都是从 26.1.2 的
  官方 jar 上 `javap` 读出来的；四个 mixin 注入点的描述符也已与字节码逐字比对过。
- **世界出生点的取法**：26.1 把 `ServerLevel#getSharedSpawnPos()` 换成了 `getRespawnData()`，
  `/setworldspawn` 写的就是它（`ServerLevel` 那一对读写只是转发给 `MinecraftServer`）。
  `SpawnHandler` 读 `server.overworld().getRespawnData().pos()` 写进 `ea.spawn`
  供数据包比对。两条已知边界：
  一是读到的 `RespawnData` 自带 `dimension()`，而计分板只存了 X/Y/Z，
  所以「出生点被 `/setworldspawn` 设到别的维度」（1.21.9 起 Java 允许）时无从区分；
  二是刷新间隔 5 秒，其间若数据包先跑到判定，它会发现 `#spawnx` 还没写、自动退回那条近似，
  所以不会误发。
