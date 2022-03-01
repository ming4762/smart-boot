package com.smart.starter.redis.constants;

import lombok.Getter;

/**
 * @author ShiZhongMing
 * 2022/2/25
 * @since 2.0.0
 */
@Getter
public enum RedisInfoResultEnum {
    /**
     * SERVER
     */
    redis_version(RedisInfoParameterEnum.SERVER, "redis_version", "Redis服务器版本"),
    redis_git_sha1(RedisInfoParameterEnum.SERVER, "redis_git_sha1", "Git SHA1"),
    redis_git_dirty(RedisInfoParameterEnum.SERVER, "redis_git_dirty", "Git dirty flag"),
    redis_build_id(RedisInfoParameterEnum.SERVER, "redis_build_id", "redis_build_id"),
    redis_mode(RedisInfoParameterEnum.SERVER, "redis_mode", "运行模式，单机（standalone）或者集群（cluster）"),
    os(RedisInfoParameterEnum.SERVER, "os", "Redis 服务器的宿主操作系统"),
    arch_bits(RedisInfoParameterEnum.SERVER, "arch_bits", "架构（32 或 64 位）"),
    multiplexing_api(RedisInfoParameterEnum.SERVER, "multiplexing_api", "Redis 所使用的事件处理机制"),
    atomicvar_api(RedisInfoParameterEnum.SERVER, "atomicvar_api", "atomicvar_api"),
    gcc_version(RedisInfoParameterEnum.SERVER, "gcc_version", "编译 Redis 时所使用的 GCC 版本"),
    process_id(RedisInfoParameterEnum.SERVER, "process_id", "服务器进程的 PID"),
    run_id(RedisInfoParameterEnum.SERVER, "run_id", "Redis 服务器的随机标识符（用于 Sentinel 和集群）"),
    tcp_port(RedisInfoParameterEnum.SERVER, "tcp_port", "TCP/IP 监听端口"),
    uptime_in_seconds(RedisInfoParameterEnum.SERVER, "uptime_in_seconds", "自 Redis 服务器启动以来，经过的秒数"),
    uptime_in_days(RedisInfoParameterEnum.SERVER, "uptime_in_days", "自 Redis 服务器启动以来，经过的天数"),
    lru_clock(RedisInfoParameterEnum.SERVER, "lru_clock", "以分钟为单位进行自增的时钟，用于 LRU 管理"),
    hz(RedisInfoParameterEnum.SERVER, "hz", "redis内部调度（进行关闭timeout的客户端，删除过期key等等）频率，程序规定serverCron每秒运行10次"),
    executable(RedisInfoParameterEnum.SERVER, "executable", "可执行文件的路径"),
    config_file(RedisInfoParameterEnum.SERVER, "config_file", "配置文件目录"),
    /**
     * CLIENTS
     */
    connected_clients(RedisInfoParameterEnum.CLIENTS, "connected_clients", "已连接客户端的数量（不包括通过从属服务器连接的客户端）"),
    client_longest_output_list(RedisInfoParameterEnum.CLIENTS, "client_longest_output_list", "当前连接的客户端当中，最长的输出列表"),
    client_biggest_input_buf(RedisInfoParameterEnum.CLIENTS, "client_biggest_input_buf", "当前连接的客户端当中，最大输入缓存"),
    blocked_clients(RedisInfoParameterEnum.CLIENTS, "blocked_clients", "正在等待阻塞命令（BLPOP、BRPOP、BRPOPLPUSH）的客户端的数量"),
    /**
     * MEMORY
     */
    used_memory(RedisInfoParameterEnum.MEMORY, "used_memory", "由 Redis 分配器分配的内存总量，以字节（byte）为单位"),
    used_memory_human(RedisInfoParameterEnum.MEMORY, "used_memory_human", "以人类可读的格式返回 Redis 分配的内存总量"),
    used_memory_rss(RedisInfoParameterEnum.MEMORY, "used_memory_rss", "从操作系统的角度，返回 Redis 已分配的内存总量（俗称常驻集大小）。这个值和 top 、 ps等命令的输出一致。"),
    used_memory_rss_human(RedisInfoParameterEnum.MEMORY, "used_memory_rss_human", "从操作系统的角度，返回 Redis 已分配的内存总量（俗称常驻集大小）。这个值和 top 、 ps等命令的输出一致"),
    used_memory_peak(RedisInfoParameterEnum.MEMORY, "used_memory_peak", "Redis 的内存消耗峰值（以字节为单位）"),
    used_memory_peak_human(RedisInfoParameterEnum.MEMORY, "used_memory_peak_human", "以人类可读的格式返回 Redis 的内存消耗峰值"),
    used_memory_peak_perc(RedisInfoParameterEnum.MEMORY, "used_memory_peak_perc", "使用内存达到峰值内存的百分比，即(used_memory/ used_memory_peak) *100%"),
    used_memory_overhead(RedisInfoParameterEnum.MEMORY, "used_memory_overhead", "Redis为了维护数据集的内部机制所需的内存开销，包括所有客户端输出缓冲区、查询缓冲区、AOF重写缓冲区和主从复制的backlog"),
    used_memory_startup(RedisInfoParameterEnum.MEMORY, "used_memory_startup", "Redis服务器启动时消耗的内存"),
    used_memory_dataset(RedisInfoParameterEnum.MEMORY, "used_memory_dataset", "数据占用的内存大小，即used_memory-used_memory_overhead"),
    used_memory_dataset_perc(RedisInfoParameterEnum.MEMORY, "used_memory_dataset_perc", "数据占用的内存大小的百分比，100%*(used_memory_dataset/(used_memory-used_memory_startup))"),
    total_system_memory(RedisInfoParameterEnum.MEMORY, "total_system_memory", "系统内存"),
    total_system_memory_human(RedisInfoParameterEnum.MEMORY, "total_system_memory_human", "以更直观的格式显示系统内存"),
    used_memory_lua(RedisInfoParameterEnum.MEMORY, "used_memory_lua", "Lua脚本存储占用的内存"),
    used_memory_lua_human(RedisInfoParameterEnum.MEMORY, "used_memory_lua_human", "以更直观的格式显示Lua脚本存储占用的内存"),
    maxmemory(RedisInfoParameterEnum.MEMORY, "maxmemory", "Redis实例的最大内存配置"),
    maxmemory_human(RedisInfoParameterEnum.MEMORY, "maxmemory_human", "以更直观的格式显示Redis实例的最大内存配置"),
    maxmemory_policy(RedisInfoParameterEnum.MEMORY, "maxmemory_policy", "当达到maxmemory时的淘汰策略"),
    mem_fragmentation_ratio(RedisInfoParameterEnum.MEMORY, "mem_fragmentation_ratio", "碎片率，used_memory_rss/ used_memory"),
    mem_allocator(RedisInfoParameterEnum.MEMORY, "mem_allocator", "在编译时指定的， Redis 所使用的内存分配器。可以是 libc 、 jemalloc 或者 tcmalloc"),
    /**
     * Persistence
     */
    loading(RedisInfoParameterEnum.PERSISTENCE, "loading", "一个标志值，记录了服务器是否正在载入持久化文件"),
    rdb_changes_since_last_save(RedisInfoParameterEnum.PERSISTENCE, "rdb_changes_since_last_save", "一个标志值，记录了服务器是否正在创建 RDB 文件"),
    rdb_last_save_time(RedisInfoParameterEnum.PERSISTENCE, "rdb_last_save_time", "最近一次成功创建 RDB 文件的 UNIX 时间戳"),
    rdb_last_bgsave_status(RedisInfoParameterEnum.PERSISTENCE, "rdb_last_bgsave_status", "一个标志值，记录了最近一次创建 RDB 文件的结果是成功还是失败"),
    rdb_last_bgsave_time_sec(RedisInfoParameterEnum.PERSISTENCE, "rdb_last_bgsave_time_sec", "记录了最近一次创建 RDB 文件耗费的秒数"),
    rdb_current_bgsave_time_sec(RedisInfoParameterEnum.PERSISTENCE, "rdb_current_bgsave_time_sec", "如果服务器正在创建 RDB 文件，那么这个域记录的就是当前的创建操作已经耗费的秒数"),
    aof_enabled(RedisInfoParameterEnum.PERSISTENCE, "aof_enabled", "一个标志值，记录了 AOF 是否处于打开状态"),
    aof_rewrite_in_progress(RedisInfoParameterEnum.PERSISTENCE, "abc", "一个标志值，记录了服务器是否正在创建 AOF 文件"),
    aof_rewrite_scheduled(RedisInfoParameterEnum.PERSISTENCE, "aof_rewrite_scheduled", "一个标志值，记录了在 RDB 文件创建完毕之后，是否需要执行预约的 AOF 重写操作"),
    aof_last_rewrite_time_sec(RedisInfoParameterEnum.PERSISTENCE, "aof_last_rewrite_time_sec", "最近一次创建 AOF 文件耗费的时长"),
    aof_current_rewrite_time_sec(RedisInfoParameterEnum.PERSISTENCE, "aof_current_rewrite_time_sec", "如果服务器正在创建 AOF 文件，那么这个域记录的就是当前的创建操作已经耗费的秒数"),
    aof_last_bgrewrite_status(RedisInfoParameterEnum.PERSISTENCE, "aof_last_bgrewrite_status", "一个标志值，记录了最近一次创建 AOF 文件的结果是成功还是失败"),
    aof_current_size (RedisInfoParameterEnum.PERSISTENCE, "aof_current_size", "AOF 文件目前的大小"),
    aof_base_size (RedisInfoParameterEnum.PERSISTENCE, "aof_base_size", "服务器启动时或者 AOF 重写最近一次执行之后，AOF 文件的大小"),
    aof_pending_rewrite(RedisInfoParameterEnum.PERSISTENCE, "aof_pending_rewrite", "一个标志值，记录了是否有 AOF 重写操作在等待 RDB 文件创建完毕之后执行"),
    aof_buffer_length(RedisInfoParameterEnum.PERSISTENCE, "aof_buffer_length", "AOF 缓冲区的大小"),
    aof_rewrite_buffer_length(RedisInfoParameterEnum.PERSISTENCE, "aof_rewrite_buffer_length", "AOF 重写缓冲区的大小"),
    aof_pending_bio_fsync(RedisInfoParameterEnum.PERSISTENCE, "aof_pending_bio_fsync", "后台 I/O 队列里面，等待执行的 fsync 调用数量"),
    aof_delayed_fsync(RedisInfoParameterEnum.PERSISTENCE, "aof_delayed_fsync", "被延迟的 fsync 调用数量"),
    /**
     * status
     */
    total_connections_received(RedisInfoParameterEnum.STATS, "total_connections_received", "服务器已接受的连接请求数量"),
    total_commands_processed(RedisInfoParameterEnum.STATS, "total_commands_processed", "服务器已执行的命令数量"),
    instantaneous_ops_per_sec(RedisInfoParameterEnum.STATS, "instantaneous_ops_per_sec", "服务器每秒钟执行的命令数量"),
    total_net_input_bytes(RedisInfoParameterEnum.STATS, "total_net_input_bytes", "redis网络入口流量字节数"),
    total_net_output_bytes(RedisInfoParameterEnum.STATS, "total_net_output_bytes", "redis网络出口流量字节数"),
    instantaneous_input_kbps(RedisInfoParameterEnum.STATS, "instantaneous_input_kbps", "redis网络入口kps"),
    instantaneous_output_kbps(RedisInfoParameterEnum.STATS, "instantaneous_output_kbps", "redis网络出口kps"),
    rejected_connections(RedisInfoParameterEnum.STATS, "rejected_connections", "因为最大客户端数量限制而被拒绝的连接请求数量"),
    sync_full(RedisInfoParameterEnum.STATS, "sync_full", "主从完全同步成功次数"),
    sync_partial_ok(RedisInfoParameterEnum.STATS, "sync_partial_ok", "主从部分同步成功次数"),
    sync_partial_err(RedisInfoParameterEnum.STATS, "sync_partial_err", "主从部分同步失败次数"),
    expired_keys(RedisInfoParameterEnum.STATS, "expired_keys", "因为过期而被自动删除的数据库键数量"),
    evicted_keys(RedisInfoParameterEnum.STATS, "evicted_keys", " 因为最大内存容量限制而被驱逐（evict）的键数量"),
    keyspace_hits(RedisInfoParameterEnum.STATS, "keyspace_hits", "查找数据库键成功的次数"),
    keyspace_misses(RedisInfoParameterEnum.STATS, "keyspace_misses", " 查找数据库键失败的次数"),
    pubsub_channels(RedisInfoParameterEnum.STATS, "pubsub_channels", "目前被订阅的频道数量"),
    pubsub_patterns(RedisInfoParameterEnum.STATS, "pubsub_patterns", "目前被订阅的模式数量"),
    latest_fork_usec(RedisInfoParameterEnum.STATS, "latest_fork_usec", " 最近一次 fork() 操作耗费的毫秒数"),
    migrate_cached_sockets(RedisInfoParameterEnum.STATS, "migrate_cached_sockets", "为迁移而打开的套接字数"),
    /**
     * replication
     */
    role(RedisInfoParameterEnum.REPLICATION, "role", "如果当前服务器没有在复制任何其他服务器，那么这个域的值就是 master ；否则的话，这个域的值就是slave 。注意，在创建复制链的时候，一个从服务器也可能是另一个服务器的主服务器。"),
    master_host(RedisInfoParameterEnum.REPLICATION, "master_host", "主服务器的 IP 地址"),
    master_port(RedisInfoParameterEnum.REPLICATION, "master_port", "主服务器的 TCP 监听端口号"),
    master_link_status(RedisInfoParameterEnum.REPLICATION, "master_link_status", "复制连接当前的状态， up 表示连接正常， down 表示连接断开"),
    master_last_io_seconds_ago(RedisInfoParameterEnum.REPLICATION, "master_last_io_seconds_ago", "距离最近一次与主服务器进行通信已经过去了多少秒钟"),
    master_sync_in_progress(RedisInfoParameterEnum.REPLICATION, "master_sync_in_progress", "一个标志值，记录了主服务器是否正在与这个从服务器进行同步"),
    master_sync_left_bytes(RedisInfoParameterEnum.REPLICATION, "master_sync_left_bytes", " 距离同步完成还缺少多少字节数据"),
    master_sync_last_io_seconds_ago(RedisInfoParameterEnum.REPLICATION, "master_sync_last_io_seconds_ago", "距离最近一次因为 SYNC 操作而进行 I/O 已经过去了多少秒"),
    master_link_down_since_seconds(RedisInfoParameterEnum.REPLICATION, "master_link_down_since_seconds", "主从服务器连接断开了多少秒"),
    connected_slaves(RedisInfoParameterEnum.REPLICATION, "connected_slaves", "已连接的从服务器数量"),
    /**
     * CPU
     */
    used_cpu_sys(RedisInfoParameterEnum.CPU, "used_cpu_sys", "Redis 服务器耗费的系统 CPU"),
    used_cpu_user(RedisInfoParameterEnum.CPU, "used_cpu_user", "Redis 服务器耗费的用户 CPU"),
    used_cpu_sys_children(RedisInfoParameterEnum.CPU, "used_cpu_sys_children", "后台进程耗费的系统 CPU"),
    used_cpu_user_children(RedisInfoParameterEnum.CPU, "used_cpu_user_children", "后台进程耗费的用户 CPU"),
    /**
     * cluster
     */
    cluster_enabled(RedisInfoParameterEnum.CLUSTER, "cluster_enabled", "一个标志值，记录集群功能是否已经开启")
    ;
    private final RedisInfoParameterEnum parameter;

    private final String value;

    private final String description;

    RedisInfoResultEnum(RedisInfoParameterEnum parameter, String value, String description) {
        this.parameter = parameter;
        this.value = value;
        this.description = description;
    }
}
