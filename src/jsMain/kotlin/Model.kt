package top.moles

typealias Rarity = Int

data class Char(
    val isNew: Boolean,
    val name: String,
    val rarity: Rarity
)

data class Gacha(
    val chars: List<Char>,
    val nickName: String,
    val pool: String,
    val ts: Long,
    val uid: String,
)

data class Pagination (
    val current: Int,
    val pageSize: Int,
    val total: Int,
)

data class GachaPage(
    val list: List<Gacha>,
    val pagination: Pagination,
)